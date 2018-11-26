package Sketches;

import GameObjects.Cell.Cell;
import Players.ClientPlayer;
import Players.LocalPlayer;
import processing.core.PApplet;

public class PlayerSketch extends BlobbySketch {

    //Players
    private ClientPlayer clientPlayer;
    private LocalPlayer localPlayer;

    // The current players Cell.
    private Cell currentPlayerCell;

    // Screens
    private final int PLAYER_CHOOSING = 0;
    private final int LOCAL_START_SCREEN = 1;
    private final int CLIENT_START_SCREEN = 2;
    public final int CLIENT_GAME_START = 3;
    public final int GAME_OVER_SCREEN = 4;
    private final int LOCAL_START_GAME = 5;

    // Local Players.Player Button Coordinates.
    private float xL = WORLD_WIDTH / 2 - 200;
    private float yL = WORLD_HEIGHT / 2 + 100;
    private int boxWidthL = 200;
    private int boxHeightL = 100;

    // Network.Client Players.Player Button Coordinates.
    private float xC = WORLD_WIDTH / 2 + 300;
    private float yC = WORLD_HEIGHT / 2 + 100;
    private int boxWidthC = 200;
    private int boxHeightC = 100;

    private boolean isClientMode = false;

    public void setup() {
        super.setup();
        setupGameLogic(this);
        localPlayer = new LocalPlayer(this);
        clientPlayer = new ClientPlayer(this);

        // For when the client leaves.
        prepareExitHandler();
    }

    public void draw() {
        clear();
        switch (currentScreen) {

            case PLAYER_CHOOSING:
                playerChoiceScreen();
                break;

            case LOCAL_START_SCREEN:
                localPlayer.localStartScreen();
                break;

            case CLIENT_START_SCREEN:
                clientPlayer.clientStartScreen();
                break;

            case CLIENT_GAME_START:
                currentPlayerCell = getPlayerCell(clientPlayer.getPlayerId());
                if (currentPlayerCell == null)// game over
                    currentScreen = GAME_OVER_SCREEN;
                else
                    clientPlayer.draw();

                break;

            case LOCAL_START_GAME:
                localPlayer.draw();
                break;

            case GAME_OVER_SCREEN:
                gameOverScreen();
                break;
        }
    }

    private void playerChoiceScreen() {
        textAlign(CENTER);
        fill(255);
        background(0);
        textSize(32);
        text("BLOBBY", width / 2, 200);
        gameDrawer.drawButton("Local", xL, yL, boxWidthL, boxHeightL);
        gameDrawer.drawButton("Connect To Server", xC, yC, boxWidthC, boxHeightC);
    }

    public Cell getCurrentPlayerCell() {
        return currentPlayerCell;
    }

    public void keyPressed() {
        // Client Mode.
        if (currentScreen == CLIENT_GAME_START)
            clientPlayer.sendButtonClick(key);

        /*
        To take user input, used code provided by:
        https://amnonp5.wordpress.com/2012/01/28/25-life-saving-tips-for-processing/
         */
        if (currentScreen == CLIENT_START_SCREEN || currentScreen == LOCAL_START_SCREEN) {
            if (keyCode == BACKSPACE) {
                if (clientPlayer.nickname.length() > 0) {
                    clientPlayer.nickname = clientPlayer.nickname.substring(0, clientPlayer.nickname.length() - 1);
                }
            } else if (keyCode == DELETE) {
                clientPlayer.nickname = "";
            } else if (keyCode != SHIFT && keyCode != CONTROL && keyCode != ALT && clientPlayer.nickname.length() < 6) {
                clientPlayer.nickname = clientPlayer.nickname + key;
            }
        }

        // Key Pressed in local mode.
        if (currentScreen == LOCAL_START_GAME) {
            if (key == 'q' || key == 'Q')
                localPlayer.getPlayer().craftPoison();
            if (key == 'w' || key == 'W')
                localPlayer.getPlayer().craftShield();
            if (key == 'e' || key == 'E')
                localPlayer.getPlayer().craftSpike();
            if (key == 'r' || key == 'R')
                localPlayer.getPlayer().craftRAbility();
        }
    }

    public void mousePressed() {
        // To choose between Local or Network.
        if (currentScreen == PLAYER_CHOOSING) {

            if (mouseX > xL && mouseX < xL + boxWidthL && mouseY > yL && mouseY < yL + boxHeightL) {
                currentScreen = LOCAL_START_SCREEN;
            }
            if (mouseX > xC && mouseX < xC + boxWidthC && mouseY > yC && mouseY < yC + boxHeightC) {
                currentScreen = CLIENT_START_SCREEN;
                clientPlayer.setup();
                isClientMode = true;
            }
        } else if (currentScreen == CLIENT_START_SCREEN) { // To indicate that the player is ready. Client

            if (mouseX > clientPlayer.x && mouseX < clientPlayer.x + clientPlayer.boxWidth && mouseY > clientPlayer.y
                    && mouseY < clientPlayer.y + clientPlayer.boxHeight)
                clientPlayer.setPlayerIsReady(!clientPlayer.getPlayerIsReady());

            if (mouseX > clientPlayer.xR && mouseX < clientPlayer.xR + clientPlayer.widthR && mouseY > clientPlayer.yR
                    && mouseY < clientPlayer.yR + clientPlayer.heightR)
                clientPlayer.incrementCurrentCell();

            if (mouseX > clientPlayer.xL && mouseX < clientPlayer.xL + clientPlayer.widthL && mouseY > clientPlayer.yL
                    && mouseY < clientPlayer.yL + clientPlayer.heightL)
                clientPlayer.decrementCurrentCell();

        } else if (currentScreen == LOCAL_START_SCREEN) { // Indicate player is ready, Local.

            if (mouseX > clientPlayer.x && mouseX < clientPlayer.x + clientPlayer.boxWidth && mouseY > clientPlayer.y
                    && mouseY < clientPlayer.y + clientPlayer.boxHeight)
                localPlayer.setPlayerIsReady(!clientPlayer.getPlayerIsReady());

            if (mouseX > clientPlayer.xR && mouseX < clientPlayer.xR + clientPlayer.widthR && mouseY > clientPlayer.yR
                    && mouseY < clientPlayer.yR + clientPlayer.heightR)
                localPlayer.incrementCurrentCell();

            if (mouseX > clientPlayer.xL && mouseX < clientPlayer.xL + clientPlayer.widthL && mouseY > clientPlayer.yL
                    && mouseY < clientPlayer.yL + clientPlayer.heightL)
                localPlayer.decrementCurrentCell();

            if (mouseX > localPlayer.x && mouseX < localPlayer.x + localPlayer.boxWidth && mouseY > localPlayer.y
                    && mouseY < localPlayer.y + localPlayer.boxHeight) {
                localPlayer.setPlayerIsReady(true);
                localPlayer.setup();
                currentScreen = LOCAL_START_GAME;
            }
        }
    }

    /* This method to do some operations when the sketch is closed was obtained
    from https://forum.processing.org/one/topic/run-code-on-exit.html
*/
    private void prepareExitHandler() {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                // only when in network mode this matters.
                if (isClientMode)
                    clientPlayer.sendLeaveRequest();
            }
        }));
    }


    public static void main(String[] args) {
        PApplet.main("Sketches.PlayerSketch");
    }

}
