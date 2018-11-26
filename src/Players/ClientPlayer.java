package Players;

import Network.Client;
import Network.StateMessage;
import Sketches.PlayerSketch;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class ClientPlayer extends Player {


    private int clientPort = 6240;
    private String clientIp = "localhost";

    private Client client;

    protected int playerId;

    // Ready Button
    public final float x = sketch.WORLD_WIDTH / 2 + 300;
    public final float y = 650;
    public final int boxWidth = 200;
    public final int boxHeight = 100;

    private int firstConnTimer = 10;

    public ClientPlayer(PlayerSketch sketch) {
        super(sketch);
    }

    public void setup() {
        client = new Client(clientIp, clientPort, sketch.serverIp, sketch.serverPort, this);
        client.startListening();
    }

    public void draw() {
        sketch.background(250, 235, 215);

        // To make sure nothing gets update while iterating this.
        synchronized (this) {
            sketch.pushMatrix();
            sketch.centreCamera(sketch.getCurrentPlayerCell());
            sketch.getGameDrawer().drawGameObjects(sketch.getPlayerCells(), sketch.getGameLogic().getBlobs(), sketch.getGameLogic().getMaterials(), sketch.getGameLogic().getShrooms());
            sketch.popMatrix();
            sketch.getGameDrawer().drawGameUI(sketch.getCurrentPlayerCell(), sketch.getPlayerCells());
        }

        client.sendMousePosition(sketch.mouseX, sketch.mouseY);
    }

    // Generate Clients start screen.
    public void clientStartScreen() {
        sketch.background(0);
        sketch.textAlign(sketch.CENTER);
        sketch.fill(255);
        sketch.textSize(20);
        cellSelection();
        sketch.textSize(20);

        sketch.fill(255);
        sketch.text("Enter Nickname: ", sketch.width / 2 - 300, sketch.height / 2 + 300);
        sketch.textAlign(0);
        sketch.text(nickname, sketch.width / 2 - 220, sketch.height / 2 + 300);
        sketch.getGameDrawer().drawStartButton(playerIsReady, x, y, boxWidth, boxHeight);

        if (playerIsReady) {
            // In case it did not have a name.
            nickname = (nickname.length() == 0) ? "NoName" : nickname;
            client.sendJoinRequest(nickname, currentCell);
        }
    }

    private void readState(byte[] data) {
        /* Solution to serialization adapted from the source below.
            http://www.coderpanda.com/java-socket-programming-transferring-java-object-through-socket-using-udp/
         */
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
            ObjectInputStream inputStream = new ObjectInputStream(byteArrayInputStream);

            StateMessage stateMessage = (StateMessage) inputStream.readObject();

            if (!stateMessage.isGameOver) {
                // Set ID.
                if (stateMessage.playerId != -1)
                    parsePlayerId(stateMessage);
                else {
                    // Update the game states
                    sketch.setGameLogic(stateMessage.getGameLogic());
                    sketch.setPlayerCells(stateMessage.getCells());

                    // Start the Game and keep on Start Game Screen. It waits a bit before joining the server.
                    if (firstConnTimer == 0)
                        sketch.setCurrentScreen(sketch.CLIENT_GAME_START);
                    else if (firstConnTimer > 0)
                        firstConnTimer--;
                }

            } else
                sketch.setCurrentScreen(sketch.GAME_OVER_SCREEN);// It is Game Over.

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void parsePlayerId(StateMessage stateMessage) {
        playerId = stateMessage.playerId;
        client.setPlayerId(playerId);
    }

    public void receive(byte[] data, String ip, int port) {
        readState(data);
    }

    public void sendButtonClick(char key) {
        client.sendButtonClick(key);
    }

    public void sendLeaveRequest() {
        client.sendLeaveRequest();
    }

    public int getPlayerId() {
        return playerId;
    }

}
