package uni.fis.contenido.service;

import java.util.List;
import uni.fis.contenido.dto.CrearPublicacionDTO;
import uni.fis.contenido.dto.PublicacionResponseDTO;
import uni.fis.contenido.entity.PublicacionEntity;

public interface PublicacionService {
    PublicacionEntity crearPublicacion(CrearPublicacionDTO dto);

    List<PublicacionResponseDTO> listarPorForo(Integer idForo);

    List<PublicacionResponseDTO> listarPorUsuario(Integer usuario);

    List<PublicacionResponseDTO> listarTodas();
}
