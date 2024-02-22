package org.sparta.scheduler.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.sparta.scheduler.Domain.Comment;
import org.sparta.scheduler.Dto.CommentDTO;
import org.sparta.scheduler.SecurityConfig.TestSecurityConfig;
import org.sparta.scheduler.Service.CommentService;
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

@Import(TestSecurityConfig.class)
@WebMvcTest(CommentController.class)
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @MockBean
    private JwtUtil jwtUtil;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @WithMockUser
    void createCommentSuccess() throws Exception {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setContent("Test comment");

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setContent(commentDTO.getContent());

        Mockito.when(commentService.addCommentToTask(Mockito.anyLong(), Mockito.anyString())).thenReturn(comment);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/tasks/1/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentDTO)))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(comment.getId()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.content").value(comment.getContent()));
    }

    @Test
    @WithMockUser
    void updateCommentSuccess() throws Exception {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setContent("Updated comment");

        Comment updatedComment = new Comment();
        updatedComment.setId(1L);
        updatedComment.setContent(commentDTO.getContent());

        Mockito.when(commentService.updateComment(Mockito.anyLong(), Mockito.anyString(), Mockito.anyString())).thenReturn(updatedComment);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/tasks/1/comments/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentDTO)))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(updatedComment.getId()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.content").value(updatedComment.getContent()));
    }

    @Test
    @WithMockUser
    void deleteCommentSuccess() throws Exception {
        Mockito.doNothing().when(commentService).deleteComment(Mockito.anyLong(), Mockito.anyString());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/tasks/1/comments/1"))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
