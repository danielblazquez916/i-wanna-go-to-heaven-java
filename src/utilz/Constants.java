package utilz;

// Usamos esta clase cuando queremos asociar un numero a un valor
// y usarlo como clave:
public class Constants {

    public static class LevelConstants {

        //////////////////////// LVLDATA NIVEL 1: ////////////////////////
        // Este es el indice de inicio por el que empieza a buscar
        // los sprites no colisionables (ni me matan, ni me choco):
        public static final int INICIO_NO_COLISION_LVL1 = 6;
        public static final int FINAL_NO_COLISION_LVL1 = 6;

        public static final int STONE_BLOCK = 0;
        public static final int CARVED_STONE_BLOCK = 1;
        public static final int ICE_BLOCK_1 = 2;
        public static final int ICE_BLOCK_2 = 3;
        public static final int SAND_BLOCK = 4;
        public static final int DIRT_BLOCK = 5;
        public static final int VOID_BLOCK = 6;
        public static final int NORMAL_SPIKE_UP = 7;
        public static final int NORMAL_SPIKE_DOWN = 8;
        public static final int NORMAL_SPIKE_LEFT = 9;
        public static final int NORMAL_SPIKE_RIGHT = 10;
        public static final int ICE_SPIKE_UP = 11;
        public static final int ICE_SPIKE_DOWN = 12;
        public static final int ICE_SPIKE_LEFT = 13;
        public static final int ICE_SPIKE_RIGHT = 14;
        public static final int DESERT_SPIKE_UP = 15;
        public static final int DESERT_SPIKE_DOWN = 16;
        public static final int DESERT_SPIKE_LEFT = 17;
        public static final int DESERT_SPIKE_RIGHT = 18;

        // si es mayor que FINAL_NO_COLISION_LVL1 ---> indices que hacen daño.
        // si es menor que INICIO_NO_COLISION_LVL1 ---> indices que provocan colision.
        //////////////////////////////////////////////////////////////////
        // Cuantos niveles haya...
    }

    public static class PlayerConstants {

        // Dentro de mi spritesheet, cada constante representa
        // el indice de fila:
        public static final int IDLE = 1;
        public static final int RUNNING = 0;
        public static final int JUMPING = 2;
        public static final int FALLING = 3;
        public static final int IDLE_REVERSED = 5;
        public static final int RUNNING_REVERSED = 4;
        public static final int JUMPING_REVERSED = 6;
        public static final int FALLING_REVERSED = 7;

        // Para el indice de columna:
        public static int getSpriteFrames(int constant_anim) {

            switch (constant_anim) {
                case IDLE:
                    return 4;
                case RUNNING:
                    return 6;
                case JUMPING:
                    return 2;
                case FALLING:
                    return 2;
                case IDLE_REVERSED:
                    return 4;
                case RUNNING_REVERSED:
                    return 6;
                case JUMPING_REVERSED:
                    return 2;
                case FALLING_REVERSED:
                    return 2;
                default:
                    return 1;
            }
        }
    }
}
