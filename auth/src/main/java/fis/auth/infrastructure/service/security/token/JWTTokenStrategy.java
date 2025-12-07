package fis.auth.infrastructure.service.security.token;

import fis.auth.domain.service.TokenStrategy;
import fis.auth.domain.model.Token;
import fis.auth.domain.model.TokenRequest;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@Qualifier("JWTTokenStrategy")
public class JWTTokenStrategy implements TokenStrategy {

    @Value("${SECRET.KEY}")
    private String SECRET_KEY;

    /**
     * Se espera que estas propiedades sean segundos (no ms).
     * Si tus properties ya están en ms, adapta o comenta la conversión.
     */
    @Value("${JWT.TOKEN.EXPIRATION}")
    private Long jwtExpirationSeconds;

    @Value("${JWT.TOKEN.REFRESH.EXPIRATION}")
    private Long refreshTokenExpirationSeconds;

    private Key getKey() {
        try {
            // Intentamos interpretar SECRET_KEY como Base64
            byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (Exception ex) {
            // Si no es base64, usamos directamente los bytes UTF-8 (menos ideal pero útil)
            log.warn("SECRET_KEY no parece base64 - usando bytes UTF-8. Recomendado: usar base64 larga segura.");
            return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        }
    }

    @Override
    public Token generate(TokenRequest tokenRequest) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("rol", tokenRequest.rol());
        claims.put("email", tokenRequest.email());
        claims.put("idRol", tokenRequest.idRol());

        String accessToken = generateToken(claims, tokenRequest.userId(), jwtExpirationSeconds);
        String refreshToken = generateToken(claims, tokenRequest.userId(), refreshTokenExpirationSeconds);

        Instant expiration = Instant.now().plusSeconds(jwtExpirationSeconds);
        return new Token(accessToken, refreshToken, expiration, true);
    }

    @Override
    public Token validate(String token) {
        try {
            if (token == null) {
                log.warn("validate(): token es null");
                return new Token(null, null, null, false);
            }
            Claims claims = getAllClaims(token);

            Instant expiration = claims.getExpiration().toInstant();

            if (expiration.isBefore(Instant.now())) {
                log.info("Token expirado (expiration: {})", expiration);
                return new Token(null, null, expiration, false);
            }

            // Devuelve expiresAt para que el caller sepa cuándo expira
            return new Token(null, null, expiration, true);

        } catch (Exception e) {
            log.error("Hubo un error con el token en JWTTokenStrategy.validate: {}", e.getMessage(), e);
            return new Token(null, null, null, false);
        }
    }

    public Token refresh(String refreshToken) {
        log.info("Refrescando desde JWTTokenStrategy");
        if (StringUtils.isBlank(refreshToken)) {
            throw new JwtException("Refresh token faltante");
        }
        try {
            Claims claims = getAllClaims(refreshToken);
            // Verificamos expiración manualmente
            Date expiration = claims.getExpiration();
            Date issued = claims.getIssuedAt();
            log.info("Token emitido en: {}, expira en: {}, tiempo actual: {}",
                    issued, expiration, new Date());

            if (expiration.before(new Date())) {
                throw new JwtException("Refresh token expirado");
            }
            // Extraemos datos y generamos nuevo token
            Integer userId = Integer.valueOf(claims.getSubject());
            Integer idRol = claims.get("idRol", Integer.class);
            String rol = claims.get("rol", String.class);
            String email = claims.get("email", String.class);

            TokenRequest req = new TokenRequest(userId, idRol, rol, email);
            return this.generate(req);

        } catch (ExpiredJwtException e) {
            log.error("Refresh token expirado: {}", e.getMessage());
            throw new JwtException("Refresh token expirado", e);
        } catch (Exception e) {
            log.error("Error validando refresh token: {}", e.getMessage());
            throw new JwtException("Refresh token inválido", e);
        }
    }

    // ------------------ privados --------------------
    private String generateToken(Map<String, Object> extraClaims, Integer subject, Long expirationSeconds) {
        long expMillis = TimeUnit.SECONDS.toMillis(expirationSeconds);
        Date now = new Date(System.currentTimeMillis());
        Date exp = new Date(System.currentTimeMillis() + expMillis);

        return Jwts.builder()
                .claims(extraClaims)
                .subject(subject.toString())
                .issuedAt(now)
                .expiration(exp)
                .signWith(getKey())
                .compact();
    }

    private Claims getAllClaims(String token) {
        if (StringUtils.isBlank(token)) {
            log.info("getAllClaims: Token está vacío o nulo");
            throw new JwtException("Token vacío o nulo");
        }
        try {
            // Forma correcta de parsear un JWT compacto firmado
            return Jwts.parser()
                    .verifyWith((SecretKey) getKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            log.error("Error parseando JWT: {}", e.getMessage(), e);
            throw new JwtException("Error parseando JWT: " + e.getMessage(), e);
        }
    }

    public Claims internalClaims(String token) {
        return getAllClaims(token);
    }

    private boolean isNotExpired(String token) {
        try {
            Date expiration = getAllClaims(token).getExpiration();
            return expiration != null && expiration.after(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
