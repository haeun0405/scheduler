package org.sparta.scheduler.Service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sparta.scheduler.Domain.Task;
import org.sparta.scheduler.Domain.User;
import org.sparta.scheduler.Dto.TaskDTO;
import org.sparta.scheduler.Repository.TaskRepository;
import org.sparta.scheduler.Repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void getTasksByUser_ExistingUser() {
        String username = "testUser";
        User user = new User();
        user.setUsername(username);

        Task task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setUser(user);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(taskRepository.findAllByUserOrderByCreatedDateDesc(user)).thenReturn(Collections.singletonList(task));

        List<Task> tasks = taskService.getTasksByUser(username);

        assertFalse(tasks.isEmpty());
        assertEquals(1, tasks.size());
        assertEquals(task.getTitle(), tasks.get(0).getTitle());
    }

    @Test
    void createTask_Success() {
        String username = "testUser";
        User user = new User();
        user.setUsername(username);

        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTitle("New Task");
        taskDTO.setContents("New Content");

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(username);

        Task savedTask = new Task();
        savedTask.setId(1L);
        savedTask.setTitle(taskDTO.getTitle());
        savedTask.setContents(taskDTO.getContents());
        savedTask.setUser(user);

        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);

        Task result = taskService.createTask(taskDTO);

        assertNotNull(result);
        assertEquals(taskDTO.getTitle(), result.getTitle());
        assertEquals(taskDTO.getContents(), result.getContents());
    }

    @Test
    void getTaskById_ExistingTask() {
        Long taskId = 1L;
        Task task = new Task();
        task.setId(taskId);
        task.setTitle("Existing Task");

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        Task foundTask = taskService.getTaskById(taskId);

        assertNotNull(foundTask);
        assertEquals(taskId, foundTask.getId());
        assertEquals("Existing Task", foundTask.getTitle());
    }

    @Test
    void updateTask_Success() {
        Long taskId = 1L;
        String username = "testUser";
        Task existingTask = new Task();
        existingTask.setId(taskId);
        existingTask.setTitle("Old Task");
        existingTask.setContents("Old Content");
        User user = new User();
        user.setUsername(username);
        existingTask.setUser(user);

        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTitle("Updated Task");
        taskDTO.setContents("Updated Content");

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(username);
        when(taskRepository.save(any(Task.class))).thenReturn(existingTask);

        Task updatedTask = taskService.updateTask(taskId, taskDTO);

        assertNotNull(updatedTask);
        assertEquals(taskId, updatedTask.getId());
        assertEquals("Updated Task", updatedTask.getTitle());
        assertEquals("Updated Content", updatedTask.getContents());
    }

    @Test
    void findById_ExistingTask() {
        Long taskId = 1L;
        Task task = new Task();
        task.setId(taskId);
        task.setTitle("Task for findById");

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        Task foundTask = taskService.findById(taskId);

        assertNotNull(foundTask);
        assertEquals(taskId, foundTask.getId());
        assertEquals("Task for findById", foundTask.getTitle());
    }

    @Test
    void save_Success() {
        Task task = new Task();
        task.setTitle("New Task");
        task.setContents("New Content");

        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task savedTask = taskService.save(task);

        assertNotNull(savedTask);
        assertEquals("New Task", savedTask.getTitle());
        assertEquals("New Content", savedTask.getContents());
    }
}
