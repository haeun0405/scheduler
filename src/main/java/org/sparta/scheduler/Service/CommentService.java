package org.sparta.scheduler.Service;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sparta.scheduler.Domain.Comment;
import org.sparta.scheduler.Domain.Task;
import org.sparta.scheduler.Domain.User;
import org.sparta.scheduler.Dto.CommentDTO;
import org.sparta.scheduler.Repository.CommentRepository;
import org.sparta.scheduler.Repository.TaskRepository;
import org.sparta.scheduler.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;

@Service
public class CommentService {
    private static final Logger logger = LoggerFactory.getLogger(Comment.class);

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
    @PreAuthorize("#username == authentication.principal.username")
    public void deleteComment(Long commentId, String username) throws AccessDeniedException {
        Comment comment = commentRepository.findByIdWithUser(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));

        logger.info("Deleting comment: {}, User: {}, Task: {}", comment.getId(), comment.getUser().getUsername(), comment.getTask().getId());

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!comment.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You do not have permission to delete this comment");
        }



//        // Comment 객체에 연결된 User가 null인지 확인
//        if (comment.getUser() == null) {
//            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "This comment does not have an associated user.");
//        }

        // 현재 요청한 사용자가 댓글을 작성한 사용자와 동일한지 확인
        if (!comment.getUser().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to delete this comment");
        }

        commentRepository.delete(comment);
    }
}

