package pl.pacinho.onearmedprogrammer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.pacinho.onearmedprogrammer.config.UIConfig;
import pl.pacinho.onearmedprogrammer.model.dto.GameDto;
import pl.pacinho.onearmedprogrammer.model.dto.SpinDto;
import pl.pacinho.onearmedprogrammer.service.GameService;

@RequiredArgsConstructor
@Controller
public class GameController {

    private final GameService gameService;

    @GetMapping(UIConfig.HOME)
    public String gameHome(Model model, Authentication authentication) {
        model.addAttribute("game", gameService.getDtoByAccount(authentication.getName()));
        return "home";
    }

    @GetMapping(UIConfig.GAME_BOARD_RELOAD)
    public String reloadBoard(Authentication authentication,
                              Model model,
                              @PathVariable(value = "gameId") String gameId) {
        GameDto gameDto = gameService.getDtoByAccount(authentication.getName());
        model.addAttribute("game", gameDto);
        if (!gameDto.getId().equals(gameId)) {
            throw new IllegalArgumentException("Invalid game id: " + gameId);
        }
        return "fragments/board :: boardFrag";
    }

    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @PostMapping(UIConfig.GAME_SPIN)
    public void spin(Authentication authentication,
                       Model model,
                       @PathVariable(value = "gameId") String gameId,
                       @RequestBody SpinDto spinDto) {
        gameService.spin(gameId, authentication.getName(), spinDto);
    }

}
