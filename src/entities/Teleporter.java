package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import utilz.LoadSave;
import game.MainGame;
import static game.MainGame.TILE_SIZE;

public class Teleporter {

    BufferedImage[] teleportAnim;
    int xPosOrigen, yPosOrigen, xPosDestino, yPosDestino;
    Rectangle hitbox;
    int height, width;
    int tick, index;
    public boolean teleportAppears, teleportDisappears;
    MainGame game;

    public Teleporter(BufferedImage textura, int xPosOrigen, int yPosOrigen, int xPosDestino, int yPosDestino, int height, int width, Rectangle hitbox, MainGame game) {
        this.game = game;
        this.height = height;
        this.width = width;
        this.xPosOrigen = xPosOrigen;
        this.yPosOrigen = yPosOrigen;
        this.xPosDestino = xPosDestino;
        this.yPosDestino = yPosDestino;
        this.hitbox = hitbox;

        hitbox.x = this.xPosOrigen + (width/2) - (hitbox.width/2);
        hitbox.y = this.yPosOrigen + (height/2) - (hitbox.height/2);

        teleportAnim = LoadSave.subdividirImagen(textura, 59);
    }
    
    public void resetTeleporter(){
        tick = 0;
        teleportAppears = false;
        teleportDisappears = false;
        index = 0;
    }

    public void updateAnimation() {

        if (!teleportAppears) {
            tick++;
            if (tick >= 5) {
                tick = 0;
                index++;
                if (index >= 39) {
                    teleportAppears = true;
                }
            }
        }else if(teleportDisappears){
            tick++;
            if(tick >= 10){
                index++;
                if (index >= 58) {
                    teleportDisappears = false;
                    Player player = game.playingState.getPlayer();
                    player.hitbox.x = xPosDestino;
                    player.hitbox.y = yPosDestino;
                }
            }
        }
    }

    public void teleportIfIntersects() {
        Player player = game.playingState.getPlayer();

        if (player.hitbox.intersects(this.hitbox)) {
            teleportDisappears = true;
        }
    }

    public void draw(Graphics g) {
        g.drawImage(teleportAnim[index], xPosOrigen - (game.playingState.lm.offsetMapX * TILE_SIZE), yPosOrigen - (game.playingState.lm.offsetMapY * TILE_SIZE), width, height, null);
        
        //g.setColor(Color.RED);
        //g.drawRect(hitbox.x - (game.playingState.lm.offsetMapX * TILE_SIZE), hitbox.y - (game.playingState.lm.offsetMapY * TILE_SIZE), hitbox.width, hitbox.height);
    }

    public void update() {
        if(teleportAppears){
            teleportIfIntersects();
        }
        
        updateAnimation();
    }
}
