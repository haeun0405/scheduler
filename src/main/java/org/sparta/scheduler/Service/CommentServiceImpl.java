package org.sparta.scheduler.Service;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sparta.scheduler.Domain.Comment;
import org.sparta.scheduler.Domain.Task;
import org.sparta.scheduler.Domain.User;
import org.sparta.scheduler.Dto.CommentDTO;
import org.sparta.scheduler.Exception.CommentNotFoundException;
import org.sparta.scheduler.Exception.TaskNotFoundException;
import org.sparta.scheduler.Exception.UnauthorizedCommentAccessException;
import org.sparta.scheduler.Repository.CommentRepository;
import org.sparta.scheduler.Repository.TaskRepository;
import org.sparta.scheduler.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentServiceImpl implements CommentService{
    private static final Logger logger = LoggerFactory.getLogger(Comment.class);

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, TaskRepository taskRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public Comment addCommentToTask(Long taskId, String content) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        User user = userRepository.findByUsername(currentUserName)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + currentUserName));
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("할일 카드를 찾을 수 없습니다." + taskId));

        Comment comment = new Comment();
        comment.setTask(task);
        comment.setContent(content);
        comment.setCreatedDate(LocalDateTime.now());
        comment.setUser(user);

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
                .orElseThrow(() -> new CommentNotFoundException("댓글을 찾을 수 없습니다. " + commentId));

        // 사용자 검증 로직 추가 (예시)
        if (!comment.getTask().getUser().getUsername().equals(username)) {
            throw new UnauthorizedCommentAccessException("권한이 없습니다.");
        }

        comment.setContent(newContent);
        return commentRepository.save(comment);
    }

    // 댓글 삭제 메소드
    @Transactional
    @PreAuthorize("#username == authentication.principal.username")
    public void deleteComment(Long commentId, String username) {
        Comment comment = commentRepository.findByIdWithUser(commentId)
            .orElseThrow(() -> new CommentNotFoundException("댓글을 찾을 수 없습니다."));

        logger.info("Deleting comment: {}, User: {}, Task: {}", comment.getId(), comment.getUser().getUsername(), comment.getTask().getId());

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!comment.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedCommentAccessException("권한이 없습니다.");
        }



//        // Comment 객체에 연결된 User가 null인지 확인
//        if (comment.getUser() == null) {
//            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "This comment does not have an associated user.");
//        }

        // 현재 요청한 사용자가 댓글을 작성한 사용자와 동일한지 확인
        if (!comment.getUser().getUsername().equals(username)) {
            throw new UnauthorizedCommentAccessException("권한이 없습니다.");
        }

        commentRepository.delete(comment);
    }
}

