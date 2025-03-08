package minigameacle.user.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.HashMap;
import java.util.Map;

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
    @MapKeyColumn(name="game_id")
    @Column(name = "game_name")
    @Fetch(FetchMode.JOIN)
    Map<String, String> games = new HashMap<>();
}
