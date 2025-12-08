package uni.fis.contenido.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uni.fis.contenido.dto.ComentarioResponseDTO;
import uni.fis.contenido.dto.CrearComentarioDTO;
import uni.fis.contenido.entity.ComentarioEntity;
import uni.fis.contenido.entity.ContenidoEntity;
import uni.fis.contenido.entity.PublicacionEntity;
import uni.fis.contenido.exception.ComentarioNoEncontradoException;
import uni.fis.contenido.exception.ContenidoNoValidoException;
import uni.fis.contenido.exception.PublicacionNoEncontradaException;
import uni.fis.contenido.repository.ComentarioRepository;
import uni.fis.contenido.repository.PublicacionRepository;
import uni.fis.contenido.service.ComentarioService;
import uni.fis.contenido.service.ContenidoService;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ComentarioServiceImpl implements ComentarioService {

    private final ComentarioRepository comentarioRepository;
    private final PublicacionRepository publicacionRepository;
    private final ContenidoService contenidoService;

    @Override
    public ComentarioEntity crearComentario(CrearComentarioDTO dto) {

        if (dto.getTextoContenido() == null || dto.getTextoContenido().isBlank()) {
            throw new ContenidoNoValidoException("El contenido del comentario no puede estar vacío.");
        }

        if (dto.getUsuario() == null) {
            throw new ContenidoNoValidoException("Debe especificarse el usuario que crea el comentario.");
        }

        ContenidoEntity contenido =
                contenidoService.crearContenido(dto.getTextoContenido(), dto.getUsuario());

        ComentarioEntity comentario = new ComentarioEntity();
        comentario.setContenido(contenido);

        if (dto.getIdComentarioPadre() != null) {
            comentario.setComentarioPadre(
                    comentarioRepository.findById(dto.getIdComentarioPadre())
                            .orElseThrow(() ->
                                    new ComentarioNoEncontradoException("Comentario padre no encontrado")
                            )
            );
        }

        if (dto.getIdPublicacion() != null) {
            comentario.setPublicacion(
                    publicacionRepository.findById(dto.getIdPublicacion())
                            .orElseThrow(() ->
                                    new PublicacionNoEncontradaException("Publicación no encontrada")
                            )
            );
        }

        return comentarioRepository.save(comentario);
    }

    @Override
    public List<ComentarioResponseDTO> listarPorUsuario(Integer usuario) {
        return comentarioRepository.findAll().stream()
                .filter(c -> c.getContenido().getIdAutor().equals(usuario))
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public List<ComentarioResponseDTO> listarPorPublicacion(Integer idPublicacion) {
        PublicacionEntity pub = publicacionRepository.findById(idPublicacion)
                .orElseThrow(() -> new PublicacionNoEncontradaException("Publicación no encontrada"));

        return comentarioRepository.findByPublicacion(pub).stream()
                .map(this::mapToDTO)
                .toList();
    }

    private ComentarioResponseDTO mapToDTO(ComentarioEntity c) {
        ComentarioResponseDTO dto = new ComentarioResponseDTO();
        dto.setId(c.getId());
        dto.setUsuario(c.getContenido().getIdAutor());
        dto.setTextoContenido(c.getContenido().getTexto());
        dto.setFechaContenido(c.getContenido().getFechaCreacion());
        dto.setIdPublicacion(c.getPublicacion() != null ? c.getPublicacion().getId() : null);
        dto.setIdComentarioPadre(c.getComentarioPadre() != null ? c.getComentarioPadre().getId() : null);
        return dto;
    }
}
