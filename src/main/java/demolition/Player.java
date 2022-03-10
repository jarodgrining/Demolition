package demolition;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import processing.core.PImage;

/**
 * Stores all information and handles all functions relevant to a particular
 * instance of the Player.
 */
public class Player extends Character {

    /**
     * The Player's current remaining lives.
     */
    public int lives;

    /*
     * Constructs the Player object.
     * @param frames A map of the animation frames for the player.
     */
    public Player(Map<Direction, ArrayList<PImage>> frames) {
        super(frames);
    }

    public boolean autoMove(Tile[][] levelMap) {
        return false;
    }

    /**
     * Makes the player drop a new bomb.
     * @param bombs The List of bombs to which the new bomb is added.
     * @param graphicsHandler The GraphicsHandler object used in the app.
     */
    public void dropBomb(List<Bomb> bombs, GraphicsHandler graphicsHandler) {
        bombs.add(new Bomb(graphicsHandler.bombFrames, graphicsHandler.explosionFrames, this.x, this.y));
    }

}
