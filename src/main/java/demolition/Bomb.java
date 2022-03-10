package demolition;

import processing.core.PImage;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

/**
 * The Bomb class is used to store information and handle functions related to
 * particular bomb entities.
 */
public class Bomb extends Entity {

    /**
     * A map of all explosion images to be passed into new explosions when they
     * are generated upon the bomb exploding.
     */
    Map<Direction, PImage> explosionImages;

    /**
     * Constructs a new bomb object to be drawn and eventually exploded.
     * @param frames, A List of the frames that the bomb uses when drawn.
     * @param explosionImages, A Map of images used in explosions.
     * @param x The x-coordinate of the bomb.
     * @param y The y-coordinate of the bomb.
     */
    public Bomb(List<PImage> frames, Map<Direction, PImage> explosionImages, int x, int y) {
        super(frames, x, y);
        this.explosionImages = explosionImages;
    }

    /**
     * Updates the game time and animation frame of the bomb.
     */
    public void tick() {
        this.gameTime++;
        if (this.gameTime % 15 == 0) {
            this.updateFrame();
        }
        // explosion checking handled in App.java
    }

    /**
     * Checks if the bomb is ready to explode, and if so, performs the explosion.
     * This involves generating the explosion entities.
     * @param levelMap The map of the current level.
     * @return A list of explosion entities generated, or null if the bomb doesn't explode.
     */
    public List<Explosion> checkExplode(Tile[][] levelMap) {
        if (this.gameTime > 120) {
            List<Explosion> explosions = new ArrayList<Explosion>();
            explosions.add(new Explosion(this.explosionImages.get(Direction.CENTRE), this.x, this.y));

            int tempX, tempY, offX, offY;

            int xLen = levelMap[0].length;
            int yLen = levelMap.length;

            Direction tempDir = Direction.UP;
            do {
                tempX = this.x;
                tempY = this.y;
                offX = tempDir.getXOffset();
                offY = tempDir.getYOffset();

                // i can assure you, dear marker, that this bizarrely convoluted implementation is better than repeating this code four times
                // however, if you put a gun to my head and told me to explain how I came up with these inequations and why they work, I'd beg you to pull the trigger
                while ((tempX * offX < this.x + offX * 2) && (tempX * offX < (xLen + offX * xLen) / 2 - (3 + offX) / 2)
                    && (tempY * offY < this.y + offY * 2) && (tempY * offY < (yLen + offY * yLen) / 2 - (3 + offY) / 2)) {
                    tempX += offX;
                    tempY += offY;

                    if (levelMap[tempY][tempX] == Tile.SOLID) {
                        break;
                    } else if (levelMap[tempY][tempX] == Tile.BROKEN) {
                        levelMap[tempY][tempX] = Tile.EMPTY;
                        explosions.add(new Explosion(this.explosionImages.get(tempDir), tempX, tempY));
                        break;
                    } else if ((tempX == this.x + offX * 2) && (tempY == this.y + offY * 2)) {
                        explosions.add(new Explosion(this.explosionImages.get(tempDir), tempX, tempY));
                        break;
                    } else {
                        explosions.add(new Explosion(this.explosionImages.get(tempDir.getAxis()), tempX, tempY));
                    }
                }

                tempDir = tempDir.rotateClockwise();
            } while (tempDir != Direction.UP);

            return explosions;

        }
        return null;
    }

}
