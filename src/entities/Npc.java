package entities;

import dialogues.DialogueSystem;
import gamestates.Playing;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import utilz.LoadSave;
import game.MainGame;
import static game.MainGame.TILE_HEIGHT;
import static game.MainGame.TILE_SIZE;
import static game.MainGame.TILE_WIDTH;

public class Npc {

    BufferedImage[][] skinAnim;
    BufferedImage etiquetaNPC;
    int rows, cols;
    String movementType;

    int indexRow, indexCol, tick, animSpeed = 5;
    boolean isMoving = true;
    BufferedImage fullImage;

    DialogueSystem dialogo;
    public static boolean enDialogo;
    public static boolean intersection;

    int xPos, yPos;
    int xPosInicial, yPosInicial;
    int xPosMaxDistance, xPosMinDistance;
    Rectangle hitbox;
    int width, height;

    // Para la etiqueta:
    Font pressE;

    int speed = (int)MainGame.valorRelativo(5);

    Playing playingState;
    String npcEspecifico = "";

    public Npc(BufferedImage fullImage, int rowsAnim, int colsAnim, int xPos, int yPos, int width, int height, Playing playingState, DialogueSystem dialogo) {
        this.fullImage = fullImage;
        this.cols = colsAnim;
        this.rows = rowsAnim;
        this.dialogo = dialogo;
        this.xPos = xPos;
        this.yPos = yPos;
        settearPosicionInicial();
        this.width = width;
        this.height = height;

        this.playingState = playingState;

        // Hitbox:
        hitbox = new Rectangle(this.xPos, this.yPos, this.width, this.height);

        xPosMaxDistance = this.playingState.settearX(136);
        xPosMinDistance = this.playingState.settearX(124);

        etiquetaNPC = LoadSave.importImg(LoadSave.NPC_ETIQUETA_SCHEMA);
        pressE = new Font(Font.DIALOG_INPUT, Font.BOLD, (int)MainGame.valorRelativo(20));

        setSkinAnimation(rows, cols);
    }

    public String isNpcEspecifico() {
        return npcEspecifico;
    }

    public void setNpcEspecifico(String npcEspecifico) {
        this.npcEspecifico = npcEspecifico;
    }

    public void setFullImage(BufferedImage fullImage) {
        this.fullImage = fullImage;
    }

    public void setAnimSpeed(int animSpeed) {
        this.animSpeed = animSpeed;
    }

    public void setMovementType(String movementType) {
        this.movementType = movementType;
    }

    public void settearPosicionInicial() {
        xPosInicial = this.xPos;
        yPosInicial = this.yPos;
    }

    public void updateHitbox() {
        hitbox.x = xPos;
        hitbox.y = yPos;
    }

    // Dividir en subimagenes la FullImage:
    public void setSkinAnimation(int rows, int cols) {
        skinAnim = new BufferedImage[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                skinAnim[i][j] = fullImage.getSubimage(j * (fullImage.getWidth() / cols), i * (fullImage.getHeight() / rows), (fullImage.getWidth() / cols), (fullImage.getHeight() / rows));
            }
        }
    }

    public static void keyPressed(KeyEvent e) {
        if (!enDialogo) {
            if (e.getKeyCode() == KeyEvent.VK_E && !Playing.startEndScreen) {
                if (intersection) {
                    enDialogo = true;
                }
            }
        } else {
            DialogueSystem.keyPressed(e);
        }
    }

    // draw:
    public void draw(Graphics g) {
        drawEtiquetaNPC(g);

        if (Npc.enDialogo) {
            g.setColor(new Color(0, 0, 0, 160));
            g.fillRect(0, 0, TILE_WIDTH, TILE_HEIGHT);
        }

        if (npcEspecifico.isBlank()) {
            g.drawImage(skinAnim[indexRow][indexCol], xPos - (playingState.lm.offsetMapX * TILE_SIZE), yPos - (playingState.lm.offsetMapY * TILE_SIZE), width, height, null);
        } else {
            if (npcEspecifico.equals("Joker")) {
                drawJoker(g);
            }
        }

        //drawHitboxNPC(g);
        if (enDialogo) {
            playingState.getPlayer().drawPlayer(g);
            dialogo.draw(g);
        }
    }

    public void drawJoker(Graphics g) {
        if (Npc.enDialogo) {
            if (playingState.getPlayer().hitbox.x >= xPos && indexCol != 0) {
                indexCol = 0;
            } else if (playingState.getPlayer().hitbox.x < xPos && indexCol != 1) {
                indexCol = 1;
            }
        } else {
            if (indexCol != 0) {
                indexCol = 0;
            }
        }

        g.drawImage(skinAnim[indexRow][indexCol], xPos - (playingState.lm.offsetMapX * TILE_SIZE), yPos - (playingState.lm.offsetMapY * TILE_SIZE), width, height, null);
    }

    public void drawEtiquetaNPC(Graphics g) {

        // etiqueta para saber que no te mata y eso:
        int widthEtiqueta = (int)MainGame.valorRelativo(75);
        int heightEtiqueta = (int)MainGame.valorRelativo(40);
        int xPosEtiqueta = (hitbox.x + (hitbox.width / 2) - (widthEtiqueta / 2)) - (playingState.lm.offsetMapX * TILE_SIZE);
        int yPosEtiqueta = (yPos - (int)MainGame.valorRelativo(50)) - (playingState.lm.offsetMapY * TILE_SIZE);

        g.drawImage(etiquetaNPC, xPosEtiqueta, yPosEtiqueta, widthEtiqueta, heightEtiqueta, null);

        /*
        g.setColor(Color.BLACK);
        

        g.fillRect(xPosRect, yPosRect, 40, 20);

        // String que pone "NPC":
        g.setFont(font);
        g.setColor(Color.WHITE);
        int xPosString = (hitbox.x + 5) - (playingState.lm.offsetMapX * TILE_SIZE);
        int yPosString = (hitbox.y - 8) - (playingState.lm.offsetMapY * TILE_SIZE);

        g.drawString("NPC", xPosString, yPosString);
         */
        // Para saber que puedo hablar con él:
        if (Npc.intersection && !Npc.enDialogo) {
            g.setFont(pressE);
            g.setColor(new Color(0, 0, 0, 120));
            g.drawString("Presiona la tecla", xPosEtiqueta - (int)MainGame.valorRelativo(60), yPosEtiqueta - (int)MainGame.valorRelativo(25));
            g.drawString("para interactuar.", xPosEtiqueta - (int)MainGame.valorRelativo(60), yPosEtiqueta - (int)MainGame.valorRelativo(10));

            g.setColor(new Color(0, 255, 0, 120));
            g.drawString("[E]", xPosEtiqueta + (int)MainGame.valorRelativo(155), yPosEtiqueta - (int)MainGame.valorRelativo(25));
        }
        
        //drawHitboxNPC(g);
    }

    public void drawHitboxNPC(Graphics g) {
        // dibujar hitbox:
        g.setColor(Color.red);
        g.drawRect(hitbox.x - (playingState.lm.offsetMapX * TILE_SIZE), hitbox.y - (playingState.lm.offsetMapY * TILE_SIZE), hitbox.width, hitbox.height);
    }

    // update:
    public void update() {
        if (movementType.equals("moving")) {
            updatePosition();
        }

        if (npcEspecifico.isBlank()) {
            updateAnimation();
        }

        updateHitbox();

        if (hitbox.intersects(playingState.getPlayer().hitbox)) {
            if (!intersection) {
                intersection = true;
            }

        } else {
            if (intersection) {
                intersection = false;
            }
        }

        // dialogos:
        if (enDialogo) {
            dialogo.update();
            sonidoJoker();
        }
        
        reiniciarSonidoJoker();
    }
    
    public void reiniciarSonidoJoker(){
        if(!enDialogo && playingState.game.effects[4].getFramePosition() >= playingState.game.effects[4].getFrameLength()){
            playingState.game.effects[4].setMicrosecondPosition(0);
        }
    }

    public void sonidoJoker() {
        if (npcEspecifico.equals("Joker")) {
            if (!playingState.game.effects[4].isRunning()) {
                playingState.game.effects[4].start();
            }
        }
    }

    public void updatePosition() {
        if (isMoving && !enDialogo) {
            xPos += speed;

            if (xPos >= xPosMaxDistance && speed > 0) {
                speed = -speed;
            }

            if (xPos <= xPosMinDistance && speed < 0) {
                speed = Math.abs(speed);
            }
        }
    }

    public void updateAnimation() {

        if (movementType.equals("moving")) {
            changeIndexRow(enDialogo, 0, 2);
            if (!enDialogo) {
                changeIndexRow(isMoving, 1, 3);
            }
        }

        tick++;
        if (tick >= animSpeed) {
            indexCol++;
            tick = 0;
            if (indexCol >= cols) {
                indexCol = 0;
            }
        }
    }

    public void changeIndexRow(boolean activador, int indexSpeedPositive, int indexSpeedNegative) {
        if (activador) {
            if (speed > 0) {
                indexRow = indexSpeedPositive;
            } else {
                indexRow = indexSpeedNegative;
            }
        }
    }

    public void resetPos() {
        this.xPos = xPosInicial;
        this.yPos = yPosInicial;
        speed = Math.abs(speed);
    }
}
