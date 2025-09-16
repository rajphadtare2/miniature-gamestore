package minigameacle.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class GameResponse {
    private Integer gameId;
    private String name;
    private Double price;
    private Integer devId;
    private String devName;
}
