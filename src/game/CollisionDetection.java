package game;

import entities.Enemy;
import entities.Player;
import entities.Projectile;
import java.awt.Rectangle;
import java.util.List;
import levels.DamageTile;
import levels.LevelManager;
import levels.Tile;
import utilz.Constants;
import static game.MainGame.MAP_HEIGHT;
import static game.MainGame.MAP_WIDTH;
import static game.MainGame.TILE_SIZE;

public class CollisionDetection {
    // Van a haber dos metodos -> uno para detectar si es colisionable o no
    //                         -> otro para determinar, si es colisionable, que no lo atreviese:

    public static boolean esColisionable(LevelManager lm, float xHitbox, float yHitbox) {

        // NO SE PUEDE SOBREPASAR LOS LIMITES DEL MAPA:
        if (xHitbox < 0 || xHitbox >= MAP_WIDTH) {
            return true;
        }

        if (yHitbox < 0 || yHitbox >= MAP_HEIGHT) {
            return true;
        }

        int nextTileIndex = lm.getCurrentLevel().getLvlData()[(int)(yHitbox / TILE_SIZE)][(int)(xHitbox / TILE_SIZE)].getIndex();

        // NO SE PUEDE SOBREPASAR NINGÚN TILE CON UN INDEX DIFERENTE A ESTOS:
        if (nextTileIndex < Constants.LevelConstants.INICIO_NO_COLISION_LVL1) {
            return true;
        }

        return false;
    }

    public static boolean mePuedeMatar(LevelManager lm, Rectangle hitboxPlayer, int xHitbox, int yHitbox) {
        Tile nextTile = lm.getCurrentLevel().getLvlData()[yHitbox / TILE_SIZE][xHitbox / TILE_SIZE];

        if (nextTile.getIndex() > Constants.LevelConstants.FINAL_NO_COLISION_LVL1) {
            DamageTile nextDamageTile = (DamageTile) nextTile;
            if (nextDamageTile.getHitbox().intersects(hitboxPlayer)) {
                return true;
            }
        }

        return false;
    }

    public static boolean mePuedeMatarEnemigo(Rectangle hitboxPlayer, List<Enemy> enemigos) {
        for (int i = 0; i < enemigos.size(); i++) {
            if (enemigos.get(i).hitbox.intersects(hitboxPlayer)) {
                return true;
            }
        }
        return false;
    }

    public static boolean estoyTocandoAlgo(Rectangle hitboxPlayer, Rectangle hitboxObjeto) {
        if (hitboxPlayer.intersects(hitboxObjeto)) {
            return true;
        }

        return false;
    }

    public static int posicionYAjustadaAlSuelo(float airSpeed, int y, int height, Player player) {
        // EL 30 ES UN NUMERO MAGICO:
        int magicNumber = (int)MainGame.valorRelativo(30);
        int currentTile = (y + magicNumber) / TILE_SIZE;

        if (airSpeed > 0) {
            int tileYPos = currentTile * TILE_SIZE;
            int offset = TILE_SIZE - height;
            return tileYPos + offset - 1;
        } else {
            return (currentTile * TILE_SIZE);
        }
    }

    public static int posicionEnXAjustada(int xSpeed, Rectangle hitbox, int xHitbox) {

        int currentTile = (xHitbox) / TILE_SIZE;

        if (xSpeed > 0) {
            int tileXPos = currentTile * TILE_SIZE;
            int offset = TILE_SIZE - hitbox.width;
            return tileXPos + offset - 1;
        } else {
            return (currentTile * TILE_SIZE);
        }
    }

    public static boolean heMuertoStatic(LevelManager lm, int xHitbox, int yHitbox, int widthHitbox, int heightHitbox, Rectangle hitboxPlayer) {
        if (mePuedeMatar(lm, hitboxPlayer, xHitbox, yHitbox)
                || mePuedeMatar(lm, hitboxPlayer, xHitbox + widthHitbox, yHitbox)
                || mePuedeMatar(lm, hitboxPlayer, xHitbox, yHitbox + heightHitbox)
                || mePuedeMatar(lm, hitboxPlayer, xHitbox + widthHitbox, yHitbox + heightHitbox)) {
            return true;
        }

        //if (mePuedeMatarEnemigo(hitboxPlayer, enemigos) || mePuedeMatarEnemigo(hitboxPlayer, balasPooling)) {
        //    return true;
        //}
        //Projectile[] balasPooling
        return false;
    }

    public static boolean mePuedoMover(LevelManager lm, float xHitbox, float yHitbox, float widthHitbox, float heightHitbox) {

        //                     xHitbox AND yHitbox <-- +-----------------------+ --> xHitbox+widthHitbox AND yHitbox
        //                                             |                       |
        //                                             |                       |
        //                                             |                       |
        //                                             |                       |
        //                                             |                       |
        //                                             |                       |
        //                                             |                       |
        //                                             |                       |
        //        xHitbox AND yHitbox+heightHitbox <-- +-----------------------+ --> xHitbox+widthHitbox AND yHitbox+heightHitbox
        // Si el tile de delante ES COLISIONABLE, no puede avanzar hacia esa dirección (por eso retornamos false):   
        if (esColisionable(lm, xHitbox, yHitbox)
                || esColisionable(lm, xHitbox + widthHitbox, yHitbox)
                || esColisionable(lm, xHitbox, yHitbox + heightHitbox)
                || esColisionable(lm, xHitbox + widthHitbox, yHitbox + heightHitbox)) {
            return false;
        }

        return true;
    }
}
