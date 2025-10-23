package game;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import utilz.PlayerPrefs;

public class WindowListener extends WindowAdapter {

    MainGame game;
    PlayerPrefs pp;
    
    public WindowListener(MainGame game, PlayerPrefs pp) {
        this.game = game;
        this.pp = pp;
    }

    @Override
    public void windowClosing(WindowEvent e) {
        
        game.serializar(this.pp);
        
        /*
        int respuesta = JOptionPane.showConfirmDialog(game.gw.ventana, "¿Estás seguro que quieres cerrar el juego?", "cerrar juego", JOptionPane.YES_NO_OPTION);
        if (respuesta == JOptionPane.YES_OPTION) {
            game.gw.ventana.dispose(); // Cierra la ventana
            game.hiloActivado = false;
        }
        */
    }
}
