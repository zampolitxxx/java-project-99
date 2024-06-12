package hexlet.code.mapper;

import hexlet.code.dto.task.TaskCreateDTO;
import hexlet.code.dto.task.TaskDTO;
import hexlet.code.dto.task.TaskUpdateDTO;
import hexlet.code.model.Task;
import hexlet.code.repository.TaskStatusRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

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

    public abstract Task map(TaskCreateDTO model);

    public abstract TaskDTO map(Task model);

    public abstract Task update(TaskUpdateDTO source, @MappingTarget Task target);
}
