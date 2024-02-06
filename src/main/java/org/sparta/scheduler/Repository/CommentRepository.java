package org.sparta.scheduler.Repository;

import org.sparta.scheduler.Domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
