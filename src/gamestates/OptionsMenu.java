package gamestates;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import ui.ChangeControls;
import ui.ChangeSkin;
import ui.MenuFPS;
import ui.SectionButton;
import utilz.LoadSave;
import game.MainGame;
import static game.MainGame.TILE_HEIGHT;
import static game.MainGame.TILE_SIZE;
import static game.MainGame.TILE_WIDTH;

public class OptionsMenu implements statemethods {

    BufferedImage bgOptionsMenu;

    // Titulo:
    BufferedImage optionsTitle;
    int widthTit = TILE_SIZE * 11, heightTit = TILE_SIZE * 5, yPosTitle = TILE_SIZE;
    int tickTitle;
    boolean scaleUp = true;

    // Opciones:
    SectionButton[] opciones;
    int width = TILE_SIZE * 6, height = TILE_SIZE * 3;
    // animacion de botones:
    public int tickScale, scaleCounter, scaleInc = 3, scaleLimit = (TILE_SIZE / 2) + (TILE_SIZE / 3);
    boolean stopScale;

    // VENTANAS MODALES QUE ABREN LAS OPCIONES:
    ChangeControls cc;
    ChangeSkin cs;
    MenuFPS fpsm;
    //String opcionSeleccionada = "";

    MainGame game;

    BufferedImage arrow;
    int index;
    int xArrow, yArrow, heightArrow = TILE_SIZE, widthArrow = TILE_SIZE;
    boolean upPressed, downPressed, enterPressed;
    int speed = TILE_SIZE / 2;

    boolean goIntoPopUp;

    public OptionsMenu(MainGame game) {
        this.game = game;

        colocarOpciones();
        importarImagenes();
        setXandYArrow();

        int widthCCMenu = TILE_SIZE * 9, heightCCMenu = TILE_SIZE * 8, xCCMenu = (TILE_WIDTH / 2) - (widthCCMenu / 2), yCCMenu = (TILE_HEIGHT / 2) - (heightCCMenu / 2);
        int widthCSMenu = TILE_SIZE * 12, heightCSMenu = TILE_SIZE * 8, xCSMenu = (TILE_WIDTH / 2) - (widthCSMenu / 2), yCSMenu = (TILE_HEIGHT / 2) - (heightCSMenu / 2);
        int widthMFMenu = TILE_SIZE * 12, heightMFMenu = TILE_SIZE * 10, xMFMenu = (TILE_WIDTH / 2) - (widthMFMenu / 2), yMFMenu = (TILE_HEIGHT / 2) - (heightMFMenu / 2);

        cc = new ChangeControls(xCCMenu, yCCMenu, heightCCMenu, widthCCMenu, this.game);
        cs = new ChangeSkin(xCSMenu, yCSMenu, heightCSMenu, widthCSMenu, this.game);
        fpsm = new MenuFPS(xMFMenu, yMFMenu, heightMFMenu, widthMFMenu, this.game);
    }

    public void setXandYArrow() {
        xArrow = (TILE_WIDTH / 2) - (widthArrow / 2);
        yArrow = opciones[0].getyPos();
    }

    public void importarImagenes() {
        arrow = LoadSave.importImg(LoadSave.ARROW_SCHEMA);
        bgOptionsMenu = LoadSave.importImg(LoadSave.BG_OPTIONS);
        optionsTitle = LoadSave.importImg(LoadSave.TITLE_OPTIONS);
    }

    public void colocarOpciones() {
        opciones = new SectionButton[4];
        int distanciaEntreBotones = 20;

        opciones[0] = new SectionButton((TILE_WIDTH / 2) - (width / 2), heightTit, width, height, LoadSave.importImg(LoadSave.BUTTON_CAMBIAR_CONTROLES));
        opciones[1] = new SectionButton((TILE_WIDTH / 2) - (width / 2), opciones[0].getyPos() + distanciaEntreBotones + height, width, height, LoadSave.importImg(LoadSave.BUTTON_CAMBIAR_SKIN));
        opciones[2] = new SectionButton((TILE_WIDTH / 2) - (width / 2), opciones[1].getyPos() + distanciaEntreBotones + height, width, height, LoadSave.importImg(LoadSave.BUTTON_CONFIGURAR_FPS));

        opciones[3] = new SectionButton((TILE_WIDTH / 2) - (width / 2), opciones[2].getyPos() + distanciaEntreBotones + height, width, height, LoadSave.importImg(LoadSave.BUTTON_VOLVER));
        opciones[3].setStateToGo(Gamestate.MENU_STATE);
    }

    ///////////////////////////////// UPDATE: ///////////////////////////////////
    @Override
    public void update() {
        // el update del minimenu de cambiar controles:
        getCc().update();
        getCs().update();
        getFpsm().update();

        // hacer el movimiento de la flecha:
        updateArrowPos();
        updateTitle();

        updateSizeScale();

        if (enterPressed && index == 3) {

            game.menuState.resetValuesIfExit();
            game.fadeTransition.setFinishTransition(false);
            game.fadeTransition.setState(Gamestate.MENU_STATE);
            goIntoPopUp = false;
            enterPressed = false;
            return;
        }

        activatesPopUp();
    }

    public void updateTitle() {
        tickTitle++;
        if (scaleUp) {
            if (tickTitle >= 1) {
                tickTitle = 0;
                widthTit += 6;
                heightTit += 3;
                if (widthTit - (TILE_SIZE * 11) >= 36) {
                    scaleUp = false;
                }
            }

        } else {
            if (tickTitle >= 1) {
                tickTitle = 0;
                widthTit -= 2;
                heightTit -= 1;
                if (widthTit - (TILE_SIZE * 11) <= 0) {
                    scaleUp = true;
                }
            }
        }
    }

    public void updateSizeScale() {
        if (!stopScale) {
            tickScale++;
            if (tickScale >= 1) {
                tickScale = 0;
                if (scaleCounter <= scaleLimit) {
                    ampliarBotonSize(scaleInc, scaleInc);
                } else {
                    stopScale = true;
                    return;
                }

                scaleCounter += scaleInc;
            }
        }
    }

    public void resetarBotonSize() {
        opciones[index].setWidth(width);
        opciones[index].setHeight(height);
        opciones[index].setxPos(opciones[index].xPosInitial);
        opciones[index].setyPos(opciones[index].yPosInitial);
    }

    public void ampliarBotonSize(int widthInc, int heightInc) {
        opciones[index].setWidth(opciones[index].getWidth() + widthInc);
        opciones[index].setHeight(opciones[index].getHeight() + widthInc);
        opciones[index].setxPos((TILE_WIDTH / 2) - (opciones[index].getWidth() / 2));
        opciones[index].setyPos(opciones[index].getyPos());
    }

    public void activatesPopUp() {
        if (enterPressed) {

            if (goIntoPopUp) {
                resetAfterQuitPopUp();
                goIntoPopUp = false;
                enterPressed = false;
                return;
            }

            goIntoPopUp = true;
            enterPressed = false;
        }
    }

    public void resetAfterQuitPopUp() {
        switch (index) {
            case 0: {
                cc.resetValues();
                break;
            }

            case 1: {
                if (!cs.getSkins()[cs.getSkinIndexSeleccionada()].equals(game.pp.skinPlayer)) {
                    changeSkin();
                }
                break;
            }

            case 2: {
                fpsm.resOptionSelected = false;
                fpsm.mouseClickedResolution = true;
                game.pp.volume[0] = game.volumen[0];
                game.pp.volume[1] = game.volumen[1];
                game.effectsVolumeUpdate();
                game.mainSongsVolumeUpdate();

                break;
            }

            // more maybee...
        }
    }

    public void changeSkin() {
        game.pp.skinPlayer = cs.getSkins()[cs.getSkinIndexSeleccionada()];
        game.pp.indexSkin = cs.getSkinIndexSeleccionada();
        game.playingState.getPlayer().PLAYER_SCHEMA = game.pp.skinPlayer;
        game.playingState.getPlayer().setSkin();
        game.playingState.getPlayer().loadAnimations(8, 6);

        cs.skinCambiada = true;
    }

    public void updateArrowPos() {
        if (upPressed) {
            goingUpAnimation();
        } else if (downPressed) {
            goingDownAnimation();
        }
    }

    public void resetValuesIfExit() {
        resetarBotonSize();
        yArrow = opciones[0].getyPos();
        index = 0;
        stopScale = false;
        tickScale = scaleCounter = 0;
    }

    public void goingUpAnimation() {

        if (yArrow - speed >= opciones[index - 1].getyPos()) {
            yArrow -= speed;
        } else {
            upPressed = false;
            resetarBotonSize();
            index--;
            if (yArrow != opciones[index].getyPos()) {
                yArrow = opciones[index].getyPos();
            }
            stopScale = false;
            scaleCounter = 0;
        }
    }

    public void goingDownAnimation() {

        if (yArrow + speed <= opciones[index + 1].getyPos()) {
            yArrow += speed;
        } else {
            downPressed = false;
            resetarBotonSize();
            index++;
            if (yArrow != opciones[index].getyPos()) {
                yArrow = opciones[index].getyPos();
            }
            stopScale = false;
            scaleCounter = 0;
        }
    }

    /////////////////////// DRAW: ///////////////////////////
    @Override
    public void draw(Graphics g) {

        drawBackground(g);
        drawTitle(g);
        drawButtons(g);
        drawArrow(g);
        drawPopUp(g);

        if (cs.skinCambiada) {
            cs.drawSkinCambiadaConExito(g);
        }

        fpsm.drawMensajeSeHaCambiadoRes(g);
    }

    public void drawPopUp(Graphics g) {
        if (goIntoPopUp) {
            getDraw(g);
        }
    }

    public void drawArrow(Graphics g) {
        // dibujar la flecha que se va a ir moviendo de arriba a abajo:
        g.drawImage(arrow, xArrow, yArrow, widthArrow, heightArrow, null);
    }

    public void drawBackground(Graphics g) {
        g.drawImage(bgOptionsMenu, 0, 0, TILE_WIDTH, TILE_HEIGHT, null);

        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, TILE_WIDTH, TILE_HEIGHT);
    }

    public void drawTitle(Graphics g) {
        g.drawImage(optionsTitle, (TILE_WIDTH / 2) - (widthTit / 2), yPosTitle, widthTit, heightTit, null);
    }

    /*
    public void drawButtons(Graphics g) {
        // dibujar los botones:
        int indexPart = 0;
        for (int i = 0; i < opciones.length; i++) {
            if (index == i) {
                indexPart = 1;
            } else {
                indexPart = 0;
            }
            g.drawImage(opciones[i].getParts()[indexPart], opciones[i].getxPos(), opciones[i].getyPos(), width, height, null);
        }
    }*/
    public void drawButtons(Graphics g) {
        // dibujar los botones:

        for (int i = 0; i < opciones.length; i++) {
            int indexPart = resaltarBoton(i);
            g.drawImage(opciones[i].getParts()[indexPart], opciones[i].getxPos(), opciones[i].getyPos(), opciones[i].getWidth(), opciones[i].getHeight(), null);
        }
    }

    public int resaltarBoton(int i) {
        int indexPart = 0;

        if (index == i) {
            indexPart = 1;
        }

        return indexPart;
    }

    public void getDraw(Graphics g) {
        switch (index) {
            case 0: {
                // draw de cambiar controles:
                cc.draw(g);
                break;
            }

            case 1: {
                // draw de cambiar skin:
                cs.draw(g);
                break;
            }

            case 2: {
                // draw de menu para mostrar lo de los fps:
                fpsm.draw(g);
                break;
            }

            // los demás casos...
            default: {
                break;
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!goIntoPopUp) {
            if (e.getKeyCode() == KeyEvent.VK_UP && !upPressed) {
                if (!downPressed && index > 0) {
                    upPressed = true;
                }

            } else if (e.getKeyCode() == KeyEvent.VK_DOWN && !downPressed) {
                if (!upPressed && index < opciones.length - 1) {
                    downPressed = true;
                }
            }

        }

        if (e.getKeyCode() == KeyEvent.VK_ENTER && !downPressed && !upPressed) {
            if (!enterPressed) {
                enterPressed = true;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // VACIOASUHJIAD
    }

    /*
    @Override
    public void keyPressed(KeyEvent e) {
        if (!goIntoPopUp) {
            if (e.getKeyCode() == KeyEvent.VK_W && !stopPressingUp) {
                isPressedUp = true;
            } else if (e.getKeyCode() == KeyEvent.VK_S && !stopPressingDown) {
                isPressedDown = true;
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_ENTER && !stopPressingEnter) {
            if (!goIntoPopUp) {
                goIntoPopUp = true;
                if (index == 3) {
                    stopPressingEnter = true;
                }

            } else {
                switch (index) {
                    case 0: {
                        cc.resetValues();
                        break;
                    }

                    case 1: {
                        if (!cs.getSkins()[cs.getSkinIndexSeleccionada()].equals(PlayerPrefs.PlayerSkin.skinPlayer)) {
                            PlayerPrefs.PlayerSkin.skinPlayer = cs.getSkins()[cs.getSkinIndexSeleccionada()];
                            PlayerPrefs.PlayerSkin.indexSkin = cs.getSkinIndexSeleccionada();
                            LoadSave.PLAYER_SCHEMA = PlayerPrefs.PlayerSkin.skinPlayer;
                            game.playingState.getPlayer().setSkin();
                            game.playingState.getPlayer().loadAnimations(8, 6);

                            cs.skinCambiada = true;
                        }
                        break;
                    }

                    // more maybee...
                }

                goIntoPopUp = false;
            }
        }
    }

    

    
    public void volverAtras(String anteriorGamestate) {
        // easy peasy
        
        game.cofbTrans.resetValues();
        cs.resetValues();
        Gamestate.setGameState(anteriorGamestate);
        goIntoPopUp = false;
    }
     
    public void goUp() {
        if (index > 0) {
            index--;
        } else {
            index = opciones.length - 1;
        }
    }

    public void goDown() {
        if (index < opciones.length - 1) {
            index++;
        } else {
            index = 0;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W) {
            isPressedUp = false;
            stopPressingUp = false;
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            isPressedDown = false;
            stopPressingDown = false;
        }
    }
     */
    // GETTERS Y DEMÁS: //
    public boolean isGoIntoPopUp() {
        return goIntoPopUp;
    }

    public int getIndex() {
        return index;
    }

    public ChangeControls getCc() {
        return cc;
    }

    public ChangeSkin getCs() {
        return cs;
    }

    public MenuFPS getFpsm() {
        return fpsm;
    }
}
