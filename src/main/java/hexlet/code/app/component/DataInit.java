package hexlet.code.app.component;

import hexlet.code.app.mapper.TaskStatusMapper;
import hexlet.code.app.dto.TaskStatusCreateDTO;
import hexlet.code.app.mapper.UserMapper;
import hexlet.code.app.dto.UserCreateDTO;
import hexlet.code.app.repository.LabelRepository;
import hexlet.code.app.repository.TaskStatusRepository;
import hexlet.code.app.repository.UserRepository;
import hexlet.code.app.model.Label;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
public class DataInit implements ApplicationRunner {
    private static final String USER_EMAIL = "hexlet@example.com";
    private static final String USER_PASSWORD = "qwerty";

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final UserMapper userMapper;

    @Autowired
    private final TaskStatusRepository taskStatusRepository;

    @Autowired
    private final TaskStatusMapper taskStatusMapper;

    @Autowired
    private final LabelRepository labelRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (userRepository.findByEmail(USER_EMAIL).isEmpty()) {
            var userData = new UserCreateDTO();
            userData.setEmail(USER_EMAIL);
            userData.setPassword(USER_PASSWORD);
            var user = userMapper.map(userData);
            userRepository.save(user);
        }

        if (taskStatusRepository.count() == 0) {
            var taskStatuses = Map.of(
                    "Draft", "draft",
                    "ToReview", "to_review",
                    "ToBeFixed", "to_be_fixed",
                    "ToPublish", "to_publish",
                    "Published", "published"
            ).entrySet()
                    .stream()
                    .map(entry -> {
                        var taskStatusData = new TaskStatusCreateDTO();
                        taskStatusData.setName(entry.getKey());
                        taskStatusData.setSlug(entry.getValue());
                        return taskStatusMapper.map(taskStatusData);
                    });
            taskStatuses.forEach(taskStatusRepository::save);
        }

        if (labelRepository.count() == 0) {
            var labels = List.of("feature", "bug")
                    .stream()
                    .map(item -> {
                        var label = new Label();
                        label.setName(item);
                        return label;
                    });
            labels.forEach(labelRepository::save);
        }
    }
}
