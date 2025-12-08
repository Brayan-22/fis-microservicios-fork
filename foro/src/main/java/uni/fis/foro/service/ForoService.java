package uni.fis.foro.service;

import uni.fis.foro.entity.ForoEntity;
import java.util.List;


public interface ForoService {

    ForoEntity crearForo(String nombre);

    List<ForoEntity> listarForos();

    List<ForoEntity> filtrarForos(String nombre);
}