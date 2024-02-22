package org.sparta.scheduler.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sparta.scheduler.Domain.Comment;
import org.sparta.scheduler.Domain.Task;
import org.sparta.scheduler.Domain.User;
import org.sparta.scheduler.Repository.CommentRepository;
import org.sparta.scheduler.Repository.TaskRepository;
import org.sparta.scheduler.Repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CommentService commentService;

    private Task task;
    private User user;
    private Comment comment;

    @BeforeEach
    void setUp() {
        // User 객체 설정
        user = new User();
        user.setId(1L);
        user.setUsername("testUser");

        // Task 객체 설정
        task = new Task();
        task.setId(1L);
        task.setUser(user); // Task에 User 연결

        // Comment 객체 설정
        comment = new Comment();
        comment.setId(1L);
        comment.setContent("Test Comment");
        comment.setCreatedDate(LocalDateTime.now());
        comment.setTask(task);
        comment.setUser(user); // Comment에 User 연결

        // TaskRepository와 UserRepository 목(mock) 설정
        lenient().when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task));
        lenient().when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
    }

    @Test
    void addCommentToTask_Success() {
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        Comment savedComment = commentService.addCommentToTask(task.getId(), "Test Comment");

        verify(commentRepository).save(any(Comment.class));
        assertNotNull(savedComment);
        assertEquals("Test Comment", savedComment.getContent());
    }

    @Test
    void updateComment_Success() {
        when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        Comment updatedComment = commentService.updateComment(comment.getId(), "Updated Content", "testUser");

        verify(commentRepository).save(any(Comment.class));
        assertNotNull(updatedComment);
        assertEquals("Updated Content", updatedComment.getContent());
    }

    @Test
    void deleteComment_Success() throws AccessDeniedException {
        // CommentRepository findByIdWithUser 메소드 목 설정
        when(commentRepository.findByIdWithUser(comment.getId())).thenReturn(Optional.of(comment));

        // 실제 메소드 호출
        commentService.deleteComment(comment.getId(), user.getUsername());

        // CommentRepository의 delete 메소드가 호출되었는지 확인
        verify(commentRepository).delete(comment);
    }
}
