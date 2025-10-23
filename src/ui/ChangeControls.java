package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import utilz.LoadSave;
import utilz.PlayerPrefs;
import game.MainGame;
import static game.MainGame.TILE_HEIGHT;
import static game.MainGame.TILE_SIZE;
import static game.MainGame.TILE_WIDTH;

public class ChangeControls {

    // un popup va a ser un menu que se abra por encima de la opcion
    // que seleccionemos.
    BufferedImage popUpBG;
    int xPos, yPos;
    int height, width;

    String opcionSeleccionada = "";
    boolean noSeHaPodidoCambiarElControl;
    int tick;
    MainGame game;
    Font fontTextoPequeño;

    String[] controlesParaCambiar;
    Rectangle[] controlesHitboxes;
    int xPosControles, yPosControles;
    int separacionEnY = TILE_SIZE * 2; // sera 100

    public ChangeControls(int xPos, int yPos, int height, int width, MainGame game) {
        this.game = game;
        this.xPos = xPos;
        this.yPos = yPos;
        this.height = height;
        this.width = width;
        fontTextoPequeño = new Font(Font.DIALOG_INPUT, Font.BOLD, TILE_SIZE / 3);

        popUpBG = LoadSave.importImg(LoadSave.MARCO_OPTIONS_POP_UPS);

        instanciarControles();
    }

    public void setOpcionSeleccionada(String opcionSeleccionada) {
        this.opcionSeleccionada = opcionSeleccionada;
    }

    public void setNoSeHaPodidoCambiarElControl(boolean noSeHaPodidoCambiarElControl) {
        this.noSeHaPodidoCambiarElControl = noSeHaPodidoCambiarElControl;
    }

    public void setTick(int tick) {
        this.tick = tick;
    }

    public void instanciarControles() {

        separacionEnY = 0;

        controlesParaCambiar = new String[3];
        controlesHitboxes = new Rectangle[controlesParaCambiar.length];

        controlesParaCambiar[0] = "Izquierda : " + KeyEvent.getKeyText(game.pp.controlIzquierda);

        controlesParaCambiar[1] = "Derecha : " + KeyEvent.getKeyText(game.pp.controlDerecha);

        controlesParaCambiar[2] = "Saltar : " + KeyEvent.getKeyText(game.pp.controlSalto);

        for (int i = 0; i < controlesParaCambiar.length; i++) {
            controlesHitboxes[i] = new Rectangle(xPosControles, yPosControles + separacionEnY, TILE_SIZE * 3, TILE_SIZE);
            separacionEnY += (TILE_SIZE * 2);
        }

        separacionEnY = (TILE_SIZE * 2);
    }

    public void resetValues() {
        setOpcionSeleccionada("");
        setTick(0);
        setNoSeHaPodidoCambiarElControl(false);
    }

    public void update() {
        if (noSeHaPodidoCambiarElControl) {
            tick++;
            if (tick >= 120) {
                tick = 0;
                noSeHaPodidoCambiarElControl = false;
            }
        }
    }

    public void drawGrayRectangle(Graphics g) {
        g.setColor(new Color(0, 0, 0, 120));
        g.fillRect(0, 0, TILE_WIDTH, TILE_HEIGHT);

        // Dibujar el cuadro de controles:
        g.drawImage(popUpBG, xPos, yPos, width, height, null);

        // el textito de abajo de lo de Enter:
        g.setColor(Color.GRAY);
        g.setFont(new Font(Font.DIALOG, Font.BOLD, TILE_SIZE / 3));
        g.drawString("Pulsa la tecla [Enter] para volver.", xPos + width, yPos + height - (TILE_SIZE * 2));

        g.setColor(Color.GRAY);
        g.setFont(new Font(Font.DIALOG, Font.BOLD, TILE_SIZE / 3));
        g.drawString("Usa el ratón para seleccionar.", xPos + width, yPos + height - (TILE_SIZE));
    }

    public void draw(Graphics g) {

        separacionEnY = 0;

        drawGrayRectangle(g);

        // dibujar los diferentes controles:
        g.setFont(new Font("Segoe UI", Font.BOLD, TILE_SIZE / 2));
        xPosControles = xPos + (width / 2) - (g.getFontMetrics().stringWidth("Izquierda : A") / 2);
        yPosControles = yPos + (TILE_SIZE * 2) + separacionEnY;

        for (int i = 0; i < controlesParaCambiar.length; i++) {
            g.setColor(Color.BLACK);
            g.drawString(controlesParaCambiar[i], xPosControles, yPosControles + separacionEnY);
            controlesHitboxes[i] = new Rectangle(xPosControles, yPosControles + separacionEnY - (TILE_SIZE / 2), TILE_SIZE * 3, TILE_SIZE);
            //g.setColor(Color.BLUE);
            //g.drawRect(controlesHitboxes[i].x, controlesHitboxes[i].y, controlesHitboxes[i].width, controlesHitboxes[i].height);
            separacionEnY += (TILE_SIZE * 2);
        }

        if (!opcionSeleccionada.isBlank()) {
            g.setFont(fontTextoPequeño);
            g.setColor(Color.WHITE);

            g.drawString("Pulsa una tecla para cambiar [" + opcionSeleccionada + "]", xPos + (width / 2) - (g.getFontMetrics().stringWidth("Pulsa una tecla para cambiar [" + opcionSeleccionada + "]") / 2), TILE_HEIGHT - (TILE_SIZE)+(TILE_SIZE/2));
        }

        if (noSeHaPodidoCambiarElControl) {
            drawNoSeHaPodidoCambiarElControl(g);
        }
    }

    public void drawNoSeHaPodidoCambiarElControl(Graphics g) {
        g.setFont(fontTextoPequeño);
        g.setColor(Color.RED);
        g.drawString("Ya hay un control asignado con esa tecla!", xPos + (width / 2) - (g.getFontMetrics().stringWidth("Ya hay un control asignado con esa tecla!") / 2), TILE_HEIGHT - (TILE_SIZE * 2)+(TILE_SIZE/2));
    }

    public void MouseClicked(MouseEvent e) {
        for (int i = 0; i < controlesHitboxes.length; i++) {
            if (controlesHitboxes[i].contains(e.getX(), e.getY())) {
                opcionSeleccionada = controlesParaCambiar[i].substring(0, controlesParaCambiar[i].indexOf(":") - 1);
                break;
            }
        }
    }

    public void KeyPressed(KeyEvent e) {
        if (!opcionSeleccionada.isBlank()) {

            int index = -1;
            int keyCode = e.getKeyCode();

            if (keyCode == KeyEvent.VK_ENTER) {
                noSeHaPodidoCambiarElControl = true;
                return;
            }

            if (keyCode == KeyEvent.VK_E || keyCode == KeyEvent.VK_R) {
                tick = 0;
                noSeHaPodidoCambiarElControl = true;
                return;
            }

            switch (opcionSeleccionada) {
                case "Izquierda": {
                    if (keyCode == game.pp.controlDerecha
                            || keyCode == game.pp.controlSalto) {
                        tick = 0;
                        noSeHaPodidoCambiarElControl = true;
                        return;
                    }

                    game.pp.controlIzquierda = keyCode;
                    noSeHaPodidoCambiarElControl = false;
                    tick = 0;
                    index = 0;
                    break;
                }

                case "Derecha": {
                    if (keyCode == game.pp.controlIzquierda
                            || keyCode == game.pp.controlSalto) {
                        tick = 0;
                        noSeHaPodidoCambiarElControl = true;
                        return;
                    }

                    game.pp.controlDerecha = keyCode;
                    noSeHaPodidoCambiarElControl = false;
                    tick = 0;
                    index = 1;
                    break;
                }

                case "Saltar": {
                    if (keyCode == game.pp.controlIzquierda
                            || keyCode == game.pp.controlDerecha) {
                        tick = 0;
                        noSeHaPodidoCambiarElControl = true;
                        return;
                    }

                    game.pp.controlSalto = keyCode;
                    noSeHaPodidoCambiarElControl = false;
                    tick = 0;
                    index = 2;
                    break;
                }
            }

            controlesParaCambiar[index] = controlesParaCambiar[index].substring(0, controlesParaCambiar[index].indexOf(":") + 2) + KeyEvent.getKeyText(keyCode);
            opcionSeleccionada = "";
        }
    }
}
