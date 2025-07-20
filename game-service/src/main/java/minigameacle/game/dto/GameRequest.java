package minigameacle.game.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import minigameacle.game.model.Developer;

@Getter
@Setter
@Builder
public class GameRequest {

    private Integer gameId;
    private String name;
    private Double price;
    private Integer devId;
    private String devName;
}
