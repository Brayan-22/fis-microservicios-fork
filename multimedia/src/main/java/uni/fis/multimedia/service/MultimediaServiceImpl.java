package uni.fis.multimedia.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import uni.fis.multimedia.dto.MultimediaResponseDTO;
import uni.fis.multimedia.entity.MultimediaEntity;
import uni.fis.multimedia.repository.MultimediaRepository;
import uni.fis.multimedia.exception.MultimediaExceptions.*;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class MultimediaServiceImpl implements MultimediaService {

    private static final Logger logger = LoggerFactory.getLogger(MultimediaServiceImpl.class);

    private final MultimediaRepository multimediaRepository;

    private static final String UPLOAD_DIR = "/uploads/";

    // Tamaño máximo permitido (15 MB)
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;

    // Tipos permitidos
    private static final Set<String> TIPOS_PERMITIDOS = Set.of(
            "image/png",
            "image/jpeg",
            "application/pdf",
            "audio/mpeg",
            "video/mp4"
    );

    public MultimediaServiceImpl(MultimediaRepository multimediaRepository) {
        this.multimediaRepository = multimediaRepository;
    }

    public boolean escanear(byte[] data) {
        logger.info("Iniciando escaneo antivirus para archivo de {} bytes", data.length);

        try (Socket socket = new Socket("clamav", 3310)) {
            OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();

            out.write("zINSTREAM\0".getBytes());

            int offset = 0;
            while (offset < data.length) {
                int chunk = Math.min(2048, data.length - offset);

                byte[] size = ByteBuffer.allocate(4).putInt(chunk).array();
                out.write(size);

                out.write(data, offset, chunk);
                offset += chunk;
            }

            out.write(ByteBuffer.allocate(4).putInt(0).array());
            out.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String result = reader.readLine();

            boolean ok = result != null && result.contains("OK");

            if (ok) logger.info("Escaneo antivirus completado: ARCHIVO LIMPIO");
            else logger.warn("Escaneo antivirus completado: ARCHIVO INFECTADO - {}", result);

            return ok;

        } catch (IOException e) {
            logger.error("Error de conexión con ClamAV: {}", e.getMessage(), e);
            throw new ClamAVConnectionException("No se pudo conectar con el servicio antivirus", e);
        } catch (Exception e) {
            logger.error("Error inesperado durante el escaneo antivirus: {}", e.getMessage(), e);
            throw new VirusScanException("Error durante el escaneo de seguridad del archivo", e);
        }
    }

    @Override
    public MultimediaEntity guardarArchivo(MultipartFile archivo) {
        logger.info("Iniciando proceso de guardado para archivo: {}", archivo.getOriginalFilename());

        if (archivo.isEmpty()) {
            logger.warn("Intento de guardar archivo vacío");
            throw new EmptyFileException("El archivo no puede estar vacío");
        }

        String nombreOriginal = archivo.getOriginalFilename();
        if (nombreOriginal == null || nombreOriginal.trim().isEmpty()) {
            logger.warn("Intento de guardar archivo sin nombre");
            throw new InvalidFileNameException("El archivo debe tener un nombre válido");
        }

        nombreOriginal = nombreOriginal.toLowerCase();

        // VALIDAR MIME TYPE
        String tipo = archivo.getContentType();
        if (!TIPOS_PERMITIDOS.contains(tipo)) {
            logger.warn("Tipo de archivo no permitido: {}", tipo);
            throw new InvalidFileTypeException(
                    "Tipo de archivo no permitido: " + tipo +
                    ". Tipos permitidos: PNG, JPG, JPEG, PDF, MP3, MP4"
            );
        }

        // VALIDAR EXTENSIÓN
        if (!(nombreOriginal.endsWith(".png") ||
                nombreOriginal.endsWith(".jpg") ||
                nombreOriginal.endsWith(".jpeg") ||
                nombreOriginal.endsWith(".pdf") ||
                nombreOriginal.endsWith(".mp3") ||
                nombreOriginal.endsWith(".mp4"))) {

            logger.warn("Extensión no permitida: {}", nombreOriginal);
            throw new InvalidFileTypeException(
                    "Extensión no permitida. Permitidas: .png, .jpg, .jpeg, .pdf, .mp3, .mp4"
            );
        }

        // VALIDAR TAMAÑO MÁXIMO (15 MB)
        if (archivo.getSize() > MAX_FILE_SIZE) {
            logger.warn("Archivo demasiado grande: {} bytes. Límite: {}", archivo.getSize(), MAX_FILE_SIZE);
            throw new FileTooLargeException("El archivo excede el tamaño máximo permitido de 15 MB");
        }

        // Crear directorio
        Path uploadPath = Paths.get(UPLOAD_DIR);
        try {
            Files.createDirectories(uploadPath);
        } catch (IOException e) {
            logger.error("Error creando directorio {}", UPLOAD_DIR, e);
            throw new FileStorageException("Error al crear directorio de almacenamiento", e);
        }

        File destino = null;

        try {
            byte[] bytes = archivo.getBytes();

            // ESCANEAR CON CLAMAV
            if (!escanear(bytes)) {
                throw new InfectedFileException("El archivo contiene un virus y fue bloqueado");
            }

            String nombreGuardado = System.currentTimeMillis() + "_" + nombreOriginal;

            destino = new File(UPLOAD_DIR + nombreGuardado);
            archivo.transferTo(destino);

            String urlPublica = "/uploads/" + nombreGuardado;

            MultimediaEntity m = new MultimediaEntity();
            m.setUrl(urlPublica);
            m.setTipoArchivo(tipo);

            return multimediaRepository.save(m);

        } catch (IOException e) {
            logger.error("Error procesando archivo", e);
            throw new FileStorageException("Error al procesar el archivo", e);

        } catch (Exception e) {

            if (destino != null && destino.exists()) {
                destino.delete();
            }

            if (e instanceof InfectedFileException) {
                throw (InfectedFileException) e;
            }

            throw new FileStorageException("Error inesperado al procesar archivo", e);
        }
    }

    @Override
    public List<MultimediaEntity> findAll() {
        return multimediaRepository.findAll();
    }

    @Override
    public MultimediaResponseDTO obtenerImagen(Long id) {
        MultimediaEntity m = multimediaRepository.findMultimediaEntityById(id);

        if (m == null) {
            throw new MultimediaNotFoundException("Multimedia no encontrado con ID: " + id);
        }

        MultimediaResponseDTO dto = new MultimediaResponseDTO();
        dto.setId(m.getId());
        dto.setUrl(m.getUrl());
        dto.setTipoArchivo(m.getTipoArchivo());
        return dto;
    }

    // NUEVA EXCEPCIÓN PERSONALIZADA
    public static class FileTooLargeException extends RuntimeException {
        public FileTooLargeException(String msg) { super(msg); }
    }
}
