package hexlet.code.app.mapper;

import hexlet.code.app.dto.task.TaskCreateDTO;
import hexlet.code.app.dto.task.TaskDTO;
import hexlet.code.app.dto.task.TaskUpdateDTO;
import hexlet.code.app.exception.ResourceNotFoundException;
import hexlet.code.app.model.Label;
import hexlet.code.app.model.Task;
import hexlet.code.app.repository.LabelRepository;
import hexlet.code.app.repository.TaskStatusRepository;
import hexlet.code.app.repository.UserRepository;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(
        uses = {JsonNullableMapper.class, ReferenceMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class TaskMapper {

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private UserRepository userRepository;

    private static final String STATUS_NOT_FOUND_MESSAGE = "Status not found";

    private static final String LABEL_NOT_FOUND_MESSAGE = "Label not found";

    private static final String USER_NOT_FOUND_MESSAGE = "User not found";

    public abstract Task map(TaskCreateDTO dto);

    @Mapping(target = "assigneeId", source = "assignee.id")
    @Mapping(target = "status", source = "taskStatus.slug")
    @Mapping(target = "labelIds", expression = "java(labelsToLabelIds(model.getLabels()))")
    public abstract TaskDTO map(Task model);

    public abstract void update(TaskUpdateDTO dto, @MappingTarget Task model);

    @AfterMapping
    public void afterUpdateMapping(TaskUpdateDTO dto, @MappingTarget Task model) {
        if (dto.getStatus() != null) {
            setStatus(dto.getStatus().get(), model);
        }
        if (dto.getLabelIds() != null) {
            setLabels(dto.getLabelIds().get(), model);
        }
        if (dto.getAssigneeId() != null) {
            setAssignee(dto.getAssigneeId().get(), model);
        }
    }

    @AfterMapping
    public void afterCreateMapping(TaskCreateDTO dto, @MappingTarget Task model) {
        setStatus(dto.getStatus(), model);
        setLabels(dto.getLabelIds(), model);
        if (dto.getAssigneeId() != null) {
            setAssignee(dto.getAssigneeId(), model);
        }
    }

    private void setStatus(String slug, Task model) {
        var status = taskStatusRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException(STATUS_NOT_FOUND_MESSAGE));
        model.setTaskStatus(status);
    }

    private void setLabels(Set<Long> labelIds, Task model) {
        var labels = labelRepository.findAllByIdIn(labelIds);

        if (labelIds.size() != labels.size()) {
            throw new ResourceNotFoundException(LABEL_NOT_FOUND_MESSAGE);
        }

        model.setLabels(labels);
    }

    private void setAssignee(Long id, Task model) {
        var assignee = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND_MESSAGE));
        model.setAssignee(assignee);
    }

    public Set<Long> labelsToLabelIds(Set<Label> labels) {
        return labels.stream()
                .map(Label::getId)
                .collect(Collectors.toSet());
    }
}