package uni.fis.contenido.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uni.fis.contenido.dto.CrearPublicacionDTO;
import uni.fis.contenido.dto.PublicacionResponseDTO;
import uni.fis.contenido.entity.ContenidoEntity;
import uni.fis.contenido.entity.PublicacionEntity;
import uni.fis.contenido.exception.ContenidoNoValidoException;
import uni.fis.contenido.mapper.PublicacionMapper;
import uni.fis.contenido.repository.PublicacionRepository;
import uni.fis.contenido.service.ContenidoService;
import uni.fis.contenido.service.PublicacionService;

import java.util.List;

@Service
@Transactional
public class PublicacionServiceImpl implements PublicacionService {

    private final PublicacionRepository publicacionRepository;
    private final ContenidoService contenidoService;
    private final PublicacionMapper publicacionMapper;

    public PublicacionServiceImpl(
            PublicacionRepository publicacionRepository,
            ContenidoService contenidoService,
            PublicacionMapper publicacionMapper) {
        this.publicacionRepository = publicacionRepository;
        this.contenidoService = contenidoService;
        this.publicacionMapper = publicacionMapper;
    }

    @Override
    public PublicacionEntity crearPublicacion(CrearPublicacionDTO dto) {

        if (dto.getTextoContenido() == null || dto.getTextoContenido().isBlank()) {
            throw new ContenidoNoValidoException("El contenido de la publicación no puede estar vacío.");
        }

        if (dto.getUsuario() == null) {
            throw new ContenidoNoValidoException("Debe especificarse el usuario creador de la publicación.");
        }

        // Crear contenido relacionado
        ContenidoEntity contenido = contenidoService.crearContenido(dto.getTextoContenido(), dto.getUsuario());

        PublicacionEntity pub = new PublicacionEntity();
        pub.setTitulo(dto.getTitulo());
        pub.setContenido(contenido);
        pub.setMultimedia(dto.getIdMultimedia());
        pub.setForo(dto.getIdForo());

        return publicacionRepository.save(pub);
    }

    @Override
    public List<PublicacionResponseDTO> listarTodo() {
        return publicacionRepository.findAll()
                .stream()
                .map(publicacionMapper::toDTO)
                .toList();
    }

    @Override
    public List<PublicacionResponseDTO> listarPorForo(Integer idForo) {
        return publicacionRepository.findByForo(idForo)
                .stream()
                .map(publicacionMapper::toDTO)
                .toList();
    }

    @Override
    public List<PublicacionResponseDTO> listarPorUsuario(Integer usuario) {
        return publicacionRepository.findAll().stream()
                .filter(p -> p.getContenido().getIdAutor().equals(usuario))
                .map(publicacionMapper::toDTO)
                .toList();
    }

    @Override
    public List<PublicacionResponseDTO> listarTodas() {
        return publicacionRepository.findAll()
                .stream()
                .map(publicacionMapper::toDTO)
                .toList();
    }
}
