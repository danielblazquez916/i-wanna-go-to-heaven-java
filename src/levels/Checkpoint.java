package levels;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import utilz.LoadSave;
import static game.MainGame.TILE_SIZE;

public class Checkpoint implements Serializable {
    int xPos, yPos;
    BufferedImage[] checkpointImgs;
    int index = 0;
    Rectangle hitbox;
    
    // AÑADIR ANIMACION DEL CHECKPOINT AL TOCARLO!!

    public Checkpoint(int xPos, int yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
        
        setCheckpointImgs(2);
        
        setHitbox();
    }

    public int getIndex() {
        return index;
    }
    
    public void setIndex(int index){
        this.index = index;
    }
    
    public void setCheckpointImgs(int numberOfSprites){
        BufferedImage spriteCompleto = LoadSave.importImg(LoadSave.CHECKPOINT_SCHEMA);
        checkpointImgs = new BufferedImage[numberOfSprites];
        
        for(int i = 0; i < checkpointImgs.length; i++){
            checkpointImgs[i] = spriteCompleto.getSubimage(i*32, 0, 32, 32);
        }
    }
    
    public void setHitbox(){
        hitbox = new Rectangle(this.xPos, this.yPos, TILE_SIZE, TILE_SIZE);
    }
    
    public Rectangle getHitbox(){
        return hitbox;
    }

    public int getxPos() {
        return xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public BufferedImage[] getCheckpointImg() {
        return checkpointImgs;
    }
}
