package uni.fis.usuario.service;

import uni.fis.usuario.dto.UserDto;
import uni.fis.usuario.dto.request.UserRequest;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<UserDto> findAll();

    Optional<UserDto> findById(int id);

    UserDto save(UserRequest user);

    Optional<UserDto> findByEmail(String email);

}
