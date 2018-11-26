package Network;

import Sketches.BlobbySketch;
import hypermedia.net.UDP;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

public class GameServer {

    private UDP server;

    public GameServer(String ip, int port, BlobbySketch blobbySketch) {
        server = new UDP(blobbySketch, port);
    }

    public void startListening() {
        server.listen(true);
    }

    public void send(String data, String clientIp, int clientPort) {
        server.send(data, clientIp, clientPort);
    }

    public void sendGameState(StateMessage stateMessage, ArrayList<PlayerAgent> players) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(outputStream);
            os.writeObject(stateMessage);
            byte[] data = outputStream.toByteArray();

            // Sends the game states to all of the players.
            for (PlayerAgent playerAgent : players) {
                server.send(data, playerAgent.getIp(), playerAgent.getPort());
            }

        } catch (ConcurrentModificationException e) {
            e.getCause();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendGameState(StateMessage stateMessage, PlayerAgent player) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(outputStream);
            os.writeObject(stateMessage);
            byte[] data = outputStream.toByteArray();

            // Sends the game states to all of the players.
            server.send(data, player.getIp(), player.getPort());

        } catch (ConcurrentModificationException e) {
            e.getCause();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
