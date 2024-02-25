package hexlet.code.app.component;

import hexlet.code.app.mapper.UserMapper;
import hexlet.code.app.dto.UserCreateDTO;
import hexlet.code.app.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DataInit implements ApplicationRunner {
    private static final String USER_EMAIL = "hexlet@example.com";
    private static final String USER_PASSWORD = "qwerty";

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final UserMapper userMapper;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (userRepository.findByEmail(USER_EMAIL).isEmpty()) {
            var userData = new UserCreateDTO();
            userData.setEmail(USER_EMAIL);
            userData.setPassword(USER_PASSWORD);
            var user = userMapper.map(userData);
            userRepository.save(user);
        }
    }
}
