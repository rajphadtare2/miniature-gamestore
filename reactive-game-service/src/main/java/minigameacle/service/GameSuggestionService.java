package minigameacle.service;

import jakarta.annotation.PostConstruct;
import minigameacle.model.Game;
import minigameacle.repository.GameRepository;
import minigameacle.structures.GameTrie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

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
        gameRepository.findAll()
                .map(game -> game.getName().toLowerCase())
                .doOnNext(gameTrie::insert)
                .subscribe();
    }

    public Flux<String> suggestGames(String prefix) {
        if (prefix == null || prefix.isEmpty()) {
            return Flux.empty();
        }
        prefix = prefix.toLowerCase();
        return Flux.fromIterable(gameTrie.suggest(prefix));
    }
}
