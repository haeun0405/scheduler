package org.sparta.scheduler.Domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.sparta.scheduler.Dto.TaskDTO;

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
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(nullable = false)
    private Boolean completed = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    public Task() {

    }

    public Task(TaskDTO taskDTO, User user) {
        this.title = taskDTO.getTitle();
        this.contents = taskDTO.getContents();
        this.assignee = taskDTO.getAssignee();
        this.user = user;
    }

}
