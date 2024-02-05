package org.sparta.scheduler.Repository;

import org.sparta.scheduler.Domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {

}
