package demolition;

import processing.core.PImage;

/**
 * This is used to handle functions relevant to a particular explosion.
 * A single Explosion is defined as the portion of an "explosion" occupying
 * one tile.
 */
public class Explosion extends Entity {

    /**
     * Constructs a new, temporary Explosion object that kills Characters.
     * @param image The explosion's image to be drawn.
     * @param x The x-coord of the explosion.
     * @param y The y-coord of the explosion.
     */
    public Explosion(PImage image, int x, int y) {
        super(image, x, y);
    }

    public void tick() {
        this.gameTime++;
    }

    /**
     * Checks whether the explosion is ready to disappear. If true, it should be
     * removed externally.
     * @return Whether the explosion is ready to disappear.
     */
    public boolean checkDisappear() {
        return (this.gameTime > 120);
    }

}
