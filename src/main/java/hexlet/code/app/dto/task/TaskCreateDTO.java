package hexlet.code.app.dto.task;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;


@Setter
@Getter
public class TaskCreateDTO {
    private Long index;

    @JsonProperty("title")
    @NotBlank
    private String name;

    @JsonProperty("content")
    private String description;

    @JsonProperty("assignee_id")
    private Long assigneeId;

    @NotNull
    private String status;

    @JsonProperty("taskLabelIds")
    private Set<Long> labelIds;
}
