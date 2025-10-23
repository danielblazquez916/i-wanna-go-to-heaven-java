package entities;

import java.awt.Rectangle;
import static game.MainGame.TILE_SIZE;

public abstract class Entity {

    public int x, y;
    public Rectangle hitbox;
    
    public Entity(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public int settearXInMap(int colx){
        return ((colx/2)-1)*TILE_SIZE;
    }

    public int settearYInMap(int rowy){
        return (rowy-1)*TILE_SIZE;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public void setHitbox(Rectangle hitbox) {
        this.hitbox = hitbox;
    }
}
