package pl.pacinho.onearmedprogrammer.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@AllArgsConstructor
@Getter
public class GameDto {

    private String id;
    private List<SlotDto> slots;
}
