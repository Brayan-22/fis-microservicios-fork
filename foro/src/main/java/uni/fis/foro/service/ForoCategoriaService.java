package uni.fis.foro.service;

import uni.fis.foro.dto.AsignarCategoriaForoDTO;
import uni.fis.foro.entity.ForoCategoriaEntity;
import java.util.List;


public interface ForoCategoriaService {
    ForoCategoriaEntity asignarCategoria(AsignarCategoriaForoDTO dto);
    List<ForoCategoriaEntity> listarPorCategoria(Integer idCategoria);
}



