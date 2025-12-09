package fis.auth.application.service;

import fis.auth.domain.service.TokenStrategy;
import fis.auth.domain.model.Token;
import fis.auth.domain.model.TokenRequest;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
    private final TokenStrategy tokenStrategy;

    public TokenService(
            TokenStrategy tokenStrategy
    ) {
        this.tokenStrategy = tokenStrategy;
    }

    public Token generateToken(TokenRequest request) {
        return tokenStrategy.generate(request);
    }

    public Token validate(String token) {
        return tokenStrategy.validate(token);
    }

    public Token refresh(String refreshToken){return tokenStrategy.refresh(refreshToken);}
}
