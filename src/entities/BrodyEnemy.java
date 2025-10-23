package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import game.MainGame;
import static game.MainGame.TILE_COLS;
import static game.MainGame.TILE_SIZE;

public class BrodyEnemy extends Enemy {

    int index, tick;
    int triggerX, triggerY, triggerWidth, triggerHeight;
    int colsSprite = 4;
    int speed = (int)MainGame.valorRelativo(15);
    int limiteDerecho;

    public BrodyEnemy(int x, int y, Rectangle hitbox, int xHitboxOffset, int yHitboxOffset, MainGame game, BufferedImage fullImage) {
        super(x, y, hitbox, xHitboxOffset, yHitboxOffset, game, fullImage);
        setSubImages(colsSprite);
        setTrigger();
        this.hitbox = new Rectangle(0, 0);

        limiteDerecho = ((int) (TILE_COLS * Math.ceil(((double) (x / TILE_SIZE) / TILE_COLS))));
    }

    public void setTrigger() {
        triggerX = settearXInMap(76);
        triggerY = settearYInMap(9);
        triggerWidth = TILE_SIZE;
        triggerHeight = TILE_SIZE * 11;
    }

    public void draw(Graphics g) {
        if (!endEvent && isTriggerActivado()) {
            g.drawImage(enemyAnim[index], x - (game.playingState.lm.offsetMapX * TILE_SIZE), y - (game.playingState.lm.offsetMapY * TILE_SIZE), (int)MainGame.valorRelativo(enemyAnim[0].getWidth()), (int)MainGame.valorRelativo(enemyAnim[0].getHeight()), null);
        }

        //drawHitbox(g);

        /*
        // draw trigger:
        g.setColor(Color.RED);
        g.drawRect(triggerX - (game.playingState.lm.offsetMapX * TILE_SIZE), triggerY - (game.playingState.lm.offsetMapY * TILE_SIZE), triggerWidth, triggerHeight);
         */
    }

    public void update() {
        if (!game.playingState.getPlayer().heRespawneado && !game.playingState.getPlayer().heMuerto) {
            updateHitbox();
        }

        if (!endEvent) {
            updateAnimation();
            if (isTriggerActivado()) {
                updatePos();
            }
        }
    }

    public void updateAnimation() {
        if (!isTriggerActivado()) {
            touchTrigger();
        } else {
            tick++;
            if (tick >= 22) {
                tick = 0;
                index++;
                if (index > enemyAnim.length - 1) {
                    index = 0;
                }
            }
        }
    }

    public void updatePos() {
        if (((this.x + (enemyAnim[0].getWidth())) / TILE_SIZE) >= limiteDerecho - 30) {
            this.x -= speed;
        } else {
            endEvent = true;
            triggerActivado = false;
        }
    }

    public void touchTrigger() {
        boolean triggeringInX = ((game.playingState.getPlayer().hitbox.x >= triggerX) && (game.playingState.getPlayer().hitbox.x <= triggerX + triggerWidth));
        boolean triggeringInY = ((game.playingState.getPlayer().hitbox.y >= triggerY) && (game.playingState.getPlayer().hitbox.y <= triggerY + triggerHeight));
        if (triggeringInX && triggeringInY) {
            setTriggerActivado(true);
            this.hitbox = this.copyHitbox;
        }
    }

}
