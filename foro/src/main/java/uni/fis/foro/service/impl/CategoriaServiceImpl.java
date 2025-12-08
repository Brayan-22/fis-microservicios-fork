package uni.fis.foro.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import uni.fis.foro.entity.CategoriaEntity;
import uni.fis.foro.repository.CategoriaRepository;
import uni.fis.foro.service.CategoriaService;

@Service
@Transactional
class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaServiceImpl(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public CategoriaEntity crearCategoria(String nombre) {
        CategoriaEntity c = new CategoriaEntity();
        c.setNombre(nombre);
        return categoriaRepository.save(c);
    }

    @Override
    public List<CategoriaEntity> listarCategorias() {
        return categoriaRepository.findAll();
    }
}

