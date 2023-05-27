package ua.taskmate.kanban.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PopulatedMemberDto {
    private MemberDto member;
    private String profileImageUrl;
    private String firstName;
    private String lastName;
    private String userId;
    private String email;
}
