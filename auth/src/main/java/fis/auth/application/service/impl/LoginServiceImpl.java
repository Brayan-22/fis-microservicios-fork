package fis.auth.application.service.impl;


import fis.auth.application.command.TokenCommand;
import fis.auth.application.command.impl.GenerateTokenCommand;
import fis.auth.application.repository.UserMSRepository;
import fis.auth.application.service.LoginService;
import fis.auth.application.service.TokenService;
import fis.auth.domain.model.Login;
import fis.auth.domain.model.Token;
import fis.auth.domain.model.TokenRequest;
import fis.auth.infrastructure.error.NoUserFoundError;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    private final TokenService tokenService;
    private final UserMSRepository repository;

    public LoginServiceImpl(
            TokenService tokenService,
            UserMSRepository repository
    ) {
        this.tokenService = tokenService;
        this.repository = repository;
    }

    // cambio para subir la rama
    @Override
    public Token execute(Login login) throws NoUserFoundError {
        //Obtenemos el TokenRequest, es decir, el nombre y rol desde el microservicio de usuario
        TokenRequest req = repository.findNameAndRolUser(login.email(), login.password());
        if (req == null) {
            throw NoUserFoundError.of("Usuario no encontrado con las credenciales proporcionadas");
        }
        // Luego generamos el token.
        TokenCommand tokenCommand = new GenerateTokenCommand(this.tokenService, req);

        return tokenCommand.execute();
    }

    @Override
    public Token refresh(String refresh) {
        return tokenService.refresh(refresh);
    }


}
