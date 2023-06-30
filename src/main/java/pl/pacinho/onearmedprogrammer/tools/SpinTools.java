package pl.pacinho.onearmedprogrammer.tools;

import pl.pacinho.onearmedprogrammer.model.dto.SlotDto;
import pl.pacinho.onearmedprogrammer.model.dto.mapper.SpinToSlotDtoMapper;
import pl.pacinho.onearmedprogrammer.model.entity.Spin;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SpinTools {
    public static Map<Integer, SlotDto> getLastSpinTopValues(List<Spin> lastSpin) {
        Map<Integer, SlotDto> collect = lastSpin.stream()
                .collect(Collectors.groupingBy(Spin::getSection))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(entry -> entry.getKey(), entry -> getTopSignOfSection(entry.getValue())));
        return collect.isEmpty()
                ? OneArmedTools.randomTopValues()
                : collect;
    }

    private static SlotDto getTopSignOfSection(List<Spin> spins) {
        Spin lastPosition = spins.stream()
                .sorted(Comparator.comparing(Spin::getIdx).reversed())
                .limit(1)
                .findFirst()
                .get();

        return new SlotDto(lastPosition.getSignIdx(), lastPosition.getSign());


    }

    public static Map<Integer, List<SlotDto>> parseLastSpin(List<Spin> lastSpins) {
        if(lastSpins==null || lastSpins.isEmpty())
            return null;

        return lastSpins.stream()
                .collect(Collectors.groupingBy(Spin::getSection))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> SpinToSlotDtoMapper.parseList(entry.getValue())));
    }
}