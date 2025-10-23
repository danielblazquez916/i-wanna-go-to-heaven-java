package utilz;

import entities.Player;
import gamestates.Playing;
import java.awt.event.KeyEvent;
import java.io.Serializable;
import game.MainGame;

public class PlayerPrefs implements Serializable {

    public int controlIzquierda = KeyEvent.VK_A;
    public int controlDerecha = KeyEvent.VK_D;
    public int controlSalto = KeyEvent.VK_UP;
    
    public String skinPlayer = "/res/player_anim.png";
    public int indexSkin = 0;
    
    public int TILE_SIZE_SAVED = 48;
    public int resolution_index = 0;
    public float[] volume = new float[]{-19.0f, -25.0f};

    public int tipoInsignia = 0;
    public int muertesTotales = 0;
    
    public int indexCP = 0;
    
    public boolean mostrarFPS = false;
    public boolean FPSLimit = false;
    
    public PlayerPrefs() {}
    
    public void resetearPartida(MainGame game){
        indexCP = 0;
        muertesTotales = 0;
        game.playingState.getPlayer().respawnX = game.playingState.lm.getCurrentLevel().getCheckpoints()[game.pp.indexCP].getxPos();
        game.playingState.getPlayer().respawnY = game.playingState.lm.getCurrentLevel().getCheckpoints()[game.pp.indexCP].getyPos();
    }
}
