package minigameacle.game.controller;

import minigameacle.game.service.GameSuggestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GameSuggestionController {

    private final GameSuggestionService suggestionService;

    @Autowired
    public GameSuggestionController(GameSuggestionService suggestionService) {
        this.suggestionService = suggestionService;
    }

    @GetMapping("/suggestGames")
    public List<String> suggestGames(@RequestParam String query) {
        return suggestionService.suggestGames(query);
    }
}
