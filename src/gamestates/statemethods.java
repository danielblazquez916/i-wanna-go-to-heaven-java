package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;

public interface statemethods {
    public void update();
    public void draw(Graphics g);
    
    // input teclado:
    public void keyPressed(KeyEvent e);
    public void keyReleased(KeyEvent e);
}
