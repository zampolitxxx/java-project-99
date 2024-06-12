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
import hexlet.code.repository.UserRepository;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

    @Mappings({
            @Mapping(source = "status", target = "taskStatus"),
            @Mapping(source = "assigneeId", target = "assignee.id"),
            @Mapping(source = "labelIds", target = "labels"),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "id", ignore = true)
    })
    public abstract Task update(TaskDTO source, @MappingTarget Task target);

    public TaskStatus toEntity(String slug) {
        return taskStatusRepository.findBySlug(slug)
                .orElseThrow();
    }

    public User toEntity(Long assigneeId) {
//        return new User().setId(assigneeId);
        User user = new User();
        user.setId(assigneeId);
        return user;

    }

    public List<Label> toEntity(Set<Long> labelIds) {
        if (labelIds == null) {
            return null;
        }
//        return labelIds.stream()
//                .map(labelId -> new Label().setId(labelId))
//                .collect(Collectors.toSet());
        List<Label> result = new ArrayList<>() {
        };
        for (var l : labelIds) {
            Label label = new Label();
            label.setId(l);
            result.add(label);
        }
        return result;
    }
}
