package dialogues;

import java.util.ArrayList;
import java.util.List;

public class Node {
    List<Node> hijos;
    String[] respuestaNpc;
    String respuestaPlayer;

    // Constructor para meter una respuesta por parte del NPC:
    public Node(String[] respuestaNpc) {
        this.respuestaNpc = respuestaNpc;
        hijos = new ArrayList<>();
    }

    // Constructor para meter una respuesta por parte del Player:
    public Node(String respuestaPlayer) {
        this.respuestaPlayer = respuestaPlayer;
        hijos = new ArrayList<>();
    }

    public List<Node> getHijos() {
        return hijos;
    }

    public void setHijos(List<Node> hijos) {
        this.hijos = hijos;
    }
    
    public void addHijos(Node respuesta){
        hijos.add(respuesta);
    }

    public String[] getRespuestaNpc() {
        return respuestaNpc;
    }

    public void setRespuestaNpc(String[] respuestaNpc) {
        this.respuestaNpc = respuestaNpc;
    }

    public String getRespuestaPlayer() {
        return respuestaPlayer;
    }

    public void setRespuestaPlayer(String respuestaPlayer) {
        this.respuestaPlayer = respuestaPlayer;
    }
}
