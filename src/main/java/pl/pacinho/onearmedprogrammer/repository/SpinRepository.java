package pl.pacinho.onearmedprogrammer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.pacinho.onearmedprogrammer.model.entity.Game;
import pl.pacinho.onearmedprogrammer.model.entity.Spin;
import pl.pacinho.onearmedprogrammer.model.entity.SpinRound;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpinRepository extends JpaRepository<Spin, Long> {
    List<Spin> findAllBySpinRound(SpinRound spinRound);
}
