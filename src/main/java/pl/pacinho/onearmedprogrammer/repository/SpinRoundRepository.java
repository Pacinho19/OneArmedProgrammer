package pl.pacinho.onearmedprogrammer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.pacinho.onearmedprogrammer.model.entity.SpinRound;
import pl.pacinho.onearmedprogrammer.model.enums.RoundStatus;

@Repository
public interface SpinRoundRepository extends JpaRepository<SpinRound, Long> {
    SpinRound findTopByGameIdAndStatusOrderByIdDesc(Long id, RoundStatus status);
}