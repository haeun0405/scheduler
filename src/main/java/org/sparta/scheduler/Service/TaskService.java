package org.sparta.scheduler.Service;

import jakarta.transaction.Transactional;
import org.sparta.scheduler.Domain.Task;
import org.sparta.scheduler.Domain.User;
import org.sparta.scheduler.Dto.TaskDTO;
import org.sparta.scheduler.Exception.ResourceNotFoundException;
import org.sparta.scheduler.Repository.TaskRepository;
import org.sparta.scheduler.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public List<Task> getTasksByUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return taskRepository.findAllByUserOrderByCreatedDateDesc(user);
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id).orElseThrow(()
                -> new IllegalArgumentException("Invalid task ID: " + id));
    }

    @Transactional
    public Task createTask(TaskDTO taskDTO) {
        // 현재 인증된 사용자의 이름을 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();

        // UserRepository를 사용하여 User 객체를 조회
        User user = userRepository.findByUsername(currentUserName)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + currentUserName));

        // TaskDTO와 User 객체를 사용하여 Task 객체 초기화
        Task task = new Task();
        task.setTitle(taskDTO.getTitle());
        task.setContents(taskDTO.getContents());
        task.setAssignee(currentUserName);
        task.setUser(user); // Task 엔티티와 User 엔티티 연결
        return taskRepository.save(task);
    }

    public Task updateTask(Long taskId, TaskDTO taskDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with id: " + taskId));

        // 사용자 검증
        if (!task.getUser().getUsername().equals(username)) {
            throw new IllegalArgumentException("You can only edit your own tasks.");
        }

        task.setTitle(taskDTO.getTitle());
        task.setContents(taskDTO.getContents());
        return taskRepository.save(task);
    }

    public Task findById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id " + id));
    }

    public Task save(Task task) {
        return taskRepository.save(task);
    }

}
