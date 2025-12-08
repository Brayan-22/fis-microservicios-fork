package uni.fis.foro.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uni.fis.foro.entity.ForoEntity;
import uni.fis.foro.exception.ForoCategoriaNoValidaException;
import uni.fis.foro.repository.ForoRepository;
import uni.fis.foro.service.ForoService;

import java.util.List;


@Service
@Transactional
public class ForoServiceImpl implements ForoService {

    private final ForoRepository foroRepository;

    public ForoServiceImpl(ForoRepository foroRepository) {
        this.foroRepository = foroRepository;
    }

    @Override
    public ForoEntity crearForo(String nombre) {
        ForoEntity foro = new ForoEntity();
        foro.setNombre(nombre);
        return foroRepository.save(foro);
    }

    @Override
    public List<ForoEntity> listarForos() {
        return foroRepository.findAll();
    }

    @Override
    public List<ForoEntity> filtrarForos(String nombre) {

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ForoCategoriaNoValidaException("El nombre de búsqueda no puede estar vacío");
        }

        return foroRepository.findAll().stream()
                .filter(f -> f.getNombre() != null &&
                        f.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .toList();
    }
}
