package game;

import gamestates.Gamestate;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

// Inputs me sirve para centralizar todos los inputs que vienen de diferentes
// partes del juego (por ejemplo los inputs en el menu, en los niveles...)
public class Inputs implements KeyListener {

    GamePanel gp;

    public Inputs(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        switch (Gamestate.currentState) {
            case Gamestate.OPTIONS_STATE: {
                break;
            }

            default: {
                break;
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (Gamestate.currentState) {
            case Gamestate.MENU_STATE: {
                if (gp.game.fadeTransition.isFinishTransition() || gp.game.fadeTransition.isFinishFadeIn()) {
                    gp.game.menuState.keyPressed(e);
                }
                break;
            }

            case Gamestate.OPTIONS_STATE: {
                if (gp.game.fadeTransition.isFinishTransition() || gp.game.fadeTransition.isFinishFadeIn()) {
                    gp.game.optionsState.keyPressed(e);
                    gp.game.optionsState.getCc().KeyPressed(e);
                }

                break;
            }

            case Gamestate.LEVEL_PLAYING_STATE: {
                if (!gp.game.playingState.getBoss().isStartScene()) {
                    gp.game.playingState.keyPressed(e);
                }
                break;
            }

            default: {
                break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (Gamestate.currentState) {
            case Gamestate.MENU_STATE: {
                gp.game.menuState.keyReleased(e);
                break;
            }

            case Gamestate.OPTIONS_STATE: {
                gp.game.optionsState.keyReleased(e);
                break;
            }

            case Gamestate.LEVEL_PLAYING_STATE: {
                gp.game.playingState.keyReleased(e);
                break;
            }

            default: {
                break;
            }
        }
    }
}
