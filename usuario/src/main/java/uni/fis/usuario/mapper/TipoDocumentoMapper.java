package uni.fis.usuario.mapper;

import uni.fis.usuario.dto.request.DocumentoRequest;
import uni.fis.usuario.entity.TipoDocumentoEntity;

public class TipoDocumentoMapper {

    public static TipoDocumentoEntity toEntity(DocumentoRequest request) {
        return new TipoDocumentoEntity(
                request.idTipoDocumento(),
                null // no es necesario pero toca buscarlo por id para consistencia.
        );
    }
}