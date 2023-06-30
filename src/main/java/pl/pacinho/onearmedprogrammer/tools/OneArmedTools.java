package pl.pacinho.onearmedprogrammer.tools;

import pl.pacinho.onearmedprogrammer.config.GameConfig;
import pl.pacinho.onearmedprogrammer.model.dto.SlotDto;
import pl.pacinho.onearmedprogrammer.model.dto.SpinDto;
import pl.pacinho.onearmedprogrammer.model.entity.Game;
import pl.pacinho.onearmedprogrammer.model.enums.Sign;
import pl.pacinho.onearmedprogrammer.utils.RandomUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class OneArmedTools {

    private static final Map<Integer, List<SlotDto>> SECTION_MAP = initSectionMap();
    private static final Map<Integer, List<Integer>> SPIN_POWERS = initSpinPowers();
    private static final Map<Integer, Integer> SECTION_SPIN_RATIO = initSectionSpinRatio();

    private static Map<Integer, Integer> initSectionSpinRatio() {
        return IntStream.rangeClosed(1, GameConfig.SECTION_COUNT)
                .boxed()
                .collect(Collectors.toMap(i -> i, i -> RandomUtils.getInt(2, 5)));
    }

    private static Map<Integer, List<SlotDto>> initSectionMap() {
        return IntStream.rangeClosed(1, GameConfig.SECTION_COUNT)
                .boxed()
                .collect(Collectors.toMap(i -> i, i -> getSlotSection()));
    }

    private static Map<Integer, List<Integer>> initSpinPowers() {
        return IntStream.rangeClosed(1, GameConfig.SECTION_COUNT)
                .boxed()
                .collect(Collectors.toMap(i -> i, i -> getRandomSpinPowers()));
    }

    private static List<Integer> getRandomSpinPowers() {
        List<Integer> integers = IntStream.rangeClosed(GameConfig.MIN_POWER_FACTOR, GameConfig.MAX_POWER_FACTOR)
                .boxed()
                .collect(Collectors.toList());

        Collections.shuffle(integers);
        return integers;
    }


    private static List<SlotDto> getSlotSection() {
        AtomicInteger counter = new AtomicInteger(0);

        List<SlotDto> section = Arrays.stream(Sign.values())
//                .map(OneArmedTools::getSignList) //Many the same symbols in one sections
//                .flatMap(List::stream)
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
        List<SlotDto> signs = SECTION_MAP.get(idx);
        return signs.get(RandomUtils.getInt(0, signs.size() - 1));
    }

    public static SlotDto getSlotFromSection(int slotIdx, int sectionIdx) {
        return SECTION_MAP.get(sectionIdx)
                .stream()
                .filter(slotDto -> slotDto.idx() == slotIdx)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Invalid slot index in section " + sectionIdx));
    }

    public static Map<Integer, List<SlotDto>> spin(Map<Integer, SlotDto> lastSpinTopValues, SpinDto spinDto) {
        return IntStream.rangeClosed(1, GameConfig.SECTION_COUNT)
                .boxed()
                .collect(Collectors.toMap(i -> i, i -> getSpinSlots(lastSpinTopValues.get(i), i, spinDto)));
    }

    private static List<SlotDto> getSpinSlots(SlotDto slot, Integer sectionIdx, SpinDto spinDto) {
        return spinSection(slot, sectionIdx, spinDto);
    }

    private static List<SlotDto> spinSection(SlotDto slot, int sectionIdx, SpinDto spinDto) {
        List<SlotDto> out = new ArrayList<>();

        int spinPower = SPIN_POWERS.get(sectionIdx).get(spinDto.value());
        int spinRatio = SECTION_SPIN_RATIO.get(sectionIdx);
        int spinCount = (spinRatio * spinPower) / 10;

        if (spinCount < 10)
            spinCount = RandomUtils.getInt(10, 20);

        List<SlotDto> section = SECTION_MAP.get(sectionIdx);
        int slotIdx = getSlotIdxInSection(section, slot);
        for (int i = 0; i < spinCount; i++) {
            out.add(section.get(slotIdx));

            if (slotIdx < section.size() - 1) {
                slotIdx++;
                continue;
            }
            slotIdx = 0;
        }
        return out;
    }

    public static int getSlotIdxInSection(List<SlotDto> section, SlotDto slot) {
        return section.stream()
                .filter(slot1 -> slot1.idx() == slot.idx())
                .findFirst()
                .map(SlotDto::idx)
                .orElseThrow(() -> new IllegalArgumentException("Cannot find slot  in section"));
    }

    public static Map<Integer, SlotDto> randomTopValues() {
        return IntStream.rangeClosed(1, GameConfig.SECTION_COUNT)
                .boxed()
                .collect(Collectors.toMap(i -> i, OneArmedTools::getRandomSignFromSection));
    }
}
