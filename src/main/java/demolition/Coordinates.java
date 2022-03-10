package demolition;

/**
 * A helper class for storing coordinates. This is useful for containers like
 * Lists and Stacks, to store pairs of integers as coordinates. It can be used
 * to represent both pixels on the screen and tiles on the level map.
 */
public class Coordinates {
    public final int x, y;
    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
