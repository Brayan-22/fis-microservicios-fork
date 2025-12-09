package fis.auth.infrastructure.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import fis.auth.domain.error.InvalidCredentialsException;
import fis.auth.domain.error.InvalidIdentificationException;
import fis.auth.domain.error.MissingRequiredFieldException;
import fis.auth.domain.error.TutorApprovalPendingException;
import fis.auth.domain.model.SignIn;
import fis.auth.infrastructure.dto.response.api.ApiResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

@Component
public class UserMSErrorHandler {

    public void handleHttpError(HttpClientErrorException e, Object requestData) {
        String responseBody = e.getResponseBodyAsString();
        String errorMessage = extractErrorMessage(responseBody);
        int errorCode = e.getStatusCode().value();

        switch (errorCode) {
            case 401: // UNAUTHORIZED - Credenciales inválidas
                throw new InvalidCredentialsException(errorMessage);

            case 404: // NOT FOUND - Usuario no existe
                throw NoUserFoundError.of(errorMessage);

            case 409: // CONFLICT - Recurso ya existe
                if (requestData instanceof SignIn signIn) {
                    if (errorMessage.contains("email") || errorMessage.contains("correo")) {
                        throw UserAlreadyExistsException.byEmail(signIn.email());
                    } else if (errorMessage.contains("identificación")) {
                        throw UserAlreadyExistsException.byIdentification(signIn.documento().numeroDocumento());
                    }
                }
                throw UserAlreadyExistsException.byEmail("unknown");

            case 400: // BAD_REQUEST - Datos inválidos
                throw new MissingRequiredFieldException(errorMessage);

            case 403: // FORBIDDEN - Aprobación pendiente
                throw new TutorApprovalPendingException(errorMessage);

            case 422: // UNPROCESSABLE_ENTITY - Identificación inválida
                throw new InvalidIdentificationException(errorMessage);

            default:
                throw new RuntimeException("Error del servicio de usuarios: " + errorMessage);
        }
    }

    private String extractErrorMessage(String responseBody) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ApiResponse<?> errorResponse = mapper.readValue(responseBody, ApiResponse.class);
            return errorResponse.getMessage();
        } catch (Exception ex) {
            return "Error desconocido del servicio";
        }
    }
}