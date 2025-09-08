package minigameacle.game.service;

import jakarta.annotation.PostConstruct;
import minigameacle.game.model.Game;
import minigameacle.game.repository.GameRepository;
import minigameacle.game.structures.GameTrie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameSuggestionService {

    private final GameRepository gameRepository;
    private final GameTrie gameTrie = new GameTrie();

    @Autowired
    public GameSuggestionService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @PostConstruct
    public void buildTrie() {
        List<Game> allGames = gameRepository.findAll();
        for (Game game : allGames) {
            String name = game.getName().toLowerCase();
            gameTrie.insert(name);
        }
    }

    public List<String> suggestGames(String prefix) {
        if (prefix == null || prefix.isEmpty()) {
            return List.of();
        }
        prefix = prefix.toLowerCase();
        return gameTrie.suggest(prefix);
    }
}

