package demolition;

import processing.core.PImage;
import processing.core.PApplet;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;


/**
 * This abstract class is used for functions relevant to all entities that can
 * move in four directions, described as Characters.
 */
public abstract class Character extends Entity {

    /**
     * The current direction of the character.
     */
    public Direction dir;

    /**
     * A map of each animation cycle, pertaining to each direction in which
     * the character could be facing.
     */
    protected Map<Direction, ArrayList<PImage>> frames;

    /**
     * Constructs a new Character object, as called by inherited classes.
     * @param frames The animation frames used by the entity.
     */
    public Character(Map<Direction, ArrayList<PImage>> frames) {
        super(frames.get(Direction.DOWN));
        this.frames = frames;
        this.dir = Direction.DOWN;
    }

    /**
     * Constructs a new Character object, as called by inherited classes.
     * This constructor is used when the entity has a starting position at initialisation.
     * @param frames The animation frames used by the entity.
     * @param startX The initial x-coordinate of the character.
     * @param startY The initial y-coordinate of the character.
     */
    public Character(Map<Direction, ArrayList<PImage>> frames, int startX, int startY) {
        super(frames.get(Direction.DOWN), startX, startY);
        this.frames = frames;
        this.dir = Direction.DOWN;
    }

    public Coordinates draw(PApplet app) {
        app.image(this.currentFrame, App.TILESIZE * this.x, App.TILESIZE * (this.y + 1) + App.UPPEROFFSET - App.CHARHEIGHT);
        return new Coordinates(this.x, this.y);
    }

    /**
     * Used to handle the automatic moving of characters.
     * @param levelMap The current level map.
     * @return Whether the character successfully moved automatically.
     */
    public abstract boolean autoMove(Tile[][] levelMap);

    public void tick() {
        this.gameTime++;
        if (this.gameTime % 12 == 0) {
            this.updateFrame();
        }
    }

    /**
     * Used to handle movement of the character.
     * @param dir The direction in which the character is trying to move.
     * @param levelMap The current level map.
     * @return Whether the move was successful.
     */
    public boolean move(Direction dir, Tile[][] levelMap) {

        Tile tempTile;
        int xOffset = dir.getXOffset();
        int yOffset = dir.getYOffset();

        if ((yOffset*this.y < yOffset*((levelMap.length + yOffset*levelMap.length)/2 - (1 + yOffset)/2 - yOffset) + 1)
            && (xOffset*this.x < xOffset*((levelMap[0].length + xOffset*levelMap[0].length)/2 - (1 + xOffset)/2 - xOffset) + 1)) {
            tempTile = levelMap[this.y + yOffset][this.x + xOffset];
            if ((tempTile == Tile.EMPTY) || (tempTile == Tile.GOAL)) {
                this.x += xOffset;
                this.y += yOffset;
                this.dir = dir;
                this.setFrames(this.frames.get(dir));
                return true;
            }
        }

        return false;

    }

}
