package demolition;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import processing.core.PImage;

/**
 * Handles all information and functionality relevant to a single Red Enemy.
 */
public class RedEnemy extends Character {

    /**
     * A list of each moveable direction that is randomised for use by this
     * enemy type's autoMove method.
     */
    private List<Direction> randomDirections;

    /**
     * Constructs a new RedEnemy. This includes initialising the list of
     * random directions used in the autoMove method.
     * @param frames A map of the RedEnemy's animation frames.
     * @param x The initial x-coord of the enemy.
     * @param y The initial y-coord of the enemy.
     */
    public RedEnemy(Map<Direction, ArrayList<PImage>> frames, int x, int y) {
        super(frames, x, y);
        randomDirections = new ArrayList<Direction>();
        randomDirections.add(Direction.UP);
        randomDirections.add(Direction.DOWN);
        randomDirections.add(Direction.LEFT);
        randomDirections.add(Direction.RIGHT);
    }

    public boolean autoMove(Tile[][] levelMap) {
        if (this.gameTime % 60 == 0) {
            if (!this.move(this.dir, levelMap)) {
                Collections.shuffle(this.randomDirections);
                for (Direction randDir : this.randomDirections) {
                    if (this.move(randDir, levelMap)) {
                        return true;
                    }
                }
            }
            return true;
        }
        return false;
    }

}
