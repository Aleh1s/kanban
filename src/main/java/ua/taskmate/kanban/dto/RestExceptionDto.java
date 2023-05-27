package ua.taskmate.kanban.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class RestExceptionDto {

    private Object payload;
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

}
