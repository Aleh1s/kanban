package ua.taskmate.kanban.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
public class BoardDto {
    private Long id;
    private String name;
    private String imageUrl;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
}
