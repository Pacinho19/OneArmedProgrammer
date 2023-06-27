package pl.pacinho.onearmedprogrammer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.pacinho.onearmedprogrammer.config.UIConfig;

@RequiredArgsConstructor
@Controller
public class GameController {

    @GetMapping(UIConfig.HOME)
    public String gameHome(Model model) {
        return "home";
    }

}
