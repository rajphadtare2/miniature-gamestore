package minigameacle.controller;

import minigameacle.service.GameSuggestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/api/games/reactive")
public class GameSuggestionController {

    private final GameSuggestionService suggestionService;

    @Autowired
    public GameSuggestionController(GameSuggestionService suggestionService) {
        this.suggestionService = suggestionService;
    }

    @GetMapping("/suggestGames")
    public Flux<String> suggestGames(@RequestParam String query) {
        return suggestionService.suggestGames(query);
    }
}
