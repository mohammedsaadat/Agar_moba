package Network;

import Players.ClientPlayer;
import hypermedia.net.UDP;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;


/* Solution to serialization adapted from the source below.
http://www.coderpanda.com/java-socket-programming-transferring-java-object-through-socket-using-udp/
*/
public class Client {

    private UDP client;
    private String serverIp;
    private int serverPort;
    private int playerId;

    public Client(String clientIp, int clientPort, String serverIp, int serverPort, ClientPlayer clientPlayer) {
        client = new UDP(clientPlayer, clientPort, clientIp);
        this.serverIp = serverIp;
        this.serverPort = serverPort;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public void startListening() {
        client.listen(true);
    }

    public void sendLeaveRequest() {
        try {

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(outputStream);

            PlayerMessage playerMessage = new PlayerMessage(playerId);
            os.writeObject(playerMessage);

            byte[] data = outputStream.toByteArray();

            client.send(data, serverIp, serverPort);
            // Close the connection.
            client.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendJoinRequest(String nickname, int cellType) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream os = null;
            os = new ObjectOutputStream(outputStream);

            PlayerMessage playerMessage = new PlayerMessage(nickname, cellType);
            os.writeObject(playerMessage);

            byte[] data = outputStream.toByteArray();

            client.send(data, serverIp, serverPort);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendButtonClick(char key) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(outputStream);

            PlayerMessage playerMessage = new PlayerMessage(playerId, key);
            os.writeObject(playerMessage);

            byte[] data = outputStream.toByteArray();

            // Send the button click message.
            client.send(data, serverIp, serverPort);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMousePosition(float mouseX, float mouseY) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(outputStream);

            PlayerMessage playerMessage = new PlayerMessage(playerId, mouseX, mouseY);
            os.writeObject(playerMessage);

            byte[] data = outputStream.toByteArray();

            // Sends the mouse position message.
            client.send(data, serverIp, serverPort);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
