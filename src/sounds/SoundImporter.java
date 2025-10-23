package sounds;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundImporter {
    // hacer una función que a partir de una ruta con
    // un sonido lo importe y pueda usarlo:
    public final static String[] MAIN_SONGS = new String[]{"menu_song", "brody", "moonsong", "pre_boss_music", "boss_music"};
    public final static String[] EFFECTS = new String[]{"jump", "jump2", "died_effect", "explosion", "joker_grito"};
    
    public static Clip soundImporter(String name){
        URL url = SoundImporter.class.getResource("/sound/" + name + ".wav");
        Clip clip = null;
        try {
            AudioInputStream audio = AudioSystem.getAudioInputStream(url);
            clip = AudioSystem.getClip();
            clip.open(audio);
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException ex) {
            Logger.getLogger(SoundImporter.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return clip;
    }
    
    public static void playSound(Clip song){
        if(!song.isRunning()){
            song.setMicrosecondPosition(0);
            song.start();
        }
    }
    
    public static void stopSound(Clip song){
        if(song.isRunning()){
            song.stop();
        }
    }
}
