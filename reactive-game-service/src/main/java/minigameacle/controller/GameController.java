package minigameacle.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import minigameacle.dto.DevRequest;
import minigameacle.dto.DevResponse;
import minigameacle.dto.GameRequest;
import minigameacle.dto.GameResponse;
import minigameacle.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/games/reactive")
@Slf4j
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @PostMapping("/createGame")
    public Mono<ResponseEntity<GameResponse>> createGame(@RequestBody GameRequest gameRequest) {
        return gameService.createGame(gameRequest)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response));
    }

    @PostMapping("/createDev")
    public Mono<ResponseEntity<DevResponse>> createDev(@RequestBody DevRequest devRequest) {
        return gameService.createDev(devRequest)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response));
    }

    @GetMapping("/allGames")
    public Flux<GameResponse> getAllGames(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "30") int size) {
        return gameService.getAllGames(page, size);
    }

    @GetMapping("/allDevs")
    public Flux<DevResponse> getAllDevs() {
        return gameService.getAllDevs();
    }

    @GetMapping
    public Mono<ResponseEntity<GameResponse>> getGameByIdOrName(@RequestParam("id") String id) {
        return gameService.getGameByIdOrName(id)
                .map(response -> ResponseEntity.status(HttpStatus.OK).body(response))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
