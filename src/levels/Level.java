package levels;

import entities.Player;
import static game.MainGame.TILE_COLS;
import static game.MainGame.TILE_ROWS;
import static game.MainGame.TILE_SIZE;

public class Level {

    Tile[][] lvlData;
    Checkpoint[] checkpoints;
    int section = 0;
    
    // el 0 cuenta, es decir; 0 1 2
    int maxNumberOfSectionsPerRow = 2;

    public Level(Tile[][] lvlData, Checkpoint[] checkpoints) {
        this.lvlData = lvlData;
        this.checkpoints = checkpoints;
    }

    public int getSection() {
        return section;
    }

    public int setSection(Player player) {
        int xSection = (int) (Math.floor((double) player.hitbox.x / TILE_SIZE) / TILE_COLS);
        int ySection = (int) (Math.floor((double) player.hitbox.y / TILE_SIZE) / TILE_ROWS);

        // AJUSTAR PARA QUE AL BAJAR EN Y SE SUME UNA SECCION, PERO, AL AVANZAR HACIA 0 HABIENDO
        // BAJADO EN Y, QUE SUME TAMBIEN:
        if(ySection > 0){
            if(ySection%2 != 0){
                xSection = ((Math.abs(xSection-maxNumberOfSectionsPerRow))+3);
            }
            
            // ULTIMA SALA:
            if(ySection == 2){
                xSection = 99;
            }
        }
        
        section = xSection;
        return section;
    }

    public Checkpoint[] getCheckpoints() {
        return checkpoints;
    }

    public int getIndexFromLVLData(int i, int j) {
        return lvlData[i][j].getIndex();
    }

    public Tile[][] getLvlData() {
        return lvlData;
    }
}
