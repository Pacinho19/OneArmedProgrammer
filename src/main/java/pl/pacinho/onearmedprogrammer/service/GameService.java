package pl.pacinho.onearmedprogrammer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import pl.pacinho.onearmedprogrammer.model.dto.GameDto;
import pl.pacinho.onearmedprogrammer.model.dto.SlotDto;
import pl.pacinho.onearmedprogrammer.model.dto.SpinDto;
import pl.pacinho.onearmedprogrammer.model.dto.mapper.GameDtoMapper;
import pl.pacinho.onearmedprogrammer.model.entity.Game;
import pl.pacinho.onearmedprogrammer.repository.AccountRepository;
import pl.pacinho.onearmedprogrammer.repository.GameRepository;
import pl.pacinho.onearmedprogrammer.tools.OneArmedTools;
import pl.pacinho.onearmedprogrammer.tools.SlotTools;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class GameService {

    private final GameRepository gameRepository;
    private final AccountRepository accountRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;


    public GameDto getDtoByAccount(String accountName) {
        Optional<Game> gameOpt = gameRepository.findByAccountName(accountName);
        Game game = gameOpt.orElseGet(() -> newGame(accountName));
        return GameDtoMapper.parse(game);
    }

    private Game newGame(String accountName) {
        Game game = new Game(accountRepository.getByName(accountName));

        List<SlotDto> slots = OneArmedTools.randomSections();
        SlotTools.setSlots(game, slots);

        return gameRepository.save(game);
    }

    public void spin(String gameId, String name, SpinDto spinDto) {
        Game game = gameRepository.findByUuidAndAccountName(gameId, name)
                .orElseThrow(() -> new IllegalArgumentException("Invalid game id: " + gameId));

        OneArmedTools.spin(game, spinDto);
        gameRepository.save(game);
        simpMessagingTemplate.convertAndSend("/reload-board/" + game.getUuid(), "");
    }
}
