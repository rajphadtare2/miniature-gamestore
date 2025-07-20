package minigameacle.game.datainit;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import minigameacle.game.dto.DevRequest;
import minigameacle.game.dto.GameRequest;
import minigameacle.game.service.GameService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@Component
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final GameService gameService;


    @Override
    public void run(String... args) {

        log.info("✔ Inserting Sample game data if not already present.");

        List<DevRequest> devs = List.of(
                DevRequest.builder().name("CD Project Red").devId(1).build(),
                DevRequest.builder().name("Rockstar games").devId(2).build(),
                DevRequest.builder().name("Ubisoft").devId(3).build(),
                DevRequest.builder().name("Valve").devId(4).build(),
                DevRequest.builder().name("Naughty Dog").devId(5).build()
        );

        List<GameRequest> games = List.of(
                GameRequest.builder().gameId(1).devId(1).name("Witcher 3: Wild Hunt").price(699.00).build(),
                GameRequest.builder().gameId(2).devId(2).name("GTA III").price(499.00).build(),
                GameRequest.builder().gameId(3).devId(2).name("Red Dead Redemption 2").price(1600.00).build(),
                GameRequest.builder().gameId(4).devId(3).name("Assassin's Creed Odyssey").price(1500.00).build(),
                GameRequest.builder().gameId(5).devId(3).name("Assassin's Creed Valhalla").price(3500.00).build(),
                GameRequest.builder().gameId(6).devId(4).name("Counter Strike 2").price(500.00).build(),
                GameRequest.builder().gameId(7).devId(5).name("The Last of Us Part I").price(4200.00).build(),
                GameRequest.builder().gameId(8).devId(1).name("Cyberpunk 2077").price(2500.00).build()

        );

        for (DevRequest dev : devs) {
            try {
                gameService.createDev(dev);
            } catch (RuntimeException e) {
                log.error("Error while creating dev. Skipping creation...", e);
            }
        }


        for (GameRequest game : games) {
            try {
                gameService.createGame(game);
            } catch (RuntimeException e) {
                log.error("Error while creating game. Skipping creation...", e);
            }
        }


        log.info("✔ Sample game data inserted if not already present.");
    }
}
