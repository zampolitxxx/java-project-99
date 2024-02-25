package hexlet.code.app.controller.api;

import hexlet.code.app.dto.UserCreateDTO;
import hexlet.code.app.dto.UserDTO;
import hexlet.code.app.dto.UserUpdateDTO;
import hexlet.code.app.exception.ResourceForbiddenException;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.UserRepository;
import hexlet.code.app.mapper.UserMapper;
import hexlet.code.app.exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UsersController {

    private static final String NOT_FOUND_MESSAGE = "User not found";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    UserDTO create(@RequestBody UserCreateDTO userData) {
        User user = userMapper.map(userData);
        userRepository.save(user);
        UserDTO userDTO = userMapper.map(user);
        return userDTO;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> index() {
        var users = userRepository.findAll()
                .stream()
                .map(userMapper::map)
                .toList();
        return ResponseEntity.ok()
                .body(users);
    }

    @GetMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    UserDTO show(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_MESSAGE));
        return userMapper.map(user);
    }

    @PutMapping("/users/{id}")
    public UserDTO update(@PathVariable Long id, @Valid @RequestBody UserUpdateDTO userData) {
        var currentUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_MESSAGE));
        userMapper.update(userData, currentUser);
        userRepository.save(currentUser);
        UserDTO userDTO = userMapper.map(currentUser);
        return userDTO;
    }

    @DeleteMapping("/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        User currentUser = userRepository.findById(id).get();
        if (!currentUser.getId().equals(id)) {
            throw new ResourceForbiddenException();
        }
        userRepository.deleteById(id);
    }
}
