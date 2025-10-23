package levels;

import entities.Player;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import game.MainGame;

public class DamageTile extends Tile {
    
    Rectangle hitbox;
    int xHitboxOffset = (int)MainGame.valorRelativo(11);
    int yHitboxOffset = (int)MainGame.valorRelativo(18);
    
    public DamageTile(int index, int xPos, int yPos, BufferedImage tileImg, Rectangle hitbox) {
        super(index, xPos, yPos, tileImg);
        this.hitbox = hitbox;
        
        hitboxEqualsPos();
    }
    
    public void hitboxEqualsPos(){
        this.hitbox.x = this.xPos+xHitboxOffset;
        this.hitbox.y = this.yPos+yHitboxOffset;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }
}
