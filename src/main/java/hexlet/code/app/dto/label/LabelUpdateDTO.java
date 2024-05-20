package hexlet.code.app.dto.label;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Setter
@Getter
public class LabelUpdateDTO {
    @Size(min = 3, max = 1000)
    private JsonNullable<String> name;
}
