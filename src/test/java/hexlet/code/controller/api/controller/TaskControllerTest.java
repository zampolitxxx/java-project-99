package hexlet.code.controller.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.task.TaskCreateDTO;
import hexlet.code.dto.task.TaskUpdateDTO;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.controller.api.util.ModelGenerator;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import java.util.HashSet;
import java.util.Set;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private ModelGenerator modelGenerator;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private UserRepository userRepository;

    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor token;

    private Task testTask;
    private TaskStatus testTaskStatus;
    private User testUser;
    private Label testLabel;

    @BeforeEach
    public void setUp() {
        testUser = Instancio.of(modelGenerator.getUserModel()).create();
        userRepository.save(testUser);
        token = jwt().jwt(builder -> builder.subject(testUser.getEmail()));

        testTaskStatus = Instancio.of(modelGenerator.getTaskStatusModel()).create();
        taskStatusRepository.save(testTaskStatus);

        testLabel = Instancio.of(modelGenerator.getLabelModel()).create();
        labelRepository.save(testLabel);

        testTask = Instancio.of(modelGenerator.getTaskModel()).create();
        testTask.setTaskStatus(testTaskStatus);
        testTask.setAssignee(testUser);
        Set<Label> s = new HashSet<>();
        s.add(testLabel);
        testTask.setLabels(s);
    }

    @Test
    public void testGetAll() throws Exception {
        taskRepository.save(testTask);
        var result = mockMvc.perform(get("/api/tasks").with(token))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    @Test
    public void testGetAllWithTitleContains() throws Exception {
        taskRepository.save(testTask);
        String cont = taskRepository.findByName(testTask.getName()).get().getName();
        var result = mockMvc.perform(get("/api/tasks?titleCont=cont").with(token))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray().allSatisfy(element ->
                assertThatJson(element)
                        .and(v -> v.node("name").asString().containsIgnoringCase(cont))
        );
    }

    @Test
    public void testGetAllWithAssignee() throws Exception {
        taskRepository.save(testTask);
        Long id = taskRepository.findByName(testTask.getName()).get().getAssignee().getId();
        var path = "/api/tasks?assigneeId=" + id;
        var result = mockMvc.perform(get(path).with(token))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray().allSatisfy(element ->
                assertThatJson(element)
                        .and(v -> v.node("assignee_id").isEqualTo(id))
        );
    }

    @Test
    public void testGetById() throws Exception {
        taskRepository.save(testTask);
        var request = get("/api/tasks/{id}", testTask.getId()).with(token);
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        assertThatJson(body)
                .and(
                        v -> v.node("title").isEqualTo(testTask.getName()),
                        v -> v.node("content").isEqualTo(testTask.getDescription())
                );
    }

    @Test
    public void testCreate() throws Exception {
        var dto = new TaskCreateDTO();
        dto.setName("task_name");
        dto.setDescription("task description");
        dto.setAssigneeId(testUser.getId());
        dto.setStatus(testTaskStatus.getSlug());
        Set<Long> s = new HashSet<>();
        s.add(testLabel.getId());
        dto.setLabelIds(s);

        var request = post("/api/tasks")
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isCreated());

        var testTask = taskRepository.findByName(dto.getName()).get();

        assertThat(testTask).isNotNull();
        assertThat(testTask.getName()).isEqualTo(dto.getName());
        assertThat(testTask.getDescription()).isEqualTo(dto.getDescription());
        assertThat(testTask.getAssignee().getId()).isEqualTo(dto.getAssigneeId());
        assertThat(testTask.getTaskStatus().getSlug()).isEqualTo(dto.getStatus());
        assertThat(testTask.getLabels().iterator().next().getId())
                .isEqualTo(dto.getLabelIds().stream().findFirst().get());
    }

    @Test
    public void testPartialUpdate() throws Exception {
        taskRepository.save(testTask);
        var dto = new TaskUpdateDTO();
        dto.setName(JsonNullable.of("updated_name"));
        dto.setDescription(JsonNullable.of("updated description"));

        var request = put("/api/tasks/{id}", testTask.getId())
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        var task = taskRepository.findByName("updated_name").get();

        assertThat(task.getName()).isEqualTo("updated_name");
        assertThat(task.getDescription()).isEqualTo("updated description");
        assertThat(task.getAssignee().getUsername()).isEqualTo(testTask.getAssignee().getUsername());
        assertThat(task.getLabels().size()).isEqualTo(testTask.getLabels().size());
        assertThat(task.getTaskStatus().getSlug()).isEqualTo(testTask.getTaskStatus().getSlug());
    }

    @Test
    public void testDestroy() throws Exception {
        taskRepository.save(testTask);
        var request = delete("/api/tasks/{id}", testTask.getId())
                .with(token);
        mockMvc.perform(request)
                .andExpect(status().isNoContent());

        assertThat(taskRepository.existsById(testTask.getId())).isEqualTo(false);
    }

    @AfterEach
    public void clean() {
        taskRepository.deleteAll();
        labelRepository.deleteAll();
        taskStatusRepository.deleteAll();
        userRepository.deleteAll();
    }

}
