package demolition;

import java.util.Map;
import java.util.ArrayList;
import processing.core.PImage;

/**
 * Handles all information and functionality relevant to a single Yellow Enemy.
 */
public class YellowEnemy extends Character {

    /**
     * Constructs a new YellowEnemy object.
     * @param frames A map of the YellowEnemy's animation frames.
     * @param x The initial x-coord of the enemy.
     * @param y The initial y-coord of the enemy.
     */
    public YellowEnemy(Map<Direction, ArrayList<PImage>> frames, int x, int y) {
        super(frames, x, y);
    }

    public boolean autoMove(Tile[][] levelMap) {
        if (this.gameTime % 60 == 0) {
            Direction originalDir = this.dir;
            while (!this.move(this.dir, levelMap)) {
                this.dir = this.dir.rotateClockwise();
                if (this.dir == originalDir) {
                    return true;
                }
            }
            return true;
        }
        return false;
    }

}
