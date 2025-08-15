package minigameacle.user.controller;

import lombok.extern.slf4j.Slf4j;
import minigameacle.user.dto.UserDTO;
import minigameacle.user.dto.UserResponse;
import minigameacle.user.service.KeycloakUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final KeycloakUserService userService;

    public AuthController(KeycloakUserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody UserDTO userDTO) {
        log.info("In Register Controller");
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userDTO));
    }

}
