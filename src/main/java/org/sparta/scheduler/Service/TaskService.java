package org.sparta.scheduler.Service;

import org.sparta.scheduler.Repository.TaskRepository;
import org.sparta.scheduler.Domain.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id).orElseThrow(()
                -> new IllegalArgumentException("Invalid task ID: " + id));
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public Task updateTask(Long id, Task taskDetails, String password) {
        Task task = taskRepository.findById(id).orElseThrow(()
                -> new IllegalArgumentException("Invalid task ID: " + id));
        // 비밀번호가 일치하지 않으면 예외 발생
        if (!task.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid password");
        }

        task.setTitle(taskDetails.getTitle());
        task.setContents(taskDetails.getContents());
        task.setAssignee(taskDetails.getAssignee());
        return taskRepository.save(task);
    }

    public void deleteTask(Long id, String password) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid task ID: " + id));

        // 비밀번호가 일치하는지 확인
        if (!task.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid password");
        }
        taskRepository.deleteById(id);
    }
}
