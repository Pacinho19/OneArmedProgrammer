package pl.pacinho.onearmedprogrammer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.pacinho.onearmedprogrammer.config.GameConfig;
import pl.pacinho.onearmedprogrammer.config.UIConfig;
import pl.pacinho.onearmedprogrammer.model.dto.GameDto;
import pl.pacinho.onearmedprogrammer.model.dto.SpinDto;
import pl.pacinho.onearmedprogrammer.service.AccountService;
import pl.pacinho.onearmedprogrammer.service.GameService;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Controller
public class GameController {

    private final GameService gameService;
    private final AccountService accountService;

    @GetMapping(UIConfig.HOME)
    public String gameHome(Model model, Authentication authentication) {
        model.addAttribute("game", gameService.getDtoByAccount(authentication.getName()));
        model.addAttribute("account", accountService.getDtoByLogin(authentication.getName()));
        model.addAttribute("sectionCount", GameConfig.SECTION_COUNT);
        return "home";
    }

    @GetMapping(UIConfig.GAME_BOARD_RELOAD)
    public String reloadBoard(Authentication authentication,
                              Model model,
                              @PathVariable(value = "gameId") String gameId) {
        GameDto gameDto = gameService.getDtoByAccount(authentication.getName());
        model.addAttribute("game", gameDto);
        model.addAttribute("account", accountService.getDtoByLogin(authentication.getName()));
        model.addAttribute("sectionCount", GameConfig.SECTION_COUNT);
        return "fragments/board :: board";
    }

    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @PostMapping(UIConfig.GAME_SPIN)
    public void spin(Authentication authentication,
                     Model model,
                     @PathVariable(value = "gameId") String gameId,
                     @RequestBody SpinDto spinDto) {
        gameService.spin(gameId, authentication.getName(), spinDto);
    }

    @PostMapping(UIConfig.NEW_GAME)
    public String spin(Authentication authentication, @RequestParam(name = "rate") BigDecimal rate) {
        gameService.newGame(authentication.getName(), rate);
        return "redirect:" + UIConfig.HOME;
    }

    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @PostMapping(UIConfig.GAME_SPIN_ANIMATION_OFF)
    public void spinAnimationOff(Authentication authentication,   @PathVariable(value = "gameId") String gameId) {
        gameService.spinAnimationOff(authentication.getName(), gameId);
    }

}