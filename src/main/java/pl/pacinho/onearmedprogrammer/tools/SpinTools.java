package pl.pacinho.onearmedprogrammer.tools;

import pl.pacinho.onearmedprogrammer.config.GameConfig;
import pl.pacinho.onearmedprogrammer.model.dto.SlotDto;
import pl.pacinho.onearmedprogrammer.model.dto.mapper.SpinToSlotDtoMapper;
import pl.pacinho.onearmedprogrammer.model.entity.Spin;
import pl.pacinho.onearmedprogrammer.model.enums.Sign;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        if (lastSpins == null || lastSpins.isEmpty())
            return null;

        return lastSpins.stream()
                .collect(Collectors.groupingBy(Spin::getSection))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> SpinToSlotDtoMapper.parseList(entry.getValue())));
    }

    public static BigDecimal calculateWinAmount(Map<Integer, List<SlotDto>> slotsMap, BigDecimal rate) {
        List<Sign> distinctSigns = IntStream.rangeClosed(1, GameConfig.SECTION_COUNT)
                .boxed()
                .map(sectionIdx -> {
                    List<SlotDto> slots = slotsMap.get(sectionIdx);
                    return slots.get(slots.size() - 1).sign();
                })
                .distinct()
                .toList();

        return distinctSigns.size() == 1
                ? rate.multiply(BigDecimal.valueOf(distinctSigns.get(0).getValue()))
                : BigDecimal.ZERO;
    }
}