package ua.taskmate.kanban.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

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
}
