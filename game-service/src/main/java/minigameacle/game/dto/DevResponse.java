package minigameacle.game.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class DevResponse {

    private Integer devId;
    private String name;
    private List<String> games;
}
