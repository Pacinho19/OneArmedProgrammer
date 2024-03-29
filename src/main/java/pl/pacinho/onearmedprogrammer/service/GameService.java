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
import pl.pacinho.onearmedprogrammer.model.enums.RoundStatus;
import pl.pacinho.onearmedprogrammer.repository.AccountRepository;
import pl.pacinho.onearmedprogrammer.repository.GameRepository;
import pl.pacinho.onearmedprogrammer.tools.OneArmedTools;
import pl.pacinho.onearmedprogrammer.tools.SpinTools;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class GameService {

    private final GameRepository gameRepository;
    private final AccountRepository accountRepository;
    private final SpinRoundService spinRoundService;
    private final SpinService spinService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public GameDto getDtoByAccount(String accountName) {
        Game game = gameRepository.findTopByAccountNameOrderByIdDesc(accountName);

        List<Spin> lastSpin = null;
        SpinRound lastSpinRound = null;
        if (game != null) {
            lastSpinRound = spinRoundService.getLastSpinRound(game, RoundStatus.FINISHED);
            lastSpin = spinService.getSpinForRound(lastSpinRound);
        }
        return GameDtoMapper.parse(game, lastSpinRound, lastSpin);
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
        account.setBalance(account.getBalance().subtract(rate));

        Game newGame = gameRepository.save(game);
        spinRoundService.newSpinRound(newGame);
        return newGame;
    }

    private Game getLastGame(String accountName) {
        return gameRepository.findTopByAccountNameOrderByIdDesc(accountName);
    }

    public void spin(String gameId, String name, SpinDto spinDto) {
        Account account = accountRepository.getByName(name);

        Game game = gameRepository.findByUuidAndAccountName(gameId, name)
                .orElseThrow(() -> new IllegalArgumentException("Invalid game id: " + gameId));

        SpinRound spinRound = spinRoundService.getLastSpinRound(game, RoundStatus.IN_PROGRESS);
        List<Spin> lastSpin = spinService.getSpinForRound(spinRound);
        Map<Integer, SlotDto> lastSpinTopValues = SpinTools.getLastSpinTopValues(lastSpin);

        Map<Integer, List<SlotDto>> slotsMap = OneArmedTools.spin(lastSpinTopValues, spinDto);

        spinService.saveAll(spinRound, slotsMap);

        game.decrementLeftRounds();
        gameRepository.save(game);

        BigDecimal winAmount = SpinTools.calculateWinAmount(slotsMap, game.getRate());
        spinRound.setStatus(RoundStatus.FINISHED);
        spinRound.setWinAmount(winAmount);
        spinRoundService.save(spinRound);

        if (winAmount.compareTo(BigDecimal.ZERO) > 0) {
            account.setBalance(account.getBalance().add(winAmount));
            accountRepository.save(account);
        }

        if (game.getLeftRounds() > 0)
            spinRoundService.newSpinRound(game);

        simpMessagingTemplate.convertAndSend("/reload-board/" + game.getUuid(), "");
    }

    public void spinAnimationOff(String name, String gameId) {
        Game game = gameRepository.findByUuidAndAccountName(gameId, name)
                .orElseThrow(() -> new IllegalArgumentException("Invalid game id: " + gameId));

        SpinRound spinRound = spinRoundService.getLastSpinRound(game, RoundStatus.FINISHED);
        spinRound.setDisplayed(true);

        spinRoundService.save(spinRound);
    }
}