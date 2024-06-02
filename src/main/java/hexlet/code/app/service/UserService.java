package hexlet.code.app.service;

import hexlet.code.app.dto.user.UserCreateDTO;
import hexlet.code.app.dto.user.UserUpdateDTO;
import hexlet.code.app.mapper.UserMapper;
import hexlet.code.app.repository.UserRepository;
import hexlet.code.app.dto.user.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsManager {
    private static final String NOT_FOUND_MESSAGE = "User not found";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(NOT_FOUND_MESSAGE));
    }

    public UserDTO create(UserCreateDTO data) {
        var user = userMapper.map(data);
        userRepository.save(user);
        return userMapper.map(user);
    }

    public List<UserDTO> getAll() {
        var users = userRepository.findAll()
                .stream()
                .map(userMapper::map)
                .toList();
        return users;
    }

    public Long countAll() {
        return userRepository.count();
    }

    public UserDTO findById(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow();
        return userMapper.map(user);
    }

    public UserDTO update(Long id, UserUpdateDTO userData) {
        var currentUser = userRepository.findById(id)
                .orElseThrow();

        userMapper.update(userData, currentUser);
        userRepository.save(currentUser);
        UserDTO userDTO = userMapper.map(currentUser);
        return userDTO;
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void createUser(UserDetails user) {
        throw new UnsupportedOperationException("Unimplemented method 'createUser'");
    }

    @Override
    public void updateUser(UserDetails user) {
        throw new UnsupportedOperationException("Unimplemented method 'updateUser'");
    }

    @Override
    public void deleteUser(String username) {
        throw new UnsupportedOperationException("Unimplemented method 'deleteUser'");
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        throw new UnsupportedOperationException("Unimplemented method 'changePassword'");
    }

    @Override
    public boolean userExists(String username) {
        throw new UnsupportedOperationException("Unimplemented method 'userExists'");
    }
}
