package minigameacle.user.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class User {
    @Id
    String email;

    @Column(nullable = false)
    String name;

    @ElementCollection
    @CollectionTable(name = "user_games", joinColumns = @JoinColumn(name = "email"))
    @Column(name = "gameName")
    List<String> games;
}
