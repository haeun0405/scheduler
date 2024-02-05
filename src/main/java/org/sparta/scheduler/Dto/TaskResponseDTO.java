package org.sparta.scheduler.Dto;

import lombok.Getter;
import lombok.Setter;
import org.sparta.scheduler.Domain.Task;

import java.time.LocalDateTime;

@Getter
@Setter

public class TaskResponseDTO {
    // 필드 정의
    private Long id;
    private String title;
    private String contents;
    private String assignee;
    private LocalDateTime createdDate;

    // 생성자
    public TaskResponseDTO() {
    }

    public TaskResponseDTO(Task task) {
        this.id = task.getId();
        this.title = task.getTitle();
        this.contents = task.getContents();
        this.assignee = task.getAssignee();
        this.createdDate = task.getCreatedDate();
    }

    public Task toEntity() {
        Task task = new Task();
        task.setId(this.id);
        task.setTitle(this.title);
        task.setContents(this.contents);
        task.setAssignee(this.assignee);
        return task;
    }
}
