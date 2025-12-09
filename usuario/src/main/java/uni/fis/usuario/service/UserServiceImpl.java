package uni.fis.usuario.service;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uni.fis.usuario.dto.UserDto;
import uni.fis.usuario.dto.request.UserRequest;
import uni.fis.usuario.entity.DocumentoEntity;
import uni.fis.usuario.entity.PasswordEntity;
import uni.fis.usuario.entity.RolEntity;
import uni.fis.usuario.entity.UsuarioEntity;
import uni.fis.usuario.error.InvalidUserDataException;
import uni.fis.usuario.error.UserAlreadyExistsException;
import uni.fis.usuario.error.UserNotFoundException;
import uni.fis.usuario.mapper.DocumentoMapper;
import uni.fis.usuario.mapper.UserMapper;
import uni.fis.usuario.repository.DocumentoRepository;
import uni.fis.usuario.repository.PasswordRepository;
import uni.fis.usuario.repository.RolRepository;
import uni.fis.usuario.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordRepository passwordRepository;
    private final DocumentoRepository documentoRepository;
    private final RolRepository rolRepository;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordRepository passwordRepository,
                           EntityManager entityManager,
                           DocumentoRepository documentoRepository,
                           RolRepository rolRepository) {
        this.userRepository = userRepository;
        this.passwordRepository = passwordRepository;
        this.documentoRepository = documentoRepository;
        this.rolRepository = rolRepository;
    }

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::entityToDto)
                .toList();
    }

    @Override
    public Optional<UserDto> findById(int id) {
        UsuarioEntity entity = userRepository.findById(id)
                .orElseThrow(() -> UserNotFoundException.byId(id));
        return Optional.of(UserMapper.entityToDto(entity));
    }

    @Override
    @Transactional
    public UserDto save(UserRequest user) {

        //Validación logica previa
        validateRequiredFields(user);

        // Email único hmm
        if (userRepository.existsByEmail(user.email())) {
            throw UserAlreadyExistsException.byEmail(user.email());
        }

        try {
            // Creamos el documento
            DocumentoEntity documento = DocumentoMapper.toEntityForCreate(user.documento());
            documento = documentoRepository.save(documento);

            // Obtener rol
            RolEntity rol = rolRepository.findById(user.idRol())
                    .orElseThrow(() -> new InvalidUserDataException("Rol no encontrado"));

            // Crear usuario
            UsuarioEntity entity = UserMapper.requestToEntity(user, documento, rol);
            UsuarioEntity userCreated = userRepository.save(entity);

            PasswordEntity password = new PasswordEntity(
                    null,
                    LocalDateTime.now(),
                    user.password(),
                    userCreated.getId()
            );
            passwordRepository.save(password);

            // 7. Retornar DTO del usuario creado
            return UserMapper.entityToDto(userCreated);

        } catch (DataIntegrityViolationException e) {

            // Documento duplicado
            if (e.getMessage() != null && e.getMessage().contains("documento")) {
                throw UserAlreadyExistsException.byDocument(user.documento().numeroDocumento());
            }

            throw new InvalidUserDataException("Error de integridad: " + e.getMessage());
        }
    }


    @Override
    public Optional<UserDto> findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(UserMapper::entityToDto);
    }

    private void validateRequiredFields(UserRequest request) {
        log.info("Validando campos requeridos {}", request);
        if (request.name() == null || request.name().trim().isEmpty()) {
            throw InvalidUserDataException.missingField("name");
        }
        if (request.apellido1() == null || request.apellido1().trim().isEmpty()) {
            throw InvalidUserDataException.missingField("apellido1");
        }
        if (request.fechaNacimiento() == null || request.fechaNacimiento().trim().isEmpty()) {
            throw InvalidUserDataException.missingField("fechaNacimiento");
        }
        if (request.documento() == null || request.documento().numeroDocumento().trim().isEmpty()) {
            throw InvalidUserDataException.missingField("documento");
        }
        if (request.email() == null || request.email().trim().isEmpty()) {
            throw InvalidUserDataException.missingField("email");
        }
        if (request.password() == null || request.password().trim().isEmpty()) {
            throw InvalidUserDataException.missingField("password");
        }

        // Validar formato de email
        if (!isValidEmail(request.email())) {
            throw InvalidUserDataException.invalidField("email", request.email());
        }
    }

    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

}