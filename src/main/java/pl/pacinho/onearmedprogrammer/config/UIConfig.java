package pl.pacinho.onearmedprogrammer.config;

public class UIConfig {
    public static final String HOME = "/one-armed-programmer";
    public static final String GAMES = HOME + "/game"+ "/{gameId}";
    public static final String GAME_BOARD_RELOAD = GAMES + "/board/reload";
    public static final String GAME_SPIN = GAMES + "/spin";
}