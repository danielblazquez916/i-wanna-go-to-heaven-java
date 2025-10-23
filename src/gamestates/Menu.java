package gamestates;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import ui.SectionButton;
import utilz.LoadSave;
import utilz.PlayerPrefs;
import game.MainGame;
import static game.MainGame.TILE_WIDTH;
import static game.MainGame.TILE_HEIGHT;
import static game.MainGame.TILE_SIZE;

public class Menu implements statemethods {

    SectionButton[] botonesMenu;
    int width = TILE_SIZE*6, height = TILE_SIZE*3, distanciaEntreBotonesY = height + (TILE_SIZE);

    BufferedImage title;
    int widthTit = TILE_SIZE*11;
    int heightTit = TILE_SIZE*5;
    int tickTitle;
    boolean scaleUp = true;

    BufferedImage background;
    BufferedImage[] insignias;

    BufferedImage arrow;
    int xArrow, yArrow, heightArrow = TILE_SIZE, widthArrow = TILE_SIZE;
    int index;
    int tickScale, scaleCounter, scaleInc = 3, scaleLimit = (TILE_SIZE/2)+(TILE_SIZE/3);
    boolean stopScale;

    boolean upPressed, downPressed, enterPressed;
    int speed = TILE_SIZE/2;
    
    String[] textoInfo;

    MainGame game;

    public Menu(MainGame game) {
        this.game = game;

        inicializarBotones();
        importarImagenes();
        setXandYArrow();
        
        textoInfo = inicializarTextoInfo();
    }
    
    public String[] inicializarTextoInfo(){
        textoInfo = new String[]{
            "La Insignia Maestra es una recompensa especial",
            "que se revelará al pasar el juego.",
            "",
            "Dependiendo del número de muertes con las que",
            "acabes la partida, se decidirá el valor de tu",
            "recompensa.",
            "",
            "Buena suerte! 😉",
            "",
            "",
            "",
            "",
            "¡No te preocupes! A las muy malas tienes en mi",
            "canal de YouTube un gameplay completo. Espero",
            "que no tengas que usarlo cabroncete, espabila.",
            "",
            "Busca en YT ⇉"
        };
        
        return textoInfo;
    }

    public void importarImagenes() {
        background = LoadSave.importImg(LoadSave.BG_MENU);
        title = LoadSave.importImg(LoadSave.TITLE_MENU);
        arrow = LoadSave.importImg(LoadSave.ARROW_SCHEMA);
        insignias = LoadSave.subdividirImagen(LoadSave.importImg(LoadSave.INSIGNIAS), 4);
    }

    public void inicializarBotones() {

        botonesMenu = new SectionButton[2];

        botonesMenu[0] = new SectionButton((TILE_WIDTH / 2) - (width / 2), (TILE_SIZE*10), width, height, LoadSave.importImg(LoadSave.BUTTON_START));
        botonesMenu[0].setStateToGo(Gamestate.LEVEL_PLAYING_STATE);

        botonesMenu[1] = new SectionButton((TILE_WIDTH / 2) - (width / 2), botonesMenu[0].getyPos() + distanciaEntreBotonesY, width, height, LoadSave.importImg(LoadSave.BUTTON_OPTIONS));
        botonesMenu[1].setStateToGo(Gamestate.OPTIONS_STATE);
    }

    public void setXandYArrow() {
        xArrow = (TILE_WIDTH / 2) - (widthArrow / 2);
        yArrow = botonesMenu[0].getyPos();
    }

    ////////////////////////////////// UPDATES: //////////////////////////////////
    @Override
    public void update() {
        updateTitle();
        updateArrowPos();
        changeScene();
        updateSizeScale();
        if (game.playingState.getPlayer().heMuerto) {
            game.playingState.getPlayer().heMuerto = false;
            game.playingState.getPlayer().heRespawneado = true;
        }
    }

    public void changeScene() {
        if (enterPressed) {
            game.fadeTransition.setFinishTransition(false);
            game.fadeTransition.setState(botonesMenu[index].getStateToGo());
            
            if(botonesMenu[index].getStateToGo().equals(Gamestate.OPTIONS_STATE)){
                game.optionsState.resetValuesIfExit();
            }
            
            game.playingState.getPlayer().heRespawneado = true;
            enterPressed = false;
        }
    }
    
    public void resetValuesIfExit(){
        resetarBotonSize();
        yArrow = botonesMenu[0].getyPos();
        index = 0;
        stopScale = false;
        tickScale = scaleCounter = 0;
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
        botonesMenu[index].setWidth(width);
        botonesMenu[index].setHeight(height);
        botonesMenu[index].setxPos(botonesMenu[index].xPosInitial);
        botonesMenu[index].setyPos(botonesMenu[index].yPosInitial);
    }

    public void ampliarBotonSize(int widthInc, int heightInc) {
        botonesMenu[index].setWidth(botonesMenu[index].getWidth() + widthInc);
        botonesMenu[index].setHeight(botonesMenu[index].getHeight() + widthInc);
        botonesMenu[index].setxPos((TILE_WIDTH / 2) - (botonesMenu[index].getWidth() / 2));
        botonesMenu[index].setyPos(botonesMenu[index].getyPos());
    }

    public void updateArrowPos() {
        if (upPressed) {
            goingUpAnimation();
        } else if (downPressed) {
            goingDownAnimation();
        }
    }

    public void updateTitle() {
        tickTitle++;
        if (scaleUp) {
            if (tickTitle >= 1) {
                tickTitle = 0;
                widthTit += 6;
                heightTit += 3;
                if (widthTit - (TILE_SIZE*11) >= 36) {
                    scaleUp = false;
                }
            }

        } else {
            if (tickTitle >= 1) {
                tickTitle = 0;
                widthTit -= 2;
                heightTit -= 1;
                if (widthTit - (TILE_SIZE*11) <= 0) {
                    scaleUp = true;
                }
            }
        }
    }

    public void goingUpAnimation() {

        if (yArrow - speed >= botonesMenu[index - 1].getyPos()) {
            yArrow -= speed;
        } else {
            upPressed = false;
            resetarBotonSize();
            index--;
            if (yArrow != botonesMenu[index].getyPos()) {
                yArrow = botonesMenu[index].getyPos();
            }
            stopScale = false;
            scaleCounter = 0;
        }
    }

    public void goingDownAnimation() {

        if (yArrow + speed <= botonesMenu[index + 1].getyPos()) {
            yArrow += speed;
        } else {
            downPressed = false;
            resetarBotonSize();
            index++;
            if (yArrow != botonesMenu[index].getyPos()) {
                yArrow = botonesMenu[index].getyPos();
            }
            stopScale = false;
            scaleCounter = 0;
        }
    }

    //////////////////////////////////// DRAW: ///////////////////////////////////
    @Override
    public void draw(Graphics g) {
        drawBackground(g);
        drawButtons(g);
        drawTitle(g);
        drawArrow(g);
        drawMenuInsignia(g);
        drawMenuInformacion(g);
    }
    
    public void drawMenuInformacion(Graphics g){
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(TILE_SIZE, TILE_HEIGHT-(TILE_SIZE*11), (TILE_SIZE*9)+(TILE_SIZE/2), TILE_SIZE*11);
        drawInfo(g, textoInfo);
    }
    
    public void drawInfo(Graphics g, String[] texto){
        int acumulador = 0;
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, (TILE_SIZE/2)));
        g.setColor(new Color(129, 209, 66, 130));
        g.drawString("- ¿Qué es la Insignia Maestra?", TILE_SIZE+(TILE_SIZE/2), TILE_HEIGHT-(TILE_SIZE*10)); 

        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, (TILE_SIZE/3)));
        g.setColor(new Color(255, 255, 255, 130));
        for(int i = 0; i < texto.length; i++){
            g.drawString(texto[i], TILE_SIZE+(TILE_SIZE/2), TILE_HEIGHT-(TILE_SIZE*9)+acumulador);
            acumulador += (TILE_SIZE-(TILE_SIZE/2));
        }
        
        g.setColor(new Color(84, 184, 222, 130));
        g.drawString("@poper505", TILE_SIZE*4, TILE_HEIGHT-(TILE_SIZE*9)+acumulador-((TILE_SIZE-(TILE_SIZE/2))));
        
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, (TILE_SIZE/2)));
        g.setColor(new Color( 56, 220, 168 , 130));
        g.drawString("- ¿Qué hago si no sé como seguir?", TILE_SIZE+(TILE_SIZE/2), TILE_HEIGHT-(TILE_SIZE*10)+(TILE_SIZE*6)); 
    }
    
    public void drawMenuInsignia(Graphics g){
        
        g.setColor(returnColorMenuInsignias(100));
        g.fillRect((TILE_WIDTH/2)-((TILE_SIZE*7)/2)+(TILE_SIZE*8), (TILE_SIZE*9)+(TILE_SIZE/2), TILE_SIZE*7, TILE_SIZE*7);
        
        g.setColor(returnColorMenuInsignias(140));
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, TILE_SIZE/2));
        g.drawString("Insignia Maestra:", ((TILE_WIDTH/2)-((TILE_SIZE*7)/2)+(TILE_SIZE*8))+((TILE_SIZE*7)/2)-(g.getFontMetrics().stringWidth("Insignia Maestra:")/2), botonesMenu[0].getyPos()+10);
        
        drawInsignia(g);
    }
    
    public Color returnColorMenuInsignias(int opacity){
        Color color = null;
        
        switch(game.pp.tipoInsignia){
            case 0 -> color = new Color(0, 0, 0, opacity);
            case 1 -> color = new Color(161, 97, 91, opacity);
            case 2 -> color = new Color(170, 197, 193, opacity);
            case 3 -> color = new Color(249, 181, 52, opacity);
            default -> color = new Color(0, 0, 0, opacity);
        }
        
        return color;
    }
    
    public void drawInsignia(Graphics g){
        int xPosMenu = (TILE_WIDTH/2)-((TILE_SIZE*7)/2)+(TILE_SIZE*8);
        int yPosMenu = (TILE_SIZE*9)+(TILE_SIZE/2);
        g.drawImage(insignias[Math.abs(game.pp.tipoInsignia-3)], xPosMenu+((TILE_SIZE*7)/2)-((TILE_SIZE*5)/2), yPosMenu+((TILE_SIZE*7)/2)-((TILE_SIZE*5)/2), TILE_SIZE*5, TILE_SIZE*5, null);
    }

    public void drawArrow(Graphics g) {
        // dibujar la flecha que se va a ir moviendo de arriba a abajo:
        g.drawImage(arrow, xArrow, yArrow, widthArrow, heightArrow, null);
    }

    public void drawBackground(Graphics g) {
        // Fondo:
        g.drawImage(background, 0, 0, TILE_WIDTH, TILE_HEIGHT, null);
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, TILE_WIDTH, TILE_HEIGHT);
    }

    public void drawTitle(Graphics g) {
        // dibujar el titulo:
        g.drawImage(title, (TILE_WIDTH / 2) - (widthTit / 2), TILE_SIZE*2, widthTit, heightTit, null);
    }

    public void drawButtons(Graphics g) {
        // dibujar los botones:

        for (int i = 0; i < botonesMenu.length; i++) {
            int indexPart = resaltarBoton(i);
            g.drawImage(botonesMenu[i].getParts()[indexPart], botonesMenu[i].getxPos(), botonesMenu[i].getyPos(), botonesMenu[i].getWidth(), botonesMenu[i].getHeight(), null);
        }
    }

    public int resaltarBoton(int i) {
        int indexPart = 0;

        if (index == i) {
            indexPart = 1;
        }

        return indexPart;
    }

    ////////////////////////////// INPUTS //////////////////////////////
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP && !upPressed && !enterPressed) {
            if (!downPressed && index > 0) {
                upPressed = true;
            }

        } else if (e.getKeyCode() == KeyEvent.VK_DOWN && !downPressed && !enterPressed) {
            if (!upPressed && index < botonesMenu.length - 1) {
                downPressed = true;
            }

        } else if (e.getKeyCode() == KeyEvent.VK_ENTER && !downPressed && !upPressed) {
            enterPressed = true;
        }
    }

    ////////////////////////////////////////////////////////////////////
    @Override
    public void keyReleased(KeyEvent e) {
        //
    }
}
