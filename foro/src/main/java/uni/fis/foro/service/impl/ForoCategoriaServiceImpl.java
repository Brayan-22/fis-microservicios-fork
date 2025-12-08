package uni.fis.foro.service.impl;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import uni.fis.foro.dto.AsignarCategoriaForoDTO;
import uni.fis.foro.entity.CategoriaEntity;
import uni.fis.foro.entity.ForoCategoriaEntity;
import uni.fis.foro.entity.ForoEntity;
import uni.fis.foro.exception.CategoriaNoEncontradaException;
import uni.fis.foro.exception.ForoNoEncontradoException;
import uni.fis.foro.repository.CategoriaRepository;
import uni.fis.foro.repository.ForoCategoriaRepository;
import uni.fis.foro.repository.ForoRepository;
import uni.fis.foro.service.ForoCategoriaService;
import java.util.List;

@Service
@Transactional
class ForoCategoriaServiceImpl implements ForoCategoriaService {

    private final ForoCategoriaRepository foroCategoriaRepository;
    private final ForoRepository foroRepository;
    private final CategoriaRepository categoriaRepository;

    public ForoCategoriaServiceImpl(
            ForoCategoriaRepository foroCategoriaRepository,
            ForoRepository foroRepository,
            CategoriaRepository categoriaRepository) {
        this.foroCategoriaRepository = foroCategoriaRepository;
        this.foroRepository = foroRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public ForoCategoriaEntity asignarCategoria(AsignarCategoriaForoDTO dto) {

        ForoEntity foro = foroRepository.findById(dto.getIdForo())
                .orElseThrow(() -> new ForoNoEncontradoException("Foro no encontrado"));

        CategoriaEntity categoria = categoriaRepository.findById(dto.getIdCategoria())
                .orElseThrow(() -> new CategoriaNoEncontradaException("Categoría no encontrada"));

        ForoCategoriaEntity fc = new ForoCategoriaEntity();
        fc.setForo(foro);
        fc.setCategoria(categoria);

        return foroCategoriaRepository.save(fc);
    }

    @Override
    public List<ForoCategoriaEntity> listarPorCategoria(Integer idCategoria) {

        CategoriaEntity categoria = categoriaRepository.findById(idCategoria)
                .orElseThrow(() -> new CategoriaNoEncontradaException("Categoría no encontrada"));

        return foroCategoriaRepository.findByCategoria(categoria);
    }
}

