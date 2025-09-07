package minigameacle.game.datainit;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;

@AllArgsConstructor
@Component
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        logger.info("Checking if developers data exists...");
        Integer devCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM developers", Integer.class);

        if (devCount == null || devCount == 0) {
            logger.info("No developers found. Starting import...");
            importDevelopers();
            importGames();
            logger.info("Data import completed.");
        } else {
            logger.info("Developers data already exists (count: {}). Skipping import.", devCount);
        }
    }

    private void importDevelopers() throws Exception {
        logger.info("Starting import of developers from CSV...");
        ClassPathResource resource = new ClassPathResource("data/developers.csv");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String sql = "COPY developers(name) FROM STDIN WITH CSV HEADER";
            jdbcTemplate.execute((ConnectionCallback<Object>) conn -> {
                logger.info("Unwrapping connection for developers import COPY...");
                Connection nativeConn = conn;
                while (nativeConn.isWrapperFor(Connection.class)) {
                    nativeConn = nativeConn.unwrap(Connection.class);
                }

                if (!(nativeConn instanceof BaseConnection pgBaseConnection)) {
                    throw new SQLException("Unable to unwrap to BaseConnection");
                }

                try (InputStream is = resource.getInputStream()) {
                    CopyManager copyManager = new CopyManager(pgBaseConnection);
                    logger.info("Executing COPY for developers CSV...");
                    copyManager.copyIn(sql, is);
                } catch (IOException e) {
                    logger.error("IOException during developers COPY", e);
                    throw new RuntimeException(e);
                }
                logger.info("Developers CSV import finished.");
                return null;
            });
        }
    }

    private void importGames() throws Exception {
        logger.info("Creating staging table for games if not exists...");
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS games_staging (" +
                "name VARCHAR(255) UNIQUE NOT NULL, " +
                "price DOUBLE PRECISION NOT NULL, " +
                "developer_name VARCHAR(255) NOT NULL)");
        logger.info("Starting import of games from CSV into staging table...");
        ClassPathResource resource = new ClassPathResource("data/games.csv");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String sql = "COPY games_staging(name, price, developer_name) FROM STDIN WITH CSV HEADER";
            jdbcTemplate.execute((ConnectionCallback<Object>) conn -> {
                logger.info("Unwrapping connection for games import COPY...");
                Connection nativeConn = conn;
                while (nativeConn.isWrapperFor(Connection.class)) {
                    nativeConn = nativeConn.unwrap(Connection.class);
                }

                if (!(nativeConn instanceof BaseConnection pgBaseConnection)) {
                    throw new SQLException("Unable to unwrap to BaseConnection");
                }

                try (InputStream is = resource.getInputStream()) {
                    CopyManager copyManager = new CopyManager(pgBaseConnection);
                    logger.info("Executing COPY for games CSV...");
                    copyManager.copyIn(sql, is);
                } catch (IOException e) {
                    logger.error("IOException during games COPY", e);
                    throw new RuntimeException(e);
                }
                logger.info("Games CSV import finished.");
                return null;
            });
        }

        logger.info("Inserting data from staging into games table...");
        int insertedRows = jdbcTemplate.update("INSERT INTO games(name, price, dev_id) " +
                "SELECT gs.name, gs.price, d.devId FROM games_staging gs " +
                "JOIN developers d ON gs.developer_name = d.name");
        logger.info("Inserted {} rows into games table.", insertedRows);

        logger.info("Dropping games staging table...");
        jdbcTemplate.execute("DROP TABLE games_staging");
        logger.info("Games staging table dropped.");
    }
}


    /*private final GameService gameService;


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
    }*/

