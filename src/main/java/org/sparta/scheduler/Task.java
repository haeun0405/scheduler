package org.sparta.scheduler;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@Getter
@Setter
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String contents;

    @Column(nullable = false)
    private String assignee;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private LocalDateTime createdDate = LocalDateTime.now();

    public Task() {

    }

    public Task(TaskDTO taskDTO) {
        this.title = taskDTO.getTitle();
        this.contents = taskDTO.getContents();
        this.assignee = taskDTO.getAssignee();
    }

}
