package ua.taskmate.kanban.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserDto {
    private String id;
    private String email;
    private String profileImageUrl;
    private String firstName;
    private String lastName;
}
