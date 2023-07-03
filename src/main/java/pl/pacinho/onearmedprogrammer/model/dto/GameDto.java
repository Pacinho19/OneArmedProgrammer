package pl.pacinho.onearmedprogrammer.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Builder
@AllArgsConstructor
@Getter
public class GameDto {

    private String id;
    private Map<Integer,List<SlotDto>> lastSpin;
    private String lastSpinJson;
    private int leftRounds;
    private BigDecimal winAmount;
}