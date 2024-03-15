package org.sparta.scheduler.Service;

import org.sparta.scheduler.Domain.Comment;
import org.sparta.scheduler.Exception.CommentNotFoundException;
import org.sparta.scheduler.Exception.TaskNotFoundException;
import org.sparta.scheduler.Exception.UnauthorizedCommentAccessException;

public interface CommentService {

    /**
     * 주어진 할일 카드에 새 댓글을 추가합니다.
     *
     * @param taskId 할일 카드의 ID
     * @param content 댓글 내용
     * @return 생성된 댓글
     * @throws TaskNotFoundException 할일 카드를 찾을 수 없을 때 발생
     */
    Comment addCommentToTask(Long taskId, String content);

    /**
     * 주어진 ID의 댓글을 업데이트합니다.
     *
     * @param commentId 댓글의 ID
     * @param newContent 새로운 댓글 내용
     * @param username 요청한 사용자의 이름
     * @return 업데이트된 댓글
     * @throws CommentNotFoundException 댓글을 찾을 수 없을 때 발생
     * @throws UnauthorizedCommentAccessException 댓글 업데이트 권한이 없을 때 발생
     */
    Comment updateComment(Long commentId, String newContent, String username);

    /**
     * 주어진 ID의 댓글을 삭제합니다.
     *
     * @param commentId 댓글의 ID
     * @param username 요청한 사용자의 이름
     * @throws CommentNotFoundException 댓글을 찾을 수 없을 때 발생
     * @throws UnauthorizedCommentAccessException 댓글 삭제 권한이 없을 때 발생
     */
    void deleteComment(Long commentId, String username);
}
