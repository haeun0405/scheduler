package org.sparta.scheduler.Service;

import org.sparta.scheduler.Domain.Comment;
import org.sparta.scheduler.Domain.Task;
import org.sparta.scheduler.Domain.User;
import org.sparta.scheduler.Dto.CommentDTO;
import org.sparta.scheduler.Repository.CommentRepository;
import org.sparta.scheduler.Repository.TaskRepository;
import org.sparta.scheduler.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, TaskRepository taskRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public Comment addCommentToTask(Long taskId, String content) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with id: " + taskId));

        Comment comment = new Comment();
        comment.setTask(task);
        comment.setContent(content);
        comment.setCreatedDate(LocalDateTime.now());

        return commentRepository.save(comment); // 저장하고 반환
    }

    private CommentDTO convertToDTO(Comment comment) {
        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setCreatedDate(comment.getCreatedDate());
        return dto;
    }

    public Comment updateComment(Long commentId, String newContent, String username) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found with id: " + commentId));

        // 사용자 검증 로직 추가 (예시)
        if (!comment.getTask().getUser().getUsername().equals(username)) {
            throw new IllegalArgumentException("Not authorized to update this comment");
        }

        comment.setContent(newContent);
        return commentRepository.save(comment);
    }

    // 댓글 삭제 메소드
    public void deleteComment(Long commentId, String username) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (!comment.getUser().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to delete this comment");
        }

        commentRepository.delete(comment);
    }
}

