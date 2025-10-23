package utilz;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.imageio.ImageIO;
import levels.DamageTile;
import levels.LevelManager;
import levels.Tile;
import static game.MainGame.MAP_COLS;
import static game.MainGame.MAP_ROWS;
import static game.MainGame.TILE_SIZE;

public class LoadSave {
    
    // AQUI SE PONDRÁN TODAS LAS RUTAS PARA IMPORTAR IMAGENES:
    public static final String LEVEL_ONE_SCHEMA = "/res/tileset_part_1.png";
    public static final String LEVEL_ONE_DATA = "/res/level_one.txt";
    public static final String CHECKPOINT_SCHEMA = "/res/cps.png";
    public static final String DEATH_SCREEN_BG = "/res/death_screen_bg.png";
    public static final String DEATH_ANIM_SCHEMA = "/res/death_anim.png";
    public static final String SERLLIBER_ENEMY_SCHEMA = "/res/serlliber.png";
    public static final String LEVEL_ONE_BG = "/res/background.png";
    public static final String LEVEL_ONE_BOSS_BG = "/res/bgboss.png";
    public static final String LEVEL_ONE_BG_SP = "/res/background_sec.jpg";
    public static final String BOX_SCHEMA = "/res/box.png";
    public static final String BUTTON_SCHEMA = "/res/button.png";
    public static final String CANION_SCHEMA = "/res/cannon.png";
    public static final String CANION_REVERSED_SCHEMA = "/res/cannon_reversed.png";
    public static final String BALA_SCHEMA = "/res/bala.png";
    public static final String BALA_REVERSED_SCHEMA = "/res/bala_reversed.png";
    public static final String JOKER_NPC_SCHEMA = "/res/joker_npc.png";
    public static final String BOMB_SCHEMA = "/res/bomba.png";
    public static final String EXPLOSION_SCHEMA = "/res/explosion_spritesheet.png";
    public static final String BOSS_SCHEMA = "/res/srpelo_boss.png";
    public static final String APPLE_SCHEMA = "/res/apple.png";
    public static final String COHETE_SCHEMA = "/res/cohete.png";
    public static final String RICKY_SCHEMA = "/res/ricky.png";
    public static final String TELEPORT_SCHEMA = "/res/portal.png";
    public static final String NEGRO = "/res/81.png";
    public static final String PROJECTIL_AZUL = "/res/332.png";
    public static final String NPC_1_SCHEMA = "/res/npc_1_anim.png";
    public static final String NPC_2_SCHEMA = "/res/dancing_guy_schema.png";
    public static final String NPC_3_SCHEMA = "/res/what.png";
    public static final String NPC_3_SCHEMA_TALK = "/res/god.png";
    public static final String NPC_ETIQUETA_SCHEMA = "/res/npc_etiqueta.png";
    public static final String BRODYQUEST_SCHEMA = "/res/brodyquest.png";
    public static final String CARTEL_BOSS_SCHEMA = "/res/cartel_boss.png";
    public static final String COCHE_SRPELO_SCHEMA = "/res/srpelo_carro.png";
    public static final String BOSS_BG_2 = "/res/BG_BOSS_2.png";
    public static final String FINAL_BG = "/res/final_bg.png";
    public static final String SUBFINAL_BG = "/res/subbg_final.png";
    public static final String LAST_JUMP = "/res/last_jump.png";
    public static final String ICON = "/res/iwgth_icon.png";
    public static final String TUTORIAL_1 = "/res/tutorial_part1.png";
    public static final String TUTORIAL_2 = "/res/tutorial_part2.png";
    
    
    // UI -> MAIN MENU:
    public static final String BUTTON_START = "/res/start_button.png";
    public static final String BUTTON_OPTIONS = "/res/options_button.png";
    public static final String BG_MENU = "/res/menu_background.jpeg";
    public static final String TITLE_MENU = "/res/title_menu.png";
    public static final String ARROW_SCHEMA = "/res/arrow_menu.png";
    public static final String INSIGNIAS = "/res/insignias.png";
    
    // UI -> OPTIONS MENU:
    public static final String BG_OPTIONS = "/res/options_menu_bg.jpg";
    public static final String TITLE_OPTIONS = "/res/options_title.png";
    public static final String BUTTON_CAMBIAR_CONTROLES = "/res/cambiar_controles_button.png";
    public static final String BUTTON_CAMBIAR_SKIN = "/res/cambiar_skin_button.png";
    public static final String BUTTON_CONFIGURAR_FPS = "/res/configurar_fps_button.png";
    public static final String BUTTON_VOLVER = "/res/volver_button.png";
    public static final String MARCO_OPTIONS_POP_UPS = "/res/marco_options.png";
    public static final String OPTION_SELECTION = "/res/option_selection.png";
    public static final String PLAYER_1_PORTADA = "/res/player_1_portada.png";
    public static final String PLAYER_2_PORTADA = "/res/player_2_portada.png";
    
    
    public static BufferedImage importImg(String route) {
        InputStream is = LoadSave.class.getResourceAsStream(route);
        BufferedImage img = null;

        try {
            img = ImageIO.read(is);

            is.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return img;
    }
    
    
    /////////////////////////// PARA HACERLO MÁS RÁPIDO ///////////////////////////
    public static BufferedImage[] subdividirImagen(BufferedImage img, int subdivisions){
        BufferedImage[] anim = new BufferedImage[subdivisions];
        
        for(int i = 0; i < anim.length; i++){
            anim[i] = img.getSubimage(i*(img.getWidth()/subdivisions), 0, img.getWidth()/subdivisions, img.getHeight());
        }
        
        return anim;
    }
    
    public static BufferedImage flipImageHorizontally(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();
        
        BufferedImage flippedImage = new BufferedImage(width, height, img.getType());
        
        Graphics2D g = flippedImage.createGraphics();
        AffineTransform tran = AffineTransform.getTranslateInstance(width, 0);
        AffineTransform flip = AffineTransform.getScaleInstance(-1d, 1d);
        tran.concatenate(flip);
        g.drawImage(img, tran, null);
        g.dispose();
        return flippedImage;
    }
    
    public static int conversor(char letra){
        int numero = (letra - 97)+10;
        return numero;
    }
    

    public static Tile[][] importMapForTXT(String route, LevelManager lm) {

        Tile[][] map = new Tile[MAP_ROWS][MAP_COLS];

        try {
            InputStream is = LoadSave.class.getResourceAsStream(route);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String linea = "";
            int index = 0;

            while (index < MAP_ROWS) {

                linea = br.readLine();

                // Para crear los tiles que quedan en el caso de que
                // en el nivel dentro del txt tenga menos filas
                // que las que debería:
                if (linea == null) {
                    linea = "";

                    for (int i = 0; i < MAP_COLS; i++) {
                        // AQUI VOY A MANDAR QUE SE PONGA EL PRIMERO DENTRO DEL INICIO
                        // EN NO COLISIÓN, EN DISTINTOS SPRITESHEETS YA SABES:
                        // (corresponde al sprite "vacio")
                        String spriteVacioIndex = Constants.LevelConstants.INICIO_NO_COLISION_LVL1 + " ";
                        linea += spriteVacioIndex;
                    }
                    
                    linea = linea.substring(0, linea.length()-2);
                }

                String[] values = linea.split(" ");

                for (int i = 0; i < MAP_COLS; i++) {

                    if (i >= values.length) {
                        // una vez mas... doy por hecho que el indice de inicio de no colision es el "vacio":
                        int spriteVacioIndex = Constants.LevelConstants.INICIO_NO_COLISION_LVL1;
                        map[index][i] = new Tile(spriteVacioIndex, i * TILE_SIZE, index * TILE_SIZE, lm.getSpriteArray()[spriteVacioIndex]);
                    } else {
                        int value = 0;
                        
                        if(!Character.isDigit(values[i].charAt(0))){
                            value = conversor(values[i].charAt(0));
                        }else{
                            value = Integer.parseInt(values[i]);
                        }
                        
                        if(value > Constants.LevelConstants.FINAL_NO_COLISION_LVL1){
                            Rectangle hitbox = new Rectangle(TILE_SIZE/2, TILE_SIZE/2);
                            map[index][i] = new DamageTile(value, i * TILE_SIZE, index * TILE_SIZE, lm.getSpriteArray()[value], hitbox);
                        }else if(value <= Constants.LevelConstants.FINAL_NO_COLISION_LVL1){
                            map[index][i] = new Tile(value, i * TILE_SIZE, index * TILE_SIZE, lm.getSpriteArray()[value]);   
                        }
                    }
                }

                index++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return map;
    }
}
