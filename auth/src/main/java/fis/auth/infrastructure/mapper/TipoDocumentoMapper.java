package fis.auth.infrastructure.mapper;

import fis.auth.domain.model.TipoDocumento;
import fis.auth.infrastructure.dto.request.DocumentoRequest;

public class TipoDocumentoMapper {

    public static TipoDocumento toDomain(DocumentoRequest request){
        return new TipoDocumento(
                request.idTipoDocumento(),
                null // puede ser null el nombre porque solo se ingresará
        );
    }
}
