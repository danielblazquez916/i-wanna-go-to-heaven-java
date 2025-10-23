package ui;

import gamestates.Gamestate;
import java.awt.image.BufferedImage;
import utilz.LoadSave;

public class SectionButton {
    int xPos, yPos;
    int width, height;
    BufferedImage[] parts;
    public int xPosInitial, yPosInitial;
    
    // OPCIONAL:
    String stateToGo;

    public SectionButton(int xPos, int yPos, int width, int height, BufferedImage buttonImage) {
        this.xPos = xPos;
        this.yPos = yPos;
        
        xPosInitial = this.xPos;
        yPosInitial = this.yPos;
        
        this.width = width;
        this.height = height;
        parts = LoadSave.subdividirImagen(buttonImage, 2);
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }

    public BufferedImage[] getParts() {
        return parts;
    }

    public String getStateToGo() {
        return stateToGo;
    }
    
    public void setStateToGo(String stateToGo) {
        this.stateToGo = stateToGo;
    }
    
    public void setGamestate(){
        Gamestate.currentState = stateToGo;
    }

    public int getxPos() {
        return xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
