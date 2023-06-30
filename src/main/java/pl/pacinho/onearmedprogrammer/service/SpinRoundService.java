package pl.pacinho.onearmedprogrammer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.pacinho.onearmedprogrammer.model.entity.Game;
import pl.pacinho.onearmedprogrammer.model.entity.SpinRound;
import pl.pacinho.onearmedprogrammer.repository.SpinRoundRepository;

@RequiredArgsConstructor
@Service
public class SpinRoundService {

    private final SpinRoundRepository spinRoundRepository;

    public SpinRound newSpinRound(Game game) {
        SpinRound spinRound = spinRoundRepository.findTopByGameIdOrderByIdDesc(game.getId());
        int roundNumber = spinRound == null ? 1 : spinRound.getNumber() + 1;
        return spinRoundRepository.save(new SpinRound(game, roundNumber));
    }

    public SpinRound getLastSpinRound(Game game) {
        return spinRoundRepository.findTopByGameIdOrderByIdDesc(game.getId());
    }
}
