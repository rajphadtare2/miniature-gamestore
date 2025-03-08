package minigameacle.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import minigameacle.user.dto.GamesRequest;
import minigameacle.user.dto.UserRequest;
import minigameacle.user.dto.UserResponse;
import minigameacle.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest userDto){
        UserResponse userResponse = userService.createUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    @PutMapping("/saveGames")
    public ResponseEntity<UserResponse> saveGamesToUser(@RequestBody GamesRequest gamesRequest){
        UserResponse userResponse = userService.saveGamesToUser(gamesRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    @GetMapping
    public ResponseEntity<UserResponse> getUser(@RequestParam String email){
        UserResponse userResponse = userService.getUserByEmail(email);
        return ResponseEntity.status(HttpStatus.OK).body(userResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        log.error("ERROR: ",ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }
}
