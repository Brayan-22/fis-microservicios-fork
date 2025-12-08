package uni.fis.contenido.service;

import uni.fis.contenido.dto.LikeResponseDTO;

public interface ContenidoLikeService {

    public LikeResponseDTO darLike(Integer idPublicacion, Integer idUsuario);
    public LikeResponseDTO quitarLike(Integer idPublicacion, Integer idUsuario);
}
