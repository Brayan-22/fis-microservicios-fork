package com.rolapet.Moderacion.Service;

import com.rolapet.Moderacion.Domain.dto.ModeracionRequestDTO;
import com.rolapet.Moderacion.Domain.dto.ModeracionResponseDTO;
import com.rolapet.Moderacion.Domain.entity.PalabraProhibida;

import java.util.List;

/**
 * Interfaz base para servicios de moderación de contenido.
 * Permite implementaciones específicas por idioma.
 */
public interface ModeracionServiceInt {
    ModeracionResponseDTO validarContenido(ModeracionRequestDTO request);
    PalabraProhibida agregarPalabraProhibida(String palabra, String descripcion);
    PalabraProhibida actualizarPalabraProhibida(Integer id, String nuevaPalabra, String descripcion);
    void eliminarPalabraProhibida(Integer id);
    PalabraProhibida desactivarPalabra(Integer id);
    PalabraProhibida activarPalabra(Integer id);
    List<PalabraProhibida> listarTodasLasPalabras();
    List<PalabraProhibida> listarPalabrasActivas();
    PalabraProhibida buscarPorId(Integer id);
    String getIdioma();
}
