package uni.fis.usuario.dto.request;

import java.time.LocalDateTime;

public record DocumentoRequest(
                int idDocumento,
                int idTipoDocumento,
                String numeroDocumento,
                LocalDateTime fechaExpiracion) {
}