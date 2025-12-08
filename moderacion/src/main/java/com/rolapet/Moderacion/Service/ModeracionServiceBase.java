package com.rolapet.Moderacion.Service;
import com.rolapet.Moderacion.Domain.dto.ModeracionRequestDTO;
import com.rolapet.Moderacion.Domain.dto.ModeracionResponseDTO;
import com.rolapet.Moderacion.Domain.entity.PalabraProhibida;
import com.rolapet.Moderacion.Factory.PalabrasMalSonantesFactory;
import com.rolapet.Moderacion.Factory.PalabrasMalSonantesFactory.PalabraProhibidaData;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public abstract class ModeracionServiceBase implements ModeracionServiceInt {

    protected final List<PalabraProhibida> palabrasProhibidas = new CopyOnWriteArrayList<>();
    protected final AtomicInteger idGenerator = new AtomicInteger(1);
    protected final PalabrasMalSonantesFactory factory;

    protected ModeracionServiceBase(PalabrasMalSonantesFactory factory) {
        if (factory == null) {
            throw new IllegalArgumentException("El factory no puede ser nulo");
        }
        this.factory = factory;
    }

    @PostConstruct
    public void inicializarPalabras() {
        log.info("Inicializando palabras prohibidas para idioma: {}", getIdioma());

        List<PalabraProhibidaData> palabras = factory.inicializarPalabras();

        for (PalabraProhibidaData data : palabras) {
            agregarPalabraProhibida(data.getPalabra(), data.getDescripcion());
        }

        log.info("Lista inicializada con {} palabras prohibidas en {}",
                palabrasProhibidas.size(), getIdioma());
    }

    @Override
    public ModeracionResponseDTO validarContenido(ModeracionRequestDTO request) {
        if (request == null) {
            log.error("Request de moderación es nulo");
            throw new IllegalArgumentException("El request de moderación no puede ser nulo");
        }
        if (request.getContenido() == null) {
            log.warn("Contenido es nulo para usuario: {}", request.getUsuarioId());
            return new ModeracionResponseDTO(
                    false,
                    "El contenido no puede estar vacío",
                    0
            );
        }
        String contenido = request.getContenido().trim();
        if (contenido.isEmpty()) {
            log.warn("Contenido vacío para usuario: {}", request.getUsuarioId());
            return new ModeracionResponseDTO(
                    false,
                    "El contenido no puede estar vacío",
                    0
            );
        }
        if (contenido.length() < 1) {
            log.warn("Contenido demasiado corto para usuario: {}", request.getUsuarioId());
            return new ModeracionResponseDTO(
                    false,
                    "El contenido debe tener al menos 1 carácter",
                    0
            );
        }
        final int MAX_CONTENIDO_LENGTH = 10000;
        if (contenido.length() > MAX_CONTENIDO_LENGTH) {
            log.warn("Contenido demasiado largo ({} caracteres) para usuario: {}",
                    contenido.length(), request.getUsuarioId());
            return new ModeracionResponseDTO(
                    false,
                    "El contenido excede el límite máximo de " + MAX_CONTENIDO_LENGTH + " caracteres",
                    0
            );
        }
        String contenidoLower = contenido.toLowerCase();
        List<String> palabrasDetectadas = new ArrayList<>();

        // Verificar palabras prohibidas
        for (PalabraProhibida palabra : palabrasProhibidas) {
            // Validación defensiva: verificar que la palabra no sea nula
            if (palabra == null || palabra.getPalabra() == null) {
                log.warn("Palabra prohibida nula encontrada, saltando...");
                continue;
            }

            if (palabra.getActiva() && contenidoLower.contains(palabra.getPalabra().toLowerCase())) {
                palabrasDetectadas.add(palabra.getPalabra());
                log.warn("Palabra prohibida detectada: '{}' para usuario: {}",
                        palabra.getPalabra(), request.getUsuarioId());
            }
        }
        if (palabrasDetectadas.isEmpty()) {
            log.info("Contenido aprobado para usuario: {}", request.getUsuarioId());
            return new ModeracionResponseDTO(true, "Contenido aprobado ✓", 0);
        }
        log.warn("Contenido rechazado para usuario: {}. Palabras detectadas: {}",
                request.getUsuarioId(), palabrasDetectadas);
        return new ModeracionResponseDTO(
                false,
                "Tu publicación contiene lenguaje inapropiado: " + String.join(", ", palabrasDetectadas),
                palabrasDetectadas.size()
        );
    }

    @Override
    public PalabraProhibida agregarPalabraProhibida(String palabra, String descripcion) {
        if (palabra == null) {
            log.error("Intento de agregar palabra nula");
            throw new IllegalArgumentException("La palabra no puede ser nula");
        }

        if (palabra.trim().isEmpty()) {
            log.error("Intento de agregar palabra vacía");
            throw new IllegalArgumentException("La palabra no puede estar vacía");
        }
        String descripcionFinal = (descripcion == null || descripcion.trim().isEmpty())
                ? "Sin descripción"
                : descripcion.trim();

        String palabraLower = palabra.trim().toLowerCase();
        if (palabraLower.length() > 200) {
            throw new IllegalArgumentException("La palabra no puede exceder 200 caracteres");
        }
        boolean existe = palabrasProhibidas.stream()
                .anyMatch(p -> p.getPalabra() != null && p.getPalabra().equalsIgnoreCase(palabraLower));

        if (existe) {
            log.warn("Intento de agregar palabra duplicada: '{}'", palabraLower);
            throw new IllegalArgumentException("La palabra '" + palabraLower + "' ya está registrada");
        }
        PalabraProhibida nueva = new PalabraProhibida();
        nueva.setId(idGenerator.getAndIncrement());
        nueva.setPalabra(palabraLower);
        nueva.setDescripcion(descripcionFinal);
        nueva.setActiva(true);

        palabrasProhibidas.add(nueva);
        log.info("Palabra '{}' agregada con ID: {}", palabraLower, nueva.getId());

        return nueva;
    }

    @Override
    public PalabraProhibida actualizarPalabraProhibida(Integer id, String nuevaPalabra, String descripcion) {

        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }

        if (nuevaPalabra == null || nuevaPalabra.trim().isEmpty()) {
            throw new IllegalArgumentException("La nueva palabra no puede estar vacía");
        }

        log.info("Actualizando palabra con ID: {}", id);

        PalabraProhibida palabra = buscarPorId(id);

        String palabraLower = nuevaPalabra.trim().toLowerCase();
        String descripcionFinal = (descripcion == null || descripcion.trim().isEmpty())
                ? palabra.getDescripcion()
                : descripcion.trim();
        boolean existeOtra = palabrasProhibidas.stream()
                .anyMatch(p -> !p.getId().equals(id) &&
                        p.getPalabra() != null &&
                        p.getPalabra().equalsIgnoreCase(palabraLower));

        if (existeOtra) {
            throw new IllegalArgumentException("Ya existe otra palabra con el texto: " + palabraLower);
        }

        palabra.setPalabra(palabraLower);
        palabra.setDescripcion(descripcionFinal);

        log.info("Palabra actualizada: '{}'", palabraLower);
        return palabra;
    }

    @Override
    public void eliminarPalabraProhibida(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }

        log.info("Eliminando palabra con ID: {}", id);

        boolean eliminada = palabrasProhibidas.removeIf(p -> p.getId() != null && p.getId().equals(id));

        if (!eliminada) {
            log.error("Palabra no encontrada con ID: {}", id);
            throw new RuntimeException("Palabra no encontrada con ID: " + id);
        }

        log.info("Palabra eliminada exitosamente");
    }

    @Override
    public PalabraProhibida desactivarPalabra(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }

        log.info("Desactivando palabra con ID: {}", id);

        PalabraProhibida palabra = buscarPorId(id);
        palabra.setActiva(false);

        log.info("Palabra '{}' desactivada", palabra.getPalabra());
        return palabra;
    }

    @Override
    public PalabraProhibida activarPalabra(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }

        log.info("Activando palabra con ID: {}", id);

        PalabraProhibida palabra = buscarPorId(id);
        palabra.setActiva(true);

        log.info("Palabra '{}' activada", palabra.getPalabra());
        return palabra;
    }

    @Override
    public List<PalabraProhibida> listarTodasLasPalabras() {
        log.debug("Listando {} palabras prohibidas", palabrasProhibidas.size());
        return new ArrayList<>(palabrasProhibidas);
    }

    @Override
    public List<PalabraProhibida> listarPalabrasActivas() {
        List<PalabraProhibida> activas = palabrasProhibidas.stream()
                .filter(p -> p != null && Boolean.TRUE.equals(p.getActiva()))
                .toList();

        log.debug("Se encontraron {} palabras activas", activas.size());
        return activas;
    }

    @Override
    public PalabraProhibida buscarPorId(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }

        return palabrasProhibidas.stream()
                .filter(p -> p != null && p.getId() != null && p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Palabra no encontrada con ID: " + id));
    }
}
