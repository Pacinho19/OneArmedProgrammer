package pl.pacinho.onearmedprogrammer.tools;

import lombok.SneakyThrows;
import pl.pacinho.onearmedprogrammer.model.dto.SlotDto;
import pl.pacinho.onearmedprogrammer.model.entity.Game;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SlotTools {
    private static final String SLOT_FIELD_PREFIX = "slot";

    public static void setSlots(Game game, List<SlotDto> slots) {
        getSlotFields()
                .forEach(field -> {
                    field.setAccessible(true);
                    try {
                        field.set(game, getValue(field.getName(), slots));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    public static List<Field> getSlotFields() {
        return Arrays.stream(Game.class.getDeclaredFields())
                .filter(field -> field.getName().startsWith(SLOT_FIELD_PREFIX))
                .collect(Collectors.toList());
    }

    private static int getValue(String fieldName, List<SlotDto> slots) {
        int index = getSectionIdx(fieldName);
        return slots.get(index - 1).idx();
    }

    @SneakyThrows
    public static SlotDto getSlot(Game game, Field field) {
        field.setAccessible(true);
        int slotIdx = (int) field.get(game);
        return OneArmedTools.getSlotFromSection(slotIdx, getSectionIdx(field.getName()));
    }

    private static int getSectionIdx(String fieldName) {
        return Integer.parseInt(fieldName.replace(SLOT_FIELD_PREFIX, ""));
    }
}
