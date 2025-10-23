package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import javax.sound.sampled.FloatControl;
import game.MainGame;
import static game.MainGame.TILE_SIZE;

public class VolumeScroller {
    // scroll para bajar o subir el sonido:
    int widthBarraMain = (TILE_SIZE * 6), heightBarraMain = (TILE_SIZE / 4);
    float widthSubBarraMain, heightSubBarraMain = heightBarraMain;
    int xPosBarraMain, yPosBarraMain;
    Rectangle hitboxBarraVol;
    boolean sobreBarra;
    int localizaciónPuntero;
    MenuFPS mfps;
    String textoTipoVolumen = "";
    public int canal = 0;
    MainGame game;

    public VolumeScroller(MenuFPS mfps, String textoTipoVolumen, int canal, MainGame game) {
        this.mfps = mfps;
        this.textoTipoVolumen = textoTipoVolumen;
        this.canal = canal;
        this.game = game;
        
        widthSubBarraMain = equivalenciaDecibeliosLongitud(game.volumen[canal]);
    }
    
    public float equivalenciaDecibeliosLongitud(float decibelios){
        return (decibelios+60.0f)*(widthBarraMain/60.0f);
    }
    
    public float equivalencialongitudDecibelios(float size){
        return ((size*60.0f)/widthBarraMain)-60.0f;
    }
    
    public void update(){
        updateBarraVolumen();
    }
    
    public void updateBarraVolumen() {
        if (sobreBarra) {
            float diferencia = ((xPosBarraMain + widthSubBarraMain) - localizaciónPuntero);
            widthSubBarraMain -= (diferencia-1);
            updateSoundVolume();
        }
    }
    
    public void updateSoundVolume(){
        game.volumen[canal] = equivalencialongitudDecibelios(widthSubBarraMain);
        if(canal == 0){
            game.actualizarVolumen(canal, game.main_songs[0]);
        }
    }
    
    public void draw(Graphics g){
        drawScrollMainSong(g);
    }
    
    public void drawScrollMainSong(Graphics g) {
        // Barra:
        xPosBarraMain = mfps.xPosOptions + (TILE_SIZE*2);
        yPosBarraMain = mfps.yPosOptions + mfps.separacionEnYOpciones;
        hitboxBarraVol = new Rectangle(xPosBarraMain, yPosBarraMain - (TILE_SIZE/4), widthBarraMain, heightBarraMain);
        Rectangle2D.Float rectScroll = new Rectangle2D.Float(xPosBarraMain, yPosBarraMain - (TILE_SIZE/4), widthSubBarraMain, heightSubBarraMain);

        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(xPosBarraMain, yPosBarraMain - (TILE_SIZE/4), widthBarraMain, heightBarraMain);

        // Subbarra para scrollear:
        Graphics2D g2d = (Graphics2D) g;
        g.setColor(new Color(0, 255, 0, 120));

        g2d.fill(rectScroll);
        
        // Texto para determinar que es:
        g.setColor(Color.BLACK);
        g.drawString(textoTipoVolumen, mfps.xPosOptions, yPosBarraMain);
    }
    
    public void mousePressed(MouseEvent e){
        pulsarSobreBarraVolumen(e);
    }

    public void MouseReleased(MouseEvent e) {
        if (sobreBarra) {
            sobreBarra = false;
        }
    }

    public void mouseDragged(MouseEvent e) {
        pulsarSobreBarraVolumen(e);
    }
    
    public void pulsarSobreBarraVolumen(MouseEvent e) {
        if (hitboxBarraVol != null && hitboxBarraVol.contains(e.getX(), e.getY())) {
            localizaciónPuntero = e.getX();
            if(!sobreBarra){
                sobreBarra = true;
            }
        }
    }
    
}
