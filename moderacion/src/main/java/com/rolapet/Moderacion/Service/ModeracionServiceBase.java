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
        String contenido = request.getContenido().toLowerCase();
        List<String> palabrasDetectadas = new ArrayList<>();

        for (PalabraProhibida palabra : palabrasProhibidas) {
            if (palabra.getActiva() && contenido.contains(palabra.getPalabra().toLowerCase())) {
                palabrasDetectadas.add(palabra.getPalabra());
                log.warn("Palabra prohibida detectada: '{}'", palabra.getPalabra());
            }
        }

        if (palabrasDetectadas.isEmpty()) {
            log.info("Contenido aprobado para usuario: {}", request.getUsuarioId());
            return new ModeracionResponseDTO(true, "Contenido aprobado ✓", 0);
        }

        log.warn("Contenido rechazado. Palabras detectadas: {}", palabrasDetectadas);
        return new ModeracionResponseDTO(
                false,
                "Tu publicación contiene lenguaje inapropiado: " + String.join(", ", palabrasDetectadas),
                palabrasDetectadas.size()
        );
    }

    @Override
    public PalabraProhibida agregarPalabraProhibida(String palabra, String descripcion) {
        if (palabra == null || palabra.trim().isEmpty()) {
            throw new IllegalArgumentException("La palabra no puede estar vacía");
        }

        String palabraLower = palabra.trim().toLowerCase();

        boolean existe = palabrasProhibidas.stream()
                .anyMatch(p -> p.getPalabra().equalsIgnoreCase(palabraLower));

        if (existe) {
            throw new IllegalArgumentException("La palabra '" + palabra + "' ya está registrada");
        }

        PalabraProhibida nueva = new PalabraProhibida();
        nueva.setId(idGenerator.getAndIncrement());
        nueva.setPalabra(palabraLower);
        nueva.setDescripcion(descripcion);
        nueva.setActiva(true);

        palabrasProhibidas.add(nueva);
        log.info("Palabra '{}' agregada con ID: {}", palabraLower, nueva.getId());

        return nueva;
    }

    @Override
    public PalabraProhibida actualizarPalabraProhibida(Integer id, String nuevaPalabra, String descripcion) {
        log.info("Actualizando palabra con ID: {}", id);

        PalabraProhibida palabra = buscarPorId(id);
        palabra.setPalabra(nuevaPalabra.toLowerCase());
        palabra.setDescripcion(descripcion);

        log.info("Palabra actualizada: '{}'", nuevaPalabra);
        return palabra;
    }

    @Override
    public void eliminarPalabraProhibida(Integer id) {
        log.info("Eliminando palabra con ID: {}", id);

        boolean eliminada = palabrasProhibidas.removeIf(p -> p.getId().equals(id));

        if (!eliminada) {
            throw new RuntimeException("Palabra no encontrada con ID: " + id);
        }

        log.info("Palabra eliminada exitosamente");
    }

    @Override
    public PalabraProhibida desactivarPalabra(Integer id) {
        log.info("Desactivando palabra con ID: {}", id);

        PalabraProhibida palabra = buscarPorId(id);
        palabra.setActiva(false);

        log.info("Palabra '{}' desactivada", palabra.getPalabra());
        return palabra;
    }

    @Override
    public PalabraProhibida activarPalabra(Integer id) {
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
                .filter(PalabraProhibida::getActiva)
                .toList();

        log.debug("Se encontraron {} palabras activas", activas.size());
        return activas;
    }

    @Override
    public PalabraProhibida buscarPorId(Integer id) {
        return palabrasProhibidas.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Palabra no encontrada con ID: " + id));
    }
}
