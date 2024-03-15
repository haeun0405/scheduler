package org.sparta.scheduler.Controller;

import org.sparta.scheduler.Domain.Comment;
import org.sparta.scheduler.Dto.CommentDTO;
import org.sparta.scheduler.Service.CommentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/api/tasks/{taskId}/comments")
public class CommentController {

    private final CommentServiceImpl commentService;

    @Autowired
    public CommentController(CommentServiceImpl commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<CommentDTO> createComment(@PathVariable Long taskId, @RequestBody CommentDTO commentDTO) {
        Comment createdComment = commentService.addCommentToTask(taskId, commentDTO.getContent());
        CommentDTO createdCommentDTO = mapToDTO(createdComment);
        return ResponseEntity.ok(createdCommentDTO);
    }

    private CommentDTO mapToDTO(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setContent(comment.getContent());
        commentDTO.setCreatedDate(comment.getCreatedDate());
        return commentDTO;
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentDTO> updateComment(@PathVariable Long commentId, @RequestBody CommentDTO commentDTO, @AuthenticationPrincipal UserDetails userDetails) {
        Comment updatedComment = commentService.updateComment(commentId, commentDTO.getContent(), userDetails.getUsername());
        CommentDTO updatedCommentDTO = mapToDTO(updatedComment);
        return ResponseEntity.ok(updatedCommentDTO);
    }

    // 댓글 삭제 엔드포인트
    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long taskId, @PathVariable Long commentId, @AuthenticationPrincipal UserDetails userDetails) throws AccessDeniedException {
        commentService.deleteComment(commentId, userDetails.getUsername());
        return ResponseEntity.ok().build(); // 삭제 성공 메시지 및 상태 코드 반환
    }

}
