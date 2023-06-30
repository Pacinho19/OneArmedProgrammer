package pl.pacinho.onearmedprogrammer.tools;

import com.google.gson.Gson;
import pl.pacinho.onearmedprogrammer.model.dto.SlotDto;

import java.util.List;
import java.util.Map;

public class JsonTools {
    public static String toJson(Map<Integer, List<SlotDto>> lastSpinMap) {
        return new Gson().toJson(lastSpinMap);
    }
}