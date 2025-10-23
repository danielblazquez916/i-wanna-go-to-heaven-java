package ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import utilz.LoadSave;
import utilz.PlayerPrefs;
import game.MainGame;
import static game.MainGame.TILE_HEIGHT;
import static game.MainGame.TILE_SIZE;
import static game.MainGame.TILE_WIDTH;

public class ChangeSkin extends ChangeControls {

    // skins que nos podemos poner:
    String[] skins;
    Rectangle[] skinHitboxes;
    BufferedImage[] portadas;
    String[] nombreSkins;

    // dimensiones del cuadrito que va a haber por skin:
    int widthMiniRect, heightMiniRect;
    int xPosMiniRect, yPosMiniRect;
    int offsetEntreRect = TILE_SIZE/2;
    int counter = 0;
    boolean isButtonActivated;

    // skin seleccionada:
    int skinIndexSeleccionada;
    public boolean skinCambiada = false;

    public ChangeSkin(int xPos, int yPos, int height, int width, MainGame game) {
        super(xPos, yPos, height, width, game);
        skinIndexSeleccionada = this.game.pp.indexSkin;
        
        instanciarSkins(4);
        nombreSkins = new String[]{"the guy", "the black guy", "null", "null"};
        instanciarPortadas();
        isButtonActivated = true;

        widthMiniRect = this.width / 4;
        heightMiniRect = this.height / 3;
        xPosMiniRect = xPos + (width / 4);
        yPosMiniRect = yPos + (height / 3);
    }
    
    public void instanciarPortadas(){
        portadas = new BufferedImage[this.skins.length];
        
        portadas[0] = LoadSave.importImg(LoadSave.PLAYER_1_PORTADA);
        portadas[1] = LoadSave.importImg(LoadSave.PLAYER_2_PORTADA);
    }

    public Rectangle[] getSkinHitboxes() {
        return skinHitboxes;
    }

    public String[] getSkins() {
        return skins;
    }

    public int getSkinIndexSeleccionada() {
        return skinIndexSeleccionada;
    }

    @Override
    public void resetValues() {
        skinCambiada = false;
    }

    public void instanciarSkins(int cantidadSkins) {
        this.skins = new String[cantidadSkins];
        this.skinHitboxes = new Rectangle[cantidadSkins];

        ///// SKINS (la unica xd): /////
        this.skins[0] = "/res/player_anim.png";
        skinHitboxes[0] = new Rectangle();

        this.skins[1] = "/res/player_anim_2.png";
        skinHitboxes[1] = new Rectangle();

        this.skins[2] = "/res/player_anim.png";
        skinHitboxes[2] = new Rectangle();

        this.skins[3] = "/res/player_anim.png";
        skinHitboxes[3] = new Rectangle();
    }

    @Override
    public void draw(Graphics g) {
        // Dibujar el cuadro de skins:
        drawGrayRectangle(g);

        // dibujar los cuadritos que estan dentro del cuadro de skins
        // (cada cuadrito representa una skin para seleccionar :)):
        
        drawSkinsToSelect(g);

        if (isButtonActivated) {
            drawHitbox(g, skinIndexSeleccionada);
        }
    }

    @Override
    public void update() {
        // cuando pulses sobre un boton,
        // hacerlo mas pequeñito y despues mas grande:
        if (skinCambiada) {
            tick++;
            if (tick >= 120) {
                tick = 0;
                skinCambiada = false;
            }
        }
    }

    public void drawSkinsToSelect(Graphics g) {
        xPosMiniRect = xPos + (width / 4) - (TILE_SIZE/3);
        yPosMiniRect = yPos + (height / 4) - TILE_SIZE;

        for (int i = 0; i < this.skins.length; i++) {

            counter++;

            drawMiniRectangle(g, i);
            createHitbox(i);

            xPosMiniRect += ((widthMiniRect) + offsetEntreRect);

            if (counter >= 2) {
                yPosMiniRect += ((heightMiniRect) + offsetEntreRect);
                xPosMiniRect = xPos + (width / 4) - (TILE_SIZE/3);
                counter = 0;
            }
        }
    }

    public void drawSkinCambiadaConExito(Graphics g) {
        
        g.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, TILE_SIZE/3));
        g.setColor(Color.GREEN);
        g.drawString("Skin cambiada con éxito! :>", (TILE_WIDTH/2)-(g.getFontMetrics().stringWidth("Skin cambiada con éxito! :>")/2), TILE_HEIGHT-(TILE_SIZE));
    }

    public void createHitbox(int index) {
        skinHitboxes[index].x = xPosMiniRect;
        skinHitboxes[index].y = yPosMiniRect;
        skinHitboxes[index].height = heightMiniRect;
        skinHitboxes[index].width = widthMiniRect;
    }

    public void drawHitbox(Graphics g, int index) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(5));

        g.setColor(Color.GREEN);
        g2.drawRect(skinHitboxes[index].x, skinHitboxes[index].y, skinHitboxes[index].width, skinHitboxes[index].height);
    }

    public void drawMiniRectangle(Graphics g, int index) {
        // Dibujar el cuadro de controles:
        g.drawImage(portadas[index], xPosMiniRect, yPosMiniRect, widthMiniRect, heightMiniRect, null);
        if(skinIndexSeleccionada == index){
            g.setColor(new Color(0, 255, 0, 70));
        }else{
            g.setColor(new Color(0, 0, 0, 70));
        }
        
        g.fillRect(xPosMiniRect, yPosMiniRect, widthMiniRect, heightMiniRect);
        
        
        g.setColor(Color.BLACK);
        g.drawRect(xPosMiniRect, yPosMiniRect, widthMiniRect, heightMiniRect);
        

        g.setColor(Color.BLACK);
        g.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, TILE_SIZE/4));

        String skinText = nombreSkins[index];
        g.drawString(skinText, xPosMiniRect + (TILE_SIZE/2), yPosMiniRect + heightMiniRect - (TILE_SIZE/3));
    }

    @Override
    public void MouseClicked(MouseEvent e) {
        for (int i = 0; i < skinHitboxes.length; i++) {
            if (skinHitboxes[i].contains(e.getX(), e.getY())) {
                skinIndexSeleccionada = i;
                isButtonActivated = true;
                break;
            }
        }
    }
}
