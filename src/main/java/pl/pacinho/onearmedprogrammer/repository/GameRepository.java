package pl.pacinho.onearmedprogrammer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.pacinho.onearmedprogrammer.model.entity.Game;

import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    Optional<Game> findByAccountName(String accountName);

    Optional<Game> findByUuidAndAccountName(String uuid, String accountName);

    Game findTopByAccountNameOrderByIdDesc(String accountName);
}
