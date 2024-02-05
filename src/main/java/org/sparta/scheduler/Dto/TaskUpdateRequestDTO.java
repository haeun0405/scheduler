package org.sparta.scheduler.Dto;

import lombok.Getter;
import lombok.Setter;
import org.sparta.scheduler.Domain.Task;

@Getter
@Setter

public class TaskUpdateRequestDTO {
// 필드 정의
    private String title;
    private String contents;
    private String assignee;
    private String password;

    // 생성자
    public TaskUpdateRequestDTO() {
    }

    public Task toEntity() {
        Task task = new Task();
        task.setTitle(this.title);
        task.setContents(this.contents);
        task.setAssignee(this.assignee);
        return task;
    }
}
