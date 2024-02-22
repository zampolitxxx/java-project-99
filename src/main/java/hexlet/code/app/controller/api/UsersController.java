package hexlet.code.app.controller.api;

import hexlet.code.app.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import hexlet.code.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api")
public class UsersController {
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/users")
    public User create(@RequestBody User user) {
        userRepository.save(user);
        return user;
    }

    @GetMapping("/users")
    public List<User> show() {
        return userRepository.findAll();
    }

    @GetMapping("/users/{id}")
    public Optional<User> show(@RequestParam Long id) {
        return userRepository.findById(id);
    }
}
