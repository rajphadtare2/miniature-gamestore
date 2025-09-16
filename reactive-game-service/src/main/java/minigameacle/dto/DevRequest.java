package minigameacle.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class DevRequest {

    private Integer devId;
    private String name;
    private List<String> games;
}
