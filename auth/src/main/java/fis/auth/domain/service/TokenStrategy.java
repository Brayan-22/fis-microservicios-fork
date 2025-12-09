package fis.auth.domain.service;

import fis.auth.domain.model.Token;
import fis.auth.domain.model.TokenRequest;

public interface TokenStrategy {
    Token generate(TokenRequest tokenRequest);

    Token validate(String token);

    Token refresh(String refreshToken);
}
