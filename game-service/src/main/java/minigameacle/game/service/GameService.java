package minigameacle.game.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import minigameacle.game.dto.DevRequest;
import minigameacle.game.dto.DevResponse;
import minigameacle.game.dto.GameRequest;
import minigameacle.game.dto.GameResponse;
import minigameacle.game.model.Developer;
import minigameacle.game.model.Game;
import minigameacle.game.repository.DeveloperRepository;
import minigameacle.game.repository.GameRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class GameService {
    private final GameRepository gameRepository;
    private final DeveloperRepository developerRepository;

    public GameResponse createGame(GameRequest gameRequest) {
        gameRepository.findByNameIgnoreCase(gameRequest.getName()).ifPresent(dev -> {
            throw new RuntimeException("Game with name " + gameRequest.getName()
                    + " already present.");
        });
        Developer dev = developerRepository.findById(gameRequest.getDevId())
                .orElseThrow(() -> new EntityNotFoundException("Developer with ID " + gameRequest.getDevId()
                        + " not found."));
        Game game = mapRequestToGame(gameRequest);
        game.setDeveloper(dev);
        Game savedGame = gameRepository.save(game);
        return mapGameToResponse(savedGame);
    }

    public DevResponse createDev(DevRequest devRequest) {
        developerRepository.findByNameIgnoreCase(devRequest.getName()).ifPresent(dev -> {
            throw new RuntimeException("Developer with name " + devRequest.getName()
                    + " already present.");});
        Developer savedDev = developerRepository.save(mapRequestToDev(devRequest));
        return mapDevToResponse(savedDev);
    }

    public List<GameResponse> getAllGames() {
        return gameRepository.findAll(Sort.by(Sort.Direction.ASC, "gameId")).stream()
                .map(this::mapGameToResponse)
                .collect(Collectors.toList());
    }

    public List<DevResponse> getAllDevs() {
        return developerRepository.findAllDevs().stream()
                .map(this::mapDevToResponse)
                .collect(Collectors.toList());
    }

    public GameResponse getGameByIdOrName(String id) {
        return Optional.ofNullable(id)
                .flatMap(i -> {
                    try {
                        return gameRepository.findById(Integer.parseInt(i));
                    } catch (NumberFormatException e) {
                        return gameRepository.findByNameIgnoreCase(i);
                    }
                })
                .map(this::mapGameToResponse)
                .orElseThrow(() -> new RuntimeException("Game with ID or Name " + id + " not found."));

    }

    private GameResponse mapGameToResponse(Game game) {
        return GameResponse.builder()
                .gameId(game.getGameId())
                .price(game.getPrice())
                .name(game.getName())
                .devId(game.getDeveloper().getDevId())
                .devName(game.getDeveloper().getName())
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

    private DevResponse mapDevToResponse(Developer developer) {
        return DevResponse.builder()
                .devId(developer.getDevId())
                .name(developer.getName())
                .games(Optional.ofNullable(developer.getGames())
                        .orElse(Collections.emptyList())
                        .stream()
                        .map(Game::getName)
                        .collect(Collectors.toList()))
                .build();
    }
}
