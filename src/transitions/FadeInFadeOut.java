package transitions;

import gamestates.Gamestate;
import java.awt.Color;
import java.awt.Graphics;
import static game.MainGame.TILE_HEIGHT;
import static game.MainGame.TILE_WIDTH;

public class FadeInFadeOut {

    int opacity, incOpacity = 1, tick;
    String state;
    int opacityTime = 1;
    boolean finishTransition = true, finishFadeIn;
    Color color;

    //////////////////////// constructores; ///////////////////////////
    public FadeInFadeOut(int opacityTime, int incOpacity, Color color) {
        this.opacityTime = opacityTime;
        this.incOpacity = incOpacity;
        this.color = color;
    }

    public FadeInFadeOut(int opacityTime) {
        this.opacityTime = opacityTime;
    }

    public FadeInFadeOut(String state) {
        this.state = state;
    }
    
    public FadeInFadeOut() {
        // vacio...
    }
    ///////////////////////////////////////////////////////////////////
    
    
    
    ////////////////////////// updates; ///////////////////////////////
    public void update() {
        if (!finishTransition) {

            fadeIn();

            // CHANGE SCENE:
            if (opacity >= 255 && !finishFadeIn) {
                if(state != null && !state.equals(Gamestate.currentState)){
                    changeScene();
                }
                
                tick++;
                if(tick >= 30){
                    tick = 0;
                    finishFadeIn = true;
                }
            }

            fadeOut();

            if (opacity == 0 && finishFadeIn) {
                finishTransition = true;
                finishFadeIn = false;
            }
        }
    }

    public void fadeOut() {
        
        if (opacity > 0 && finishFadeIn) {
            tick++;
            if (tick >= opacityTime) {
                tick = 0;
                opacity -= incOpacity;
                if (opacity < 0) {
                    opacity = 0;
                }
            }
        }
    }

    public void fadeIn() {
        if (opacity < 255 && !finishFadeIn) {
            tick++;
            if (tick >= opacityTime) {
                tick = 0;
                opacity += incOpacity;
                if (opacity > 255) {
                    opacity = 255;
                }
               
            }
        }
    }

    public void changeScene() {
        Gamestate.setGameState(state);
    }
    ///////////////////////////////////////////////////////////////////
    
    
    // |    |    |    |    |    |    |    |    |    |    |    |    | \\
    // v    v    v    v    v    v    v    v    v    v    v    v    v \\
    

    ////////////////////////// draws; /////////////////////////////////
    public void draw(Graphics g) {
        g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), opacity));
        g.fillRect(0, 0, TILE_WIDTH, TILE_HEIGHT);
    }
    ///////////////////////////////////////////////////////////////////

    public void setState(String state) {
        this.state = state;
    }

    public boolean isFinishTransition() {
        return finishTransition;
    }   

    public String getState() {
        return state;
    }

    public void setFinishTransition(boolean finishTransition) {
        this.finishTransition = finishTransition;
    }

    public boolean isFinishFadeIn() {
        return finishFadeIn;
    }

    public void setOpacity(int opacity) {
        this.opacity = opacity;
    }

    public void setTick(int tick) {
        this.tick = tick;
    }
}
