package entities;

import gamestates.Playing;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import sounds.SoundImporter;
import transitions.FadeInFadeOut;
import utilz.LoadSave;
import game.MainGame;
import static game.MainGame.TILE_SIZE;
import static game.MainGame.TILE_WIDTH;

public class Boss extends Entity {

    // Imagenes y animacion:
    BufferedImage bossImage;
    BufferedImage[] bossAnim;
    int indexAnim, tickAnim, animSpeed = 10;

    // Barra de tiempo:
    float widthBarra = MainGame.valorRelativo(480);
    float decrementBarra = (widthBarra / (30*60));
    BufferedImage cartel_barra;

    // Tamaños:
    int bossWidth = (int)MainGame.valorRelativo(400), bossHeight = (int)MainGame.valorRelativo(408);

    // Limites, posicion inicial, direccion...:
    int limitXPositive = 29, limitXNegative = 1;
    int initX, initY;
    public int origenXBala, origenYBala;
    String direction = "right", secuencia = "0";

    // estados del combate y escena inicial:
    boolean startScene;
    boolean endScene;
    boolean isAttacking, startCombat;
    int barraDeTiempoSec = 30, lastTime = barraDeTiempoSec;
    float bossSpeed = 0;
    int tickMov, tickTime, tickAttack, tickWaiting;
    float bossSpeedSave = MainGame.valorRelativo(20);

    // Sistema de disparos:
    Projectile[] numeroDeDisparos;
    Projectile cohete;
    int tickDisparo, direccionBala;
    Projectile fragment;
    int direccionFragment = 3;

    // Coche cabron:
    BufferedImage[] cocheAnim;
    int xPosCoche, yPosCoche;
    public Rectangle hitboxCoche;

    FadeInFadeOut transitionEnd;
    BufferedImage newBossBG;
    BufferedImage lastBossBG;

    // Disparos (deprecated):
    /*BufferedImage bala = LoadSave.importImg(LoadSave.APPLE_SCHEMA);
    int xBala, yBala;
    int heightBala = 50, widthBala = 50;
    int xBalaMov, yBalaMov;
    int balaSpeed = 5;
    int esquinaX, esquinaY;
     */
    MainGame game;
    Playing pl;

    public Boss(int x, int y, BufferedImage image, Rectangle hitbox, MainGame game, Playing pl) {
        super(x, y);

        this.game = game;
        this.pl = pl;
        this.bossImage = image;

        initX = this.x;
        initY = this.y;

        //xBala = this.x - 40;
        //yBala = this.y;
        setHitbox(hitbox);

        instanciarDisparos(2);
        instanciarCohete();
        instanciarFragment();

        cartel_barra = LoadSave.importImg(LoadSave.CARTEL_BOSS_SCHEMA);
        bossAnim = LoadSave.subdividirImagen(bossImage, 6);
        newBossBG = LoadSave.importImg(LoadSave.BOSS_BG_2);

        instanciarSrPeloCoche();
        transitionEnd = new FadeInFadeOut(1, 5, new Color(255, 255, 255));
        transitionEnd.setFinishTransition(false);
        
        lastBossBG = this.pl.lm.BGBoss;

        followHitbox();
    }

    public void instanciarSrPeloCoche() {
        cocheAnim = LoadSave.subdividirImagen(LoadSave.importImg(LoadSave.COCHE_SRPELO_SCHEMA), 2);
        xPosCoche = ((66 / 2) - 1) * TILE_SIZE;
        yPosCoche = (38 - 1) * TILE_SIZE;
        hitboxCoche = new Rectangle((int)MainGame.valorRelativo(100), (int)MainGame.valorRelativo(100));
    }

    /*
    public void trocearImagen(int rows, int cols){
        bossAnim = new BufferedImage[rows][cols];
        for(int i = 0; i < bossAnim.length; i++){
            for(int j = 0; j < bossAnim[i].length; j++){
                bossAnim[i][j] = bossImage.getSubimage(j*(bossImage.getWidth()/cols), i*(bossImage.getHeight()/rows), bossImage.getWidth()/cols, bossImage.getHeight()/rows);
            }
        }
    }*/
    public boolean isEndScene() {
        return endScene;
    }

    public void instanciarFragment() {
        Rectangle fragmentHitboxVacia = new Rectangle(0, 0);
        fragment = new Projectile(3, cohete.x, cohete.y, fragmentHitboxVacia, 0, 0, game, LoadSave.importImg(LoadSave.RICKY_SCHEMA), cohete.x, cohete.y, TILE_SIZE, TILE_SIZE);
        fragment.projectileSpeed = (int)MainGame.valorRelativo(20);
    }

    public void instanciarCohete() {
        Rectangle coheteHitboxVacia = new Rectangle(0, 0);
        cohete = new Projectile(2, this.x, this.y, coheteHitboxVacia, 0, 0, game, LoadSave.importImg(LoadSave.COHETE_SCHEMA), this.x, this.y, (int)MainGame.valorRelativo(50), (int)MainGame.valorRelativo(150));
        cohete.setProjectileSpeed((int)MainGame.valorRelativo(20));
    }

    public void instanciarDisparos(int nBalasALaVez) {
        this.numeroDeDisparos = new Projectile[nBalasALaVez];

        for (int i = 0; i < numeroDeDisparos.length; i++) {
            Rectangle myHitbox = new Rectangle(TILE_SIZE, TILE_SIZE);
            numeroDeDisparos[i] = new Projectile(0, this.x, this.y, myHitbox, 0, 0, game, LoadSave.importImg(LoadSave.APPLE_SCHEMA), this.x, this.y, TILE_SIZE, TILE_SIZE);
            numeroDeDisparos[i].setProjectileSpeed((int)MainGame.valorRelativo(25));
        }
    }

    public boolean intersection(Player player) {
        if (player.hitbox.intersects(this.hitbox)) {
            return true;
        }

        return false;
    }

    public String hitboxMiddlePosition() {
        // para hacer que la hitbox se encuentre en el medio de
        // la imagen:
        int xOffset = ((bossWidth - (int)MainGame.valorRelativo(50)) / 2) - (hitbox.width / 2);
        int yOffset = ((bossHeight - (int)MainGame.valorRelativo(100)) / 2) - (hitbox.height / 2);

        return xOffset + ";" + yOffset;
    }

    public void updateFragment() {
        if (!fragment.activo) {
            if (cohete.isImpact) {
                fragment.settearYLimiteArriba(this.y, 0);

                // poner la direccion contraria en cada
                // disparo:
                if (direccionFragment != 3) {
                    direccionFragment = 3;
                } else {
                    direccionFragment = 4;
                }

                Rectangle fragmentHitbox = new Rectangle(TILE_SIZE, TILE_SIZE);
                fragment.setHitbox(fragmentHitbox);
                fragment.direccionDeBala = direccionFragment;
                fragment.x = Integer.parseInt(cohete.coordenadasDelAnteriorImpacto.split(";")[0]);
                fragment.y = Integer.parseInt(cohete.coordenadasDelAnteriorImpacto.split(";")[1]) + (int)MainGame.valorRelativo(20);

                fragment.activo = true;
                cohete.isImpact = false;
            }

        } else {
            fragment.update();
        }
    }

    public void updateCohete() {
        if (!cohete.isActivo()) {

            if (!isAttacking) {
                cohete.settearYLimiteAbajo(this.y, 0);
                Rectangle coheteHitbox = new Rectangle((int)MainGame.valorRelativo(50), (int)MainGame.valorRelativo(150));
                cohete.setHitbox(coheteHitbox);
                cohete.x = this.x + ((bossWidth - (int)MainGame.valorRelativo(50)) / 2) - ((int)MainGame.valorRelativo(50) / 2);
                cohete.y = this.y + ((bossHeight - (int)MainGame.valorRelativo(100)) / 2) - ((int)MainGame.valorRelativo(150) / 2);

                cohete.activo = true;
            }
        } else {
            cohete.update();
        }
    }

    public void updateAnimation() {
        tickAnim++;

        if (tickAnim >= animSpeed) {
            tickAnim = 0;
            indexAnim++;
            if (!isAttacking) {
                if (indexAnim >= 3) {
                    indexAnim = 1;
                }
            } else {
                if (indexAnim >= 5) {
                    indexAnim = 3;
                }
            }

        }
    }

    /*
    if(((yBalaMov <= bossHeight-100) || (xBalaMov >= bossWidth-50)) && yBalaMov >= 0){
           yBalaMov += balaSpeed;
        }else{
            xBalaMov += balaSpeed;
            if(xBalaMov >= bossWidth-50){
                balaSpeed = -balaSpeed;
            }
            
            if(xBalaMov <= 0){
                balaSpeed = Math.abs(balaSpeed);
            }
        }
     */
 /*
    public void updateBalaPos() {

        // Movimiento semi circular:
        if (balaSpeed > 0) {
            esquinaY = bossHeight - 100;
            esquinaX = bossWidth - 50;

            if (yBalaMov <= esquinaY) {
                yBalaMov += balaSpeed;
            } else {
                if (xBalaMov <= esquinaX) {
                    xBalaMov += balaSpeed;
                } else {
                    balaSpeed = -balaSpeed;
                }
            }
        } else {
            esquinaY = 0;
            esquinaX = 0;

            if (yBalaMov >= esquinaY) {
                yBalaMov += balaSpeed;
            } else {
                if (xBalaMov >= esquinaX) {
                    xBalaMov += balaSpeed;
                } else {
                    balaSpeed = Math.abs(balaSpeed);
                }
            }
        }

        // seguir en posicion al boss:
        xBala = (this.x - 40) + xBalaMov;
        yBala = this.y + yBalaMov;
    }
     */
    public void updateDisparos() {

        tickDisparo++;
        for (int i = 0; i < numeroDeDisparos.length; i++) {
            if (!numeroDeDisparos[i].isActivo()) {
                if (tickDisparo >= 8 && isAttacking) {

                    // poner la direccion contraria en cada
                    // disparo:
                    if (direccionBala != 0) {
                        direccionBala = 0;
                    } else {
                        direccionBala = 1;
                    }

                    tickDisparo = 0;
                    numeroDeDisparos[i].activo = true;

                    numeroDeDisparos[i].x = this.x + ((bossWidth - (int)MainGame.valorRelativo(50)) / 2) - (TILE_SIZE / 2);
                    numeroDeDisparos[i].y = this.y + ((bossHeight - (int)MainGame.valorRelativo(100)) / 2) - (TILE_SIZE / 2);
                    numeroDeDisparos[i].direccionDeBala = direccionBala;
                }

            } else {
                numeroDeDisparos[i].update();
            }
        }
    }

    public void updatePosBoss() {
        if (direction.equals("right")) {
            int distance = Math.abs(limitXPositive - (this.x / TILE_SIZE));

            if (bossSpeed < Math.abs(bossSpeedSave) && distance >= 14) {
                bossSpeed += MainGame.valorRelativo(1);
            }

            if (distance <= 14) {
                tickMov++;
                if (tickMov >= 2) {
                    tickMov = 0;
                    if (bossSpeed > 0) {
                        bossSpeed -= MainGame.valorRelativo(1);
                    } else {
                        direction = "left";
                    }
                }
            }
        }

        if (direction.equals("left")) {
            int distance = Math.abs(limitXNegative - (this.x / TILE_SIZE));

            if (bossSpeed > -bossSpeedSave && distance >= 14) {
                bossSpeed -= MainGame.valorRelativo(1);
            }

            if (distance <= 6) {
                tickMov++;
                if (tickMov >= 2) {
                    tickMov = 0;
                    if (bossSpeed < 0) {
                        bossSpeed += MainGame.valorRelativo(1);
                    } else {
                        direction = "right";
                    }
                }
            }
        }

        this.x += bossSpeed;
    }

    public boolean isStartScene() {
        return startScene;
    }

    public void update() {

        if (game.playingState.getPlayer().heMuerto) {
            return;
        }

        startTriggerZone(TILE_SIZE * 22, TILE_SIZE * 29, TILE_SIZE, TILE_SIZE * 2, game.playingState.getPlayer());
        startScene(game.playingState.getPlayer());

        if (startScene && startCombat) {
            startCombat = false;
        }

        if (!startScene && !endScene) {

            SoundImporter.playSound(game.main_songs[4]);

            if (!startCombat) {
                startCombat = true;
                cohete.activo = false;
            }

            updateAnimation();

            tickTime++;

            if (barraDeTiempoSec > 0) {
                widthBarra -= decrementBarra;
            }
            

            if (tickTime >= 60) {
                tickTime = 0;
                if (barraDeTiempoSec > 0) {
                    barraDeTiempoSec--;
                }
            }

            if (barraDeTiempoSec > 0) {
                if (!isAttacking && (lastTime - barraDeTiempoSec) > 5) {
                    isAttacking = true;
                }
            } else {
                transitionEnd.update();
                if (transitionEnd.isFinishFadeIn()) {
                    endScene = true;
                    game.main_songs[4].stop();
                    game.playingState.lm.BGBoss = newBossBG;
                    restart();
                }
            }

            if (!isAttacking) {
                updatePosBoss();
            } else {
                downAndUp(1, (int)MainGame.valorRelativo(15), 33, 27);
            }

            updateCohete();
            updateFragment();
            updateDisparos();

            //updateBalaPos();
            updateSrPeloCoche();
            updateHitboxCoche();
        }

        followHitbox();
        if (endScene) {
            transitionEnd.update();
        }
    }

    public void updateSrPeloCoche() {
        if (xPosCoche < -(int)MainGame.valorRelativo(200)) {
            if (!isAttacking) {
                xPosCoche = ((66 / 2) - 1) * TILE_SIZE;
            }

        } else {
            xPosCoche -= (int)MainGame.valorRelativo(20);
        }
    }

    public void updateHitboxCoche() {
        hitboxCoche.x = xPosCoche + ((int)MainGame.valorRelativo(150) / 2) - (hitboxCoche.width / 2);
        hitboxCoche.y = yPosCoche + ((int)MainGame.valorRelativo(150) / 2) - (hitboxCoche.height / 2) + (int)MainGame.valorRelativo(80);
    }

    ////////////// POSIBLES ATAQUES: OBJETIVO --> HACER 5: ///////////////////////
    public void downAndUp(int tickMax, int velocity, int filaMaxDown, int filaMaxUp) {
        updatePosBoss();

        // ARREGLAR CUANDO SE EJECUTA DE NUEVO PARA SUBIR:
        if (this.y / TILE_SIZE <= filaMaxDown && secuencia.equals("0")) {
            tickAttack++;
            if (tickAttack >= tickMax) {
                tickAttack = 0;
                this.y += velocity;
            }
        } else {
            if (!secuencia.equals("1")) {
                secuencia = "1";
            }

            if (this.y / TILE_SIZE >= filaMaxUp && secuencia.equals("1")) {
                if (tickWaiting <= 180) {
                    tickWaiting++;
                }

                if (tickWaiting >= 180) {
                    tickAttack++;
                    if (tickAttack >= tickMax) {
                        tickAttack = 0;
                        this.y -= velocity;
                    }
                }
            } else {
                isAttacking = false;
                lastTime = barraDeTiempoSec;
                secuencia = "0";
                tickWaiting = 0;
            }
        }
    }

    // dibujar una cosa girando alrededor del boss:
    /*
    public void dibujarBalaDandoVueltas(Graphics g) {
        g.drawImage(bala, xBala - (game.playingState.lm.offsetMapX * TILE_SIZE), yBala - (game.playingState.lm.offsetMapY * TILE_SIZE), widthBala, heightBala, null);
    }
     */
    /////////////////////////////////////////////////////////////////////////////
    public void followHitbox() {
        hitbox.x = this.x + Integer.parseInt(hitboxMiddlePosition().split(";")[0]);
        hitbox.y = this.y + Integer.parseInt(hitboxMiddlePosition().split(";")[1]);
    }

    public void draw(Graphics g) {
        Font font = new Font(Font.SANS_SERIF, Font.BOLD, (int)MainGame.valorRelativo(30));
        g.setFont(font);

        g.drawImage(bossAnim[indexAnim], this.x - (game.playingState.lm.offsetMapX * TILE_SIZE), this.y - (game.playingState.lm.offsetMapY * TILE_SIZE), bossWidth - (int)MainGame.valorRelativo(50), bossHeight - (int)MainGame.valorRelativo(100), null);

        // trigger zone:
        //g.setColor(Color.red);
        //g.drawRect((TILE_SIZE * 29) - (game.playingState.lm.offsetMapX * TILE_SIZE), (TILE_SIZE * 22) - (game.playingState.lm.offsetMapY * TILE_SIZE), TILE_SIZE, TILE_SIZE * 2);

        if (startCombat) {
            drawBarraDeTiempo(g);
            //dibujarBalaDandoVueltas(g);
            drawDisparos(g);
            drawCohete(g);
            drawFragment(g);
            drawCocheSrPelo(g);
        }

        // tamaño imagen:
        //g.setColor(Color.blue);
        //g.drawRect(this.x - (game.playingState.lm.offsetMapX * TILE_SIZE), this.y - (game.playingState.lm.offsetMapY * TILE_SIZE), bossWidth - 50, bossHeight - 100);
        // hitbox:
        //g.setColor(Color.red);
        //g.drawRect(hitbox.x - (game.playingState.lm.offsetMapX * TILE_SIZE), hitbox.y - (game.playingState.lm.offsetMapY * TILE_SIZE), hitbox.width, hitbox.height);

        if (endScene) {
            g.drawImage(bossAnim[5], (((22 / 2) - 1) * TILE_SIZE) - (game.playingState.lm.offsetMapX * TILE_SIZE), ((35 - 1) * TILE_SIZE) - (game.playingState.lm.offsetMapY * TILE_SIZE), bossWidth - (int)MainGame.valorRelativo(50), bossHeight - (int)MainGame.valorRelativo(100), null);
        }

        transitionEnd.draw(g);
    }

    public void drawCocheSrPelo(Graphics g) {
        g.drawImage(cocheAnim[0], xPosCoche - (game.playingState.lm.offsetMapX * TILE_SIZE), ((37 - 1) * TILE_SIZE) - (game.playingState.lm.offsetMapY * TILE_SIZE), (int)MainGame.valorRelativo(150), (int)MainGame.valorRelativo(150), null);
        //drawHitboxCoche(g);
    }

    public void drawHitboxCoche(Graphics g) {
        g.setColor(Color.red);
        g.drawRect(hitboxCoche.x - (game.playingState.lm.offsetMapX * TILE_SIZE), hitboxCoche.y - (game.playingState.lm.offsetMapY * TILE_SIZE), hitboxCoche.width, hitboxCoche.height);
    }

    public void drawBarraDeTiempo(Graphics g) {

        g.drawImage(cartel_barra, (TILE_WIDTH / 2) - ((int)MainGame.valorRelativo(200) / 2) - (game.playingState.lm.offsetMapX * TILE_SIZE), (TILE_SIZE * 23) - (game.playingState.lm.offsetMapY * TILE_SIZE), (int)MainGame.valorRelativo(200), (int)MainGame.valorRelativo(100), null);

        Graphics2D g2d = (Graphics2D) g;
        g.setColor(new Color(240, 230, 140));

        Rectangle2D.Float barra = new Rectangle2D.Float((TILE_WIDTH / 2) - (MainGame.valorRelativo(480) / 2) - (game.playingState.lm.offsetMapX * TILE_SIZE), (TILE_SIZE * 22) - (game.playingState.lm.offsetMapY * TILE_SIZE), widthBarra, MainGame.valorRelativo(20));
        g2d.fill(barra);

        g.setColor(Color.WHITE);
        g.drawRect((TILE_WIDTH / 2) - ((int)MainGame.valorRelativo(480) / 2) - (game.playingState.lm.offsetMapX * TILE_SIZE), (TILE_SIZE * 22) - (game.playingState.lm.offsetMapY * TILE_SIZE), (int)MainGame.valorRelativo(480), (int)MainGame.valorRelativo(20));
    }

    public void drawDisparos(Graphics g) {
        for (int i = 0; i < numeroDeDisparos.length; i++) {
            if (numeroDeDisparos[i].activo) {
                numeroDeDisparos[i].draw(g);
            }
        }
    }

    public void drawFragment(Graphics g) {
        if (fragment.activo) {
            fragment.draw(g);
        }
    }

    public void drawCohete(Graphics g) {
        if (cohete.activo) {
            cohete.draw(g);
        }
    }

    public void startTriggerZone(int triggerY, int triggerX, int triggerWidth, int triggerHeight, Player player) {
        if (player.hitbox.x >= triggerX && player.hitbox.x <= triggerX + triggerWidth
                && player.hitbox.y >= triggerY && player.hitbox.y <= triggerY + triggerHeight && !startScene) {
            startScene = true;
        }
    }

    public void resetBalls() {
        // first thing
        tickDisparo = 0;

        // second thing
        for (int i = 0; i < numeroDeDisparos.length; i++) {
            numeroDeDisparos[i].setActivo(false);
            numeroDeDisparos[i].x = this.x + ((bossWidth - (int)MainGame.valorRelativo(50)) / 2) - (TILE_SIZE / 2);
            numeroDeDisparos[i].y = this.y + ((bossHeight - (int)MainGame.valorRelativo(100)) / 2) - (TILE_SIZE / 2);
            numeroDeDisparos[i].updateHitbox();
        }
    }

    public void restart() {
        // restarear posiciones:
        this.x = initX;
        this.y = initY;

        // restartear variables de cuentan el tiempo:
        tickMov = tickAttack = tickTime = tickWaiting = tickAnim = indexAnim = 0;

        // restartear variables de cambio de estado:
        isAttacking = startCombat = false;
        barraDeTiempoSec = 30; // 60 segundos (se aumentará!)
        lastTime = barraDeTiempoSec;

        // variables varias -> direccion, velocidad del boss, la barra de tiempo...
        direction = "right";
        secuencia = "0";
        bossSpeedSave = Math.abs(bossSpeedSave);
        bossSpeed = bossSpeedSave;

        // resetear mis bolas y el cohete:
        resetBalls();
        fragment.activo = false;
        restartCohete();
        transitionEnd.setFinishTransition(false);
        xPosCoche = ((66 / 2) - 1) * TILE_SIZE;
        yPosCoche = (36 - 1) * TILE_SIZE;

        widthBarra = MainGame.valorRelativo(480);
    }
    
    public void resetBGandTransition(){
        // Resetear transiciones:
        transitionEnd.setOpacity(0);
        transitionEnd.setTick(0);
        
        game.playingState.lm.BGBoss = lastBossBG;
    }

    public void restartCohete() {
        cohete.activo = false;
        cohete.isImpact = false;
        cohete.indexEx = 0;
        cohete.endExplosion = cohete.startExplosion = false;
        cohete.tickExplosion = 0;
        cohete.hitbox.width = cohete.widthHitboxCopy;
    }

    // escena antes de empezar el combate:
    public void startScene(Player player) {
        if (startScene) {
            if (this.x != initX - MainGame.valorRelativo(20)) {
                this.x = initX - (int)MainGame.valorRelativo(20);
            }

            // primera escena -> player moviendose y cayendo:
            if ((player.hitbox.x / TILE_SIZE) >= 15) {
                player.playerSpeed = 0;
                player.isMoving = true;
                player.hitbox.x -= MainGame.valorRelativo(4);

                // segunda escena -> boss baja epicamente:
            } else if (this.y / TILE_SIZE <= 27) {
                this.y += MainGame.valorRelativo(5);
            } else {
                // no hay mas :)
                tickMov++;
                if (tickMov >= 120) {
                    startScene = false;
                    player.playerSpeed = player.playerSpeedSave;
                    tickMov = 0;
                }
            }
        }
    }
}
