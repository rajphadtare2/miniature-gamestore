package minigameacle.game.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DevRequest {

    private Integer devId;
    private String name;
    private List<String> games;
}
