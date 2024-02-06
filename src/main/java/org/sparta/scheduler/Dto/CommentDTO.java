package org.sparta.scheduler.Dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class CommentDTO {
    private Long id;
    private String content;
    private LocalDateTime createdDate;
}
