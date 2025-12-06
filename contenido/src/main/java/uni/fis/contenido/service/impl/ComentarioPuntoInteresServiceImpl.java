package uni.fis.contenido.service.impl;

import org.springframework.stereotype.Service;
import uni.fis.contenido.dto.ComentarioPuntoInteresDTO;
import uni.fis.contenido.entity.ComentarioPuntoInteresEntity;
import uni.fis.contenido.exception.ContenidoNoValidoException;
import uni.fis.contenido.mapper.ComentarioPuntoInteresMapper;
import uni.fis.contenido.repository.ComentarioPuntoInteresRepository;
import uni.fis.contenido.repository.ContenidoRepository;
import uni.fis.contenido.service.ComentarioPuntoInteresService;
import uni.fis.contenido.exception.PuntoInteresNoValidoException;
import uni.fis.contenido.exception.ComentarioNoEncontradoException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComentarioPuntoInteresServiceImpl implements ComentarioPuntoInteresService {

    private final ComentarioPuntoInteresRepository comentarioRepo;
    private final ComentarioPuntoInteresMapper mapper;

    public ComentarioPuntoInteresServiceImpl(
            ComentarioPuntoInteresRepository comentarioRepo,
            ComentarioPuntoInteresMapper mapper,
            ContenidoRepository contenidoRepository
    ) {
        this.comentarioRepo = comentarioRepo;
        this.mapper = mapper;
    }

    @Override
    public List<ComentarioPuntoInteresDTO> findAll() {
        List<ComentarioPuntoInteresEntity> lista = comentarioRepo.findAll();

        if (lista.isEmpty()) {
            throw new ComentarioNoEncontradoException("No hay comentarios registrados");
        }

        return lista.stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<ComentarioPuntoInteresDTO> findByPunto(Integer idPunto) {

        if (idPunto == null) {
            throw new PuntoInteresNoValidoException("Debe enviar el idPunto en la URL");
        }

        return comentarioRepo.findByIdPunto(idPunto)
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }


    @Override
    public ComentarioPuntoInteresDTO create(ComentarioPuntoInteresDTO comentarioDTO) {

        if (comentarioDTO.getIdPunto() == null) {
            throw new PuntoInteresNoValidoException("Debe enviar el idPunto desde el frontend");
        }

        if (comentarioDTO.getIdComentario() == null) {
            throw new ContenidoNoValidoException("Debe enviar el idContenido para asociar el comentario");
        }

        ComentarioPuntoInteresEntity entity = mapper.toEntity(comentarioDTO);
        ComentarioPuntoInteresEntity saved = comentarioRepo.save(entity);

        return mapper.toDTO(saved);
    }

}
