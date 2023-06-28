package pl.pacinho.onearmedprogrammer.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Sign {

    ANDROID("android2", 1, 6),
    CHROME("browser-chrome", 3, 5),
    DATABASE("database", 5, 4),
    DEVICE_SSD("device-ssd", 10, 3),
    GIT("android2", 50, 2),
    GITHUB("github", 100, 1);

    private final String iconClass;
    private final Integer value;
    private final Integer count;

}
