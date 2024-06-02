package hexlet.code.app.service;

import hexlet.code.app.dto.taskStatus.TaskStatusDTO;
import hexlet.code.app.dto.taskStatus.TaskStatusCreateDTO;
import hexlet.code.app.dto.taskStatus.TaskStatusUpdateDTO;
import hexlet.code.app.repository.TaskStatusRepository;
import hexlet.code.app.mapper.TaskStatusMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TaskStatusService {

    @Autowired
    private TaskStatusMapper taskStatusMapper;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    public Long countAll() {
        return taskStatusRepository.count();
    }

    public List<TaskStatusDTO> getAll() {
        var a = taskStatusRepository.findAll();
        var b = a.stream()
                .map(taskStatusMapper::map)
                .toList();
        return b;
    }

    public TaskStatusDTO findById(Long id) {
        var taskStatus = taskStatusRepository.findById(id)
                .orElseThrow();
        return taskStatusMapper.map(taskStatus);
    }

    public TaskStatusDTO create(TaskStatusCreateDTO data) {
        var taskStatus = taskStatusMapper.map(data);
        taskStatusRepository.save(taskStatus);
        return taskStatusMapper.map(taskStatus);
    }

    public TaskStatusDTO update(Long id, TaskStatusUpdateDTO data) {
        var taskStatus = taskStatusRepository.findById(id)
                .orElseThrow();
        taskStatusMapper.update(data, taskStatus);
        taskStatusRepository.save(taskStatus);
        return taskStatusMapper.map(taskStatus);
    }

    public void delete(Long id) {
        var taskStatus = taskStatusRepository.findById(id)
                .orElseThrow();
        if (taskStatus.getTasks().isEmpty()) {
            taskStatusRepository.deleteById(id);
        }
    }
}
