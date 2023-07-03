package pl.pacinho.onearmedprogrammer.model.dto.mapper;

import pl.pacinho.onearmedprogrammer.model.dto.GameDto;
import pl.pacinho.onearmedprogrammer.model.dto.SlotDto;
import pl.pacinho.onearmedprogrammer.model.entity.Game;
import pl.pacinho.onearmedprogrammer.model.entity.Spin;
import pl.pacinho.onearmedprogrammer.model.entity.SpinRound;
import pl.pacinho.onearmedprogrammer.tools.GameTools;
import pl.pacinho.onearmedprogrammer.tools.JsonTools;
import pl.pacinho.onearmedprogrammer.tools.SpinTools;

import java.util.List;
import java.util.Map;

public class GameDtoMapper {

    public static GameDto parse(Game game, SpinRound lastFinishedRound, List<Spin> lastSpins) {
        if (game == null)
            return null;

        Map<Integer, List<SlotDto>> lastSpinMap = SpinTools.parseLastSpin(lastSpins);
        return GameDto.builder()
                .id(game.getUuid())
                .leftRounds(game.getLeftRounds())
                .lastSpin(lastSpinMap)
                .lastSpinJson(JsonTools.toJson(lastSpinMap))
                .winAmount(lastFinishedRound != null ? lastFinishedRound.getWinAmount() : null)
                .build();
    }
}