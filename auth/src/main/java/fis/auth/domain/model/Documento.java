package fis.auth.domain.model;

import java.time.LocalDateTime;

public record Documento(
        Integer id,
        TipoDocumento tipoDocumento,
        String numeroDocumento,
        LocalDateTime fechaExpiracion
) {

}
