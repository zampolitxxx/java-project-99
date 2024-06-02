package hexlet.code.app.controller.api;

import hexlet.code.app.dto.user.UserCreateDTO;
import hexlet.code.app.dto.user.UserDTO;
import hexlet.code.app.dto.user.UserUpdateDTO;
import hexlet.code.app.service.UserService;
import hexlet.code.app.util.UserUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
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
@RequestMapping("/api/users")
public class UsersController {

    private static final String NOT_FOUND_MESSAGE = "User not found";

    @Autowired
    private UserService userService;

    @Autowired
    private UserUtils userUtils;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAll() {
        var users = userService.getAll();
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(userService.countAll()))
                .body(users);
    }

    @GetMapping("/{id}")
    public UserDTO getById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO create(@Valid @RequestBody UserCreateDTO data) {
        return userService.create(data);
    }

    @PutMapping("/{id}")
    public UserDTO update(@PathVariable Long id, @Valid @RequestBody UserUpdateDTO userData) {
        var currentUser = userUtils.getCurrentUser();

        if (!currentUser.getId().equals(id)) {
            throw new AccessDeniedException("");
        }
        return userService.update(id, userData);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        var currentUser = userUtils.getCurrentUser();

        if (!currentUser.getId().equals(id)) {
            throw new AccessDeniedException("");
        }
        userService.delete(id);

    }
}
