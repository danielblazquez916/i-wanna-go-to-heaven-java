package entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import utilz.EnemyMovements;
import utilz.LoadSave;
import game.MainGame;
import static game.MainGame.TILE_SIZE;

public class Cannon extends Enemy {
    
    Projectile[] balasPooling;
    boolean seHaActivadoUnProyectil;
    int tickCounter;
    int timeout = 60;
    
    public Cannon(int x, int y, Rectangle hitbox, int xHitboxOffset, int yHitboxOffset, MainGame game, BufferedImage fullImage, int dirBala) {
        super(x, y, hitbox, xHitboxOffset, yHitboxOffset, game, fullImage);
        setBalasPooling(5, dirBala);
    }
    
    public void setBalasPooling(int cantidadDeBalas, int dirBala){
        balasPooling = new Projectile[cantidadDeBalas];
        
        for(int i = 0; i < balasPooling.length; i++){
            Rectangle hitboxBala = new Rectangle(TILE_SIZE-(int)MainGame.valorRelativo(10), TILE_SIZE-(int)MainGame.valorRelativo(25));
            BufferedImage balaSchema = null;
            if(dirBala == 1){
                balaSchema = LoadSave.importImg(LoadSave.BALA_SCHEMA);
            }else if(dirBala == 0){
                balaSchema = LoadSave.importImg(LoadSave.BALA_REVERSED_SCHEMA);
            }
            
            balasPooling[i] = new Projectile(dirBala, this.x, this.y, hitboxBala, (int)MainGame.valorRelativo(5), (int)MainGame.valorRelativo(12), game, balaSchema, this.x, this.y, TILE_SIZE, TILE_SIZE);
        }
    }
    
    public void draw(Graphics g){
        //this.drawHitbox(g);
        g.drawImage(fullImage, x - (game.playingState.lm.offsetMapX * TILE_SIZE), y - (game.playingState.lm.offsetMapY * TILE_SIZE), TILE_SIZE, TILE_SIZE, null);
        
        for(int i = 0; i < balasPooling.length; i++){
            if(balasPooling[i].isActivo()){
                balasPooling[i].draw(g);
            }
        }
    }
    
    public void resetBalls(){
        // first thing
        seHaActivadoUnProyectil = false;
        tickCounter = 0;
        
        // second thing
        for(int i = 0; i < balasPooling.length; i++){
            balasPooling[i].setActivo(false);

            balasPooling[i].x = this.x;
            balasPooling[i].y = this.y;
            balasPooling[i].updateHitbox();
        }
    }
    
    public void update(){
        
        EnemyMovements.goingUpAndDown(this, game);
        this.updateHitbox();
        
        ///////////////////// CONTROL DE BALAS /////////////////////
       // |   |   |   |   |   |   |   |   |   |   |   |   |   |   | //
       // V   V   V   V   V   V   V   V   V   V   V   V   V   V   V //
        
        // 5 SEGUNDOS SON 60TPS X 5 ---> 120
        
        if(seHaActivadoUnProyectil){
            tickCounter++;
            if(tickCounter >= timeout){
                seHaActivadoUnProyectil = false;
                tickCounter = 0;
            }
        }
        
        // primero añado la bala que quiere disparar el cañon al almacen:
        for(int i = 0; i < balasPooling.length; i++){
            if(!balasPooling[i].isActivo() && !seHaActivadoUnProyectil){
                balasPooling[i].setActivo(true);
                seHaActivadoUnProyectil = true;
                
                // settear la posición para seguir la posición del
                // cañon (se esta moviendo) y empezar justo en su origen:
                balasPooling[i].setX(this.x);
                balasPooling[i].setY(this.y);
            }
            
            if(balasPooling[i].isActivo()){
                balasPooling[i].update();
            }
        }
        
        // despues, busco en el almacen los updates de las balas siempre que esten activas:
        // en el caso de que no estar activas (se han destruido), lo remuevo:
        //....
    }
   
}
