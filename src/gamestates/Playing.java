package gamestates;

import dialogues.AllDialogues;
import dialogues.DialogueSystem;
import entities.Boss;
import entities.Box;
import entities.BrodyEnemy;
import entities.Cannon;
import entities.Enemy;
import entities.Npc;
import entities.Player;
import entities.Teleporter;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.sound.sampled.Clip;
import levels.LevelManager;
import transitions.FadeInFadeOut;
import utilz.Constants;
//import utilz.CheckpointLoader;
import utilz.EnemyDraws;
import utilz.EnemyMovements;
import utilz.LoadSave;
import utilz.PlayerPrefs;
import game.MainGame;
import static game.MainGame.TILE_HEIGHT;
import static game.MainGame.TILE_SIZE;
import static game.MainGame.TILE_WIDTH;

public class Playing implements statemethods {

    Player player;

    List<Enemy> enemigos;
    Npc[] npcs;
    Cannon[] cañones;
    Boss boss;
    public Teleporter teleport;

    Box caja;
    Enemy button;
    Enemy[] blocksToDestroy;
    public Clip main_song;

    BufferedImage subBG;
    public BufferedImage lastJump;

    Font fontMessageEscape;

    public int counterPressed;
    boolean escapePressed;

    public LevelManager lm;

    BufferedImage deathScreenBG;
    float opacity;

    BufferedImage tutorial_screen_1;
    BufferedImage tutorial_screen_2;

    boolean isPressed;
    public MainGame game;

    public int dialogueCounting;
    static public boolean startEndScreen;
    public BufferedImage NPC_3_OTHER = LoadSave.importImg(LoadSave.NPC_3_SCHEMA_TALK);
    public BufferedImage NPC_3_ORIG = LoadSave.importImg(LoadSave.NPC_3_SCHEMA);
    public int opacityBibleMention;
    public int tickEndScene;

    public Playing(MainGame game) {
        this.game = game;
        lm = new LevelManager(this);
        player = new Player(0, 0, this.game, this);

        // seccion del mapa donde se encuentra el player:
        lm.getCurrentLevel().setSection(player);

        subBG = LoadSave.importImg(LoadSave.NEGRO);
        lastJump = LoadSave.importImg(LoadSave.LAST_JUMP);

        fontMessageEscape = new Font(Font.DIALOG, Font.BOLD, (int)MainGame.valorRelativo(20));

        instanciarPuerta();
        spawnearEnemigos();
        instanciarBoton();
        instanciarCaja();
        instanciarCañones();
        instanciarBoss();
        instanciarTeleporter();
        instanciarNPCs();

        deathScreenBG = LoadSave.importImg(LoadSave.DEATH_SCREEN_BG);
        tutorial_screen_1 = LoadSave.importImg(LoadSave.TUTORIAL_1);
        tutorial_screen_2 = LoadSave.importImg(LoadSave.TUTORIAL_2);
    }

    public void restartEndScene() {
        dialogueCounting = 0;
        startEndScreen = false;
        opacityBibleMention = 0;
        tickEndScene = 0;
    }

    public void instanciarNPCs() {
        npcs = new Npc[4];

        DialogueSystem dialogoNpc1 = new DialogueSystem(AllDialogues.helloKittyDialogue(), 200, "down");
        npcs[0] = new Npc(LoadSave.importImg(LoadSave.NPC_1_SCHEMA), 4, 3, settearX(128), settearY(28), TILE_SIZE, TILE_SIZE, this, dialogoNpc1);
        npcs[0].setMovementType("moving");

        DialogueSystem dialogoNpc2 = new DialogueSystem(AllDialogues.blackManDialogue(), 200, "down");
        npcs[1] = new Npc(LoadSave.importImg(LoadSave.NPC_2_SCHEMA), 1, 9, settearX(40), settearY(14), TILE_SIZE, TILE_SIZE, this, dialogoNpc2);
        npcs[1].setMovementType("idle");
        npcs[1].setAnimSpeed(4);

        DialogueSystem dialogoNpc3 = new DialogueSystem(AllDialogues.nullDialogue(), 210, "up");
        npcs[2] = new Npc(LoadSave.importImg(LoadSave.NPC_3_SCHEMA), 1, 1, settearX(48), settearY(58), TILE_SIZE * 2, TILE_SIZE * 2, this, dialogoNpc3);
        npcs[2].setMovementType("idle");

        DialogueSystem dialogoNpc4 = new DialogueSystem(AllDialogues.jokerDialogue(), 200, "down");
        npcs[3] = new Npc(LoadSave.importImg(LoadSave.JOKER_NPC_SCHEMA), 1, 2, settearX(96), settearY(6), TILE_SIZE, TILE_SIZE, this, dialogoNpc4);
        npcs[3].setMovementType("idle");
        npcs[3].setNpcEspecifico("Joker");
    }

    public Boss getBoss() {
        return boss;
    }

    public int settearX(int colx) {
        return ((colx / 2) - 1) * TILE_SIZE;
    }

    public int settearY(int rowy) {
        return (rowy - 1) * TILE_SIZE;
    }

    public void instanciarTeleporter() {
        Rectangle hitboxTel = new Rectangle((int)MainGame.valorRelativo(20), (int)MainGame.valorRelativo(20));
        teleport = new Teleporter(LoadSave.importImg(LoadSave.TELEPORT_SCHEMA), TILE_SIZE * 22, TILE_SIZE * 37, settearX(6), settearY(59), (int)MainGame.valorRelativo(100), (int)MainGame.valorRelativo(100), hitboxTel, game);
    }

    public void instanciarBoss() {
        Rectangle hitboxBoss = new Rectangle(0, 0, TILE_SIZE * 3, TILE_SIZE * 4);
        boss = new Boss(TILE_SIZE * 12, TILE_SIZE * 12, LoadSave.importImg(LoadSave.BOSS_SCHEMA), hitboxBoss, game, this);
    }

    public void instanciarCañones() {
        cañones = new Cannon[2];

        Rectangle hitboxCannon1 = new Rectangle(TILE_SIZE, TILE_SIZE);
        cañones[0] = new Cannon(TILE_SIZE * 88, TILE_SIZE * 3, hitboxCannon1, 0, 0, game, LoadSave.importImg(LoadSave.CANION_SCHEMA), 0);

        Rectangle hitboxCannon2 = new Rectangle(TILE_SIZE, TILE_SIZE);
        cañones[1] = new Cannon(TILE_SIZE * 61, TILE_SIZE * 9, hitboxCannon2, 0, 0, game, LoadSave.importImg(LoadSave.CANION_REVERSED_SCHEMA), 1);
    }

    public Cannon[] getCañones() {
        return cañones;
    }

    public void instanciarPuerta() {
        blocksToDestroy = new Enemy[2];

        blocksToDestroy[0] = new Enemy(TILE_SIZE * 59, TILE_SIZE * 4, new Rectangle(0, 0, TILE_SIZE, TILE_SIZE), 0, 0, game, lm.getSpriteArray()[Constants.LevelConstants.STONE_BLOCK]);
        blocksToDestroy[1] = new Enemy(TILE_SIZE * 59, TILE_SIZE * 5, new Rectangle(0, 0, TILE_SIZE, TILE_SIZE), 0, 0, game, lm.getSpriteArray()[Constants.LevelConstants.STONE_BLOCK]);
    }

    public Player getPlayer() {
        return player;
    }

    public void instanciarBoton() {

        Rectangle hitboxButton = new Rectangle(TILE_SIZE, TILE_SIZE);
        button = new Enemy(TILE_SIZE * 50, TILE_SIZE * 18, hitboxButton, 0, 0, game, LoadSave.importImg(LoadSave.BUTTON_SCHEMA));
    }

    public void instanciarCaja() {
        caja = new Box(TILE_SIZE * 39, TILE_SIZE * 6, game, LoadSave.importImg(LoadSave.BOX_SCHEMA));
    }

    public Box getCaja() {
        return caja;
    }

    //////////////////////////////////// ENEMIGOS: ////////////////////////////////////
    public void instanciarEnemigosSeccion0() {
        // Enemigo 1, seccion 0 -> serlliber:
        generarObstaculo(16, 14, LoadSave.importImg(LoadSave.SERLLIBER_ENEMY_SCHEMA));
        enemigos.get(0).setEnemySpeed((int)MainGame.valorRelativo(16));
        enemigos.get(0).setSubImages(2);

        // Enemigo 2, seccion 0 -> pincho:
        generarObstaculo(2, 4, lm.getSpriteArray()[Constants.LevelConstants.NORMAL_SPIKE_DOWN]);

        // Enemigo 3, seccion 0 -> pincho:
        generarObstaculo(34, 16, lm.getSpriteArray()[Constants.LevelConstants.NORMAL_SPIKE_DOWN]);

        // Enemigo 4, seccion 0 -> pincho:
        generarObstaculo(30, 17, lm.getSpriteArray()[Constants.LevelConstants.NORMAL_SPIKE_UP]);

        // Enemigo 5, seccion 0 -> pincho:
        generarObstaculo(40, 20, lm.getSpriteArray()[Constants.LevelConstants.NORMAL_SPIKE_UP]);
        enemigos.get(4).setEnemySpeed((int)MainGame.valorRelativo(20));

        // Enemigos 6,7 y 8, seccion 0 -> pinchos:
        generarObstaculosProximos(3, 50, 20, false, lm.getSpriteArray()[Constants.LevelConstants.NORMAL_SPIKE_UP]);

        // Enemigos 9 y 10, seccion 0 -> mas pinchos:
        generarObstaculosProximos(2, 58, 13, true, lm.getSpriteArray()[Constants.LevelConstants.NORMAL_SPIKE_LEFT]);

        // Enemigo 11, seccion 1 -> un pinchin:
        generarObstaculosProximos(4, 68, 15, true, lm.getSpriteArray()[Constants.LevelConstants.NORMAL_SPIKE_RIGHT]);
    }

    public void generarObstaculo(int xPos, int yPos, BufferedImage image) {
        Rectangle hitboxEnemigo = new Rectangle(TILE_SIZE/2, TILE_SIZE/2);
        enemigos.add(new Enemy(settearX(xPos), settearY(yPos), hitboxEnemigo, (int)MainGame.valorRelativo(11), (int)MainGame.valorRelativo(18), this.game, image));
    }

    public void resetarCañones() {
        for (int i = 0; i < getCañones().length; i++) {
            Cannon cañon = getCañones()[i];

            // resetear todos los disparos:
            cañon.resetBalls();

            // resetear las posiciones de los cañones:
            cañon.x = cañon.initX;
            cañon.y = cañon.initY;

            // resetear la direccion:
            cañon.setEnemySpeed(Math.abs(cañon.getEnemySpeed()));
        }
    }

    // para generar pinchos que se colocan sucesivamente
    // es decir: ->^^^->...
    public void generarObstaculosProximos(int cantidadDePinchos, int xPos, int yPos, boolean coordenadaY, BufferedImage image) {
        int countX = 0;
        int countY = 0;

        for (int i = 0; i < cantidadDePinchos; i++) {

            Rectangle hitboxEnemigos = new Rectangle(TILE_SIZE/2, TILE_SIZE/2);
            enemigos.add(new Enemy(settearX(xPos + countX), settearY(yPos + countY), hitboxEnemigos, (int)MainGame.valorRelativo(11), (int)MainGame.valorRelativo(18), this.game, image));

            if (coordenadaY) {
                countY += 1;
            } else {
                countX += 2;
            }
        }
    }

    public void instanciarEnemigosSeccion1() {
        // Enemigo 1, seccion 1 -> brody:
        enemigos.add(new BrodyEnemy(settearX(120), settearY(4), new Rectangle(TILE_SIZE * 3, TILE_SIZE * 15), (int)MainGame.valorRelativo(300), (int)MainGame.valorRelativo(160), this.game, LoadSave.importImg(LoadSave.BRODYQUEST_SCHEMA)));
    }

    public void instanciarEnemigosSeccion2() {
        // Enemigo 1, seccion 2 -> pincho:
        //Rectangle hitboxEnemigo1 = new Rectangle(TILE_SIZE - 20, TILE_SIZE - 20);
        //enemigos.add(new Enemy(TILE_SIZE * 65, TILE_SIZE * 2, hitboxEnemigo1, 10, 13, this.game, lm.getSpriteArray()[Constants.LevelConstants.NORMAL_SPIKE_DOWN]));
    }

    public void instanciarEnemigosSeccion3() {
        // ninguno de momento.
    }

    public void instanciarEnemigosSeccion4() {
        // Enemigo 1, seccion 4 //

    }

    public void spawnearEnemigos() {

        // La diferencia entre los DamageTiles y los Enemies
        // es que unos son fijos y los otros no.
        enemigos = new ArrayList<>();

        instanciarEnemigosSeccion0();
        instanciarEnemigosSeccion1();
        instanciarEnemigosSeccion2();
        instanciarEnemigosSeccion3();
        instanciarEnemigosSeccion4();
    }

    public void restartPositionsEnemies() {
        for (int i = 0; i < enemigos.size(); i++) {
            enemigos.get(i).resetToInitialPosition();
            //enemigos.get(i).setEnemySpeed(enemigos.get(i).getEnemySpeed());
            enemigos.get(i).setTriggerActivado(false);
            enemigos.get(i).endEvent = false;
            enemigos.get(i).setInvisible(false);
        }
    }

    public void restartPositionNpcs() {
        for (int i = 0; i < npcs.length; i++) {
            npcs[i].resetPos();
            if (i == 2) {
                npcs[i].setFullImage(NPC_3_ORIG);
                npcs[i].setSkinAnimation(1, 1);
            }
        }
    }

    public List<Enemy> getEnemigos() {
        return enemigos;
    }

    // hacer que solo funcionen los updates cuando este viendo el percal:
    @Override
    public void update() {

        // SI HACE MUCHO:
        lm.update();
        ///////////////

        /*
        if(Gamestate.currentState.equals(Gamestate.LEVEL_PLAYING_STATE)
                && !main_song.isRunning()){
            main_song.setMicrosecondPosition(0);
            main_song.start();
        }*/
        /// SECCIONES ///
        updateSection99();

        updateSection5();

        updateSection4();

        updateSection3();

        updateSection2();

        updateSection1();

        updateSection0();
        /////////////////

        if (!player.heMuerto) {
            if (opacity != 0) {
                opacity = 0;
            }
            if (!teleport.teleportDisappears) {
                player.updatePlayer();
            }

        } else {
            if (opacity < 1f - 0.03f) {
                opacity += 0.03f;
            } else {
                opacity = 1.0f;
            }
        }

        if (!player.notContinue && player.heMuerto) {
            player.setAnimationNotContinue();
            Npc.enDialogo = false;
            game.effects[2].start();
        }

        if (escapePressed) {
            game.fadeTransition.setFinishTransition(false);
            game.fadeTransition.setState(Gamestate.MENU_STATE);
            escapePressed = false;
        }
    }

    public void updateSection99() {
        if (lm.getCurrentLevel().getSection() == 99) {
            npcs[2].update();
            if (Npc.enDialogo && dialogueCounting < 1) {
                dialogueCounting++;
                npcs[2].setFullImage(NPC_3_OTHER);
                npcs[2].setSkinAnimation(1, 1);
            }

            if (!Npc.enDialogo && dialogueCounting == 1 && !startEndScreen) {
                startEndScreen = true;
                game.fadeTransition.setFinishTransition(false);
                Player.serInmortalGame = true;
            }

            game.fadeTransition.update();
            if (game.fadeTransition.isFinishTransition() && startEndScreen) {
                if (opacityBibleMention < 255) {
                    tickEndScene++;
                    if (tickEndScene >= 1) {
                        tickEndScene = 0;
                        opacityBibleMention++;
                    }
                } else {
                    if (!escapePressed) {
                        tickEndScene++;
                        if (tickEndScene >= 300) {
                            tickEndScene = 0;
                            escapePressed = true;
                            asignarInsignia();
                            game.pp.resetearPartida(game);
                        }
                    }
                }
            }
        }
    }

    public void asignarInsignia() {
        int muertes = game.pp.muertesTotales;
        if (muertes <= 0) {
            game.pp.tipoInsignia = 3;
        } else if (muertes <= 30) {
            game.pp.tipoInsignia = 2;
        } else {
            game.pp.tipoInsignia = 1;
        }
    }

    public void updateSection5() {
        if (lm.getCurrentLevel().getSection() == 5) {
            boss.update();
            if (boss.isEndScene()) {
                teleport.update();
            }
        }
    }

    //  TO DO
    public void updateSection4() {
        if (lm.getCurrentLevel().getSection() == 4) {
            //
        }
    }

    public void updateSection3() {
        if (lm.getCurrentLevel().getSection() == 3) {
            npcs[0].update();
        }
    }

    public void updateSection2() {
        if (lm.getCurrentLevel().getSection() == 2) {
            // CAÑONES:
            for (int i = 0; i < cañones.length; i++) {
                cañones[i].update();
            }

            // PINCHO QUE CAE HACIA ABAJO:
            //EnemyMovements.goingDownWhenTrigger(enemigos.get(3), game, (enemigos.get(3).x - 40), enemigos.get(3).y, (TILE_SIZE + 40), (TILE_SIZE * 2), true, false);
            //enemigos.get(3).updateHitbox();
            updateBlocksToDestroy();
        }
    }

    public void updateSection1() {
        if (lm.getCurrentLevel().getSection() == 1) {
            // CAJA: //
            caja.update();
            updateBlocksToDestroy();

            // primer, segundo y tercer enemigo:
            updateObstaculosProximos(new int[]{10, 11, 12, 13}, settearX(88), settearY(16), TILE_SIZE, TILE_SIZE * 2, EnemyMovements.RIGHT, false, false, 100);

            npcs[3].update();

            BrodyEnemy brodyquest = (BrodyEnemy) enemigos.get(14);
            brodyquest.update();
        }
    }

    public void updateSection0() {

        if (lm.getCurrentLevel().getSection() == 0) {

            // npc 2:
            npcs[1].update();

            //primer enemigo -> movimiento regular de izquierda a derecha y viceversa:
            updateObstaculo(enemigos.get(0), 0, 0, 0, 0, EnemyMovements.LEFT_RIGHT_REPEAT, true, false);

            // segundo y tercer enemigo -> movimiento hacia abajo al pasar por un lugar: //
            updateObstaculo(enemigos.get(1), enemigos.get(1).x, enemigos.get(1).y, TILE_SIZE, TILE_SIZE * 2, EnemyMovements.DOWN_MOVE, true, false);

            updateObstaculo(enemigos.get(2), (enemigos.get(2).x - (int)MainGame.valorRelativo(40)), enemigos.get(2).y, TILE_SIZE, TILE_SIZE * 2, EnemyMovements.DOWN_MOVE, true, false);

            // cuarto obstaculo -> movimiento hacia arriba al pasar por un lugar:
            updateObstaculo(enemigos.get(3), settearX(30) - (int)MainGame.valorRelativo(20), settearY(16), TILE_SIZE, TILE_SIZE * 2, EnemyMovements.UP_MOVE, true, false);

            // quinto obstaculo:
            updateObstaculo(enemigos.get(4), settearX(40) - (int)MainGame.valorRelativo(20), settearY(16), TILE_SIZE, TILE_SIZE * 2, EnemyMovements.UP_MOVE, false, true);

            // sexto, septimo y octavo:
            updateObstaculosProximos(new int[]{5, 6, 7}, settearX(50), settearY(15), TILE_SIZE * 2, TILE_SIZE * 2, EnemyMovements.UP_MOVE, false, true, (int)MainGame.valorRelativo(17));

            // noveno, decimo y undecimo:
            updateObstaculosProximos(new int[]{8, 9}, settearX(46), settearY(13), TILE_SIZE, TILE_SIZE * 2, EnemyMovements.LEFT, true, true, (int)MainGame.valorRelativo(17));
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void updateObstaculo(Enemy enemigo, int posInicialX, int posInicialY, int triggerWidth, int triggerHeight, String attackType, boolean limits, boolean invisibleWhenStops) {
        switch (attackType) {
            case EnemyMovements.UP_MOVE ->
                EnemyMovements.goingUpWhenTrigger(enemigo, game, posInicialX, posInicialY, triggerWidth, triggerHeight, limits, invisibleWhenStops);

            case EnemyMovements.DOWN_MOVE ->
                EnemyMovements.goingDownWhenTrigger(enemigo, game, posInicialX, posInicialY, triggerWidth, triggerHeight, limits, invisibleWhenStops);

            case EnemyMovements.LEFT_RIGHT_REPEAT ->
                EnemyMovements.goingBackAndForth(enemigo, game);

            case EnemyMovements.LEFT ->
                EnemyMovements.goingLeftOrRightWhenTrigger(enemigo, game, posInicialX, posInicialY, triggerWidth, triggerHeight, limits, invisibleWhenStops, EnemyMovements.LEFT);

            case EnemyMovements.RIGHT ->
                EnemyMovements.goingLeftOrRightWhenTrigger(enemigo, game, posInicialX, posInicialY, triggerWidth, triggerHeight, limits, invisibleWhenStops, EnemyMovements.RIGHT);
        }

        enemigo.updateHitbox();
    }

    // Tipos de ataques -> "up", "down", "left_right_repeat":
    public void updateObstaculosProximos(int[] indexes, int posInicialX, int posInicialY, int triggerWidth, int triggerHeight, String attackType, boolean limits, boolean invisibleWhenStops, int enemySpeed) {
        for (int i = 0; i < indexes.length; i++) {

            switch (attackType) {
                case EnemyMovements.UP_MOVE ->
                    EnemyMovements.goingUpWhenTrigger(enemigos.get(indexes[i]), game, posInicialX, posInicialY, triggerWidth, triggerHeight, limits, invisibleWhenStops);

                case EnemyMovements.DOWN_MOVE ->
                    EnemyMovements.goingDownWhenTrigger(enemigos.get(indexes[i]), game, posInicialX, posInicialY, triggerWidth, triggerHeight, limits, invisibleWhenStops);

                case EnemyMovements.LEFT_RIGHT_REPEAT ->
                    EnemyMovements.goingBackAndForth(enemigos.get(indexes[i]), game);

                case EnemyMovements.LEFT ->
                    EnemyMovements.goingLeftOrRightWhenTrigger(enemigos.get(indexes[i]), game, posInicialX, posInicialY, triggerWidth, triggerHeight, limits, invisibleWhenStops, EnemyMovements.LEFT);

                case EnemyMovements.RIGHT ->
                    EnemyMovements.goingLeftOrRightWhenTrigger(enemigos.get(indexes[i]), game, posInicialX, posInicialY, triggerWidth, triggerHeight, limits, invisibleWhenStops, EnemyMovements.RIGHT);
            }

            enemigos.get(indexes[i]).updateHitbox();
            enemigos.get(indexes[i]).setEnemySpeed(enemySpeed);
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void updateBlocksToDestroy() {

        // BOTON: //
        EnemyMovements.boxIntersection(button, caja, game, blocksToDestroy);
        //player.playerSpeed = player.playerSpeedSave;

        // BLOQUES QUE SE DESTRUYEN AL PULSAR EL BOTON:
        if ((!blocksToDestroy[0].isTriggerActivado() || !blocksToDestroy[1].isTriggerActivado()) && !caja.iCantGoBeyondX) {
            if (EnemyMovements.colisionConPuertaHaciaAdelante(game, blocksToDestroy[0])
                    || EnemyMovements.colisionConPuertaHaciaAdelante(game, blocksToDestroy[1])) {
                player.playerSpeed = 0;
            } else {
                if (player.playerSpeed == 0) {
                    player.playerSpeed = player.playerSpeedSave;
                }
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void draw(Graphics g) {

        lm.draw(g);

        ///// SECCIONES /////
        drawSection99(g);

        drawSection5(g);

        drawSection4(g);

        drawSection3(g);

        drawSection2(g);

        drawSection1(g);

        drawSection0(g);
        ////////////////////

        // hitbox de los enemigos: //
        //enemigos[0].drawHitbox(g);
        //enemigos[1].drawHitbox(g);
        //enemigos[2].drawHitbox(g);
        //////////////////////////////
        if (!teleport.teleportDisappears && !Npc.enDialogo) {
            player.drawPlayer(g);
        }

        if (player.heMuerto) {
            g.setColor(new Color(0, 0, 0, 90));
            g.fillRect(0, 0, TILE_WIDTH, TILE_HEIGHT);

            //BufferedImage imageWithOpacity = new BufferedImage(deathScreenBG.getWidth(), deathScreenBG.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = (Graphics2D) g.create();

            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));

            // Dibuja la imagen original sobre la nueva imagen con opacidad
            g2.drawImage(deathScreenBG, (TILE_WIDTH / 2) - ((int)MainGame.valorRelativo(deathScreenBG.getWidth()) / 2), (TILE_HEIGHT / 2) - ((int)MainGame.valorRelativo(deathScreenBG.getHeight()) / 2), (int)MainGame.valorRelativo(deathScreenBG.getWidth()), (int)MainGame.valorRelativo(deathScreenBG.getHeight()), null);
        }

        g.drawImage(subBG, 0, 0, TILE_WIDTH, TILE_HEIGHT, null);
        drawEscapeMessage(g);
        drawNumeroMuertes(g);
    }

    public void drawNumeroMuertes(Graphics g) {
        g.setFont(new Font(Font.DIALOG, Font.BOLD, (int)MainGame.valorRelativo(15)));
        g.setColor(Color.WHITE);
        g.drawString("Muertes: " + game.pp.muertesTotales, (int)MainGame.valorRelativo(20), (int)MainGame.valorRelativo(35));
    }

    public void drawBibleMention(Graphics g) {
        g.setColor(new Color(255, 255, 255, opacityBibleMention));
        g.setFont(new Font(Font.MONOSPACED, Font.BOLD, (int)MainGame.valorRelativo(25)));
        g.drawString("2 Timoteo 1:7", (TILE_WIDTH / 2) - (int)MainGame.valorRelativo(680), (TILE_HEIGHT / 2));
        g.setFont(new Font(Font.MONOSPACED, Font.ITALIC, (int)MainGame.valorRelativo(25)));
        g.drawString("Porque no nos ha dado Dios espíritu de cobardía, sino de poder, de amor y de dominio propio.", (TILE_WIDTH / 2) - (int)MainGame.valorRelativo(680), (TILE_HEIGHT / 2) + (int)MainGame.valorRelativo(50));
    }

    public void drawEscapeMessage(Graphics g) {
        g.setFont(fontMessageEscape);
        g.setColor(new Color(0, 0, 0, 120));
        g.drawString("Pulsa la tecla [Escape] para volver al menú principal.", (TILE_WIDTH / 2) - ((int)MainGame.valorRelativo(450) / 2), (int)MainGame.valorRelativo(30));
    }

    public void drawSection99(Graphics g) {
        if (lm.getCurrentLevel().getSection() == 99) {
            npcs[2].draw(g);
            if (startEndScreen) {
                game.fadeTransition.draw(g);
                if (game.fadeTransition.isFinishFadeIn() && dialogueCounting == 1) {
                    dialogueCounting++;
                }

                if (dialogueCounting == 2) {
                    g.setColor(Color.BLACK);
                    g.fillRect(0, 0, TILE_WIDTH, TILE_HEIGHT);
                    drawBibleMention(g);
                }
            }
        }
    }

    public void drawBlackSubScreen(Graphics g) {
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, TILE_WIDTH, TILE_HEIGHT);
    }

    public void drawSection5(Graphics g) {
        if (lm.getCurrentLevel().getSection() == 5) {
            boss.draw(g);
            if (boss.isEndScene()) {
                teleport.draw(g);
            }
        }
    }

    public void drawSection4(Graphics g) {

        if (lm.getCurrentLevel().getSection() == 4) {
            //
        }
    }

    public void drawSection3(Graphics g) {
        if (lm.getCurrentLevel().getSection() == 3) {
            npcs[0].draw(g);
        }
    }

    public void drawSection2(Graphics g) {

        if (lm.getCurrentLevel().getSection() == 2) {
            // DIBUJAR CAÑONES:
            for (int i = 0; i < cañones.length; i++) {
                cañones[i].draw(g);
            }

            // DIBUJAR EL PINCHO QUE CAE:
            //EnemyDraws.drawStaticSprites(enemigos.get(3), g, game);
        }
    }

    public void drawSection1(Graphics g) {

        if (lm.getCurrentLevel().getSection() == 1) {

            // Botón, y los bloques destructibles: //
            EnemyDraws.drawButton(button, g, game);

            for (int i = 0; i < blocksToDestroy.length; i++) {
                EnemyDraws.drawStaticSprites(blocksToDestroy[i], g, game);
            }

            // objetos movibles (caja): /////////////
            caja.draw(g);

            drawPinchosProximos(new int[]{10, 11, 12, 13}, g);

            drawTutorialScreen(tutorial_screen_2, g);

            npcs[3].draw(g);

            BrodyEnemy brodyquest = (BrodyEnemy) enemigos.get(14);
            brodyquest.draw(g);
        }
    }

    public void drawSection0(Graphics g) {

        if (lm.getCurrentLevel().getSection() == 0) {
            // enemigos: ////////////////

            EnemyDraws.drawSerlliber(enemigos.get(0), g, game);

            // ESTRUCTURAS FIJAS QUE SOLO SE REPITEN UNA VEZ:
            // (HABLO DE ANIMACION INTERNA)
            EnemyDraws.drawStaticSprites(enemigos.get(1), g, game);
            EnemyDraws.drawStaticSprites(enemigos.get(2), g, game);
            EnemyDraws.drawStaticSprites(enemigos.get(3), g, game);

            if (!enemigos.get(4).isInvisible()) {
                EnemyDraws.drawStaticSprites(enemigos.get(4), g, game);
            }

            drawPinchosProximos(new int[]{5, 6, 7}, g);

            drawPinchosProximos(new int[]{8, 9}, g);

            drawTutorialScreen(tutorial_screen_1, g);

            npcs[1].draw(g);
        }
    }

    public void drawTutorialScreen(BufferedImage tutorialScreen, Graphics g) {
        g.drawImage(tutorialScreen, 0, 0, TILE_WIDTH, TILE_HEIGHT, null);
    }

    public void drawPinchosProximos(int[] indexes, Graphics g) {
        for (int i = 0; i < indexes.length; i++) {

            if (!enemigos.get(indexes[i]).isInvisible()) {
                EnemyDraws.drawStaticSprites(enemigos.get(indexes[i]), g, game);
            }
        }
    }

    /*
    public void resetPosAndCps() {
        if (game.pp.cpCogido != null) {
            player.getHitbox().x = game.pp.cpCogido.getxPos();
            player.getHitbox().y = game.pp.cpCogido.getyPos();
        } else {
            player.getHitbox().x = Integer.parseInt(player.getRespawnPoint().split(";")[0]);
            player.getHitbox().y = Integer.parseInt(player.getRespawnPoint().split(";")[1]);
        }
    }*/
    public void resetControlsPlayer() {
        player.setIsRight(false);
        player.setIsLeft(false);
        isPressed = false;
        player.isJumping = false;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    public void controlesPlayerPressed(KeyEvent e) {

        if (e.getKeyCode() == game.pp.controlSalto && !startEndScreen) {
            if (!isPressed) {
                if (!player.inAir) {
                    player.jumpingFromGround = true;
                }

                player.isJumping = true;
                isPressed = true;

                if (player.jumpingFromGround && !player.heMuerto && !Player.serInmortalGame) {
                    game.effectUpdate(game.effects[1]);
                }

                if (!getPlayer().cannotJumpMore && !player.jumpingFromGround && !player.heMuerto) {
                    game.effectUpdate(game.effects[0]);
                }
            }

        } else if (e.getKeyCode() == game.pp.controlIzquierda) {
            player.setIsLeft(true);

        } else if (e.getKeyCode() == game.pp.controlDerecha) {
            player.setIsRight(true);

        } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE && !startEndScreen) {

            escapePressed = true;
            //Gamestate.setGameState(Gamestate.MENU_STATE);
            // resetear los valores del nivel:
            //resetControlsPlayer();
            //resetPosAndCps();

        } else if (e.getKeyCode() == KeyEvent.VK_R && !startEndScreen) {
            player.heMuerto = false;
            player.heRespawneado = true;
            game.pp.muertesTotales++;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!Npc.enDialogo) {
            controlesPlayerPressed(e);
        }

        Npc.keyPressed(e);
    }

    public void controlesPlayerReleased(KeyEvent e) {
        if (e.getKeyCode() == game.pp.controlSalto) {
            player.isJumping = false;
            isPressed = false;
            counterPressed = 0;

            if (!player.jumpingFromGround) {
                if (player.numberOfJumpsOnAir <= 1) {
                    player.cannotJumpMore = true;
                }

                player.numberOfJumpsOnAir--;
            } else {
                player.jumpingFromGround = false;
            }

        } else if (e.getKeyCode() == game.pp.controlIzquierda) {
            player.setIsLeft(false);

        } else if (e.getKeyCode() == game.pp.controlDerecha) {
            player.setIsRight(false);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        controlesPlayerReleased(e);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
}
