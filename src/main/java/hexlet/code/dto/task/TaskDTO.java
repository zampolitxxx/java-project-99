package hexlet.code.dto.task;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Setter
@Getter
public class TaskDTO {
    private Long id;

    @JsonProperty("title")
    private String name;

    @JsonProperty("content")
    private String description;

    private Long index;

    private String status;

    @JsonProperty("assignee_id")
    private Long assigneeId;

    private LocalDate createdAt;

    @JsonProperty("taskLabelIds")
    private Set<Long> labelIds;
}
