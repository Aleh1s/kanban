package ua.taskmate.kanban.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "member")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Builder.Default
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Builder.Default
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private MemberRole role;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Builder.Default
    @Setter(AccessLevel.PRIVATE)
    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Issue> issues = new ArrayList<>();

    @Builder.Default
    @Setter(AccessLevel.PRIVATE)
    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

    @Builder.Default
    @Setter(AccessLevel.PRIVATE)
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Assignee> assignees = new ArrayList<>();

    public void addIssue(Issue issue) {
        this.issues.add(issue);
        issue.setCreator(this);
    }

    public void deleteIssue(Issue issue) {
        this.issues.remove(issue);
        issue.setCreator(null);
    }

    public void addAssignee(Assignee assignee) {
        this.assignees.add(assignee);
        assignee.setMember(this);
    }

    public void deleteAssignee(Assignee assignee) {
        this.assignees.remove(assignee);
        assignee.setMember(null);
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
        comment.setCreator(this);
    }

    public void deleteComment(Comment comment) {
        this.comments.remove(comment);
        comment.setCreator(null);
    }
}