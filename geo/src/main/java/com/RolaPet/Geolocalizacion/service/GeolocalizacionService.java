package com.RolaPet.Geolocalizacion.service;

import com.RolaPet.Geolocalizacion.client.NominatimClient;
import com.RolaPet.Geolocalizacion.domain.dto.*;
import com.RolaPet.Geolocalizacion.domain.entity.Direccion;
import com.RolaPet.Geolocalizacion.domain.entity.PuntoInteres;
import com.RolaPet.Geolocalizacion.repository.DireccionRepository;
import com.RolaPet.Geolocalizacion.repository.PuntoInteresRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class GeolocalizacionService {

    private final NominatimClient nominatimClient;
    private final PuntoInteresRepository puntoInteresRepository;
    private final DireccionRepository direccionRepository;

    private final Map<String, CoordenadaDTO> cacheGeocode = new ConcurrentHashMap<>();
    private final Map<String, DireccionDTO> cacheReverse = new ConcurrentHashMap<>();

    @Value("${geolocalizacion.cache.enabled:true}")
    private boolean cacheEnabled;

    public CoordenadaDTO geocode(GeocodeRequestDTO request) {
        String cacheKey = generarCacheKey(request.getDireccion(), request.getCiudad(), request.getPais());

        if (cacheEnabled && cacheGeocode.containsKey(cacheKey)) {
            log.info("⚡ Cache hit para geocoding: {}", cacheKey);
            return cacheGeocode.get(cacheKey);
        }

        String query = construirQuery(request);
        NominatimResponse response = nominatimClient.search(query);

        CoordenadaDTO coordenada = CoordenadaDTO.builder()
                .latitud(Double.parseDouble(response.getLat()))
                .longitud(Double.parseDouble(response.getLon()))
                .displayName(response.getDisplay_name())
                .tipo(response.getType())
                .build();

        if (cacheEnabled) {
            cacheGeocode.put(cacheKey, coordenada);
        }

        return coordenada;
    }

    public DireccionDTO reverseGeocode(ReverseGeocodeRequestDTO request) {
        String cacheKey = generarCacheKey(request.getLatitud(), request.getLongitud());

        if (cacheEnabled && cacheReverse.containsKey(cacheKey)) {
            log.info("⚡ Cache hit para reverse geocoding: {}", cacheKey);
            return cacheReverse.get(cacheKey);
        }

        NominatimResponse response = nominatimClient.reverse(
                request.getLatitud(),
                request.getLongitud()
        );

        DireccionDTO direccion = convertirNominatimADireccionDTO(response);

        if (cacheEnabled) {
            cacheReverse.put(cacheKey, direccion);
        }

        return direccion;
    }

    public PuntoInteresDTO registrarPuntoInteres(PuntoInteresRequestDTO request) {
        log.info("➕ Registrando punto de interés: {}", request.getNombre());

        Direccion direccion = crearOBuscarDireccion(request.getDireccion());

        PuntoInteres punto = PuntoInteres.builder()
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .imgPun(request.getImgPun())
                .idDireccion(direccion.getId())
                .build();

        PuntoInteres guardado = puntoInteresRepository.save(punto);
        log.info("Punto registrado con ID: {}", guardado.getId());

        return convertirAPuntoInteresDTO(guardado);
    }

    public PuntoInteresDTO actualizarPuntoInteres(Integer id, PuntoInteresRequestDTO request) {
        log.info("Actualizando punto de interés ID: {}", id);

        PuntoInteres punto = puntoInteresRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Punto de interés no encontrado con ID: " + id));

        Direccion direccion = crearOBuscarDireccion(request.getDireccion());

        punto.setNombre(request.getNombre());
        punto.setDescripcion(request.getDescripcion());
        punto.setImgPun(request.getImgPun());
        punto.setIdDireccion(direccion.getId());

        PuntoInteres actualizado = puntoInteresRepository.save(punto);
        log.info("Punto actualizado exitosamente");

        return convertirAPuntoInteresDTO(actualizado);
    }

    public void eliminarPuntoInteres(Integer id) {
        log.info("Eliminando punto de interés ID: {}", id);

        if (!puntoInteresRepository.existsById(id)) {
            throw new RuntimeException("Punto de interés no encontrado con ID: " + id);
        }

        puntoInteresRepository.deleteById(id);
        log.info("Punto eliminado exitosamente");
    }

    public PuntoInteresDTO obtenerPuntoInteresPorId(Integer id) {
        log.info("Consultando punto de interés ID: {}", id);

        PuntoInteres punto = puntoInteresRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Punto de interés no encontrado con ID: " + id));

        return convertirAPuntoInteresDTO(punto);
    }

    public List<PuntoInteresDTO> listarPuntosInteres() {
        log.info("Listando todos los puntos de interés");

        return puntoInteresRepository.findAll().stream()
                .map(this::convertirAPuntoInteresDTO)
                .collect(Collectors.toList());
    }

    public List<PuntoInteresDTO> buscarPorNombre(String nombre) {
        log.info("Buscando puntos por nombre: {}", nombre);

        return puntoInteresRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(this::convertirAPuntoInteresDTO)
                .collect(Collectors.toList());
    }

    public DireccionDTO crearDireccion(DireccionDTO dto) {
        log.info("Creando dirección");

        boolean existe = direccionRepository.existsByComponentes(
                dto.getViaPrincipal(),
                dto.getNumeroVia(),
                dto.getNumeroUno(),
                dto.getNumeroDos(),
                dto.getComplemento()
        );

        if (existe) {
            log.warn("La dirección ya existe");
            throw new RuntimeException("La dirección ya existe");
        }

        Direccion direccion = Direccion.builder()
                .viaPrincipal(dto.getViaPrincipal())
                .numeroVia(dto.getNumeroVia())
                .letraUno(dto.getLetraUno())
                .bis(dto.getBis())
                .cardinalidadUno(dto.getCardinalidadUno())
                .numeroUno(dto.getNumeroUno())
                .letraDos(dto.getLetraDos())
                .cardinalidadDos(dto.getCardinalidadDos())
                .numeroDos(dto.getNumeroDos())
                .complemento(dto.getComplemento())
                .build();

        Direccion guardada = direccionRepository.save(direccion);
        log.info("Dirección creada con ID: {}", guardada.getId());

        return convertirADireccionDTO(guardada);
    }

    public DireccionDTO obtenerDireccionPorId(Integer id) {
        log.info("Consultando dirección ID: {}", id);

        Direccion direccion = direccionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dirección no encontrada con ID: " + id));

        return convertirADireccionDTO(direccion);
    }

    public Map<String, Object> obtenerEstadisticasCache() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("geocodificacionesEnCache", cacheGeocode.size());
        stats.put("direccionesEnCache", cacheReverse.size());
        stats.put("totalEntradas", cacheGeocode.size() + cacheReverse.size());
        stats.put("cacheActivo", cacheEnabled);
        return stats;
    }

    public void limpiarCache() {
        cacheGeocode.clear();
        cacheReverse.clear();
        log.info("🧹 Cache limpiado");
    }

    private Direccion crearOBuscarDireccion(DireccionDTO dto) {
        Optional<Direccion> existente = direccionRepository.findByComponentesExactos(
                dto.getViaPrincipal(),
                dto.getNumeroVia(),
                dto.getNumeroUno(),
                dto.getNumeroDos()
        );

        if (existente.isPresent()) {
            return existente.get();
        }

        Direccion direccion = Direccion.builder()
                .viaPrincipal(dto.getViaPrincipal())
                .numeroVia(dto.getNumeroVia())
                .letraUno(dto.getLetraUno())
                .bis(dto.getBis())
                .cardinalidadUno(dto.getCardinalidadUno())
                .numeroUno(dto.getNumeroUno())
                .letraDos(dto.getLetraDos())
                .cardinalidadDos(dto.getCardinalidadDos())
                .numeroDos(dto.getNumeroDos())
                .complemento(dto.getComplemento())
                .build();

        return direccionRepository.save(direccion);
    }

    private String safeDecode(String value) {
        if (value == null || value.isBlank()) {
            return value;
        }

        try {
            String decoded = URLDecoder.decode(value, StandardCharsets.UTF_8);
            if (decoded.contains("%")) {
                decoded = URLDecoder.decode(decoded, StandardCharsets.UTF_8);
            }
            return decoded;
        } catch (Exception e) {
            return value;
        }
    }

    private String construirQuery(GeocodeRequestDTO request) {
        String direccion = safeDecode(request.getDireccion());
        String ciudad = safeDecode(request.getCiudad());
        String pais = safeDecode(request.getPais());

        StringBuilder query = new StringBuilder(direccion);

        if (ciudad != null && !ciudad.isBlank()) {
            query.append(", ").append(ciudad);
        }
        if (pais != null && !pais.isBlank()) {
            query.append(", ").append(pais);
        }

        return query.toString();
    }

    private String generarCacheKey(Object... params) {
        return String.join("_",
                Arrays.stream(params)
                        .map(String::valueOf)
                        .toArray(String[]::new)
        );
    }

    private DireccionDTO convertirNominatimADireccionDTO(NominatimResponse response) {
        NominatimResponse.Address address = response.getAddress();

        DireccionDTO dto = new DireccionDTO();
        dto.setDireccionCompleta(response.getDisplay_name());
        return dto;
    }

    private DireccionDTO convertirADireccionDTO(Direccion direccion) {
        DireccionDTO dto = DireccionDTO.builder()
                .id(direccion.getId())
                .viaPrincipal(direccion.getViaPrincipal())
                .numeroVia(direccion.getNumeroVia())
                .letraUno(direccion.getLetraUno())
                .bis(direccion.getBis())
                .cardinalidadUno(direccion.getCardinalidadUno())
                .numeroUno(direccion.getNumeroUno())
                .letraDos(direccion.getLetraDos())
                .cardinalidadDos(direccion.getCardinalidadDos())
                .numeroDos(direccion.getNumeroDos())
                .complemento(direccion.getComplemento())
                .build();

        dto.setDireccionCompleta(dto.buildDireccionCompleta());
        return dto;
    }

    private PuntoInteresDTO convertirAPuntoInteresDTO(PuntoInteres punto) {
        PuntoInteresDTO dto = PuntoInteresDTO.builder()
                .id(punto.getId())
                .nombre(punto.getNombre())
                .descripcion(punto.getDescripcion())
                .imgPun(punto.getImgPun())
                .idDireccion(punto.getIdDireccion())
                .build();

        if (punto.getIdDireccion() != null) {
            direccionRepository.findById(punto.getIdDireccion()).ifPresent(direccion -> {
                DireccionDTO direccionDTO = convertirADireccionDTO(direccion);
                dto.setDireccion(direccionDTO);
                dto.setDireccionCompleta(direccionDTO.getDireccionCompleta());
            });
        }

        return dto;
    }
}