package pl.pacinho.onearmedprogrammer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.pacinho.onearmedprogrammer.model.dto.SlotDto;
import pl.pacinho.onearmedprogrammer.model.entity.Spin;
import pl.pacinho.onearmedprogrammer.model.entity.SpinRound;
import pl.pacinho.onearmedprogrammer.repository.SpinRepository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class SpinService {

    private final SpinRepository spinRepository;

    private void save(SpinRound spinRound, int spinIdx, Integer section, SlotDto slotDto) {
        spinRepository.save(
                new Spin(spinIdx, spinRound, slotDto.sign(), slotDto.idx(), section)
        );
    }

    public List<Spin> getSpinForRound(SpinRound spinRound) {
        return spinRepository.findAllBySpinRound(spinRound);
    }

    public void saveAll(SpinRound spinRound, Map<Integer, List<SlotDto>> slotsMap) {

        slotsMap.entrySet()
                .forEach(entry -> {
                    AtomicInteger counter = new AtomicInteger(1);
                    entry.getValue()
                            .forEach(slotDto ->
                                    save(spinRound, counter.getAndIncrement(), entry.getKey(), slotDto)
                            );
                });

    }
}
