package fis.auth.infrastructure.mapper;

import fis.auth.domain.model.Documento;
import fis.auth.infrastructure.dto.request.DocumentoRequest;

public class DocumentoMapper {

    public static Documento toDomain(DocumentoRequest request) {
        return new Documento(
                request.idDocumento(),
                TipoDocumentoMapper.toDomain(request),
                request.numeroDocumento(),
                request.fechaExpiracion()
        );
    }
}
