package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import game.MainGame;
import static game.MainGame.TILE_SIZE;

public class Enemy extends Entity {

    // De momento no tiene animaciones, pero si las tuviera
    // ya sabes que hacer:
    BufferedImage[] enemyAnim;
    BufferedImage fullImage;
    public boolean endEvent;
    int enemySpeed = (int)MainGame.valorRelativo(7);
    int xHitboxOffset, yHitboxOffset;
    // 10 13
    public int initX, initY;
    Rectangle copyHitbox; 
   
    
    // la variable de trigger va a servir para cuando el 
    // enemigo se mueve o hace algo cuando el player pasa
    // por algún lugar:
    boolean triggerActivado = false;
    boolean invisible;
    MainGame game;

    public Enemy(int x, int y, Rectangle hitbox, int xHitboxOffset, int yHitboxOffset, MainGame game, BufferedImage fullImage) {
        super(x, y);
        this.game = game;
        this.hitbox = hitbox;
        copyHitbox = this.hitbox;
        this.fullImage = fullImage;

        initX = x;
        initY = y;
        this.xHitboxOffset = xHitboxOffset;
        this.yHitboxOffset = yHitboxOffset;

        hitbox.x = x + xHitboxOffset;
        hitbox.y = y + yHitboxOffset;
    }

    public boolean isInvisible() {
        return invisible;
    }

    public void setInvisible(boolean invisible) {
        this.invisible = invisible;
    }

    public boolean isTriggerActivado() {
        return triggerActivado;
    }
    
    /*
    public void playerMorirPorEnemigo(){
        Player player = game.playingState.getPlayer();
        
        if(player.hitbox.intersects(this.hitbox)){
            player.heMuerto = true;
        }
    }
    */

    public void setTriggerActivado(boolean triggerActivado) {
        this.triggerActivado = triggerActivado;
    }

    public BufferedImage[] getEnemyAnim() {
        return enemyAnim;
    }

    public BufferedImage getFullImage() {
        return fullImage;
    }

    public void setFullImage(BufferedImage fullImage) {
        this.fullImage = fullImage;
    }

    public void setEnemySpeed(int enemySpeed) {
        this.enemySpeed = enemySpeed;
    }

    public int getEnemySpeed() {
        return enemySpeed;
    }

    public void setSubImages(int numberOfSprites) {
        enemyAnim = new BufferedImage[numberOfSprites];

        for (int i = 0; i < enemyAnim.length; i++) {
            enemyAnim[i] = fullImage.getSubimage(i * (fullImage.getWidth() / numberOfSprites), 0, fullImage.getWidth() / numberOfSprites, fullImage.getHeight());
        }
    }

    public void updateHitbox() {
        
        disapearHitbox();
        
        hitbox.x = x + xHitboxOffset;
        hitbox.y = y + yHitboxOffset;
    }
    
    public void disapearHitbox(){
        if(isInvisible()){
            hitbox = new Rectangle(0, 0);
        }else{
            if(hitbox.width == 0 && hitbox.height == 0){
                hitbox = copyHitbox;
            }
        }
    }
    
    public void drawHitbox(Graphics g){
        g.setColor(Color.GREEN);
        g.drawRect(hitbox.x - (game.playingState.lm.offsetMapX * TILE_SIZE), hitbox.y - (game.playingState.lm.offsetMapY * TILE_SIZE), hitbox.width, hitbox.height);
    }

    public void resetToInitialPosition() {
        x = initX;
        y = initY;
    }
}
