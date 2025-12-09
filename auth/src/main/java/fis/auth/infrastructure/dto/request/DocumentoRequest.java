package fis.auth.infrastructure.dto.request;

import java.time.LocalDateTime;

public record DocumentoRequest(
        int idDocumento,
        int idTipoDocumento,
        String numeroDocumento,
        LocalDateTime fechaExpiracion
) {
}