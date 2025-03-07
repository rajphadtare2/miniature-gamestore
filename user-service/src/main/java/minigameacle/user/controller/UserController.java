package minigameacle.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        log.error("ERROR: ",ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User Could not be created due to internal error");
    }
}
