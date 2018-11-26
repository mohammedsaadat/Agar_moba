package Sketches;

import GameObjects.Cell.*;
import Network.GameServer;
import Network.PlayerAgent;
import Network.PlayerMessage;
import Network.StateMessage;
import Players.ClientPlayer;
import processing.core.PApplet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;

public class ServerSketch extends BlobbySketch {

    private GameServer server;

    private final int SERVER_GAME_START_SCREEN = 0;

    private ArrayList<PlayerAgent> players = new ArrayList<>();
    private HashMap<Integer, PlayerAgent> playersId = new HashMap<>();

    private int currentAvailableId = 0;

    public void setup() {
        super.setup();

        // creates game logic and game drawer.
        setupGameLogic(this);

        // Setting up the connection.
        server = new GameServer(serverIp, serverPort, this);
        server.startListening();

        currentScreen = SERVER_GAME_START_SCREEN;

        // Setting up the world on the server.
        gameLogic.setupBlobs();
        gameLogic.setupHerbs();
        gameLogic.setupWood();
        gameLogic.setupSteel();

    }

    public void draw() {
        switch (currentScreen) {
            case SERVER_GAME_START_SCREEN:
                serverDraw();
                break;
            default:
                println("Error: Wrong Input for Server Draw switch statement");
        }
    }

    private void serverDraw() {

        server.sendGameState(getGameState(), players);

        background(255);

        // to show the whole world
        translate(width / 2, height / 2);
        scale(0.5f);

        synchronized (this) {
            // detect collisions
            gameLogic.detectCellBlobCollections(playerCells);
            gameLogic.detectCellMaterialCollections(playerCells);
            // Removing all the dead players.
            ArrayList<Cell> deadCells = gameLogic.detectCellCollisions2(playerCells);
            removeDeadPlayers(deadCells);
            // The ones that die from riven shrooms
            ArrayList<Cell> deadFromShroom = gameLogic.detectCellCollisionWithShrooms(playerCells);
            removeDeadPlayers(deadFromShroom);
            // generate blobs and materials
            gameLogic.generateBlobsAndMaterials();

            gameDrawer.drawGameObjects(playerCells, gameLogic.getBlobs(), gameLogic.getMaterials(), gameLogic.getShrooms());
        }


        server.sendGameState(getGameState(), players);
    }

    private void removeDeadPlayers(ArrayList<Cell> deadCells) {
        ArrayList<PlayerAgent> agentsToRemove = new ArrayList<>();
        for (Cell cell : deadCells) {
            agentsToRemove.add(playersId.get(cell.getPlayerId()));
        }
        players.removeAll(agentsToRemove);
        server.sendGameState(new StateMessage(true), agentsToRemove);
    }

    private StateMessage getGameState() {
        return new StateMessage(gameLogic, playerCells);
    }

    public void receive(byte[] data, String ip, int port) {
        if (currentScreen == SERVER_GAME_START_SCREEN) {
            readMessage(data, ip, port);
        }
    }

    private void readMessage(byte[] data, String ip, int port) {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
            ObjectInputStream inputStream = new ObjectInputStream(byteArrayInputStream);

            PlayerMessage playerMessage = (PlayerMessage) inputStream.readObject();

            // Joining the server.
            if (playerMessage.messageType == PlayerMessage.MessageType.JoinMessage) {
                serverReceivingStartConnection(ip, port, playerMessage.getNickname(), playerMessage.getCellType());
                return;
            }

            // Get the player cell.
            Cell c = getPlayerCell(playerMessage.getPlayerId());

            //If it is a leave message then leave the server.
            if (playerMessage.messageType == PlayerMessage.MessageType.LeaveMessage)
                applyPlayerLeave(c, playerMessage);

            if (c != null) {
                if (playerMessage.messageType == PlayerMessage.MessageType.MouseMessage) { // movement action
                    gameLogic.applyMouseAction(c, playerMessage);
                } else if (playerMessage.messageType == PlayerMessage.MessageType.ButtonMessage) { // button click
                    gameLogic.applyKeyAction(c, playerMessage);
                }
                server.sendGameState(getGameState(), players);
            }
        } catch (ConcurrentModificationException e) {
            e.getCause();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void applyPlayerLeave(Cell cell, PlayerMessage playerMessage) {
        Iterator<PlayerAgent> playerAgentIterator = players.iterator();
        while (playerAgentIterator.hasNext()) {

            PlayerAgent p = playerAgentIterator.next();
            if (p.playerId == playerMessage.getPlayerId()) {
                playerAgentIterator.remove();
                break;
            }
        }
        playerCells.remove(cell);
    }

    private void serverReceivingStartConnection(String ip, int port, String nickname, int cellType) {
        boolean exists = false;

        // Check if the player is there.
        for (PlayerAgent playerAgent : players) {
            if (playerAgent.getPort() == port && playerAgent.getIp().equalsIgnoreCase(ip))
                exists = true;
        }

        if (!exists) {
            PlayerAgent playerAgent = new PlayerAgent(port, ip, currentAvailableId);
            players.add(playerAgent);

            Cell cell = createCell(nickname, cellType);

            playersId.put(currentAvailableId, playerAgent);

            // send cell id.
            server.sendGameState(new StateMessage(currentAvailableId), playerAgent);

            // Adding the cell to the array list.
            playerCells.add(cell);

            // Increase the current ID.
            currentAvailableId++;
        }
    }

    // Generate the cell based on the client choice.
    private Cell createCell(String nickname, int cellType) {
        if (cellType == ClientPlayer.RIVEN)
            return new RivenCell(this, random(-width, width), random(-height, height), 0, 0,
                    35, 4, currentAvailableId, nickname, gameLogic.getShrooms());
        else if (cellType == ClientPlayer.LULU)
            return new LuluCell(this, random(-width, width), random(-height, height), 0, 0,
                    35, 4, currentAvailableId, nickname);
        else if (cellType == ClientPlayer.JAX)
            return new JaxCell(this, random(-width, width), random(-height, height), 0, 0,
                    35, 4, currentAvailableId, nickname);
        else if (cellType == ClientPlayer.ZED)
            return new ZedCell(this, random(-width, width), random(-height, height), 0, 0,
                    35, 4, currentAvailableId, nickname);
        else return null;
    }

    public static void main(String[] args) {
        PApplet.main("Sketches.ServerSketch");
    }

}
