package utilz;

import entities.Enemy;
import java.awt.Color;
import java.awt.Graphics;
import game.MainGame;
import static game.MainGame.TILE_SIZE;

public class EnemyDraws {

    public static void drawSerlliber(Enemy enemigo, Graphics g, MainGame game) {
        // importante para que esten guardadas en enemyAnim:
        if (enemigo.getEnemySpeed() > 0) {
            g.drawImage(enemigo.getEnemyAnim()[0], enemigo.x - (game.playingState.lm.offsetMapX * TILE_SIZE), enemigo.y - (game.playingState.lm.offsetMapY * TILE_SIZE), TILE_SIZE, TILE_SIZE, null);
        } else {
            g.drawImage(enemigo.getEnemyAnim()[1], enemigo.x - (game.playingState.lm.offsetMapX * TILE_SIZE), enemigo.y - (game.playingState.lm.offsetMapY * TILE_SIZE), TILE_SIZE, TILE_SIZE, null);
        }
    }
    
    public static void drawButton(Enemy enemigo, Graphics g, MainGame game){
        enemigo.setSubImages(2);
        
        if(!enemigo.isTriggerActivado()){
            g.drawImage(enemigo.getEnemyAnim()[0], enemigo.x - (game.playingState.lm.offsetMapX * TILE_SIZE), enemigo.y - (game.playingState.lm.offsetMapY * TILE_SIZE), TILE_SIZE, TILE_SIZE, null);
        }else{
            g.drawImage(enemigo.getEnemyAnim()[1], enemigo.x - (game.playingState.lm.offsetMapX * TILE_SIZE), enemigo.y - (game.playingState.lm.offsetMapY * TILE_SIZE), TILE_SIZE, TILE_SIZE, null);
        }
    }
    
    public static void drawStaticSprites(Enemy enemigo, Graphics g, MainGame game){
        g.drawImage(enemigo.getFullImage(), enemigo.x - (game.playingState.lm.offsetMapX * TILE_SIZE), enemigo.y - (game.playingState.lm.offsetMapY * TILE_SIZE), TILE_SIZE, TILE_SIZE, null);
        
        //g.setColor(Color.red);
        //g.drawRect((int)enemigo.getHitbox().getX() - (game.playingState.lm.offsetMapX * TILE_SIZE), (int)enemigo.getHitbox().getY() - (game.playingState.lm.offsetMapX * TILE_SIZE), (int)enemigo.getHitbox().getWidth(),(int) enemigo.getHitbox().getHeight());
    }
}
