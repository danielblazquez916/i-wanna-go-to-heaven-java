package game;
import javax.swing.JFrame;
import utilz.LoadSave;

public class GameWindow {

    public JFrame ventana;
    
    public GameWindow(GamePanel gp) {
        ventana = new JFrame("I Wanna Go To Heaven");
        
        // CONFIGURACION:
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setIconImage(LoadSave.importImg(LoadSave.ICON));
        
        ventana.setResizable(false);
        ventana.add(gp);
        ventana.pack();
        
        ventana.setLocationRelativeTo(null);
        
        ventana.setVisible(true);
    }
}
