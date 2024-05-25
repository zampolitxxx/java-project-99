package hexlet.code.app.dto.taskStatus;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TaskStatusDTO {
    private Long id;
    private String name;
    private String slug;
    private LocalDate createdAt;
}
