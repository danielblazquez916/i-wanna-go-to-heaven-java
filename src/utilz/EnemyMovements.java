package utilz;

import entities.Box;
import entities.Enemy;
import entities.Player;
import game.MainGame;
import static game.MainGame.TILE_COLS;
import static game.MainGame.TILE_ROWS;
import static game.MainGame.TILE_SIZE;

public class EnemyMovements {

    // move types:
    public static final String UP_MOVE = "up";
    public static final String DOWN_MOVE = "down";
    public static final String LEFT_RIGHT_REPEAT = "left_right_repeat";
    public static final String LEFT = "left";
    public static final String RIGHT = "right";

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // going left OR right:
    public static void goingLeftOrRightWhenTrigger(Enemy enemigo, MainGame game, int triggerX, int triggerY, int triggerWidth, int triggerHeight, boolean limits, boolean invisibleWhenStops, String goingLeftOrRight) {

        Player player = game.playingState.getPlayer();
        triggeringArea(enemigo, player, triggerX, triggerY, triggerWidth, triggerHeight);

        if (enemigo.isTriggerActivado()) {
            if (goingLeftOrRight.equals(LEFT)) {
                if (!limits) {
                    int limiteIzquierdo = ((int) (TILE_COLS * Math.floor(((double) (enemigo.x / TILE_SIZE) / TILE_COLS))));

                    if (enemigo.x >= limiteIzquierdo * TILE_SIZE) {
                        enemigo.x -= enemigo.getEnemySpeed();
                    } else {
                        if (invisibleWhenStops) {
                            enemigo.setInvisible(true);
                        }
                    }
                } else {
                    int tileIndex = game.playingState.lm.getCurrentLevel().getLvlData()[enemigo.y / TILE_SIZE][(enemigo.x - enemigo.getEnemySpeed()) / TILE_SIZE].getIndex();

                    if (tileIndex >= Constants.LevelConstants.INICIO_NO_COLISION_LVL1) {
                        enemigo.x -= enemigo.getEnemySpeed();
                    } else {
                        int current = ((enemigo.x - enemigo.getEnemySpeed()) / TILE_SIZE);
                        int xPosTile = current * TILE_SIZE;

                        enemigo.x = xPosTile + TILE_SIZE;
                        enemigo.setTriggerActivado(false);

                        if (invisibleWhenStops) {
                            enemigo.setInvisible(true);
                        }
                    }
                }
            } else if (goingLeftOrRight.equals(RIGHT)) {
                if (!limits) {
                    int limiteDerecho = ((int) (TILE_COLS * Math.ceil(((double) (enemigo.x / TILE_SIZE) / TILE_COLS))));

                    if (enemigo.x <= limiteDerecho * TILE_SIZE) {
                        enemigo.x += enemigo.getEnemySpeed();
                    } else {
                        if (invisibleWhenStops) {
                            enemigo.setInvisible(true);
                        }
                    }
                }
            }
        }
    }

    // going left right repeat:
    public static void goingBackAndForth(Enemy enemigo, MainGame game) {
        enemigo.x += enemigo.getEnemySpeed();

        if (enemigo.getEnemySpeed() > 0) {
            int index = game.playingState.lm.getCurrentLevel().getLvlData()[enemigo.y / TILE_SIZE][(enemigo.x + TILE_SIZE) / TILE_SIZE].getIndex();
            if (index < Constants.LevelConstants.INICIO_NO_COLISION_LVL1) {
                enemigo.setEnemySpeed(-enemigo.getEnemySpeed());
            }
        } else {
            int index = game.playingState.lm.getCurrentLevel().getLvlData()[enemigo.y / TILE_SIZE][(int) Math.ceil((enemigo.x - 1) / TILE_SIZE)].getIndex();
            if (index < Constants.LevelConstants.INICIO_NO_COLISION_LVL1) {
                enemigo.setEnemySpeed(Math.abs(enemigo.getEnemySpeed()));
            }
        }
    }

    // going down:
    public static void goingDownWhenTrigger(Enemy enemigo, MainGame game, int triggerX, int triggerY, int triggerWidth, int triggerHeight, boolean limits, boolean invisibleWhenStops) {

        Player player = game.playingState.getPlayer();
        triggeringArea(enemigo, player, triggerX, triggerY, triggerWidth, triggerHeight);

        if (enemigo.isTriggerActivado()) {

            if (limits) {
                int tileIndex = game.playingState.lm.getCurrentLevel().getLvlData()[(enemigo.y + TILE_SIZE) / TILE_SIZE][enemigo.x / TILE_SIZE].getIndex();
                int yPos = game.playingState.lm.getCurrentLevel().getLvlData()[(enemigo.y + TILE_SIZE) / TILE_SIZE][enemigo.x / TILE_SIZE].getyPos();

                if (tileIndex >= Constants.LevelConstants.INICIO_NO_COLISION_LVL1) {
                    enemigo.y += enemigo.getEnemySpeed();
                } else {
                    enemigo.setTriggerActivado(false);
                    enemigo.y = yPos - TILE_SIZE;

                    if (invisibleWhenStops) {
                        enemigo.setInvisible(true);
                    }
                }

            } else {

                int limiteAbajo = ((int) (TILE_ROWS * Math.ceil(((double) (enemigo.y / TILE_SIZE) / TILE_ROWS))));

                if (enemigo.y <= limiteAbajo * TILE_SIZE) {
                    enemigo.y += enemigo.getEnemySpeed();
                } else {
                    enemigo.setTriggerActivado(false);

                    if (invisibleWhenStops) {
                        enemigo.setInvisible(true);
                    }
                }
            }
        }
    }

    // going up:
    public static void goingUpWhenTrigger(Enemy enemigo, MainGame game, int triggerX, int triggerY, int triggerWidth, int triggerHeight, boolean limits, boolean invisibleWhenStops) {
        int index = game.playingState.lm.getCurrentLevel().getLvlData()[(enemigo.y) / TILE_SIZE][(enemigo.x) / TILE_SIZE].getIndex();
        Player player = game.playingState.getPlayer();
        triggeringArea(enemigo, player, triggerX, triggerY, triggerWidth, triggerHeight);

        if (enemigo.isTriggerActivado()) {
            if (limits) {
                if (index >= Constants.LevelConstants.INICIO_NO_COLISION_LVL1) {
                    enemigo.y -= enemigo.getEnemySpeed();
                } else {
                    enemigo.setTriggerActivado(false);

                    if (invisibleWhenStops) {
                        enemigo.setInvisible(true);
                    }
                }
            } else {
                int limiteArriba = ((int) (TILE_ROWS * Math.floor(((double) (enemigo.y / TILE_SIZE) / TILE_ROWS))));

                if (enemigo.y >= limiteArriba * TILE_SIZE) {
                    enemigo.y -= enemigo.getEnemySpeed();
                } else {
                    if (invisibleWhenStops) {
                        enemigo.setInvisible(true);
                    }

                    enemigo.setTriggerActivado(false);
                }
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void boxIntersection(Enemy button, Box caja, MainGame game, Enemy[] blocksToDestroy) {
        // COORDENADA X:
        if (caja.xPos > button.hitbox.x && caja.xPos < (button.hitbox.x + button.hitbox.width)
                || (caja.xPos + TILE_SIZE) > button.hitbox.x && (caja.xPos + TILE_SIZE) < (button.hitbox.x + button.hitbox.width)) {

            // COORDENADA Y:
            if ((caja.yPos + TILE_SIZE) >= button.hitbox.y && (caja.yPos + TILE_SIZE) <= (button.hitbox.y + button.hitbox.height)) {

                if (!button.isTriggerActivado()) {
                    button.setTriggerActivado(true);
                    brokeBlocksWhenTrigger(button, game, blocksToDestroy);
                }

                return;
            }
        }

        if (button.isTriggerActivado()) {
            button.setTriggerActivado(false);
            for (int i = 0; i < blocksToDestroy.length; i++) {
                blocksToDestroy[i].setFullImage(game.playingState.lm.getSpriteArray()[Constants.LevelConstants.STONE_BLOCK]);
                blocksToDestroy[i].setTriggerActivado(false);
            }
        }
    }

    public static void brokeBlocksWhenTrigger(Enemy thingWithTrigger, MainGame game, Enemy[] blocksToDestroy) {
        if (thingWithTrigger.isTriggerActivado()) {
            // ponemos bloques a 2 (índice vacío):
            for (int i = 0; i < blocksToDestroy.length; i++) {
                blocksToDestroy[i].setFullImage(game.playingState.lm.getSpriteArray()[Constants.LevelConstants.VOID_BLOCK]);
                blocksToDestroy[i].setTriggerActivado(true);
            }
        }

        System.out.println("sustituido!");
    }

    public static boolean colisionConPuertaHaciaAdelante(MainGame game, Enemy block) {
        Player player = game.playingState.getPlayer();
        String direction = player.direction;
        boolean chocando = false;

        // EN EL CASO DE QUE LA COLISIÓN SEA EN EL EJE X --> izquierda y derecha (pero también involucra
        // al EJE Y ya que hay que poner los límites en ambas):
        if (direction.equals("right")) {
            if ((player.hitbox.x + player.hitbox.width >= block.hitbox.x - 6 && player.hitbox.x + player.hitbox.width < block.hitbox.x + 15)
                    && ((player.hitbox.y >= block.hitbox.y && player.hitbox.y <= (block.hitbox.y + TILE_SIZE))
                    || (player.hitbox.y + player.hitbox.height <= (block.hitbox.y + TILE_SIZE) && player.hitbox.y + player.hitbox.height >= block.hitbox.y))) {
                // lado derecho
                chocando = true;
            }
        } else {
            if ((player.hitbox.x <= (block.hitbox.x + TILE_SIZE + 6) && player.hitbox.x > (block.hitbox.x + TILE_SIZE) - 15)
                    && ((player.hitbox.y >= block.hitbox.y && player.hitbox.y <= (block.hitbox.y + TILE_SIZE))
                    || (player.hitbox.y + player.hitbox.height <= (block.hitbox.y + TILE_SIZE) && player.hitbox.y + player.hitbox.height >= block.hitbox.y))) {
                // lado izquierdo
                chocando = true;
            }
        }

        return chocando;
    }

    public static void goingUpAndDown(Enemy enemigo, MainGame game) {

        int index = 0;

        if (enemigo.getEnemySpeed() > 0) {
            index = game.playingState.lm.getCurrentLevel().getLvlData()[(enemigo.y + TILE_SIZE) / TILE_SIZE][(enemigo.x) / TILE_SIZE].getIndex();
        } else {
            index = game.playingState.lm.getCurrentLevel().getLvlData()[(enemigo.y) / TILE_SIZE][(enemigo.x) / TILE_SIZE].getIndex();
        }

        if (index >= Constants.LevelConstants.INICIO_NO_COLISION_LVL1) {
            enemigo.y += enemigo.getEnemySpeed();
        } else {
            enemigo.setEnemySpeed(-enemigo.getEnemySpeed());
        }

    }

    public static void triggeringArea(Enemy enemigo, Player player, int triggerX, int triggerY, int triggerWidth, int triggerHeight) {
        // este metodo es para automatizar a la hora de detectar
        // si el player pasa por un area en concreto:

        boolean triggeringInX = ((player.hitbox.x >= triggerX) && (player.hitbox.x <= triggerX + triggerWidth));
        boolean triggeringInY = ((player.hitbox.y >= triggerY) && (player.hitbox.y <= triggerY + triggerHeight));
        if (triggeringInX && triggeringInY) {
            enemigo.setTriggerActivado(true);
        }
    }
}
