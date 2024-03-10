package hexlet.code.app.dto.task;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class TaskDTO {
    private Long id;
    private String title;
    private String content;
    private Integer index;
    private String status;
    private Long assigneeId;
    private LocalDate createdAt;
}
