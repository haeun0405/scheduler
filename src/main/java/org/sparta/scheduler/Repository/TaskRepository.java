package org.sparta.scheduler.Repository;

import org.sparta.scheduler.Domain.Task;
import org.sparta.scheduler.Domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByUserOrderByCreatedDateDesc(User user);

}
