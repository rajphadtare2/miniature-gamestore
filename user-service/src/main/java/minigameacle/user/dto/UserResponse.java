package minigameacle.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserResponse {
    String email;
    String name;
    List<String> games;
}
