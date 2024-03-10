package hexlet.code.app.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
//    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate createdAt;
}
