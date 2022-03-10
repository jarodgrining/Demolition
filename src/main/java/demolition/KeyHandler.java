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
 * A helper class for handling keyboard input by the player.
 */
public class KeyHandler {

    /**
     * Stores the state of their respective keys, that is, whether that key is currently down.
     */
    public boolean upDown, downDown, leftDown, rightDown, spaceDown;

    /**
     * Constructs the KeyHandler object.
     */
    public KeyHandler() {
        upDown = false;
        downDown = false;
        leftDown = false;
        rightDown = false;
        spaceDown = false;
    }

    /**
     * Handles a new key press from the player. This includes moving and dropping bombs.
     * @param keyCode The JS code of the key being pressed.
     * @param player The Player object.
     * @param bombs The List of current bombs.
     * @param graphicsHandler The GraphicsHandler object used in the app.
     * @param levelMap The current level map.
     * @return Whether the press successfully performed an action.
     */
    public boolean handlePress(int keyCode, Player player, List<Bomb> bombs, GraphicsHandler graphicsHandler, Tile[][] levelMap) {
        // space - 32
        // left - 37
        // up - 38
        // right - 39
        // down - 40

        if ((37 <= keyCode) && (keyCode <= 40)) {

            Direction dir = null;

            switch (keyCode) {

                case 37:
                    if (!leftDown) {
                        leftDown = true;
                        dir = Direction.LEFT;
                    }
                    break;

                case 38:
                    if (!upDown) {
                        upDown = true;
                        dir = Direction.UP;
                    }
                    break;

                case 39:
                    if (!rightDown) {
                        rightDown = true;
                        dir = Direction.RIGHT;
                    }
                    break;

                case 40:
                    if (!downDown) {
                        downDown = true;
                        dir = Direction.DOWN;
                    }
                    break;

            }

            if (dir != null) {
                return player.move(dir, levelMap);
            }

        } else if (keyCode == 32 && !spaceDown) {
            spaceDown = true;
            player.dropBomb(bombs, graphicsHandler);
            return true;
        }

        return false;

    }

    /**
     * Resets the keyDown variables when a key is released.
     * @param keyCode The JS code of the key being released.
     * @return Whether this actually resulted in a keyDown variable being reset.
     */
    public boolean handleRelease(int keyCode) {
        switch (keyCode) {
            case 32:
                spaceDown = false;
                break;
            case 37:
                leftDown = false;
                break;
            case 38:
                upDown = false;
                break;
            case 39:
                rightDown = false;
                break;
            case 40:
                downDown = false;
                break;
            default:
                return false;
        }
        return true;
    }

}
