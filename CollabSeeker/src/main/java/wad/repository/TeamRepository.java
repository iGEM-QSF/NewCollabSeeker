package wad.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import wad.domain.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {
    
    Team findByName(String name);
    List<Team> findByDescriptionContainingIgnoreCase(String word);
    List<Team> findByTagsContainingIgnoreCase(String word);
    List<Team> findByNameContainingIgnoreCase(String word);
    
}