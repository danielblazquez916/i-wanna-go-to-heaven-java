package dialogues;

import entities.Npc;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import game.MainGame;
import static game.MainGame.TILE_HEIGHT;
import static game.MainGame.TILE_WIDTH;

public class DialogueSystem {

    Node nodosDeDialogo;
    Node dialogoRaiz;
    int indexNode;
    int tick, talkSpeed, indexTalkingNpc;
    static boolean playerTurn;
    //boolean endConversation;

    static boolean pressedUp, pressedDown, pressedEnter;

    String dialoguePosition = "down";

    Font fontNPC;
    int xPosStringNpc, yPosStringNpc;

    Font fontPlayer;
    int xPosStringPlayer, yPosStringPlayer;

    int widthNpcSquare = (int)MainGame.valorRelativo(800), heightNpcSquare = (int)MainGame.valorRelativo(248), xPosNpcSquare, yPosNpcSquare;

    int widthPlayerSquare = (int)MainGame.valorRelativo(400), heightPlayerSquare = (int)MainGame.valorRelativo(62), heightPortion = heightPlayerSquare, xPosPlayerSquare, yPosPlayerSquare;

    public DialogueSystem(Node nodosDeDialogo, int talkSpeed, String dialoguePosition) {
        this.dialoguePosition = dialoguePosition;
        this.nodosDeDialogo = nodosDeDialogo;
        dialogoRaiz = this.nodosDeDialogo;

        this.talkSpeed = talkSpeed;

        setXandYPosNpcSquare();
        setXandYPosPlayerSquare();
        setXandYPosStringNpc();
        setXandYPosStringPlayer();
        instanciarFonts();
    }

    public void setDialoguePosition(String dialoguePosition) {
        this.dialoguePosition = dialoguePosition;
    }

    public void instanciarFonts() {
        fontNPC = new Font(Font.DIALOG_INPUT, Font.BOLD, (int)MainGame.valorRelativo(20));
        fontPlayer = new Font(Font.MONOSPACED, Font.BOLD, (int)MainGame.valorRelativo(17));
    }

    public void setXandYPosStringPlayer() {
        xPosStringPlayer = xPosPlayerSquare;
        yPosStringPlayer = yPosPlayerSquare;
    }

    public void setXandYPosStringNpc() {
        xPosStringNpc = xPosNpcSquare + (int)MainGame.valorRelativo(50);
        yPosStringNpc = yPosNpcSquare + (int)MainGame.valorRelativo(50);
    }

    public void setXandYPosNpcSquare() {
        int fullWidth = widthNpcSquare + widthPlayerSquare + (int)MainGame.valorRelativo(20);

        xPosNpcSquare = (TILE_WIDTH / 2) - (fullWidth / 2);

        if (dialoguePosition.equals("down")) {
            yPosNpcSquare = TILE_HEIGHT - (int)MainGame.valorRelativo(290);
        } else if (dialoguePosition.equals("up")) {
            yPosNpcSquare = (int)MainGame.valorRelativo(50);
        }
    }

    public void setXandYPosPlayerSquare() {
        xPosPlayerSquare = xPosNpcSquare + widthNpcSquare + (int)MainGame.valorRelativo(20);
        yPosPlayerSquare = yPosNpcSquare;
    }

    public List<String> dividirTexto(String texto, int numeroCaracteres) {
        List<String> textoDividido = new ArrayList<>();
        String textPart = "";
        int counter = 0;

        if (texto.length() <= numeroCaracteres) {
            textoDividido.add(texto);
            return textoDividido;
        }

        for (int i = 0; i < texto.length(); i++) {

            if (counter >= numeroCaracteres && texto.charAt(i) == ' ') {
                textoDividido.add(textPart);
                textPart = "";
                counter = 0;
                continue;
            }

            textPart += texto.charAt(i);
            counter++;
        }

        if (!textPart.isBlank()) {
            textoDividido.add(textPart);
        }

        return textoDividido;
    }

    public void mostrarDialogoNPCDividido(Graphics g, int distanciaEntreTexto) {
        List<String> dialogoNpc = dividirTexto(nodosDeDialogo.respuestaNpc[indexTalkingNpc], 50);
        int acumulador = 0;
        
        g.setColor(Color.WHITE);
        g.setFont(fontNPC);

        for(int i = 0; i < dialogoNpc.size(); i++){
            g.drawString(String.valueOf(dialogoNpc.get(i)), xPosStringNpc, yPosStringNpc + acumulador);
            acumulador += distanciaEntreTexto;
        }
        
        //g.drawString(dialogoNpc.get(i), xPosStringNpc, yPosStringNpc + acumulador);
    }
    
    

    //////////////////////////// DRAW: ///////////////////////////
    public void draw(Graphics g) {
        drawNPCSquare(g);
        drawRespuestaNPC(g);
        if (playerTurn) {
            drawPlayerSquare(g);
            drawRespuestasPlayer(g);
        }
    }

    public void drawRespuestaNPC(Graphics g) {
        mostrarDialogoNPCDividido(g, (int)MainGame.valorRelativo(30));
    }

    public void drawNPCSquare(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(xPosNpcSquare, yPosNpcSquare, widthNpcSquare, heightNpcSquare);

        // Dibujar bordes en todo el cuadro:
        g.setColor(Color.WHITE);
        g.drawRect(xPosNpcSquare, yPosNpcSquare, widthNpcSquare, heightNpcSquare);
    }

    public void drawPlayerSquare(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(xPosPlayerSquare, yPosPlayerSquare, widthPlayerSquare, heightPlayerSquare);

        // Dibujar bordes en cada porción:
        int acumulador = 0;
        g.setColor(Color.WHITE);
        for (int i = 0; i < nodosDeDialogo.getHijos().size(); i++) {
            g.drawRect(xPosPlayerSquare, yPosPlayerSquare + acumulador, widthPlayerSquare, heightPortion);
            acumulador += heightPortion;
        }

        acumulador = (heightPortion * indexNode);
        g.fillRect(xPosPlayerSquare, yPosPlayerSquare + acumulador, widthPlayerSquare, heightPortion);
    }

    public void drawRespuestasPlayer(Graphics g) {
        int acumulador = 0;

        g.setFont(fontPlayer);
        for (int i = 0; i < nodosDeDialogo.getHijos().size(); i++) {
            if (indexNode == i) {
                g.setColor(Color.BLACK);
            } else {
                g.setColor(Color.WHITE);
            }

            g.drawString(nodosDeDialogo.getHijos().get(i).getRespuestaPlayer(), xPosStringPlayer + (int)MainGame.valorRelativo(10), yPosStringPlayer + acumulador + (int)MainGame.valorRelativo(37));
            acumulador += heightPortion;
        }
    }
    /////////////////////////////////////////////////////////////

    ////////////////////////// UPDATE: //////////////////////////
    public void update() {
        updateNpcTalking();
        updateHeightPlayerSquare();
        goDownAndUpAnswersPlayer();
        selectAnswerPlayer();
    }

    public void selectAnswerPlayer() {
        if (pressedEnter) {
            playerTurn = false;
            indexTalkingNpc = 0;

            // pasamos al siguiente nodo:
            nodosDeDialogo = nodosDeDialogo.getHijos().get(indexNode).getHijos().getFirst();
            indexNode = 0;

            pressedEnter = false;
        }
    }

    public void goDownAndUpAnswersPlayer() {
        if (pressedUp) {
            if (indexNode > 0) {
                indexNode--;
            }
            pressedUp = false;
        } else if (pressedDown) {
            if (indexNode < nodosDeDialogo.getHijos().size() - 1) {
                indexNode++;
            }
            pressedDown = false;
        }
    }

    public void updateHeightPlayerSquare() {
        if (playerTurn) {
            int adjustedHeight = (heightPortion * nodosDeDialogo.getHijos().size());
            if (heightPlayerSquare != adjustedHeight) {
                heightPlayerSquare = adjustedHeight;
            }
        }
    }

    public void updateNpcTalking() {
        if (!playerTurn) {
            tick++;
            if (tick >= talkSpeed) {
                tick = 0;
                if (indexTalkingNpc >= nodosDeDialogo.respuestaNpc.length - 1) {

                    if (nodosDeDialogo.getHijos().isEmpty()) {
                        nodosDeDialogo = dialogoRaiz;
                        Npc.enDialogo = false;
                        indexTalkingNpc = 0;
                    } else {
                        playerTurn = true;
                    }

                    return;
                }

                indexTalkingNpc++;
            }
        }
    }
    /////////////////////////////////////////////////////////////

    ////////////////////////// INPUTS: //////////////////////////
    public static void keyPressed(KeyEvent e) {
        if (playerTurn) {
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                pressedUp = true;
            } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                pressedDown = true;
            }

            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                pressedEnter = true;
            }
        }
    }

    /////////////////////////////////////////////////////////////
}
