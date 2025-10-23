package gamestates;

public class Gamestate {
    
    public static final String MENU_STATE = "menu";
    public static final String LEVEL_PLAYING_STATE = "onlevel";
    public static final String OPTIONS_STATE = "options";
    
    public static String currentState = MENU_STATE;
    
    public static void setGameState(String state){
        currentState = state;
    }
}
