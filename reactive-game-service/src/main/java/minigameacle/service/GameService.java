package minigameacle.service;

import lombok.RequiredArgsConstructor;
import minigameacle.dto.DevRequest;
import minigameacle.dto.DevResponse;
import minigameacle.dto.GameRequest;
import minigameacle.dto.GameResponse;
import minigameacle.model.Developer;
import minigameacle.model.Game;
import minigameacle.repository.DeveloperRepository;
import minigameacle.repository.GameRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class GameService {
    private final GameRepository gameRepository;
    private final DeveloperRepository developerRepository;

    public Mono<GameResponse> createGame(GameRequest gameRequest) {
        return gameRepository.findByNameIgnoreCase(gameRequest.getName())
                .flatMap(existing -> Mono.<GameResponse>error(
                        new RuntimeException("Game with name " + gameRequest.getName() + " already present.")
                ))
                .switchIfEmpty(
                        developerRepository.findById(gameRequest.getDevId())
                                .switchIfEmpty(Mono.error(new RuntimeException("Developer with ID " + gameRequest.getDevId() + " not found.")))
                                .flatMap(dev -> {
                                    Game game = mapRequestToGame(gameRequest);
                                    game.setDevId(dev.getDevId());
                                    return gameRepository.save(game)
                                            .map(saved -> mapGameToResponse(saved, dev));
                                })
                );
    }

    public Mono<DevResponse> createDev(DevRequest devRequest) {
        return developerRepository.findByNameIgnoreCase(devRequest.getName())
                .flatMap(existing -> Mono.<DevResponse>error(
                        new RuntimeException("Developer with name " + devRequest.getName() + " already present.")
                ))
                .switchIfEmpty(
                        developerRepository.save(mapRequestToDev(devRequest))
                                .flatMap(this::mapDevToResponse)
                );
    }

    public Flux<GameResponse> getAllGames(int page, int size) {
        return gameRepository.findAll()
                .skip((long) page * size)
                .take(size)
                .flatMap(game ->
                        developerRepository.findById(game.getDevId())
                                .flatMap(dev -> Mono.just(mapGameToResponse(game, dev)))
                );
    }

    public Flux<DevResponse> getAllDevs() {
        return developerRepository.findAll()
                .flatMap(this::mapDevToResponse);
    }

    public Mono<GameResponse> getGameByIdOrName(String id) {
        return Mono.defer(() -> {
            try {
                int gameId = Integer.parseInt(id);
                return gameRepository.findById(gameId)
                        .flatMap(game -> developerRepository.findById(game.getDevId())
                                .flatMap(dev -> Mono.just(mapGameToResponse(game, dev)))
                        );
            } catch (NumberFormatException e) {
                return gameRepository.findByNameIgnoreCase(id)
                        .flatMap(game -> developerRepository.findById(game.getDevId())
                                .flatMap(dev -> Mono.just(mapGameToResponse(game, dev)))
                        );
            }
        }).switchIfEmpty(Mono.error(new RuntimeException("Game with ID or Name " + id + " not found.")));
    }


    // ---- Mappers ----
    private GameResponse mapGameToResponse(Game game, Developer dev) {
        return GameResponse.builder()
                .gameId(game.getGameId())
                .price(game.getPrice())
                .name(game.getName())
                .devId(game.getDevId())
                .devName(dev != null ? dev.getName() : null)
                .build();
    }

    private Game mapRequestToGame(GameRequest gameRequest) {
        return Game.builder()
                .name(gameRequest.getName())
                .price(gameRequest.getPrice())
                .build();
    }

    private Developer mapRequestToDev(DevRequest devRequest) {
        return Developer.builder()
                .name(devRequest.getName())
                .build();
    }

    private Mono<DevResponse> mapDevToResponse(Developer developer) {
        return gameRepository.findAllByDevId(developer.getDevId())
                .map(Game::getName)
                .collectList()
                .map(gameNames -> DevResponse.builder()
                        .devId(developer.getDevId())
                        .name(developer.getName())
                        .games(gameNames)
                        .build()
                );
    }
}
