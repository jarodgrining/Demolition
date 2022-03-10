package demolition;

import processing.core.PImage;
import processing.core.PApplet;
import java.util.List;
import java.util.ArrayList;

/**
 * This abstract class is used to store and handle functions common to all entities.
 */
public abstract class Entity {

    /**
     * The current entity's coordinates.
     */
    public int x, y;

    /**
     * The number of ticks since the entity was created.
     */
    public int gameTime;

    /**
     * The frame in the animation cycle it's currently on.
     */
    protected int animationState;

    /**
     * The current frame to be drawn to the screen.
     */
    protected PImage currentFrame;

    /**
     * A List of all frames in the entity's animation cycle.
     */
    protected List<PImage> frames;

    /**
     * Creates a new entity.
     * @param frames The animation frames used by the entity.
     */
    public Entity(List<PImage> frames) {
        this.x = -1;
        this.y = -1;
        this.frames = frames;
        this.animationState = 0;
        this.gameTime = 0;
        this.currentFrame = frames.get(0);
    }

    /**
     * Creates a new entity. This constructor is used when the coordinates are
     * known at time of initialisation.
     * @param frames The animation frames used by the entity.
     * @param startX The entity's initial x-coord.
     * @param startY The entity's initial y-coord.
     */
    public Entity(List<PImage> frames, int startX, int startY) {
        this.x = startX;
        this.y = startY;
        this.frames = frames;
        this.animationState = 0;
        this.gameTime = 0;
        this.currentFrame = frames.get(0);
    }

    /**
     * Creates a new entity. This constructor is used when the entity is not animated.
     * @param image The animation's static image.
     * @param x The entity's x-coord.
     * @param y The entity's y-coord.
     */
    public Entity(PImage image, int x, int y) {
        this.x = x;
        this.y = y;
        this.animationState = 0;
        this.gameTime = 0;
        this.currentFrame = image;
        this.frames = new ArrayList<PImage>();
        this.frames.add(image);
    }

    /**
     * Updates the entity by one tick (1/60 of a second) and handles relevant internal events.
     */
    public abstract void tick();

    /**
      * Draws the entity's current frame at its current position to the game.
      * @param app The app.
      * @return The coordinates at which the entity is drawn.
      */
    public Coordinates draw(PApplet app) { // for some reason if I change the method signature to type App instead of PApplet this breaks the whole program
        app.image(this.currentFrame, App.TILESIZE * this.x, App.TILESIZE * this.y + App.UPPEROFFSET);
        return new Coordinates(this.x, this.y);
    }

    /**
     * Changes the current frame to the next one in the entity's animation cycle.
     */
    public void updateFrame() {
        this.animationState = (this.animationState + 1) % frames.size();
        this.currentFrame = this.frames.get(animationState);
    }

    /**
     * Sets the entity's frames to a new List of frames.
     * @param frames The new List of frames assigned to the entity.
     */
    public void setFrames(List<PImage> frames) {
        this.frames = frames;
        this.currentFrame = this.frames.get(animationState);
    }

}
