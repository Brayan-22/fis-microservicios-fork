package uni.fis.foro.service;

import java.util.List;
import uni.fis.foro.entity.CategoriaEntity;


public interface CategoriaService {
    CategoriaEntity crearCategoria(String nombre);
    List<CategoriaEntity> listarCategorias();
}

