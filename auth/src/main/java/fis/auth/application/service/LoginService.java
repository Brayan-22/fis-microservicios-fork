package fis.auth.application.service;

import fis.auth.domain.model.Login;
import fis.auth.domain.model.Token;

public interface LoginService {
    Token execute(Login login);
    Token refresh(String refresh);
}
