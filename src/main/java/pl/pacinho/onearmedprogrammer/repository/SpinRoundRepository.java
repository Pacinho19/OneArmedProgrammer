package pl.pacinho.onearmedprogrammer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.pacinho.onearmedprogrammer.model.entity.SpinRound;

@Repository
public interface SpinRoundRepository extends JpaRepository<SpinRound, Long> {
    SpinRound findTopByGameIdOrderByIdDesc(Long id);
}
