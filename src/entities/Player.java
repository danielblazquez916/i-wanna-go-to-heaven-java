package entities;

import gamestates.Playing;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import levels.Checkpoint;
import static utilz.Constants.PlayerConstants.*;
import utilz.LoadSave;
import game.CollisionDetection;
import game.MainGame;
import static game.MainGame.TILE_HEIGHT;
import static game.MainGame.TILE_SIZE;
import static game.MainGame.TILE_WIDTH;

public class Player extends Entity {

    BufferedImage playerImg;
    MainGame game;
    Playing ps;
    public BufferedImage[][] animations;
    public BufferedImage[] deathAnimation;
    public BufferedImage glowPlayer;

    int animTick, animIndex, animSpeed = 5;
    int deathAnimIndex;
    public boolean notContinue;
    public static boolean desactivarPlayer;

    public int playerState = IDLE;
    public String direction = "right";

    public boolean isMoving = false;
    public boolean isLeft, isRight, isJumping;
    public int playerSpeed = (int)MainGame.valorRelativo(7);
    public int playerSpeedSave = playerSpeed;
    int xSpeed = 0;

    public boolean inAir = true;
    public boolean cannotJumpMore, jumpingFromGround;
    public int numberOfJumpsOnAir = 1, numberOfJumpsOnAirSave = 1;
    public int jumpSpeed = (int)MainGame.valorRelativo(8);
    public float airSpeed = 0;
    public float gravity = MainGame.valorRelativo(1.0f);
    

    public boolean heMuerto = false;
    public boolean heRespawneado = false;
    public boolean muertoPorProjectil;

    public int respawnX = 0;
    public int respawnY = 0;

    public int xPosMapReference, yPosMapReference;
    int positionXOffset, positionYOffset;

    // PARA SER INMORTAL:
    public static boolean serInmortalDev = false;
    public static boolean serInmortalGame = false;

    public String PLAYER_SCHEMA;

    // ojo hay que tener en cuenta que x e y es la posición de la que parte el sprite -> la esquina superior izquierda:
    public Player(int x, int y, MainGame game, Playing ps) {
        super(x, y);
        this.game = game;
        this.ps = ps;
        PLAYER_SCHEMA = this.game.pp.skinPlayer;

        // Hitbox para el jugador:
        hitbox = new Rectangle(this.x, this.y, TILE_SIZE - (int)MainGame.valorRelativo(23), TILE_SIZE - (int)MainGame.valorRelativo(10));
        xPosMapReference = hitbox.x;
        yPosMapReference = hitbox.y;

        // Para poder usar las imagenes:
        setSkin();

        //updateRespawn();
        respawnX = ps.lm.getCurrentLevel().getCheckpoints()[game.pp.indexCP].getxPos();
        respawnY = ps.lm.getCurrentLevel().getCheckpoints()[game.pp.indexCP].getyPos();

        // punto inicial al empezar el juego:
        setRespawnPoint(respawnX, respawnY);

        this.hitbox.x = respawnX;
        this.hitbox.y = respawnY;

        // Importar animaciones en el array:
        loadAnimations(8, 6);
        loadDeathAnimation(9);
    }

    public void setSkin() {
        playerImg = LoadSave.importImg(PLAYER_SCHEMA);
    }

    public void setRespawnPoint(int xPos, int yPos) {
        respawnX = xPos;
        respawnY = yPos;
    }

    public String getRespawnPoint() {
        return respawnX + ";" + respawnY;
    }

    public void updatePlayer() {

        if (heRespawneado) {
            // METER CADA UNO POR SECCION:
            // resetear la posicion del npc...
            backToRespawn();
            setPlayerXOffsetForMap();
            setPlayerYOffsetForMap();

            if (game.playingState.lm.getCurrentLevel().getSection() != 2) {
                serInmortalGame = false;

                game.playingState.restartPositionNpcs();
                game.playingState.restartPositionsEnemies();
                game.playingState.getCaja().returnToInitialPos();
                game.playingState.getBoss().restart();
                game.playingState.getBoss().resetBGandTransition();
                game.playingState.getBoss().endScene = false;
                game.playingState.restartEndScene();
                game.playingState.teleport.resetTeleporter();
            }

            direction = "right";
            setIsLeft(false);
            setIsRight(false);

            // ya que solo hay cañones en la sección dos:
            if (game.playingState.lm.getCurrentLevel().getSection() == 2) {
                game.playingState.resetarCañones();
            }

            deathAnimIndex = 0;
            heRespawneado = false;
            muertoPorProjectil = false;
        }

        updateAnimation();
        setAnimation();
        setPlayerXOffsetForMap();
        setPlayerYOffsetForMap();

        updatePos();

        updateRespawn();

        die();

    }

    public void die() {
        //// SER INMORTAL ////

        if (serInmortalDev || serInmortalGame) {
            return;
        }
        //////////////////////

        boolean died = CollisionDetection.heMuertoStatic(game.playingState.lm, hitbox.x, hitbox.y, hitbox.width, hitbox.height, hitbox)
                || CollisionDetection.mePuedeMatarEnemigo(this.hitbox, game.playingState.getEnemigos())
                || muertoPorProjectil
                || (game.playingState.lm.getCurrentLevel().getSection() == 5 && (game.playingState.getBoss().intersection(this)
                || game.playingState.getBoss().hitboxCoche.intersects(hitbox)));

        if (died) {
            game.effects[2].setMicrosecondPosition(0);
            heMuerto = true;
        }
    }

    public void setAnimationNotContinue() {
        animTick++;
        if (animTick >= animSpeed) {
            animTick = 0;
            if (deathAnimIndex >= 8) {
                notContinue = false;
                return;
            }
            deathAnimIndex++;
        }
    }

    public void backToRespawn() {
        hitbox.x = Integer.parseInt(getRespawnPoint().split(";")[0]);
        hitbox.y = Integer.parseInt(getRespawnPoint().split(";")[1]);
    }

    public void updateRespawn() {
        // CREAR EN EL FUTURO UN METODO EN LEVELMANAGER QUE ENCAPSULE TODOS LOS CHECKPOINTS
        // EXISTENTES PARA PONERLOS A FALSE CADA VEZ QUE SE SELECCIONE UNO:
        // quiza en vez de el metodo se puede detectar cual ha sido el anterior y quitarlo.
        Checkpoint[] hitboxesCp = ps.lm.getCurrentLevel().getCheckpoints();

        for (int i = 0; i < hitboxesCp.length; i++) {
            if (CollisionDetection.estoyTocandoAlgo(hitbox, hitboxesCp[i].getHitbox())
                    && hitboxesCp[i].getIndex() != 1) {

                game.playingState.lm.ponerImagenDefaultACheckpointAnterior();

                respawnX = hitboxesCp[i].getxPos();
                respawnY = hitboxesCp[i].getyPos();

                setRespawnPoint(respawnX, respawnY);

                game.pp.indexCP = i;
                //hitboxesCp[i].setTocado(true);

                // cambio el indice a 1 para mostrar la imagen del checkpoint agarrado:
                //game.playingState.lm.setImageCpDefaultAll(hitboxesCp);
                hitboxesCp[i].setIndex(1);
                break;
            }
        }
    }

    public void setAnimation() {

        int startAnimation = playerState;

        // Si se mueve, ponemos la animacion de running, si esta quieto, usamos la de idle:
        if (isMoving && !inAir) {
            if (direction.equals("right")) {
                playerState = RUNNING;
            } else if (direction.equals("left")) {
                playerState = RUNNING_REVERSED;
            }
        } else {
            if (direction.equals("right")) {
                playerState = IDLE;
            } else if (direction.equals("left")) {
                playerState = IDLE_REVERSED;
            }
        }

        // animaciones de cuando cae y cuando salta:
        if (inAir) {
            if (airSpeed > 0) {
                if (direction.equals("right")) {
                    playerState = FALLING;
                } else if (direction.equals("left")) {
                    playerState = FALLING_REVERSED;
                }
            } else {
                if (direction.equals("right")) {
                    playerState = JUMPING;
                } else if (direction.equals("left")) {
                    playerState = JUMPING_REVERSED;
                }
            }
        }

        if (startAnimation != playerState) {
            animIndex = 0;
            animTick = 0;
        }
    }

    public void drawPlayer(Graphics g) {

        if (!heMuerto) {
            g.drawImage(animations[playerState][animIndex], this.hitbox.x - (game.playingState.lm.offsetMapX * TILE_SIZE) - (int)MainGame.valorRelativo(12), this.hitbox.y - (game.playingState.lm.offsetMapY * TILE_SIZE) - (int)MainGame.valorRelativo(10), TILE_SIZE, TILE_SIZE, null);
        } else {
            g.drawImage(deathAnimation[deathAnimIndex], this.hitbox.x - (game.playingState.lm.offsetMapX * TILE_SIZE) - (int)MainGame.valorRelativo(12), this.hitbox.y - (game.playingState.lm.offsetMapY * TILE_SIZE) - (int)MainGame.valorRelativo(10), TILE_SIZE, TILE_SIZE, null);
        }

        //g.setColor(Color.red);
        //g.drawRect(this.hitbox.x - (game.playingState.lm.offsetMapX * TILE_SIZE), this.hitbox.y- (game.playingState.lm.offsetMapY * TILE_SIZE), this.hitbox.width, this.hitbox.height);
    }

    public void loadDeathAnimation(int maxCols) {
        deathAnimation = new BufferedImage[maxCols];
        BufferedImage allImage = LoadSave.importImg(LoadSave.DEATH_ANIM_SCHEMA);

        for (int i = 0; i < deathAnimation.length; i++) {
            deathAnimation[i] = allImage.getSubimage(i * 32, 0, 32, 32);
        }
    }

    public void loadAnimations(int maxPNGrows, int maxPNGcols) {

        animations = new BufferedImage[maxPNGrows][maxPNGcols];

        for (int i = 0; i < animations.length; i++) {
            for (int j = 0; j < animations[i].length; j++) {
                animations[i][j] = playerImg.getSubimage(j * 32, i * 32, 32, 32);
            }
        }
    }

    public void setMoving(boolean moving) {
        isMoving = moving;
    }

    public void updateAnimation() {

        animTick++;
        if (animTick >= animSpeed) {
            animTick = 0;
            animIndex++;
            if (animIndex >= getSpriteFrames(playerState)) {
                animIndex = 0;
            }
        }
    }

    public void saltar() {
        inAir = true;
        airSpeed = -jumpSpeed;
    }

    public void setPlayerXOffsetForMap() {

        // EN X: //////////////////////////////////
        if (xPosMapReference >= TILE_WIDTH) {

            game.playingState.lm.limitesSuperadosEnXPositivo = true;
            xPosMapReference -= TILE_WIDTH;
            positionXOffset += TILE_WIDTH;

            game.playingState.lm.getCurrentLevel().setSection(this);

            System.out.println(game.playingState.lm.getCurrentLevel().getSection());

            //System.out.println(game.playingState.lm.getCurrentLevel().getSection());
        } else if (xPosMapReference <= -1) {

            game.playingState.lm.limitesSuperadosEnXNegativo = true;
            xPosMapReference += TILE_WIDTH;
            positionXOffset -= TILE_WIDTH;

            game.playingState.lm.getCurrentLevel().setSection(this);

            System.out.println(game.playingState.lm.getCurrentLevel().getSection());
            //System.out.println(game.playingState.lm.getCurrentLevel().getSection());
        }
    }

    public void setPlayerYOffsetForMap() {

        // EN Y: //////////////////////////////////
        if (yPosMapReference >= TILE_HEIGHT) {

            game.playingState.lm.limitesSuperadosEnYNegativo = true;
            yPosMapReference -= TILE_HEIGHT;
            positionYOffset += TILE_HEIGHT;

            game.playingState.lm.getCurrentLevel().setSection(this);
            //System.out.println(game.playingState.lm.getCurrentLevel().getSection());

        } else if (yPosMapReference <= -1) {

            game.playingState.lm.limitesSuperadosEnYPositivo = true;
            yPosMapReference += TILE_HEIGHT;
            positionYOffset -= TILE_HEIGHT;

            game.playingState.lm.getCurrentLevel().setSection(this);
            //System.out.println(game.playingState.lm.getCurrentLevel().getSection());

        }
    }

    public void updatePos() {
        setMoving(false);
        xSpeed = 0;

        // Encontrar la posición relativa:
        xPosMapReference = (hitbox.x - positionXOffset);
        yPosMapReference = (hitbox.y - positionYOffset);

        if (!inAir) {
            cannotJumpMore = false;
            numberOfJumpsOnAir = numberOfJumpsOnAirSave;
        }

        // Saltar:
        if (isJumping && !cannotJumpMore) {

            saltar();
            game.playingState.counterPressed++;
            if (game.playingState.counterPressed >= 10) {
                isJumping = false;
                if (!jumpingFromGround) {
                    if (numberOfJumpsOnAir <= 1) {
                        cannotJumpMore = true;
                    }
                }
            }
        }

        // asignar la dirección:
        if (!isIsRight() && isIsLeft()) {
            xSpeed = -playerSpeed;
            direction = "left";
            setMoving(true);
        } else if (isIsRight() && !isIsLeft()) {
            xSpeed = playerSpeed;
            direction = "right";
            setMoving(true);
        }

        // Responder a la gravedad según las colisiones:
        if (inAir) {

            // No puedes mezclar en el if este tanto el movimiento en "x" como el de "y".
            // Esto es porque en el else estas solo controlando "y", y por tanto, si se
            // activa la colisión por "x", estarías ajustando el offset de algo que no toca ya
            // que no estaría tocando superficie plana o techo.
            if (CollisionDetection.mePuedoMover(game.playingState.lm, hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height)) {
                
                hitbox.y += airSpeed;
                airSpeed += gravity;

                if (CollisionDetection.mePuedoMover(game.playingState.lm, hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height)) {
                    hitbox.x += xSpeed;
                } else {
                    hitbox.x = CollisionDetection.posicionEnXAjustada(xSpeed, hitbox, hitbox.x);
                }

            } else {
                // aqui ajustas el offset DE "Y":
                hitbox.y = CollisionDetection.posicionYAjustadaAlSuelo(airSpeed, hitbox.y, hitbox.height, this);

                if (airSpeed > 0) {
                    inAir = false;
                    airSpeed = 0;

                } else {
                    airSpeed = 0;
                    isJumping = false;
                }
            }

        } else {
            if (CollisionDetection.mePuedoMover(game.playingState.lm, hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height)) {
                hitbox.x += xSpeed;
            } else {
                hitbox.x = CollisionDetection.posicionEnXAjustada(xSpeed, hitbox, hitbox.x);
            }

            // Detectamos si al avanzar en una superficie, ya no hay más suelo, y por tanto, tenemos que
            // poner inAir a true para indicar que se tiene que caer.
            if (!CollisionDetection.esColisionable(game.playingState.lm, hitbox.x, (hitbox.y + hitbox.height) + 1)
                    && !CollisionDetection.esColisionable(game.playingState.lm, hitbox.x + hitbox.width, (hitbox.y + hitbox.height) + 1)) {
                inAir = true;
            }

            // OTRA FORMA DE HACERLO, SIMPLEMENTE APROVECHO EL OTRO METODO Y DETECTO LOS TILES DE ABAJO SUMANDO 1 Y POR TANTO
            // ENTRANDO EN EL AREA DEL TILE DE ABAJO:
            //  if(CollisionDetection.mePuedoMover(game.lm.getCurrentLevel().getLvlOneData(), hitbox.x, hitbox.y+1, hitbox.width, hitbox.height)){
            //      inAir = true;
            //  }
        }
    }

    // GETTERS Y SETTERS: ////////////////
    public boolean isIsLeft() {
        return isLeft;
    }

    public void setIsLeft(boolean isLeft) {
        this.isLeft = isLeft;
    }

    public boolean isIsRight() {
        return isRight;
    }

    public void setIsRight(boolean isRight) {
        this.isRight = isRight;
    }

    //////////////////////////////////////
}
