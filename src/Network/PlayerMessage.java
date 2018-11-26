package Network;

import java.io.Serializable;

/*
This class represents all the messages that the players send to the server.
 */
public class PlayerMessage implements Serializable {


    public enum MessageType {MouseMessage, ButtonMessage, JoinMessage, LeaveMessage}

    private int playerId = -1;
    private float mouseX;
    private float mouseY;
    private char keyPressed;
    public MessageType messageType;
    private String nickname;
    private int cellType;

    PlayerMessage(int playerId, float mouseX, float mouseY) {
        this.playerId = playerId;
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        messageType = MessageType.MouseMessage;
    }

    PlayerMessage(int playerId, char keyPressed) {
        this.playerId = playerId;
        this.keyPressed = keyPressed;
        messageType = MessageType.ButtonMessage;
    }

    PlayerMessage(String name, int cellType) {
        nickname = name;
        messageType = MessageType.JoinMessage;
        this.cellType = cellType;
    }

    PlayerMessage(int playerId) {
        this.playerId = playerId;
        messageType = MessageType.LeaveMessage;
    }

    public float getMouseX() {
        return mouseX;
    }

    public float getMouseY() {
        return mouseY;
    }

    public char getKeyPressed() {
        return keyPressed;
    }

    public int getPlayerId() {
        return playerId;
    }

    public String getNickname() {
        return nickname;
    }

    public int getCellType() {
        return cellType;
    }
}
