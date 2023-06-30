package pl.pacinho.onearmedprogrammer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import pl.pacinho.onearmedprogrammer.model.dto.GameDto;
import pl.pacinho.onearmedprogrammer.model.dto.SlotDto;
import pl.pacinho.onearmedprogrammer.model.dto.SpinDto;
import pl.pacinho.onearmedprogrammer.model.dto.mapper.GameDtoMapper;
import pl.pacinho.onearmedprogrammer.model.entity.Account;
import pl.pacinho.onearmedprogrammer.model.entity.Game;
import pl.pacinho.onearmedprogrammer.model.entity.Spin;
import pl.pacinho.onearmedprogrammer.model.entity.SpinRound;
import pl.pacinho.onearmedprogrammer.repository.AccountRepository;
import pl.pacinho.onearmedprogrammer.repository.GameRepository;
import pl.pacinho.onearmedprogrammer.tools.OneArmedTools;
import pl.pacinho.onearmedprogrammer.tools.SpinTools;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class GameService {

    private final GameRepository gameRepository;
    private final AccountRepository accountRepository;
    private final SpinRoundService spinRoundService;
    private final SpinService spinService;
    private final SimpMessagingTemplate simpMessagingTemplate;


    public GameDto getDtoByAccount(String accountName) {
        Optional<Game> gameOpt = gameRepository.findByAccountName(accountName);
        return gameOpt.map(GameDtoMapper::parse).orElse(null);
    }

    public Game newGame(String accountName, BigDecimal rate) {
        Account account = accountRepository.getByName(accountName);
        if (rate.compareTo(BigDecimal.ZERO) <= 0 || account.getBalance().compareTo(rate) < 0)
            throw new IllegalArgumentException("Invalid balance");

        Game lastGame = getLastGame(accountName);
        if (lastGame != null && lastGame.getLeftRounds() > 0)
            throw new IllegalStateException("Game in progress. Cannot create new game!");

        Game game = new Game(account);
        game.setRate(rate);

        Game newGame = gameRepository.save(game);
        spinRoundService.newSpinRound(newGame);
        return newGame;
    }

    private Game getLastGame(String accountName) {
        return gameRepository.findTopByAccountNameOrderByIdDesc(accountName);
    }

    public void spin(String gameId, String name, SpinDto spinDto) {
        Game game = gameRepository.findByUuidAndAccountName(gameId, name)
                .orElseThrow(() -> new IllegalArgumentException("Invalid game id: " + gameId));

        SpinRound spinRound = spinRoundService.getLastSpinRound(game);
        List<Spin> lastSpin = spinService.getSpinForRound(spinRound);
        Map<Integer, SlotDto> lastSpinTopValues = SpinTools.getLastSpinTopValues(lastSpin);

        Map<Integer, List<SlotDto>> slotsMap = OneArmedTools.spin(lastSpinTopValues, spinDto);

        spinService.saveAll(spinRound, slotsMap);

        game.decrementLeftRounds();
        gameRepository.save(game);

        spinRoundService.newSpinRound(game);

        simpMessagingTemplate.convertAndSend("/reload-board/" + game.getUuid(), "");
    }
}
