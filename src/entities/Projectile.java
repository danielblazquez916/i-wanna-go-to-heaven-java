package entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.sound.sampled.Clip;
import sounds.SoundImporter;
import utilz.LoadSave;
import game.MainGame;
import static game.MainGame.TILE_COLS;
import static game.MainGame.TILE_ROWS;
import static game.MainGame.TILE_SIZE;

public class Projectile extends Enemy {

    int direccionDeBala = 1;
    boolean activo;
    int projectileSpeed = (int)MainGame.valorRelativo(25);
    public int limiteIzquierdo, limiteDerecho, limiteAbajo, limiteArriba;
    public int xPosInicial, yPosInicial;
    int heightBala, widthBala;
    boolean isImpact;
    String coordenadasDelAnteriorImpacto = "";
    int widthHitboxCopy;

    BufferedImage[] explosionAnim;
    int tickExplosion, indexEx;
    boolean endExplosion = false, startExplosion = false;

    public Projectile(int direccionDeBala, int x, int y, Rectangle hitbox, int xHitboxOffset, int yHitboxOffset, MainGame game, BufferedImage fullImage, int xPosInicial, int yPosInicial, int widthBala, int heightBala) {
        super(x, y, hitbox, xHitboxOffset, yHitboxOffset, game, fullImage);
        this.direccionDeBala = direccionDeBala;
        this.hitbox = hitbox;
        this.heightBala = heightBala;
        this.widthBala = widthBala;
        widthHitboxCopy = this.hitbox.width;

        this.yPosInicial = yPosInicial;
        this.xPosInicial = xPosInicial;

        // para encontrar los limites de la bala (cuando se destruye):
        limiteIzquierdo = ((int) (TILE_COLS * Math.floor(((double) (this.x / TILE_SIZE) / TILE_COLS))));
        limiteDerecho = ((int) (TILE_COLS * Math.ceil(((double) (this.x / TILE_SIZE) / TILE_COLS))));
        limiteAbajo = ((int) (TILE_ROWS * Math.ceil(((double) (this.y / TILE_SIZE) / TILE_ROWS))));
        limiteArriba = ((int) (TILE_ROWS * Math.floor(((double) (this.y / TILE_SIZE) / TILE_ROWS))));

        BufferedImage explosion = LoadSave.importImg(LoadSave.EXPLOSION_SCHEMA);
        explosionAnim = LoadSave.subdividirImagen(explosion, 6);
    }

    public void setxPosInicial(int xPosInicial) {
        this.xPosInicial = xPosInicial;
    }

    public void settearYLimiteAbajo(int newY, int extra) {
        limiteAbajo = ((int) (TILE_ROWS * Math.ceil(((double) (newY / TILE_SIZE) / TILE_ROWS)))) + extra;
    }

    public void settearYLimiteArriba(int newY, int extra) {
        limiteArriba = ((int) (TILE_ROWS * Math.floor(((double) (newY / TILE_SIZE) / TILE_ROWS)))) + extra;
    }

    public void setyPosInicial(int yPosInicial) {
        this.yPosInicial = yPosInicial;
    }

    public void draw(Graphics g) {

        if (!startExplosion && !endExplosion) {
            g.drawImage(fullImage, x - (game.playingState.lm.offsetMapX * TILE_SIZE), y - (game.playingState.lm.offsetMapY * TILE_SIZE), widthBala, heightBala, null);
        } else {
            g.drawImage(explosionAnim[indexEx], (x + (widthBala / 2) - (int)MainGame.valorRelativo(100)) - (game.playingState.lm.offsetMapX * TILE_SIZE), y - (game.playingState.lm.offsetMapY * TILE_SIZE), (int)MainGame.valorRelativo(200), (int)MainGame.valorRelativo(200), null);
        }
        //this.drawHitbox(g);
    }

    public void setProjectileSpeed(int projectileSpeed) {
        this.projectileSpeed = projectileSpeed;
    }

    public void playerMuertoPorBala() {
        //// SER INMORTAL ////
        if (Player.serInmortalDev) {
            return;
        }
        //////////////////////

        Player player = game.playingState.getPlayer();

        if (!player.heRespawneado) {
            if (player.hitbox.intersects(this.hitbox)) {
                player.muertoPorProjectil = true;
            }
        }
    }

    public void update() {
        // QUIZA CONVENDRÍA MODIFICARLO UN PELIN PARA HACER QUE LLEGE HASTA EL FONDO.
        // MODIFICAR LA HITBOX DE LAS BALAS PARA QUE SEAN MAS PEQUEÑAS.
        // (NOTA: GRACIAS A HACER LO DEL UMBRAL, AHORA DA IGUAL QUE LAS BALAS SEAN MAS PEQUEÑAS)
        playerMuertoPorBala();

        switch (direccionDeBala) {
            // SI ES 0 --> EMPIEZA EN DERECHA Y ACABA EN IZQUIERDA.
            case 0: {
                if (this.x / TILE_SIZE > (limiteIzquierdo - 1)) {
                    this.x -= projectileSpeed;
                } else {
                    this.x = xPosInicial;
                    this.y = yPosInicial;

                    activo = false;
                }
                break;
            }

            // SI ES 1 --> EMPIEZA EN IZQUIERDA Y ACABA EN DERECHA.
            case 1: {
                if ((limiteDerecho - 1) > this.x / TILE_SIZE) {
                    this.x += projectileSpeed;
                } else {
                    this.x = xPosInicial;
                    this.y = yPosInicial;

                    activo = false;
                }
                break;
            }

            // SI ES 2 --> EMPIEZA ARRIBA Y ACABA EN ABAJO.
            case 2: {

                // SUMAR PARA QUE CAIGA BIEN TU SABE
                if (limiteAbajo > ((this.y + heightBala) / TILE_SIZE)) {
                    this.y += projectileSpeed;
                } else {
                    coordenadasDelAnteriorImpacto = this.x + ";" + this.y;
                    if (!startExplosion && !endExplosion) {
                        startExplosion = true;
                        endExplosion = false;
                    }
                    isImpact = true;
                }
                break;
            }

            // SI ES 3 --> EMPIEZA ABAJO Y VA HACIA LOS LADOS (IZQUIERDO).
            case 3: {
                if ((limiteIzquierdo) < this.x / TILE_SIZE
                        && (limiteArriba) < this.y / TILE_SIZE) {
                    this.y -= projectileSpeed;
                    this.x -= projectileSpeed;
                } else {
                    this.x = xPosInicial;
                    this.y = yPosInicial;

                    activo = false;
                }
                break;
            }

            // SI ES 4 --> EMPIEZA ABAJO Y VA HACIA LOS LADOS (DERECHO).
            case 4: {
                if ((limiteDerecho) > this.x / TILE_SIZE
                        && (limiteArriba) < this.y / TILE_SIZE) {
                    this.y -= projectileSpeed;
                    this.x += projectileSpeed;
                } else {
                    this.x = xPosInicial;
                    this.y = yPosInicial;

                    activo = false;
                }
                break;
            }

            default:
                break;
        }

        updateExplosion();

        if (!startExplosion && !endExplosion) {
            this.updateHitbox();
        }
    }

    public void updateExplosion() {
        if (endExplosion && !startExplosion) {
            indexEx = 0;
            this.x = xPosInicial;
            this.y = yPosInicial;
            tickExplosion = 0;
            activo = false;
            endExplosion = startExplosion = false;
        }

        if (startExplosion && !endExplosion) {

            if (this.hitbox.width != widthHitboxCopy + (int)MainGame.valorRelativo(100)) {
                this.hitbox.width = widthHitboxCopy;
                this.hitbox.width += (int)MainGame.valorRelativo(100);
                this.hitbox.x -= (int)MainGame.valorRelativo(20);
                this.hitbox.y += (int)MainGame.valorRelativo(50);
            }

            tickExplosion++;
            
            if (tickExplosion >= 5) {
                tickExplosion = 0;
                indexEx++;
                if (indexEx >= 4) {
                    endExplosion = true;
                    startExplosion = false;
                    this.hitbox.width = widthHitboxCopy;
                    this.hitbox.x += (int)MainGame.valorRelativo(20);
                    this.hitbox.y -= (int)MainGame.valorRelativo(50);
                }
            }
        }
    }

    public int getDireccionDeBala() {
        return direccionDeBala;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
