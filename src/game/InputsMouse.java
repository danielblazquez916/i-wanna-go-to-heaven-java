package game;

import gamestates.Gamestate;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class InputsMouse implements MouseListener, MouseMotionListener {

    GamePanel gp;

    public InputsMouse(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        switch (Gamestate.currentState) {
            case Gamestate.MENU_STATE: {
                break;
            }

            case Gamestate.LEVEL_PLAYING_STATE: {
                // hola
                break;
            }

            case Gamestate.OPTIONS_STATE: {
                if (gp.game.optionsState.isGoIntoPopUp()) {
                    switch (gp.game.optionsState.getIndex()) {
                        case 0: {
                            // mouse input al cambiar de controles:
                            gp.game.optionsState.getCc().MouseClicked(e);
                            break;
                        }
                        
                        case 1: {
                            // mouse input al cambiar de controles:
                            gp.game.optionsState.getCs().MouseClicked(e);
                            break;
                        }
                        
                        case 2: {
                            gp.game.optionsState.getFpsm().MouseClicked(e);
                            break;
                        }

                        // los demás casos...
                        default: {
                            break;
                        }
                    }
                }
                
                break;
            }

            default: {
                System.out.println("bro no puede ser");
                break;
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(gp.game.optionsState.isGoIntoPopUp()){
            gp.game.optionsState.getFpsm().mousePressed(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(gp.game.optionsState.isGoIntoPopUp()){
            gp.game.optionsState.getFpsm().MouseReleased(e);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // no lo voy a poder usar al pasar por encima de la imagen
        // del boton porque NO ES UN COMPONENTE!!
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(gp.game.optionsState.isGoIntoPopUp()){
            gp.game.optionsState.getFpsm().mouseDragged(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        //
    }
}
