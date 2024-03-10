package hexlet.code.app.dto.task;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

import java.util.Set;

@Setter
@Getter
public class TaskUpdateDTO {

    @NotBlank
    private JsonNullable<String> title;
    @NotBlank
    private JsonNullable<String> content;
    @NotBlank
    private JsonNullable<String> status;
    @NotBlank
    private JsonNullable<Long> assigneeId;
    @NotBlank
    private JsonNullable<Set<Long>> taskLabelIds;
}
