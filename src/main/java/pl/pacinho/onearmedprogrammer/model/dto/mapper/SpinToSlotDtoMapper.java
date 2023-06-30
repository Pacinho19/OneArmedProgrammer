package pl.pacinho.onearmedprogrammer.model.dto.mapper;

import pl.pacinho.onearmedprogrammer.model.dto.SlotDto;
import pl.pacinho.onearmedprogrammer.model.entity.Spin;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SpinToSlotDtoMapper {
    public static List<SlotDto> parseList(List<Spin> lastSpins) {
        return lastSpins.stream()
                .sorted(Comparator.comparing(Spin::getIdx))
                .map(SpinToSlotDtoMapper::parse)
                .collect(Collectors.toList());
    }

    private static SlotDto parse(Spin spin) {
        return new SlotDto(spin.getSignIdx(), spin.getSign());
    }
}