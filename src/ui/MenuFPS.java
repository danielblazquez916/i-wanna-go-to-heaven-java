package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import utilz.LoadSave;
import game.MainGame;
import static game.MainGame.TILE_COLS;
import static game.MainGame.TILE_HEIGHT;
import static game.MainGame.TILE_SIZE;

public class MenuFPS extends ChangeControls {

    String[] options;
    Rectangle[] hitboxesOptions;
    BufferedImage[] selectedButton;

    // Hasta que cree las imagenes que representen
    // las diferentes opciones, de momento me tengo
    // que conformar con usar posiciones asi:
    int xPosOptions, yPosOptions;
    int separacionEnYOpciones = TILE_SIZE * 2;
    int indexOpcion = -1;
    boolean mouseClicked;
    int indexSaved = -1;

    // Opcion para cambiar la resolucion:
    String resOption = "CAMBIAR RESOLUCIÓN";
    Rectangle resOptionHitbox;
    public boolean resOptionSelected;

    String[] resolucionesDisponibles = new String[]{"1440 x 960", "1290 x 860", "1140 x 760", "900 x 600"};
    Rectangle[] hitboxesResDisp = new Rectangle[resolucionesDisponibles.length];
    int resOptionSelecc = 0;
    public boolean mouseClickedResolution;
    boolean mostrarMensajeSeHaCambiadoRes;
    public int tickMensaje;

    // scroll para bajar o subir el BGM:
    VolumeScroller volumeScrollBGM;
    
    // scroll para bajar o subir el SFX:
    VolumeScroller volumeScrollSFX;
    
    /*
    int widthBarraMain = (TILE_SIZE * 6), heightBarraMain = (TILE_SIZE / 4);
    float widthSubBarraMain = TILE_SIZE, heightSubBarraMain = heightBarraMain;
    int xPosBarraMain, yPosBarraMain;
    Rectangle hitboxBarraVol;
    boolean sobreBarra;
    int localizaciónPuntero;
    */

    public MenuFPS(int xPos, int yPos, int height, int width, MainGame game) {
        super(xPos, yPos, height, width, game);
        instanciarOpciones();

        xPosOptions = xPos + (TILE_SIZE * 2);
        yPosOptions = yPos + separacionEnYOpciones;

        selectedButton = LoadSave.subdividirImagen(LoadSave.importImg(LoadSave.OPTION_SELECTION), 2);

        resolucionesDisponibles = new String[]{"1440 x 960", "1290 x 860", "1140 x 760", "900 x 600"};
        resOptionSelecc = game.pp.resolution_index;
        
        // Volumen BGM:
        volumeScrollBGM = new VolumeScroller(this, "BGM: ", 0, this.game);
        volumeScrollSFX = new VolumeScroller(this, "SFX: ", 1, this.game);
    }

    @Override
    public void update() {
        // determinar la opcion seleccionada, y
        // hacer la accion en cuestion:
        if (mouseClicked) {
            indexOpcion = indexSaved;
            mouseClicked = false;
        }

        if (mouseClickedResolution) {
            if (game.pp.resolution_index != resOptionSelecc) {
                tickMensaje = 0;
                mostrarMensajeSeHaCambiadoRes = true;
                setResolution();
            }

            mouseClickedResolution = false;
        }

        if (indexOpcion != -1) {
            switch (indexOpcion) {

                case 0: {
                    if (!this.game.pp.mostrarFPS) {
                        this.game.pp.mostrarFPS = true;
                    } else {
                        this.game.pp.mostrarFPS = false;
                    }

                    indexOpcion = -1;

                    break;
                }

                case 1: {
                    if (!this.game.pp.FPSLimit) {
                        this.game.pp.FPSLimit = true;
                    } else {
                        this.game.pp.FPSLimit = false;
                    }

                    indexOpcion = -1;
                }
            }
        }

        updateMensajeSeHaCambiadoRes();
        //updateBarraVolumen();
        
        volumeScrollBGM.update();
        volumeScrollSFX.update();
    }

    /*
    public void updateBarraVolumen() {
        if (sobreBarra) {
            float diferencia = ((xPosBarraMain + widthSubBarraMain) - localizaciónPuntero);
            widthSubBarraMain -= (diferencia-1);
        }
    }*/

    public void updateMensajeSeHaCambiadoRes() {
        if (mostrarMensajeSeHaCambiadoRes) {
            tickMensaje++;
            if (tickMensaje >= 200) {
                tickMensaje = 0;
                mostrarMensajeSeHaCambiadoRes = false;
            }
        }
    }

    public void setResolution() {

        // HACER QUE SE APLIQUE AL REINICIAR EL JUEGO (SINO NO VA XD)
        String size = resolucionesDisponibles[resOptionSelecc].split(" x ")[0];
        game.pp.TILE_SIZE_SAVED = Integer.parseInt(size) / TILE_COLS;
        game.pp.resolution_index = resOptionSelecc;
    }

    public void instanciarOpciones() {
        options = new String[2];
        hitboxesOptions = new Rectangle[options.length];

        // Primera opcion para mostrar FPS:
        options[0] = "MOSTRAR FPS:";

        // Segunda opcion para desbloquear en su totalidad
        // los fps maximos (por defecto lo ajustaremos a 
        // los hercios del monitor que este ejecutando esto):
        options[1] = "DESBLOQUEAR FPS:";
    }

    public void drawOptions(Graphics g) {
        separacionEnYOpciones = 0;
        int index = 0;

        // dibujar las opciones en si:
        g.setFont(new Font("Segoe UI", Font.BOLD, TILE_SIZE / 2));
        for (int i = 0; i < options.length; i++) {
            g.setColor(Color.BLACK);
            g.drawString(options[i], xPosOptions, yPosOptions + separacionEnYOpciones);

            if ((this.game.pp.mostrarFPS && i == 0) || (this.game.pp.FPSLimit && i == 1)) {
                index = 1;
            } else {
                index = 0;
            }

            g.drawImage(selectedButton[index], xPosOptions + g.getFontMetrics().stringWidth(options[i]) + (TILE_SIZE / 2), yPosOptions + separacionEnYOpciones - (TILE_SIZE) + (TILE_SIZE / 3), TILE_SIZE, TILE_SIZE, null);
            hitboxesOptions[i] = new Rectangle(xPosOptions + g.getFontMetrics().stringWidth(options[i]) + (TILE_SIZE / 2), yPosOptions + separacionEnYOpciones - (TILE_SIZE) + (TILE_SIZE / 3), TILE_SIZE, TILE_SIZE);

            //drawHitboxes(g, i, Color.RED);
            separacionEnYOpciones += (TILE_SIZE * 2);
        }

        volumeScrollBGM.drawScrollMainSong(g);
        
        separacionEnYOpciones += (TILE_SIZE);
        
        volumeScrollSFX.drawScrollMainSong(g);

        separacionEnYOpciones += (TILE_SIZE * 2);

        drawResOption(g, separacionEnYOpciones);
        // poder ver los fps en la parte superior de la pantalla:
        // en el main.
    }

    /*
    public void drawScrollMainSong(Graphics g) {
        // Barra:
        xPosBarraMain = xPosOptions + (TILE_SIZE*2);
        yPosBarraMain = yPosOptions + separacionEnYOpciones;
        hitboxBarraVol = new Rectangle(xPosBarraMain, yPosBarraMain - (TILE_SIZE/4), widthBarraMain, heightBarraMain);
        Rectangle2D.Float rectScroll = new Rectangle2D.Float(xPosBarraMain, yPosBarraMain - (TILE_SIZE/4), widthSubBarraMain, heightSubBarraMain);

        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(xPosBarraMain, yPosBarraMain - (TILE_SIZE/4), widthBarraMain, heightBarraMain);

        // Subbarra para scrollear:
        Graphics2D g2d = (Graphics2D) g;
        g.setColor(new Color(0, 255, 0, 120));

        g2d.fill(rectScroll);
        
        // Texto para determinar que es BGM (background music):
        g.setColor(Color.BLACK);
        g.drawString("BGM: ", xPosOptions, yPosBarraMain);
    }*/

    public void drawMensajeSeHaCambiadoRes(Graphics g) {
        if (mostrarMensajeSeHaCambiadoRes) {
            g.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, TILE_SIZE / 3));
            g.setColor(Color.GREEN);
            String textoMensaje = "Se ha cambiado la resolucion a " + resolucionesDisponibles[game.pp.resolution_index] + ", reinicia el juego para que se aplique.";
            g.drawString(textoMensaje, xPos + (width / 2) - (g.getFontMetrics().stringWidth(textoMensaje) / 2), TILE_HEIGHT - (TILE_SIZE / 2));
        }
    }

    public void drawResOption(Graphics g, int sepEnY) {
        int separacion = TILE_SIZE;
        String palomita = "Λ";

        g.setColor(Color.BLACK);

        if (resOptionSelected) {
            palomita = "V";
        }

        resOptionHitbox = new Rectangle(xPosOptions, yPosOptions + separacionEnYOpciones - (TILE_SIZE) + (TILE_SIZE / 3), g.getFontMetrics().stringWidth(resOption)+(TILE_SIZE), TILE_SIZE);
        g.drawString(resOption + "   " + palomita, xPosOptions, yPosOptions + sepEnY);

        if (resOptionSelected) {
            drawResolutionSubMenu(g, sepEnY, separacion);
        }
    }

    public void drawResolutionSubMenu(Graphics g, int sepEnY, int separacion) {
        g.setColor(new Color(0, 0, 0, 120));
        g.fillRect(xPosOptions, yPosOptions + sepEnY + (TILE_SIZE / 2), g.getFontMetrics().stringWidth(resOption), resolucionesDisponibles.length * TILE_SIZE);

        for (int i = 0; i < resolucionesDisponibles.length; i++) {
            hitboxesResDisp[i] = new Rectangle(xPosOptions, yPosOptions + sepEnY + separacion - (TILE_SIZE / 2), g.getFontMetrics().stringWidth(resOption), TILE_SIZE);
            //g.setColor(Color.RED);
            //g.drawRect(hitboxesResDisp[i].x, hitboxesResDisp[i].y, hitboxesResDisp[i].width, hitboxesResDisp[i].height);

            if (resOptionSelecc == i) {
                //g.setColor(Color.BLACK);
                //g.drawRect(xPosOptions, yPosOptions + sepEnY + separacion - (TILE_SIZE / 2), g.getFontMetrics().stringWidth(resOption), TILE_SIZE);

                g.setColor(new Color(255, 205, 0, 150));
                g.fillRect(xPosOptions, yPosOptions + sepEnY + separacion - (TILE_SIZE / 2), g.getFontMetrics().stringWidth(resOption), TILE_SIZE);
            }

            g.setColor(new Color(255, 255, 255, 180));
            g.drawString(resolucionesDisponibles[i], xPosOptions + (g.getFontMetrics().stringWidth(resOption) / 2) - (g.getFontMetrics().stringWidth(resolucionesDisponibles[i]) / 2), yPosOptions + separacionEnYOpciones - (TILE_SIZE) + (TILE_SIZE / 3) + separacion + (TILE_SIZE) - (TILE_SIZE / 5));
            separacion += TILE_SIZE;
        }
    }

    public void drawHitboxes(Graphics g, int index, Color color) {
        g.setColor(color);
        g.drawRect(hitboxesOptions[index].x, hitboxesOptions[index].y, hitboxesOptions[index].width, hitboxesOptions[index].height);
    }

    @Override
    public void draw(Graphics g) {
        // dibujamos el cuadrito tu sabes manito:
        drawGrayRectangle(g);

        // dibujamos las diferentes opciones e implementamos
        // las dimensiones de las hitboxes:
        drawOptions(g);
    }

    @Override
    public void MouseClicked(MouseEvent e) {
        for (int i = 0; i < hitboxesOptions.length; i++) {
            if (hitboxesOptions[i].contains(e.getX(), e.getY())) {
                mouseClicked = true;
                indexSaved = i;
                break;
            }
        }

        if (resOptionSelected) {
            for (int i = 0; i < hitboxesResDisp.length; i++) {
                if (hitboxesResDisp[i].contains(e.getX(), e.getY())) {
                    resOptionSelecc = i;
                    break;
                }
            }
        }

        if (resOptionHitbox.contains(e.getX(), e.getY())) {
            if (resOptionSelected) {
                resOptionSelected = false;
                mouseClickedResolution = true;
            } else {
                resOptionSelected = true;
            }
        }
    }

    /*
    public void pulsarSobreBarraVolumen(MouseEvent e) {
        if (hitboxBarraVol.contains(e.getX(), e.getY())) {
            localizaciónPuntero = e.getX();
            if(!sobreBarra){
                sobreBarra = true;
            }
        }
    }*/
    
    public void mousePressed(MouseEvent e){
        volumeScrollBGM.mousePressed(e);
        volumeScrollSFX.mousePressed(e);
    }

    public void MouseReleased(MouseEvent e) {
        volumeScrollBGM.MouseReleased(e);
        volumeScrollSFX.MouseReleased(e);
    }

    public void mouseDragged(MouseEvent e) {
        volumeScrollBGM.mouseDragged(e);
        volumeScrollSFX.mouseDragged(e);
    }
}
