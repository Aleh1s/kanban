package ua.taskmate.kanban.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "_user")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @Column(name = "id",
            unique = true,
            nullable = false
    )
    private String id;
    @Column(name = "sub",
            unique = true,
            nullable = false
    )
    private String sub;
    @Column(name = "profile_image_url",
            nullable = false
    )
    private String profileImageUrl;
    @Column(name = "first_name",
            nullable = false
    )
    private String fistName;
    @Column(name = "last_name",
            nullable = false
    )
    private String lastName;
    @Column(name = "email",
            nullable = false
    )
    private String email;

    @OneToMany(mappedBy = "user",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    private List<Member> members = new ArrayList<>();

    @PrePersist
    public void setUpId() {
        this.id = UUID.randomUUID().toString();
    }

    public void addMember(Member member) {
        this.members.add(member);
        member.setUser(this);
    }

    public void deleteMember(Member member) {
        this.members.remove(member);
        member.setUser(null);
    }
}
