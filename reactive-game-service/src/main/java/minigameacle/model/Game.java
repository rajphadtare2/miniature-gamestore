package minigameacle.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table("games")
public class Game {
    @Id
    private Integer gameId;
    private String name;
    private Double price;

    @Column("dev_id")
    private Integer devId; // no @ManyToOne
}

