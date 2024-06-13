package hexlet.code.mapper;

import hexlet.code.dto.task.TaskCreateDTO;
import hexlet.code.dto.task.TaskDTO;
import hexlet.code.dto.task.TaskUpdateDTO;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskStatusRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
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

            @Mapping(source = "assigneeId", target = "assignee", qualifiedByName = "assigneeIdToUsers")
            @Mapping(target = "createdAt", ignore = true)
            @Mapping(source = "status", target = "taskStatus", qualifiedByName = "statusToTaskStatus")
            @Mapping(source = "labelIds", target = "labels", qualifiedByName = "idsToLabels")
    public abstract Task map(TaskCreateDTO model);


            @Mapping(source = "assignee.id", target = "assigneeId")
            @Mapping(target = "createdAt", ignore = true)
            @Mapping(source = "taskStatus.slug", target = "status")
            @Mapping(source = "labels", target = "labelIds", qualifiedByName = "labelsToIds")
    public abstract TaskDTO map(Task model);

    public abstract Task update(TaskUpdateDTO source, @MappingTarget Task target);

    @Named("statusToTaskStatus")
    public TaskStatus statusToTaskStatus(String status) {
        return taskStatusRepository.findBySlug(status)
                .orElseThrow();
    }

    @Named("labelsToIds")
    public Set<Long> labelsToIds(Set<Label> labels) {
        return labels == null ? new HashSet<>()
                : labels.stream()
                .map(Label::getId)
                .collect(Collectors.toSet());
    }

    @Named("idsToLabels")
    public Set<Label> idsToLabels(Set<Long> labelIds) {
        return labelIds == null ? new HashSet<>() : labelRepository.findAllByIdIn(labelIds);
    }

    @Named("assigneeIdToUsers")
    public User assigneeIdToUsers(Long assigneeId) {
        if (assigneeId == null) {
            return null;
        }
        return new User().setId(assigneeId);
    }
}
