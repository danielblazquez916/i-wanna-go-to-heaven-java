package levels;

import entities.Npc;
import gamestates.Playing;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
//import utilz.CheckpointLoader;
import utilz.LoadSave;
import utilz.PlayerPrefs;
import game.MainGame;
import static game.MainGame.TILE_COLS;
import static game.MainGame.TILE_HEIGHT;
import static game.MainGame.TILE_ROWS;
import static game.MainGame.TILE_SIZE;
import static game.MainGame.TILE_WIDTH;

public class LevelManager {

    BufferedImage[] spriteArray;

    // Posibles backgrounds:
    BufferedImage BGFirstBlock;
    public BufferedImage BGBoss;
    BufferedImage finalBG;
    BufferedImage BGSecondBlock;
    BufferedImage subBGFinal;
    

    BufferedImage entireImage;
    public int offsetMapX = 0, offsetMapY;
    public boolean limitesSuperadosEnXPositivo, limitesSuperadosEnXNegativo;
    public boolean limitesSuperadosEnYPositivo, limitesSuperadosEnYNegativo;

    Level levelOne;
    Playing playingState;

    public LevelManager(Playing playingState) {
        this.playingState = playingState;

        entireImage = LoadSave.importImg(LoadSave.LEVEL_ONE_SCHEMA);

        BGFirstBlock = LoadSave.importImg(LoadSave.LEVEL_ONE_BG);
        BGBoss = LoadSave.importImg(LoadSave.LEVEL_ONE_BOSS_BG);
        BGSecondBlock = LoadSave.importImg(LoadSave.LEVEL_ONE_BG_SP);
        finalBG = LoadSave.importImg(LoadSave.FINAL_BG);
        subBGFinal = LoadSave.importImg(LoadSave.SUBFINAL_BG);

        loadSprites(1, 19, 19);

        initializeLevels();
    }

    
    public void ponerImagenDefaultACheckpointAnterior() {
        getCurrentLevel().getCheckpoints()[playingState.game.pp.indexCP].setIndex(0);
    }

    public BufferedImage[] getSpriteArray() {
        return this.spriteArray;
    }

    public Checkpoint[] checkpointsOnLevelOne() {
        Checkpoint cp1 = new Checkpoint(TILE_SIZE * 2, TILE_SIZE * 17);
        Checkpoint cp2 = new Checkpoint(TILE_SIZE * 15, TILE_SIZE * 13);
        Checkpoint cp3 = new Checkpoint(TILE_SIZE * 26, TILE_SIZE * 6);
        Checkpoint cp4 = new Checkpoint(TILE_SIZE * 61, TILE_SIZE * 4);
        Checkpoint cp5 = new Checkpoint(TILE_SIZE * 81, TILE_SIZE * 16);
        Checkpoint cp6 = new Checkpoint(TILE_SIZE * 63, TILE_SIZE * 26);
        Checkpoint cp7 = new Checkpoint(playingState.settearX(68), playingState.settearY(22));

        return new Checkpoint[]{cp1, cp2, cp3, cp4, cp5, cp6, cp7};
    }

    public void initializeLevels() {
        levelOne = new Level(getLevelDataManager(LoadSave.LEVEL_ONE_DATA), checkpointsOnLevelOne());
    }

    public Object[] settingBackground() {
        BufferedImage bg = null;
        Color colorSubScene = null;

        if (getCurrentLevel().section >= 2 && getCurrentLevel().section < 4) {
            bg = BGSecondBlock;
            colorSubScene = new Color(1, 92, 98, 190);

        } else if (getCurrentLevel().section <= 1 || getCurrentLevel().section == 4) {
            bg = BGFirstBlock;
            colorSubScene = new Color(53, 27, 0, 190);
        } else if (getCurrentLevel().section == 5) {
            bg = BGBoss;
            colorSubScene = new Color(53, 27, 0, 190);
        }
        
        if(getCurrentLevel().section == 99){
            bg = finalBG;
            colorSubScene = new Color(1, 92, 98, 190);
        }

        return new Object[]{bg, colorSubScene};
    }

    // Dibujar todos los tiles del juego:
    public void drawTiles(Graphics g) {
        for (int i = 0; i < TILE_ROWS; i++) {
            for (int j = 0; j < TILE_COLS; j++) {
                if (getCurrentLevel().getLvlData()[i + offsetMapY][j + offsetMapX] instanceof DamageTile) {
                    DamageTile tile = (DamageTile) getCurrentLevel().getLvlData()[i + offsetMapY][j + offsetMapX];

                    g.drawImage(tile.getTileImg(), tile.getxPos() - (offsetMapX * TILE_SIZE), tile.getyPos() - (offsetMapY * TILE_SIZE), TILE_SIZE, TILE_SIZE, null);

                    // dibujar Hitbox del pincho o el obstaculo:
                    //g.setColor(Color.RED);
                    //g.drawRect(tile.getHitbox().x-(offsetMapX * TILE_SIZE), tile.getHitbox().y-(offsetMapY * TILE_SIZE), tile.getHitbox().width, tile.getHitbox().height);
                } else {
                    Tile tile = getCurrentLevel().getLvlData()[i + offsetMapY][j + offsetMapX];

                    g.drawImage(tile.getTileImg(), tile.getxPos() - (offsetMapX * TILE_SIZE), tile.getyPos() - (offsetMapY * TILE_SIZE), TILE_SIZE, TILE_SIZE, null);
                }
            }
        }
    }

    // Dibujar todos los checkpoints:
    public void drawCheckpoints(Graphics g) {
        // dibujar los checkpoints del nivel que se este corriendo:
        Checkpoint[] cps = getCurrentLevel().getCheckpoints();

        for (int i = 0; i < cps.length; i++) {
            Checkpoint cp = cps[i];
            int xPosReal = cp.getxPos() - (offsetMapX * TILE_SIZE);
            int yPosReal = cp.getyPos() - (offsetMapY * TILE_SIZE);

            g.drawImage(cp.getCheckpointImg()[cp.index], xPosReal, yPosReal, TILE_SIZE, TILE_SIZE, null);

            // para dibujar hitbox:
            //g.setColor(Color.RED);
            //g.drawRect(xPosReal, yPosReal, cp.getHitbox().width, cp.getHitbox().height);
        }
    }

    public void draw(Graphics g) {

        BufferedImage actualBg = null;

        // Background escena:
        actualBg = (BufferedImage) settingBackground()[0];

        g.drawImage(actualBg, 0, 0, TILE_WIDTH, TILE_HEIGHT, null);
        g.setColor((Color) settingBackground()[1]);
        g.fillRect(0, 0, TILE_WIDTH, TILE_HEIGHT);
        
        if(getCurrentLevel().getSection() == 99){
            g.drawImage(subBGFinal, 0, 0, TILE_WIDTH, TILE_HEIGHT, null);
            playingState.drawBlackSubScreen(g);
            g.drawImage(playingState.lastJump, playingState.settearX(20) - (offsetMapX*TILE_SIZE), playingState.settearY(53) - (offsetMapY*TILE_SIZE), TILE_SIZE*3, TILE_SIZE*3, null);
        }

        // Dibujar todos los tiles del juego:
        drawTiles(g);

        // Dibujar todos los checkpoints:
        drawCheckpoints(g);

        if (getCurrentLevel().getSection() == 4) {

            Graphics2D g2d = (Graphics2D) g.create();

            // Dibuja el fondo negro
            g2d.setColor(new Color(0, 0, 0, 245));
            g2d.fillRect(0, 0, TILE_WIDTH, TILE_HEIGHT);

            float ovalPosX = playingState.getPlayer().hitbox.x - (playingState.lm.offsetMapX * TILE_SIZE) - MainGame.valorRelativo(12);
            float ovalPosY = playingState.getPlayer().hitbox.y - (playingState.lm.offsetMapY * TILE_SIZE) - MainGame.valorRelativo(10);

            Shape circle = new Ellipse2D.Double(ovalPosX - (TILE_SIZE), ovalPosY - (TILE_SIZE), TILE_SIZE + MainGame.valorRelativo(100), TILE_SIZE + MainGame.valorRelativo(100));

            Shape originalClip = g2d.getClip();

            g2d.setClip(circle);

            ///////// LO QUE SE ESTA DIBUJANDO DENTRO DEL CIRCULO /////////
            g2d.drawImage(actualBg, 0, 0, TILE_WIDTH, TILE_HEIGHT, null);
            g2d.setColor((Color) settingBackground()[1]);
            g2d.fillRect(0, 0, TILE_WIDTH, TILE_HEIGHT);
            drawTiles(g2d);
            drawCheckpoints(g2d);
            g2d.setColor(new Color(255, 165, 0, 100));
            g2d.fillRect(0, 0, TILE_WIDTH, TILE_HEIGHT);
            ////////////////////////////////////////////////////////////////

            g2d.setClip(originalClip);
        }
    }

    // ojittoooooo
    public void update() {
        // Ajustar en la coordenada "x" para que se muestre
        // la siguiente parte del mapa (cuando el jugador cruza un limite 
        // ---> TILE_WIDTH):

        if (limitesSuperadosEnXPositivo) {

            offsetMapX += TILE_COLS;
            limitesSuperadosEnXPositivo = false;

        } else if (limitesSuperadosEnXNegativo) {

            offsetMapX -= TILE_COLS;
            limitesSuperadosEnXNegativo = false;
        }

        // Ajustar en la coordenada "y" para que se muestre
        // la siguiente parte del mapa (cuando el jugador cruza un limite 
        // ---> TILE_HEIGHT):
        if (limitesSuperadosEnYPositivo) {

            offsetMapY -= TILE_ROWS;
            limitesSuperadosEnYPositivo = false;

        } else if (limitesSuperadosEnYNegativo) {

            offsetMapY += TILE_ROWS;
            limitesSuperadosEnYNegativo = false;
        }
    }

    // importamos nuestro primer spritesheet de tiles (terrain_sprites):
    public void loadSprites(int filas, int cols, int numeroDeSprites) {
        spriteArray = new BufferedImage[numeroDeSprites];

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < cols; j++) {
                int index = i * cols + j;
                spriteArray[index] = entireImage.getSubimage(j * 32, i * 32, 32, 32);
            }
        }
    }

    // Lo haremos con el metodo del TXT:
    public Tile[][] getLevelDataManager(String dataRoute) {
        Tile[][] levelArray = LoadSave.importMapForTXT(dataRoute, this);
        return levelArray;
    }

    // ES MUY IMPORTANTE:
    public Level getCurrentLevel() {
        return levelOne;
    }
}
