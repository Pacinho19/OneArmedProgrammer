package pl.pacinho.onearmedprogrammer.tools;

import pl.pacinho.onearmedprogrammer.config.GameConfig;
import pl.pacinho.onearmedprogrammer.model.dto.SlotDto;
import pl.pacinho.onearmedprogrammer.model.enums.Sign;
import pl.pacinho.onearmedprogrammer.utils.RandomUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class OneArmedTools {

    private static Map<Integer, List<SlotDto>> sectionsMap = initSectionMap();

    private static Map<Integer, List<SlotDto>> initSectionMap() {
        return IntStream.rangeClosed(1, GameConfig.SECTION_COUNT)
                .boxed()
                .collect(Collectors.toMap(i -> i, i -> getSlotSection()));
    }

    private static List<SlotDto> getSlotSection() {
        AtomicInteger counter = new AtomicInteger(0);

        List<SlotDto> section = Arrays.stream(Sign.values())
                .map(OneArmedTools::getSignList)
                .flatMap(List::stream)
                .map(sign -> new SlotDto(counter.getAndIncrement(), sign))
                .collect(Collectors.toList());

        Collections.shuffle(section);
        return section;
    }

    private static List<Sign> getSignList(Sign sign) {
        return IntStream.range(0, sign.getCount())
                .boxed()
                .map(i -> sign)
                .toList();
    }

    public static List<SlotDto> randomSections() {
        return IntStream.rangeClosed(1, GameConfig.SECTION_COUNT)
                .boxed()
                .map(OneArmedTools::getRandomSignFromSection)
                .collect(Collectors.toList());
    }

    private static SlotDto getRandomSignFromSection(Integer idx) {
        List<SlotDto> signs = sectionsMap.get(idx);
        return signs.get(RandomUtils.getInt(0, signs.size() - 1));
    }

    public static SlotDto getSlotFromSection(int slotIdx, int sectionIdx) {
        return sectionsMap.get(sectionIdx)
                .stream()
                .filter(slotDto -> slotDto.idx() == slotIdx)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Invalid slot index in section " + sectionIdx));
    }
}
