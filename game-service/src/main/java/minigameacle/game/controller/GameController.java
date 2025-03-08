package minigameacle.game.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import minigameacle.game.dto.DevRequest;
import minigameacle.game.dto.DevResponse;
import minigameacle.game.dto.GameRequest;
import minigameacle.game.dto.GameResponse;
import minigameacle.game.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/games")
@Slf4j
@RequiredArgsConstructor
public class GameController {

    final GameService gameService;

    @PostMapping("/createGame")
    public ResponseEntity<GameResponse> createGame(@RequestBody GameRequest gameRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(gameService.createGame(gameRequest));
    }

    @PostMapping("/createDev")
    public ResponseEntity<DevResponse> createDev(@RequestBody DevRequest devRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(gameService.createDev(devRequest));
    }

    @GetMapping("/allGames")
    public ResponseEntity<List<GameResponse>> getAllGames(){
        return ResponseEntity.status(HttpStatus.OK).body(gameService.getAllGames());
    }

    @GetMapping("/allDevs")
    public ResponseEntity<List<DevResponse>> getAllDevs(){
        return ResponseEntity.status(HttpStatus.OK).body(gameService.getAllDevs());
    }

    @GetMapping
    public ResponseEntity<GameResponse> getGameByIdOrName(@RequestParam("id") String id){
        return ResponseEntity.status(HttpStatus.OK).body(gameService.getGameByIdOrName(id));
    }
}
