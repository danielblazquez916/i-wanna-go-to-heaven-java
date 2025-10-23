package game;

import gamestates.Gamestate;
import gamestates.Menu;
import gamestates.OptionsMenu;
import gamestates.Playing;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import sounds.SoundImporter;
import transitions.FadeInFadeOut;
import utilz.PlayerPrefs;

public class MainGame implements Runnable {

    // Paneles y clases de juego:
    public GamePanel gp;
    public GameWindow gw;
    public Playing playingState;
    public Menu menuState;
    public OptionsMenu optionsState;
    public FadeInFadeOut fadeTransition;
    public Clip[] main_songs;
    public Clip[] effects;
    WindowListener wl;

    int FPS = 60;
    int UPS = 60;

    int frameCatcher = FPS;

    // HILO PARA LA PARTE UPDATES, CHECKS, EVENTS...
    Thread hilo;

    public PlayerPrefs pp = new PlayerPrefs();

    // Configuración para la colocación de los tiles:
    //public static int INITIAL_TILE_SIZE = 16;
    //public static int SCALE = 3;
    
    public static int TILE_SIZE; // 48 (lo que diga la resolución)
    public static int TILE_COLS = 30;
    public static int TILE_ROWS = 20;
    public static int TILE_WIDTH; // TILE_COLS * TILE_SIZE
    public static int TILE_HEIGHT; // TILE_ROWS * TILE_SIZE
    public static int MAP_ROWS = 80;
    public static int MAP_COLS = 100;
    public static int MAP_WIDTH; // MAP_COLS * TILE_SIZE
    public static int MAP_HEIGHT; // MAP_ROWS * TILE_SIZE
    
    // Volumen: // min -80.0f // max 0.0f
    public float[] volumen = new float[]{0, 0};

    public MainGame() {

        if (!existeSaveData()) {
            serializar(pp);
        }

        pp = deserializar();
        
        volumen[0] = pp.volume[0];
        volumen[1] = pp.volume[1];
        
        settearResolucion();

        menuState = new Menu(this);
        playingState = new Playing(this);
        optionsState = new OptionsMenu(this);

        fadeTransition = new FadeInFadeOut(1, 5, new Color(0, 0, 0));
        mainSongsImporter();
        effectsImporter();

        gp = new GamePanel(this);
        gw = new GameWindow(gp);

        FPS = hzDisplay();

        wl = new WindowListener(this, pp);
        gw.ventana.addWindowListener(wl);

        startThread();
    }
    
    // Esta funcion sirve para colocar una medida y que se ajuste en resoluciones
    // mas pequeñas:
    public static float valorRelativo(float size){
        return (size * TILE_SIZE)/48;
    }
    
    public void settearResolucion(){
        TILE_SIZE = pp.TILE_SIZE_SAVED;
        TILE_WIDTH = TILE_COLS * TILE_SIZE;
        TILE_HEIGHT = TILE_ROWS * TILE_SIZE;
        MAP_WIDTH = MAP_COLS * TILE_SIZE;
        MAP_HEIGHT = MAP_ROWS * TILE_SIZE;
    }

    public void serializar(PlayerPrefs playerprefs) {
        try {
            FileOutputStream fos = new FileOutputStream("save_data.ser");

            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(playerprefs);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public boolean existeSaveData() {
        File save = new File("save_data.ser");
        if (save.exists()) {
            return true;
        }

        return false;
    }

    public PlayerPrefs deserializar() {
        PlayerPrefs pprefs = null;

        try {
            FileInputStream fis = new FileInputStream("save_data.ser");

            ObjectInputStream ois = new ObjectInputStream(fis);

            pprefs = (PlayerPrefs) ois.readObject();

        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }

        return pprefs;
    }
    
    public void mainSongsVolumeUpdate(){
        for (int i = 0; i < SoundImporter.MAIN_SONGS.length; i++) {
            FloatControl controlVolumen = (FloatControl) main_songs[i].getControl(FloatControl.Type.MASTER_GAIN);
            controlVolumen.setValue(volumen[0]);
            System.out.println(volumen[0]);
        }
    }
    
    public void effectsVolumeUpdate(){
        for (int i = 0; i < SoundImporter.EFFECTS.length; i++) {
            FloatControl controlVolumen = (FloatControl) effects[i].getControl(FloatControl.Type.MASTER_GAIN);
            controlVolumen.setValue(volumen[1]);
        }
    }

    public void mainSongsImporter() {
        main_songs = new Clip[SoundImporter.MAIN_SONGS.length];

        for (int i = 0; i < SoundImporter.MAIN_SONGS.length; i++) {

            main_songs[i] = SoundImporter.soundImporter(SoundImporter.MAIN_SONGS[i]);
            FloatControl controlVolumen = (FloatControl) main_songs[i].getControl(FloatControl.Type.MASTER_GAIN);
            controlVolumen.setValue(volumen[0]);
        }
    }

    public void effectsImporter() {
        effects = new Clip[SoundImporter.EFFECTS.length];

        for (int i = 0; i < SoundImporter.EFFECTS.length; i++) {
            effects[i] = SoundImporter.soundImporter(SoundImporter.EFFECTS[i]);
            FloatControl controlVolumen = (FloatControl) effects[i].getControl(FloatControl.Type.MASTER_GAIN);
            controlVolumen.setValue(volumen[1]);
        }
    }

    public int hzDisplay() {
        GraphicsConfiguration gc = gw.ventana.getGraphicsConfiguration();

        GraphicsDevice gd = gc.getDevice();

        return gd.getDisplayMode().getRefreshRate();
    }

    public void startThread() {
        hilo = new Thread(this);
        hilo.start();
    }

    public void updateAll() {

        switch (Gamestate.currentState) {
            case Gamestate.MENU_STATE: {
                menuState.update();
                mainSongUpdate(main_songs[0]);

                break;
            }

            case Gamestate.LEVEL_PLAYING_STATE: {
                playingState.update();

                if (playingState.lm.getCurrentLevel().getSection() <= 1) {
                    mainSongUpdate(main_songs[1]);
                } else if (playingState.lm.getCurrentLevel().getSection() <= 4) {
                    mainSongUpdate(main_songs[2]);
                } else {
                    mainSongUpdate(main_songs[3]);
                }

                if (playingState.lm.getCurrentLevel().getSection() == 99) {
                    mainSongUpdate(main_songs[3]);
                }

                break;
            }

            case Gamestate.OPTIONS_STATE: {
                optionsState.update();
                break;
            }

            default: {
                break;
            }
        }

        fadeTransition.update();
    }

    public void stopAllActiveMainSongs() {
        for (int i = 0; i < main_songs.length; i++) {
            if (main_songs[i].isRunning()) {
                main_songs[i].stop();
            }
        }
    }

    public void mainSongUpdate(Clip song) {
        if (!song.isRunning()) {
            //actualizarVolumen(0, song);
            stopAllActiveMainSongs();
            SoundImporter.playSound(song);
        }
    }
    
    public void actualizarVolumen(int canal, Clip song){
        FloatControl controlVolumen = (FloatControl) song.getControl(FloatControl.Type.MASTER_GAIN);
        controlVolumen.setValue(volumen[canal]);
    }

    public void effectUpdate(Clip effect) {
        SoundImporter.playSound(effect);
    }

    public void renderAll(Graphics g) {

        switch (Gamestate.currentState) {
            case Gamestate.MENU_STATE: {
                menuState.draw(g);
                break;
            }

            case Gamestate.LEVEL_PLAYING_STATE: {
                playingState.draw(g);
                break;
            }

            case Gamestate.OPTIONS_STATE: {
                optionsState.draw(g);
                break;
            }

            default: {
                break;
            }
        }

        fadeTransition.draw(g);

        // dibujar por encima de cualquier otro draw
        // los fps:
        if (pp.mostrarFPS) {
            showFPSOnScreen(g);
        }
    }

    @Override
    public void run() {

        double timePerFrame = 1000000000 / FPS;
        double timePerUpdate = 1000000000 / UPS;
        double deltaR = 0;
        double deltaU = 0;
        long previousTime = System.nanoTime();
        int updates = 0;
        int frames = 0;
        long timer = 0;

        while (true) {
            long actualTime = System.nanoTime();
            deltaU += (actualTime - previousTime) / timePerUpdate;
            deltaR += (actualTime - previousTime) / timePerFrame;

            if (deltaU >= 1) {
                updateAll();

                updates++;
                deltaU--;
            }

            if (pp.FPSLimit) {
                gp.repaint();
                frames++;
            } else {
                if (deltaR >= 1) {
                    gp.repaint();

                    frames++;
                    deltaR--;
                }
            }

            previousTime = actualTime;

            if (System.currentTimeMillis() - timer >= 1000) {
                timer = System.currentTimeMillis();
                frameCatcher = frames;
                System.out.println("TPS: " + updates + " | " + "FPS: " + frames);
                updates = 0;
                frames = 0;
            }
        }
    }

    public void showFPSOnScreen(Graphics g) {
        g.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, (int)valorRelativo(20)));
        g.setColor(Color.WHITE);
        g.drawString("FPS: " + String.valueOf(frameCatcher), (int)valorRelativo(20), (int)valorRelativo(60));
    }

}
