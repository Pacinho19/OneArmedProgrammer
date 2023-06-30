package pl.pacinho.onearmedprogrammer.config;

public class UIConfig {
    public static final String HOME = "/one-armed-programmer";
    public static final String GAME = HOME + "/game";
    public static final String GAMES_ID = GAME + "/{gameId}";
    public static final String NEW_GAME = GAME + "/new";
    public static final String GAME_BOARD_RELOAD = GAMES_ID + "/board/reload";
    public static final String GAME_SPIN = GAMES_ID + "/spin";
}