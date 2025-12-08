package uni.fis.contenido.service.impl;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import uni.fis.contenido.dto.LikeResponseDTO;
import uni.fis.contenido.entity.ContenidoEntity;
import uni.fis.contenido.entity.ContenidoLikeEntity;
import uni.fis.contenido.exception.ContenidoNoValidoException;
import uni.fis.contenido.exception.LikeDuplicadoException;
import uni.fis.contenido.repository.ContenidoLikeRepository;
import uni.fis.contenido.repository.ContenidoRepository;
import uni.fis.contenido.service.ContenidoLikeService;

@Service
public class ContenidoLikeServiceImpl implements ContenidoLikeService {

    private final ContenidoLikeRepository likeRepository;
    private final ContenidoRepository ContenidoRepository;
    private final SimpMessagingTemplate ws;

    public ContenidoLikeServiceImpl(
            ContenidoLikeRepository likeRepository,
            ContenidoRepository ContenidoRepository,
            SimpMessagingTemplate ws
    ) {
        this.likeRepository = likeRepository;
        this.ContenidoRepository = ContenidoRepository;
        this.ws = ws;
    }

    @Override
    public LikeResponseDTO darLike(Integer idContenido, Integer idUsuario) {

        // Validar publicación
        ContenidoEntity contenido = ContenidoRepository.findById(idContenido)
                .orElseThrow(() -> new ContenidoNoValidoException(
                        "El Contenido con ID " + idContenido + " no existe"
                ));

        // Validar like duplicado
        if (likeRepository.existsByContenidoIdAndIdUsuario(idContenido, idUsuario)) {
            throw new LikeDuplicadoException(idContenido, idUsuario);
        }

        // Crear like
        ContenidoLikeEntity like = new ContenidoLikeEntity();
        like.setIdUsuario(idUsuario);
        like.setContenido(contenido);
        likeRepository.save(like);

        // Recalcular likes
        int totalLikes = likeRepository.countByContenidoId(idContenido);
        ContenidoRepository.save(contenido);

        enviarEventoWs(idContenido, totalLikes);

        return new LikeResponseDTO(idContenido, totalLikes);
    }

    @Override
    public LikeResponseDTO quitarLike(Integer idContenido, Integer idUsuario) {

        // Validar publicación
        ContenidoEntity contenido = ContenidoRepository.findById(idContenido)
                .orElseThrow(() -> new ContenidoNoValidoException(
                        "La publicación con ID " + idContenido + " no existe"
                ));

        // Validar que exista el like
        if (!likeRepository.existsByContenidoIdAndIdUsuario(idContenido, idUsuario)) {
            throw new ContenidoNoValidoException(
                    "El usuario " + idUsuario + " no tiene like en la publicación " + idContenido
            );
        }

        // Eliminar like
        likeRepository.deleteByContenidoIdAndIdUsuario(idContenido, idUsuario);

        // Recalcular
        int totalLikes = likeRepository.countByContenidoId(idContenido);
        ContenidoRepository.save(contenido);

        enviarEventoWs(idContenido, totalLikes);

        return new LikeResponseDTO(idContenido, totalLikes);
    }

    private void enviarEventoWs(Integer idContenido, int totalLikes) {
        ws.convertAndSend("/topic/contenido/" + idContenido + "/likes", totalLikes);
    }
}
