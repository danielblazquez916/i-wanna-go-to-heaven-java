package entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import levels.Tile;
import utilz.Constants;
import utilz.LoadSave;
import game.MainGame;
import static game.MainGame.TILE_SIZE;

public class Box {

    public int xPos, yPos;
    int xInitPos, yInitPos;
    public boolean iCantGoBeyondX;

    public float xSpeedBox = MainGame.valorRelativo(5);
    float initialFallSpeedBox = MainGame.valorRelativo(2);
    float fallSpeedBox = initialFallSpeedBox;
    float incrementalSpeedFall = MainGame.valorRelativo(1);
    boolean isFalling;
    BufferedImage boxSprite;
    MainGame game;

    public Box(int xPos, int yPos, MainGame game, BufferedImage boxSprite) {
        this.game = game;
        this.xPos = xPos;
        this.yPos = yPos;
        setInitPos();

        this.boxSprite = boxSprite;
    }

    public void returnToInitialPos() {
        xPos = xInitPos;
        yPos = yInitPos;
    }

    public void setInitPos() {
        xInitPos = xPos;
        yInitPos = yPos;
    }

    public void update() {

        Player player = game.playingState.getPlayer();
        int value = playerIsTouchingBox();

        // en el caso de que la caja se tenga que caer:
        boolean isFallingBox = fallingBox();

        // SI EL VALOR ES 3 SIGNIFICA QUE ESTOY ENCIMA:
        if (value == 3) {

            // para que el player pueda estar encima sin que se buguee drasticamente
            // (sinceramente es más un parche que otra cosa, pero sinceramente esto de las
            //  colisiones es bastante chungo a veces):
            if (player.airSpeed >= 0) {
                isFalling = true;
            }

            if (isFalling) {
                player.airSpeed = 0;
                player.inAir = false;
                player.jumpingFromGround = true;

                if (!isFallingBox) {
                    player.hitbox.y = yPos - player.hitbox.height - 1;
                }

                isFalling = false;
            }
            /////////////////////////////////////////////////////////////////////////////
        }

        // SI EL VALOR ES 1 O 2 SIGNIFICA QUE VOY POR LOS LADOS:
        if (value == 1 || value == 2) {

            if (value == 1) {

                int TileRightIndex = game.playingState.lm.getCurrentLevel().getLvlData()[yPos / TILE_SIZE][((xPos + TILE_SIZE) + (int)MainGame.valorRelativo(10)) / TILE_SIZE].getIndex();
                int xPosRightTile = game.playingState.lm.getCurrentLevel().getLvlData()[yPos / TILE_SIZE][((xPos + TILE_SIZE) + (int)MainGame.valorRelativo(10)) / TILE_SIZE].getxPos();

                stopBoxMovingAndPlayer(TileRightIndex, xPosRightTile, player, value);

                /*if(value == 1){
                xPos = (xPosTile-TILE_SIZE)-1;
                    }else{
                xPos = xPosTile+1;
                    }*/
                if (player.playerSpeed <= 0) {
                    if (!iCantGoBeyondX) {
                        xPos = (xPosRightTile-TILE_SIZE)-1;
                        iCantGoBeyondX = true;
                    }
                }

            } else {
                int indexTileLeft = game.playingState.lm.getCurrentLevel().getLvlData()[yPos / TILE_SIZE][((xPos) - (int)MainGame.valorRelativo(10)) / TILE_SIZE].getIndex();
                int xPosLeftTile = game.playingState.lm.getCurrentLevel().getLvlData()[yPos / TILE_SIZE][((xPos) - (int)MainGame.valorRelativo(10)) / TILE_SIZE].getxPos();

                stopBoxMovingAndPlayer(indexTileLeft, xPosLeftTile, player, value);
                
                if (player.playerSpeed <= 0) {
                    if (!iCantGoBeyondX) {
                        xPos = (xPosLeftTile + TILE_SIZE)+1;
                        iCantGoBeyondX = true;
                    }
                }
            }
        }
    }

    public boolean fallingBox() {
        int tileIndexDownLeft = game.playingState.lm.getCurrentLevel().getLvlData()[(yPos + TILE_SIZE) / TILE_SIZE][(xPos + 2) / TILE_SIZE].getIndex();
        int tileIndexDownRight = game.playingState.lm.getCurrentLevel().getLvlData()[(yPos + TILE_SIZE) / TILE_SIZE][(xPos + TILE_SIZE - 2) / TILE_SIZE].getIndex();
        boolean isFallingBox = false;

        if (tileIndexDownLeft >= Constants.LevelConstants.INICIO_NO_COLISION_LVL1
                && tileIndexDownRight >= Constants.LevelConstants.INICIO_NO_COLISION_LVL1) {
            isFallingBox = true;
            fallSpeedBox += incrementalSpeedFall;
            yPos += fallSpeedBox;

        } else {
            Tile currentTile = game.playingState.lm.getCurrentLevel().getLvlData()[(yPos) / TILE_SIZE][(xPos) / TILE_SIZE];
            yPos = currentTile.getyPos();
            fallSpeedBox = initialFallSpeedBox;
        }

        return isFallingBox;
    }

    public void stopBoxMovingAndPlayer(int indexTile, int xPosTile, Player player, int value) {

        if (indexTile >= Constants.LevelConstants.INICIO_NO_COLISION_LVL1) {
            game.playingState.getPlayer().playerSpeed = (int)Math.abs(xSpeedBox);
            iCantGoBeyondX = false;

            if (value == 1) {
                xPos += xSpeedBox;
            } else {
                xPos -= xSpeedBox;
            }

        } else {
            player.playerSpeed = 0;
        }
    }

    public int playerIsTouchingBox() {
        Player player = game.playingState.getPlayer();
        player.playerSpeed = player.playerSpeedSave;
        String direction = player.direction;

        // COLISIÓN EN EL EJE Y (PORQUE ESTOY ENCIMA DE LA CAJA):
        if (((player.hitbox.x >= xPos && player.hitbox.x <= xPos + TILE_SIZE) || (player.hitbox.x + player.hitbox.width >= xPos && player.hitbox.x + player.hitbox.width <= xPos + TILE_SIZE))
                && (player.hitbox.y + player.hitbox.height <= yPos && (player.hitbox.y + player.hitbox.height) + (player.hitbox.height / 2) >= yPos)) {
            return 3;
        }

        // EN EL CASO DE QUE LA COLISIÓN SEA EN EL EJE X --> izquierda y derecha (pero también involucra
        // al EJE Y ya que hay que poner los límites en ambas):
        if (direction.equals("right")) {
            if ((player.hitbox.x + player.hitbox.width >= xPos && player.hitbox.x + player.hitbox.width < xPos + MainGame.valorRelativo(15))
                    && ((player.hitbox.y >= yPos && player.hitbox.y <= (yPos + TILE_SIZE))
                    || (player.hitbox.y + player.hitbox.height <= (yPos + TILE_SIZE) && player.hitbox.y + player.hitbox.height >= yPos))) {
                // lado derecho
                return 1;
            }
        } else {
            if ((player.hitbox.x <= (xPos + TILE_SIZE) && player.hitbox.x > (xPos + TILE_SIZE - MainGame.valorRelativo(10)))
                    && ((player.hitbox.y >= yPos && player.hitbox.y <= (yPos + TILE_SIZE))
                    || (player.hitbox.y + player.hitbox.height <= (yPos + TILE_SIZE) && player.hitbox.y + player.hitbox.height >= yPos))) {
                // lado izquierdo
                return 2;
            }
        }

        return 0;
    }

    public void draw(Graphics g) {
        g.drawImage(boxSprite, xPos - (game.playingState.lm.offsetMapX * TILE_SIZE), yPos - (game.playingState.lm.offsetMapY * TILE_SIZE), TILE_SIZE, TILE_SIZE, null);
    }

    public void setBoxSprite(String route) {
        boxSprite = LoadSave.importImg(route);
    }

    public BufferedImage getBoxSprite() {
        return boxSprite;
    }
}
