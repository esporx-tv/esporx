package tv.esporx.repositories;

import org.springframework.data.repository.CrudRepository;
import tv.esporx.domain.Game;

public interface GameRepository extends CrudRepository<Game, Long> {
	Game findByTitle(String title);
}