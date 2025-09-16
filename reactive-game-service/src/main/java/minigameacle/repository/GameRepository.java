package minigameacle.repository;

import minigameacle.model.Game;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

public interface GameRepository extends ReactiveCrudRepository<Game, Integer> {
    Mono<Game> findByNameIgnoreCase(String name);
    Flux<Game> findAll();
    Flux<Game> findAllByDevId(Integer devId);
}

