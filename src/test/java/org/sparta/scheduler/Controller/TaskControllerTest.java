package org.sparta.scheduler.Controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.sparta.scheduler.Domain.Task;
import org.sparta.scheduler.Dto.TaskDTO;
import org.sparta.scheduler.SecurityConfig.TestSecurityConfig;
import org.sparta.scheduler.Service.TaskService;
import org.sparta.scheduler.Service.UserService;
import org.sparta.scheduler.Util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
@WebMvcTest(TaskController.class)
@Import(TestSecurityConfig.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    @WithMockUser(username="testUser")
    void getAllTasksSuccess() throws Exception {
        Task task = new Task();
        Mockito.when(taskService.getTasksByUser(Mockito.anyString())).thenReturn(Arrays.asList(task));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$").isArray());
    }

    @Test
    @WithMockUser(username="testUser")
    void getTaskByIdSuccess() throws Exception {
        Task task = new Task();
        task.setId(1L);
        Mockito.when(taskService.getTaskById(Mockito.anyLong())).thenReturn(task);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/tasks/1")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser(username="testUser")
    void createTaskSuccess() throws Exception {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTitle("Test Task");
        taskDTO.setContents("Test Content");

        Task mockTask = new Task();
        mockTask.setId(1L);
        mockTask.setTitle(taskDTO.getTitle());
        mockTask.setContents(taskDTO.getContents());

        Mockito.when(taskService.createTask(ArgumentMatchers.any(TaskDTO.class))).thenReturn(mockTask);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(taskDTO)))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(mockTask.getId())); // 여기에서 id 값이 있는지 확인합니다.
    }

    @Test
    @WithMockUser(username="testUser")
    void updateTaskSuccess() throws Exception {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTitle("Updated Task");
        taskDTO.setContents("Updated Content");
        Task task = new Task();
        task.setId(1L);
        task.setTitle(taskDTO.getTitle());
        task.setContents(taskDTO.getContents());

        Mockito.when(taskService.updateTask(Mockito.anyLong(), ArgumentMatchers.any(TaskDTO.class))).thenReturn(task);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Updated Task\",\"contents\":\"Updated Content\"}"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
    }
}
