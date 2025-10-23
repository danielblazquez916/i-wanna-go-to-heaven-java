package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;
import static game.MainGame.TILE_WIDTH;
import static game.MainGame.TILE_HEIGHT;

public class GamePanel extends JPanel {

    Inputs inp;
    InputsMouse minp;
    MainGame game;

    public GamePanel(MainGame game) {

        inp = new Inputs(this);
        minp = new InputsMouse(this);
        this.game = game;

        // CONFIGURACION:
        setSize();
        this.setBackground(Color.WHITE);
        this.addKeyListener(inp);
        this.addMouseListener(minp);
        this.addMouseMotionListener(minp);
        this.setFocusable(true);
    }

    // Metodo principal para el tamaño de nuestra ventana:
    public void setSize() {
        this.setPreferredSize(new Dimension(TILE_WIDTH, TILE_HEIGHT));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        game.renderAll(g);
    }
}
