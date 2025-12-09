package uni.fis.usuario.mapper;

import uni.fis.usuario.dto.request.DocumentoRequest;
import uni.fis.usuario.entity.DocumentoEntity;
import uni.fis.usuario.entity.TipoDocumentoEntity;

public class DocumentoMapper {

    public static DocumentoEntity toEntityForCreate(DocumentoRequest request) {
        return new DocumentoEntity(
                null, // null para crear
                new TipoDocumentoEntity(request.idTipoDocumento(), null),
                request.numeroDocumento(),
                request.fechaExpiracion());
    }

}