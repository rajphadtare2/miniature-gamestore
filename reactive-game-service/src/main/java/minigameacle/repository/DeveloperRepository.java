package minigameacle.repository;

import minigameacle.model.Developer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface DeveloperRepository extends ReactiveCrudRepository<Developer, Integer> {
    Mono<Developer> findByNameIgnoreCase(String name);
    Flux<Developer> findAll();
}

