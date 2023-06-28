package pl.pacinho.onearmedprogrammer.model.dto.mapper;

import pl.pacinho.onearmedprogrammer.model.dto.GameDto;
import pl.pacinho.onearmedprogrammer.model.entity.Game;
import pl.pacinho.onearmedprogrammer.tools.GameTools;

public class GameDtoMapper {

    public static GameDto parse(Game game) {
        return GameDto.builder()
                .id(game.getUuid())
                .slots(GameTools.getSlots(game))
                .build();
    }
}
