package levels;

import java.awt.image.BufferedImage;

public class Tile {
    int xPos, yPos;
    BufferedImage tileImg;
    int index;

    public Tile(int index, int xPos, int yPos, BufferedImage tileImg) {
        this.index = index;
        this.xPos = xPos;
        this.yPos = yPos;
        this.tileImg = tileImg;
    }

    public void setIndex(int index) {
        this.index = index;
    }
    
    public int getIndex(){
        return this.index;
    }

    public int getxPos() {
        return xPos;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }

    public BufferedImage getTileImg() {
        return tileImg;
    }

    public void setTileImg(BufferedImage tileImg) {
        this.tileImg = tileImg;
    }
}
