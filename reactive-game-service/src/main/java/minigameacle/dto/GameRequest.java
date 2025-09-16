package minigameacle.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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
