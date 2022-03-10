package demolition;

import processing.core.*;
import processing.data.*;
import processing.event.*;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Stack;
import java.util.Map;
import java.util.HashMap;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * The App class implements the methods for setting up and drawing the game
 * and handling player input. Almost all of the logic and varibales is offloaded
 * to Setup, GameState, GraphicsHandler and KeyHandler. App merely helps them interact.
 */
public class App extends PApplet {

    public static final int WIDTH = 480;
    public static final int HEIGHT = 480;
    public static final int MAPHEIGHT = 13;
    public static final int MAPWIDTH = 15;
    public static final int CHARHEIGHT = 48;
    public static final int UPPEROFFSET = 64;
    public static final int TILESIZE = 32;
    public static final int FONTSIZE = 16;
    public static final int FPS = 60;

    private Setup setup;
    private KeyHandler keyHandler;
    private GraphicsHandler graphicsHandler;
    private GameState gameState;

    public App() {
    }

    /**
     * Provides settings (window size) for the PApplet
     */
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    /**
     * Initialises the Setup, KeyHandler, GraphicsHandler and GameState objects
     * as well as setting attributes for the app such as the framerate,
     * background colour and font.
     * Also calls initialisation methods in the aforementioned objects.
     */
    public void setup() {

        setup = new Setup();
        keyHandler = new KeyHandler();
        graphicsHandler = new GraphicsHandler(this);
        gameState = new GameState();

        gameState.player = setup.player(this);
        gameState.enemies = setup.enemies(this, graphicsHandler);
        gameState.bombs = setup.bombs(this, graphicsHandler);
        gameState.explosions = setup.explosions(this, graphicsHandler);

        frameRate(FPS);
        background(GraphicsHandler.ORANGE[0], GraphicsHandler.ORANGE[1], GraphicsHandler.ORANGE[2]);
        setup.parseJSON(gameState.player, "");

        textFont(setup.loadImages(this, graphicsHandler));

        gameState.levelMap = gameState.initialiseLevelMap(setup, graphicsHandler, "");
        graphicsHandler.drawLevel(gameState.levelMap, gameState.player);

    }

    /**
     * Calls methods in GraphicsHandler and GameState in order to draw and
     * update all entities in the game.
     */
    public void draw() {
        if (gameState.gameEnded) {
            return;
        }
        graphicsHandler.handleAllGraphics(gameState);
        if (gameState.updateAllEntities()) {
            gameState.doLoss(setup, graphicsHandler);
        }
    }

    /**
     * Calls methods in KeyHandler and GameState in order to handle input
     * from the player.
     * @param k A KeyEvent passed in by processing.
     */
    public void keyPressed(KeyEvent k) {
        if (gameState.gameEnded) {
            return;
        }
        if (keyHandler.handlePress(k.getKeyCode(), gameState.player, gameState.bombs, graphicsHandler, gameState.levelMap)) {
            gameState.checkLoss(setup, graphicsHandler);
            gameState.checkWin(setup, graphicsHandler);
        }
    }

    /**
     * Calls methods in KeyHandler to reflect that the player has released a key.
     * @param k A KeyEvent passed in by processing.
     */
    public void keyReleased(KeyEvent k) {
        if (gameState.gameEnded) {
            return;
        }
        keyHandler.handleRelease(k.getKeyCode());
    }

    /**
     * Runs the app.
     * @param args, Command line arguments.
     */
    public static void main(String[] args) {
        PApplet.main("demolition.App");
    }
}
