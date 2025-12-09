package uni.fis.usuario.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import uni.fis.usuario.entity.PasswordEntity;
import uni.fis.usuario.error.InvalidCredentialsException;
import uni.fis.usuario.error.UserNotFoundException;
import uni.fis.usuario.repository.PasswordRepository;
import uni.fis.usuario.repository.UserRepository;

import java.util.Optional;

@Service
@Slf4j
public class PasswordServiceImpl implements PasswordService {

    private final PasswordRepository repository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public PasswordServiceImpl(PasswordRepository repository,
            UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public boolean validate(String email, String password) {
        // Buscamos el usuario
        log.info("Entramos a validate -- y vamos a buscar por email");
        var usuario = userRepository.findByEmail(email);
        log.info("Usuario encontrado: " + usuario);
        if (usuario.isEmpty()) {
            throw UserNotFoundException.byEmail(email); // ← 404
        }

        // Buscamos la última contraseña
        Optional<PasswordEntity> ultima = repository.findMasRecienteById(usuario.get().getId());

        if (ultima.isEmpty()) {
            throw new InvalidCredentialsException("No se encontró contraseña para el usuario");
        }

        boolean isValid = passwordEncoder.matches(password, ultima.get().getChars());

        if (!isValid) {
            throw InvalidCredentialsException.invalid(); // ← 401
        }

        return true;
    }
}