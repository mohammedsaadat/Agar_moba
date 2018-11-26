package Network;

public class PlayerAgent {

    private int port;
    private String ip;
    public int playerId;

    public PlayerAgent(int port, String ip, int playerId) {
        this.port = port;
        this.ip = ip;
        this.playerId = playerId;
    }

    public int getPort() {
        return port;
    }

    public String getIp() {
        return ip;
    }

    public void setPlayerId(int id) {
        playerId = id;
    }
}
