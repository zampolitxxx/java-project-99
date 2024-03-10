package hexlet.code.app.dto.task;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class TaskCreateDTO {
    @NotNull
    private String title;

    @NotNull
    private String content;

    private Integer index;

    @NotNull
    private String status;

    @JsonProperty("assignee_id")
    @NotNull
    private Long assigneeId;
}
