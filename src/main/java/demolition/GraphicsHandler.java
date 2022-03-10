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
 * A helper class for handling graphics. This includes storing and drawing images.
 */
public class GraphicsHandler {

    public static final int[] ORANGE = {239, 129, 0};
    public static final int[] GREEN = {75, 105, 47};

    public App app;

    /**
     * Maps of the animation cycles of each enemy type.
     */
    public Map<Direction, ArrayList<PImage>> redFrames, yellowFrames;

    /**
     * The animation cycle for bombs.
     */
    public List<PImage> bombFrames;

    /**
     * A map of all tile images.
     */
    public Map<Tile, PImage> tileImages;

    /**
     * A map of all explosion images.
     */
    public Map<Direction, PImage> explosionFrames;

    /**
     * Icons for the UI at the top of the screen.
     */
    public PImage livesIcon, timeIcon;

    /**
     * The pixel coordinates on the screen at which the time must be rendered.
     */
    public Coordinates timeCoords;

    /**
     * A stack used for storing the coordinates of tiles that need to be redrawn.
     */
    public Stack<Coordinates> redrawTiles;

    /**
     * Constructs the GraphicsHandler object. Initialises a new redrawTiles stack.
     * @param app The app object.
     */
    public GraphicsHandler(App app) {
        this.app = app;
        redrawTiles = new Stack<Coordinates>();
    }

    /**
     * Draws the level map to the screen, including the lives and time remaining.
     * @param levelMap The current level map.
     * @param player The Player object.
     */
    public void drawLevel(Tile[][] levelMap, Player player) {

        app.fill(ORANGE[0], ORANGE[1], ORANGE[2]);
        app.stroke(ORANGE[0], ORANGE[1], ORANGE[2]);
        app.rect(0, 0, App.WIDTH, App.HEIGHT);

        for (int i = 0; i < levelMap.length; i++)
            for (int j = 0; j < levelMap[0].length; j++)
                app.image(tileImages.get(levelMap[i][j]), j*App.TILESIZE, i*App.TILESIZE + App.UPPEROFFSET);

        // do upper ui

        int xLives = (int) (App.WIDTH * 4 / 15);
        int xTime = (int) (App.WIDTH * 8 / 15);
        int yIcons = (int) (App.UPPEROFFSET / 4);

        app.image(livesIcon, xLives, yIcons);
        app.image(timeIcon, xTime, yIcons);

        app.fill(0, 0, 0);
        app.text(player.lives, xLives + livesIcon.width + 5, yIcons + (int) ((livesIcon.height + App.FONTSIZE) / 2));

        timeCoords = new Coordinates(xTime + timeIcon.width + 5, yIcons + (int) ((timeIcon.height + App.FONTSIZE) / 2));

    }

    /**
     * Calls all draw methods that must be called every frame.
     * @param gameState The GameState object used in the app.
     */
    public void handleAllGraphics(GameState gameState) {
        redrawTiles(gameState.levelMap);
        drawPlayer(gameState.player);
        drawEnemies(gameState.enemies);
        drawBombs(gameState.bombs);
        drawExplosions(gameState.explosions);
        drawTime(gameState.timeLeft);
    }

    /**
     * Based on the redrawTiles stack, redraws tiles that were previously covered by entities.
     * @param levelMap The current level map.
     */
    public void redrawTiles(Tile[][] levelMap) {

        app.fill(ORANGE[0], ORANGE[1], ORANGE[2]);
        app.stroke(ORANGE[0], ORANGE[1], ORANGE[2]);

        Coordinates tempCoords;
        while (redrawTiles.size() > 0) {
            tempCoords = redrawTiles.pop();

            if (tempCoords.y < 0) {
                app.rect(tempCoords.x * App.TILESIZE, tempCoords.y * App.TILESIZE + App.UPPEROFFSET, App.TILESIZE, App.TILESIZE);
            } else {
                app.image(tileImages.get(levelMap[tempCoords.y][tempCoords.x]), tempCoords.x * App.TILESIZE, tempCoords.y * App.TILESIZE + App.UPPEROFFSET);
            }
        }

    }

    /**
     * Draws the player.
     * @param player The Player object.
     */
    public void drawPlayer(Player player) {
        addCharRedraw(player.draw(app));
    }

    /**
     * Draws the enemies.
     * @param enemies The List of enemies currently in the game.
     */
    public void drawEnemies(List<Character> enemies) {
        for (Character enemy : enemies) {
            addCharRedraw(enemy.draw(app));
        }
    }

    /**
     * Draws the bombs.
     * @param bombs The List of bombs currently in the game.
     */
    public void drawBombs(List<Bomb> bombs) {
        for (Bomb bomb : bombs) {
            redrawTiles.push(bomb.draw(app));
        }
    }

    /**
     * Draws the explosions.
     * @param explosions The List of explosions currently in the game.
     */
    public void drawExplosions(List<Explosion> explosions) {
        for (Explosion explosion : explosions) {
            redrawTiles.push(explosion.draw(app));
        }
    }

    /**
     * Draws the new time to the top of the screen.
     * @param timeLeft The current time remaining.
     */
    public void drawTime(int timeLeft) {
        app.fill(ORANGE[0], ORANGE[1], ORANGE[2]);
        app.stroke(ORANGE[0], ORANGE[1], ORANGE[2]);
        app.rect(timeCoords.x, timeCoords.y - App.FONTSIZE, App.WIDTH, App.FONTSIZE);
        app.fill(0, 0, 0);
        app.text((int) (timeLeft/60), timeCoords.x, timeCoords.y);
    }

    /**
     * Used when the tiles beneath a character need to be redrawn, to account
     * for the additional height of character sprites that occupies another tile.
     * @param coords The coordinates of the relevant character.
     */
    public void addCharRedraw(Coordinates coords) {
        redrawTiles.push(coords);
        Coordinates upperCoords = new Coordinates(coords.x, coords.y-1);
        redrawTiles.push(upperCoords);
    }

    /**
     * Displays the win screen.
     */
    public void displayWin() {
        app.fill(ORANGE[0], ORANGE[1], ORANGE[2]);
        app.stroke(ORANGE[0], ORANGE[1], ORANGE[2]);
        app.rect(0, 0, App.WIDTH, App.HEIGHT);
        app.fill(0, 0, 0);
        app.text("YOU WIN", (int) (App.WIDTH * 8 / 20), (int) ((App.HEIGHT - App.FONTSIZE) / 2));
    }

    /**
     * Displays the game over screen.
     */
    public void displayLoss() {
        app.fill(ORANGE[0], ORANGE[1], ORANGE[2]);
        app.stroke(ORANGE[0], ORANGE[1], ORANGE[2]);
        app.rect(0, 0, App.WIDTH, App.HEIGHT);
        app.fill(0, 0, 0);
        app.text("GAME OVER", (int) (App.WIDTH * 7 / 20), (int) ((App.HEIGHT - App.FONTSIZE) / 2));
    }

}
