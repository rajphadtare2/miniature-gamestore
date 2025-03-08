package minigameacle.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class GamesRequest {
    String email;
    Map<String,String> games;
}
