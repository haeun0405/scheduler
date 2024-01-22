package org.sparta.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // 모든 일정 조회
    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> getAllTasks() {
        List<Task> tasks = taskService.getAllTasks();
        List<TaskResponseDTO> dtos = tasks.stream()
                .map(TaskResponseDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // 특정 일정 조회
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> getTaskById(@PathVariable Long id) {
        Task task = taskService.getTaskById(id);
        TaskResponseDTO taskResponseDTO = new TaskResponseDTO(task);
        return ResponseEntity.ok(taskResponseDTO);
    }

    // 신규 일정 생성
    @PostMapping
    public ResponseEntity<TaskResponseDTO> createTask(@RequestBody TaskDTO taskDTO) {
        Task task = taskDTO.toEntity();
        Task createdTask = taskService.createTask(task);
        TaskResponseDTO createdTaskResponseDTO = new TaskResponseDTO(createdTask);
        return new ResponseEntity<>(createdTaskResponseDTO, HttpStatus.CREATED);
    }


    // 일정 수정
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> updateTask(@PathVariable Long id, @RequestBody TaskUpdateRequestDTO taskUpdateRequest) {
        Task taskToUpdate = taskUpdateRequest.toEntity();
        taskToUpdate.setId(id);
        Task updatedTask = taskService.updateTask(id, taskToUpdate, taskUpdateRequest.getPassword());
        TaskResponseDTO updatedTaskResponseDTO = new TaskResponseDTO(updatedTask);
        return ResponseEntity.ok(updatedTaskResponseDTO);
    }

    // 일정 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String password = body.get("password");
        taskService.deleteTask(id, password);
        return ResponseEntity.ok().build();
    }
}
