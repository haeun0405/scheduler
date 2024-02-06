package org.sparta.scheduler.Dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class TaskDTO {
    // 필드 정의


    private Long id;

    private String title;
    private String contents;
    private String assignee;
    private String password;
    private String createdDate;

    public TaskDTO(Long id, String title, String contents, String assignee, LocalDateTime createdDate) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.assignee = assignee;
    }

}
