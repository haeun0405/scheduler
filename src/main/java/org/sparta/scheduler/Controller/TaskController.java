package org.sparta.scheduler.Controller;

import java.util.List;
import java.util.stream.Collectors;
import org.sparta.scheduler.Domain.Task;
import org.sparta.scheduler.Domain.User;
import org.sparta.scheduler.Dto.TaskDTO;
import org.sparta.scheduler.Dto.TaskResponseDTO;
import org.sparta.scheduler.Service.TaskServiceImpl;
import org.sparta.scheduler.Service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskServiceImpl taskService;
    private final UserServiceImpl userService;

    @Autowired
    public TaskController(TaskServiceImpl taskService, UserServiceImpl userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    // 모든 할일카드 조회
    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> getAllTasks() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        List<Task> tasks = taskService.getTasksByUser(username);
        List<TaskResponseDTO> dtos = tasks.stream()
                .map(TaskResponseDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // 특정 할일카드 조회
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> getTaskById(@PathVariable Long id) {
        Task task = taskService.getTaskById(id);
        TaskResponseDTO taskResponseDTO = new TaskResponseDTO(task);
        return ResponseEntity.ok(taskResponseDTO);
    }

    // 신규 할일 카드 생성
    @PostMapping
    public ResponseEntity<TaskResponseDTO> createTask(@RequestBody TaskDTO taskDto) {
        Task createdTask = taskService.createTask(taskDto);
        return ResponseEntity.ok(new TaskResponseDTO(createdTask));
    }


    // 할일카드 수정
    @PutMapping("/{taskId}")
    public ResponseEntity<TaskResponseDTO> updateTask(@PathVariable Long taskId, @RequestBody TaskDTO taskDTO) {
        Task updatedTask = taskService.updateTask(taskId, taskDTO);
        return ResponseEntity.ok(new TaskResponseDTO(updatedTask));
    }

    // 할일카드 완료처리
    @PostMapping("/{taskId}/complete")
    public ResponseEntity<?> completeTask(@PathVariable Long taskId, @AuthenticationPrincipal UserDetails userDetails) {
        // 토큰에서 사용자 정보 추출
        String username = userDetails.getUsername();

        User user = userService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));


        // 할 일 조회 및 사용자 권한 검증
        Task task = taskService.findById(taskId);
        if (!task.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not the owner of this task.");
        }

        // 할 일 완료 처리
        task.setCompleted(true);
        taskService.save(task);

        return ResponseEntity.ok().body("Task completed successfully.");
    }



}
