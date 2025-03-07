package minigameacle.game.repository;

import minigameacle.game.model.Developer;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeveloperRepository extends JpaRepository<Developer, Integer> {

    @EntityGraph(attributePaths = {"games"})  // Fetch games along with developers
    @Query("SELECT d FROM Developer d")
    List<Developer> findAllDevs();

    @Query("SELECT d FROM Developer d WHERE LOWER(d.name) = LOWER(:name)")
    Optional<Developer> findByNameIgnoreCase(@Param("name") String name);
}
