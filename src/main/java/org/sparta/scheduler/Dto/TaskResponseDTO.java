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
    private Boolean completed;

    // 생성자
    public TaskResponseDTO() {
    }

    public TaskResponseDTO(Task task) {
        this.id = task.getId();
        this.title = task.getTitle();
        this.contents = task.getContents();
        this.assignee = task.getAssignee(); //할일 엔티티에서 작성자 정보를 가져옴
        this.createdDate = task.getCreatedDate();
        this.completed = task.getCompleted();
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
