package pl.pacinho.onearmedprogrammer.model.dto;

import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpinRoundDto {

    @Getter
    private Map<Integer, List<SlotDto>> spinSectionMap;

    public SpinRoundDto() {
        this.spinSectionMap = new HashMap<>();
    }

}
