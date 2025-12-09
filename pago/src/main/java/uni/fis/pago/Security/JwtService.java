package uni.fis.pago.Security;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class JwtService {

    @Value("${SECRET.KEY}")
    private String SECRET_KEY;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    public boolean isValid(String token) {
        try {
            Claims claims = extractAllClaims(token);
            String username = claims.getSubject();
            Date expirationDate = claims.getExpiration();
            
            if (StringUtils.isBlank(username) || expirationDate.before(new Date())) {
                return false;
            }
            return true;
        } catch (ExpiredJwtException e) {
            log.error("Token expirado", e);
            return false;
        } catch (JwtException e) {
            log.error("Token inválido", e);
            return false;
        } catch (Exception e) {
            log.error("Error al validar token", e);
            return false;
        }
    }

    private Claims extractAllClaims(String token) {
        if (StringUtils.isBlank(token)) {
            throw new JwtException("Token vacío o nulo");
        }
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getRole(String token) {
        return getClaim(token, claims -> claims.get("role", String.class))
                .orElseThrow(() -> new JwtException("No se encontró el rol en el token"));
    }

    public String getSubject(String token) {
        return getClaim(token, Claims::getSubject)
                .orElseThrow(() -> new JwtException("No se encontró el subject en el token"));
    }

    public Date getExpiration(String token) {
        return getClaim(token, Claims::getExpiration)
                .orElseThrow(() -> new JwtException("No se encontró la expiración en el token"));
    }
    
    public Integer getUserId(String token) {
        return getClaim(token, claims -> claims.get("userId", Integer.class))
                .orElseThrow(() -> new JwtException("No se encontró el userId en el token"));
    }

    private <T> Optional<T> getClaim(String token, Function<Claims, T> claimResolver) {
        try {
            final Claims claims = extractAllClaims(token);
            return Optional.ofNullable(claimResolver.apply(claims));
        } catch (Exception e) {
            log.error("Error al extraer claim del token", e);
            return Optional.empty();
        }
    }

    public boolean isNotExpired(String token) {
        try {
            Date expiration = getExpiration(token);
            return expiration.after(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}