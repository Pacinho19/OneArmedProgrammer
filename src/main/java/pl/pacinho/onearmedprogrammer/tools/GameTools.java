package pl.pacinho.onearmedprogrammer.tools;

import pl.pacinho.onearmedprogrammer.model.dto.SlotDto;
import pl.pacinho.onearmedprogrammer.model.entity.Game;

import java.util.List;
import java.util.stream.Collectors;

public class GameTools {
    public static List<SlotDto> getSlots(Game game) {
       return SlotTools.getSlotFields()
               .stream()
               .map(field -> SlotTools.getSlot(game, field))
               .collect(Collectors.toList());
    }
}
