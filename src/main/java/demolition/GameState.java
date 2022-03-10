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
 * A helper class used to store and handle all data and functions relevant to
 * handling the current state of the game.
 */
public class GameState {

    /**
     * The current level map, using the Tile enumerator.
     */
    public Tile[][] levelMap;

    /**
     * The current level in the game.
     */
    public int currentLevel;

    /**
     * The time remaining in the current level (before the player dies).
     */
    public int timeLeft;

    /**
     * The coordinates of the goal in the level map, stored for convenience.
     */
    public Coordinates goalCoords;

    /**
     * Whether the game has been concluded.
     */
    public boolean gameEnded;

    /**
     * List of all enemies.
     */
    public List<Character> enemies;

    /**
     * List of all bombs.
     */
    public List<Bomb> bombs;

    /**
     * List of all explosions.
     */
    public List<Explosion> explosions;

    /**
     * The Player object.
     */
    public Player player;

    /**
     * Constructs the new GameState object. Sets the current level to 0.
     */
    public GameState() {
        currentLevel = 0;
    }

    /**
     * Initialises the level map. This involves reading the level file and
     * storing its relevant data in a new Tile array as well as adding to the
     * Player object and List of enemies already existing in GameState.
     * If the specified level file is not found, an empty map is instead generated.
     * @param setup The Setup object used in the app.
     * @param graphicsHandler The GraphicsHandler object used in the app.
     * @param filePathExtension A prefix to add to the level file path. Only used in testing.
     * @return A new Tile array representing a level map.
     */
    public Tile[][] initialiseLevelMap(Setup setup, GraphicsHandler graphicsHandler, String filePathExtension) {
        Tile[][] output = new Tile[App.MAPHEIGHT][App.MAPWIDTH];
        timeLeft = setup.levelTimes.get(currentLevel) * App.FPS;
        try {
            File f = new File(filePathExtension + setup.levelFiles.get(currentLevel));
            Scanner scan = new Scanner(f);
            String line;
            int i = 0;

            while (scan.hasNextLine()) {
                line = scan.nextLine();
                for (int j = 0; j < line.length(); j++) {
                    output[i][j] = Tile.EMPTY;
                    switch (line.charAt(j)) {
                        case 'W':
                        output[i][j] = Tile.SOLID;
                        break;

                        case 'B':
                        output[i][j] = Tile.BROKEN;
                        break;

                        case 'G':
                        output[i][j] = Tile.GOAL;
                        goalCoords = new Coordinates(j, i);
                        break;

                        case 'P':
                        player.x = j;
                        player.y = i;
                        break;

                        case 'R':
                        enemies.add(new RedEnemy(graphicsHandler.redFrames, j, i));
                        break;

                        case 'Y':
                        enemies.add(new YellowEnemy(graphicsHandler.yellowFrames, j, i));
                        break;
                    }
                }
                i++;
            }
        } catch (FileNotFoundException e) {
            for (int i = 0; i < App.MAPHEIGHT; i++)
                for (int j = 0; j < App.MAPWIDTH; j++)
                    output[i][j] = Tile.EMPTY;
            player.x = 0;
            player.y = 0;
            goalCoords = new Coordinates(App.MAPWIDTH-1, App.MAPHEIGHT-1);
        }

        return output;

    }

    /**
     * Updates all entities in the game.
     * @return Whether the player now occupies the same tile as an enemy or explosion.
     */
    public boolean updateAllEntities() {
        player.tick();
        updateBombs();
        if (updateEnemies() || updateExplosions()) {
            return true;
        }
        timeLeft--;
        return false;
    }

    /**
     * Updates all enemies in the game.
     * @return Whether at least one enemy has moved into a tile with the Player.
     */
    public boolean updateEnemies() {
        for (Character enemy : enemies) {
            if (enemy.autoMove(levelMap)) {
                if (checkEnemyKill(enemy)) {
                    return true;
                }
            }
            enemy.tick();
        }
        return false;
    }

    /**
     * Updates all bombs in the game, including exploding them.
     * @return Whether the updating was successful.
     */
    public boolean updateBombs() {
        List<Explosion> tempExplosions;
        List<Bomb> bombsToRemove = new LinkedList<Bomb>();
        for (Bomb bomb : bombs) {
            bomb.tick();
            tempExplosions = bomb.checkExplode(levelMap);
            if (tempExplosions != null) {
                explosions.addAll(tempExplosions);
                bombsToRemove.add(bomb);
            }
        }

        for (Bomb bomb : bombsToRemove) {
            bombs.remove(bomb);
        }
        return true;
    }

    /**
     * Updates all explosions in the game.
     * @return Whether the player now occupies the same tile as the Player.
     */
    public boolean updateExplosions() {
        List<Explosion> explosionsToRemove = new LinkedList<Explosion>();
        for (Explosion explosion : explosions) {
            if (checkExplosionKill(explosion)) {
                return true;
            }
            explosion.tick();
            if (explosion.checkDisappear()) {
                explosionsToRemove.add(explosion);
            }
        }

        for (Explosion explosion : explosionsToRemove) {
            explosions.remove(explosion);
        }

        return false;
    }

    /**
     * Handles the collision of an explosion with Characters in order to kill them.
     * @param explosion The explosion object to be checked.
     * @return Whether the explosion has killed the Player.
     */
    public boolean checkExplosionKill(Explosion explosion) {
        if ((player.x == explosion.x) && (player.y == explosion.y)) {
            return true;
        } else {
            List<Character> enemiesToKill = new LinkedList<Character>();

            for (Character enemy : enemies) {
                if ((explosion.x == enemy.x) && (explosion.y == enemy.y)) {
                    enemiesToKill.add(enemy);
                }
            }

            for (Character enemy : enemiesToKill) {
                enemies.remove(enemy);
            }
        }
        return false;
    }

    /**
     * Check whether a given enemy has killed the Player.
     * @param enemy The enemy to check.
     * @return Whether the enemy occupies the same tile as the Player.
     */
    public boolean checkEnemyKill(Character enemy) {
        return ((player.x == enemy.x) && (player.y == enemy.y));
    }

    /**
     * Check whether the player has won a level, and if so, performs the relevant tasks.
     * @param setup The Setup object used in the app.
     * @param graphicsHandler The GraphicsHandler object used in the app.
     * @return Whether the final level has been won by the Player.
     */
    public boolean checkWin(Setup setup, GraphicsHandler graphicsHandler) {
        if ((player.x == goalCoords.x) && (player.y == goalCoords.y)) {
            currentLevel++;
            if (currentLevel >= setup.levelFiles.size()) {
                gameEnded = true;
                graphicsHandler.displayWin();
                return true;
            } else {
                resetLevel(setup, graphicsHandler);
            }
        }
        return false;
    }

    /**
     * Resets the level by clearing all entities, then re-initialising the map.
     * @param setup The Setup object used in the app.
     * @param graphicsHandler The GraphicsHandler object used in the app.
     */
    public void resetLevel(Setup setup, GraphicsHandler graphicsHandler) {

        enemies.clear();
        bombs.clear();
        explosions.clear();
        graphicsHandler.redrawTiles.clear();

        levelMap = initialiseLevelMap(setup, graphicsHandler, "");
        graphicsHandler.drawLevel(levelMap, player);

    }

    /**
     * Checks whether the player has lost, and if so, performs the relevant tasks.
     * @param setup The Setup object used in the app.
     * @param graphicsHandler The GraphicsHandler object used in the app.
     * @return Whether the level has been lost.
     */
    public boolean checkLoss(Setup setup, GraphicsHandler graphicsHandler) {
        for (Explosion explosion : explosions) {
            if ((player.x == explosion.x) && (player.y == explosion.y)) {
                doLoss(setup, graphicsHandler);
                return true;
            }
        }

        for (Character enemy : enemies) {
            if ((player.x == enemy.x) && (player.y == enemy.y)) {
                doLoss(setup, graphicsHandler);
                return true;
            }
        }
        return false;
    }

    /**
     * Performs the loss, either by resetting the level or displaying a game over.
     * @param setup The Setup object used in the app.
     * @param graphicsHandler The GraphicsHandler object used in the app.
     * @return Whether the player has run out of lives, resulting in a game over.
     */
    public boolean doLoss(Setup setup, GraphicsHandler graphicsHandler) {
        player.lives--;
        if (player.lives <= 0) {
            gameEnded = true;
            graphicsHandler.displayLoss();
            return true;
        } else {
            resetLevel(setup, graphicsHandler);
        }
        return false;
    }

}
