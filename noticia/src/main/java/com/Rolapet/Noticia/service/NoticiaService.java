package com.Rolapet.Noticia.service;

import com.Rolapet.Noticia.domain.dto.ActualizarNoticiaRequest;
import com.Rolapet.Noticia.domain.dto.AutorDTO;
import com.Rolapet.Noticia.domain.dto.CrearNoticiaRequest;
import com.Rolapet.Noticia.domain.dto.NoticiaDTO;
import com.Rolapet.Noticia.domain.entity.Contenido;
import com.Rolapet.Noticia.domain.entity.Publicacion;
import com.Rolapet.Noticia.repository.ContenidoRepository;
import com.Rolapet.Noticia.repository.PublicacionRepository;
import com.Rolapet.Noticia.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticiaService {

    private final PublicacionRepository publicacionRepository;
    private final ContenidoRepository contenidoRepository;
    private final UsuarioRepository usuarioRepository;

    // Prefijo para identificar noticias
    private static final String PREFIJO_NOTICIA = "[NOTICIA] ";

    /**
     * RF41: Listar todas las noticias
     */
    @Transactional(readOnly = true)
    public List<NoticiaDTO> listarNoticias(String estado) {
        List<Publicacion> publicaciones;
        LocalDateTime ahora = LocalDateTime.now();

        if ("publicado".equalsIgnoreCase(estado)) {
            publicaciones = publicacionRepository.findPublicadas(ahora);
        } else if ("borrador".equalsIgnoreCase(estado)) {
            publicaciones = publicacionRepository.findBorradores(ahora);
        } else {
            publicaciones = publicacionRepository.findAllNoticias();
        }

        return publicaciones.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtener noticia por ID
     */
    @Transactional(readOnly = true)
    public NoticiaDTO obtenerPorId(Integer id) {
        Publicacion publicacion = publicacionRepository
                .findNoticiaById(id)
                .orElseThrow(() -> new RuntimeException("Noticia no encontrada"));

        return convertirADTO(publicacion);
    }

    /**
     * Crear nueva noticia
     */
    @Transactional
    public NoticiaDTO crear(CrearNoticiaRequest request, Integer autorId) {
        // 1. Crear contenido
        Contenido contenido = new Contenido();
        contenido.setFechaCreacion(LocalDateTime.now());
        contenido.setTexto(request.getTexto());
        contenido.setAutorId(autorId);
        contenido = contenidoRepository.save(contenido);

        // 2. Determinar fecha según estado
        LocalDateTime fechaPublicacion;
        if ("borrador".equalsIgnoreCase(request.getEstado())) {
            fechaPublicacion = request.getFechaProgramada() != null
                    ? request.getFechaProgramada()
                    : LocalDateTime.now().plusYears(1);
        } else {
            fechaPublicacion = LocalDateTime.now();
        }

        // 3. Agregar prefijo [NOTICIA] al título
        String tituloConPrefijo = agregarPrefijoSiNoExiste(request.getTitulo());

        // 4. Crear publicación
        Publicacion publicacion = new Publicacion();
        publicacion.setTitulo(tituloConPrefijo);
        publicacion.setFecha(fechaPublicacion);
        publicacion.setIdImg(request.getIdImg());
        publicacion.setContenidoId(contenido.getId());
        publicacion.setForoId(request.getForoId());
        publicacion = publicacionRepository.save(publicacion);

        return convertirADTO(publicacion);
    }

    /**
     *  Actualizar noticia existente
     */
    @Transactional
    public NoticiaDTO actualizar(Integer id, ActualizarNoticiaRequest request, Integer usuarioId) {
        Publicacion publicacion = publicacionRepository
                .findNoticiaById(id)
                .orElseThrow(() -> new RuntimeException("Noticia no encontrada"));

        Contenido contenido = publicacion.getContenido();

        // Actualizar contenido si se proporciona
        if (request.getTexto() != null && !request.getTexto().trim().isEmpty()) {
            contenido.setTexto(request.getTexto());
            contenidoRepository.save(contenido);
        }

        // Actualizar publicación
        if (request.getTitulo() != null && !request.getTitulo().trim().isEmpty()) {
            String tituloConPrefijo = agregarPrefijoSiNoExiste(request.getTitulo());
            publicacion.setTitulo(tituloConPrefijo);
        }

        if (request.getIdImg() != null) {
            publicacion.setIdImg(request.getIdImg());
        }

        if (request.getForoId() != null) {
            publicacion.setForoId(request.getForoId());
        }

        // Manejar cambio de estado
        if (request.getEstado() != null) {
            if ("publicado".equalsIgnoreCase(request.getEstado())) {
                publicacion.setFecha(LocalDateTime.now());
            } else if ("borrador".equalsIgnoreCase(request.getEstado())) {
                LocalDateTime fechaProgramada = request.getFechaProgramada() != null
                        ? request.getFechaProgramada()
                        : LocalDateTime.now().plusYears(1);
                publicacion.setFecha(fechaProgramada);
            }
        }

        publicacion = publicacionRepository.save(publicacion);

        return convertirADTO(publicacion);
    }

    /**
     * Eliminar noticia (cambiar a borrador)
     */
    @Transactional
    public void eliminar(Integer id, Integer usuarioId) {
        Publicacion publicacion = publicacionRepository
                .findNoticiaById(id)
                .orElseThrow(() -> new RuntimeException("Noticia no encontrada"));

        publicacion.setFecha(LocalDateTime.now().plusYears(1));
        publicacionRepository.save(publicacion);
    }

    /**
     * Agregar prefijo [NOTICIA] si no existe
     */
    private String agregarPrefijoSiNoExiste(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            return PREFIJO_NOTICIA;
        }

        // Si ya tiene el prefijo, no lo duplica
        if (titulo.startsWith(PREFIJO_NOTICIA)) {
            return titulo;
        }

        return PREFIJO_NOTICIA + titulo;
    }

    /**
     * Quitar prefijo [NOTICIA] para mostrarlo limpio en el DTO
     */
    private String quitarPrefijo(String titulo) {
        if (titulo == null) {
            return null;
        }

        if (titulo.startsWith(PREFIJO_NOTICIA)) {
            return titulo.substring(PREFIJO_NOTICIA.length());
        }

        return titulo;
    }

    /**
     * Convertir entidad a DTO
     */
    private NoticiaDTO convertirADTO(Publicacion publicacion) {
        NoticiaDTO dto = new NoticiaDTO();
        dto.setId(publicacion.getId());

        // Quitar prefijo [NOTICIA] al mostrar
        dto.setTitulo(quitarPrefijo(publicacion.getTitulo()));
        dto.setFecha(publicacion.getFecha());

        if (publicacion.getContenido() != null) {
            dto.setTexto(publicacion.getContenido().getTexto());
            dto.setFechaCreacion(publicacion.getContenido().getFechaCreacion());

            if (publicacion.getContenido().getAutor() != null) {
                AutorDTO autor = new AutorDTO();
                autor.setId(publicacion.getContenido().getAutor().getId());
                autor.setNombre(publicacion.getContenido().getAutor().getNombre());
                autor.setApellido(publicacion.getContenido().getAutor().getApellido1());
                autor.setEmail(publicacion.getContenido().getAutor().getEmail());
                dto.setAutor(autor);
            }
        }

        if (publicacion.getMultimedia() != null) {
            dto.setImagenUrl(publicacion.getMultimedia().getUrl());
            dto.setImagenTipo(publicacion.getMultimedia().getTipoArchivo());
        }

        // Determinar estado
        dto.setEstado(publicacion.getFecha().isAfter(LocalDateTime.now())
                ? "borrador" : "publicado");

        return dto;
    }
}
