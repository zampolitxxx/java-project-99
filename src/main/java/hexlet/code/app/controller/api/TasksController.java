package hexlet.code.app.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/tasks")
public class TasksController {

    public ResponseEntity<List<TaskDTO>> index() {

    }
}
