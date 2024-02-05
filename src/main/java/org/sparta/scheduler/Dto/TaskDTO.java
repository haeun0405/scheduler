package org.sparta.scheduler.Dto;


import lombok.Getter;
import lombok.Setter;
import org.sparta.scheduler.Domain.Task;

import java.time.LocalDateTime;

@Getter
@Setter
public class TaskDTO {
    // 필드 정의


    private Long id;

    private String title;
    private String contents;
    private String assignee;
    private String password;
    private String createdDate;

    // 생성자
    public TaskDTO() {
    }

    public TaskDTO(Long id, String title, String contents, String assignee, LocalDateTime createdDate) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.assignee = assignee;
        this.createdDate = createdDate.toString();
    }

    public Task toEntity() {
        Task task = new Task();
        task.setId(this.id);
        task.setTitle(this.title);
        task.setContents(this.contents);
        task.setAssignee(this.assignee);
        task.setPassword(this.password);
        return task;
    }

}
