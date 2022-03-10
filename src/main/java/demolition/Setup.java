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
 * A helper class for setting up the game, including loading images and the
 * config file.
 */
public class Setup {

    /**
     * The list of level file names.
     */
    public List<String> levelFiles;

    /**
     * The list of level times, corresponding with the file names.
     */
    public List<Integer> levelTimes;

    /**
     * The font used in the game.
     */
    public PFont font;

    public static final String RESOURCEPATH = "src/main/resources";

    public Setup() {
    }

    /**
     * Sets up the player, including loading the image files.
     * @param app The app object.
     * @return The initialised Player object.
     */
    public Player player(App app) {

        Map<Direction, ArrayList<PImage>> frames = new HashMap<Direction, ArrayList<PImage>>();

        frames.put(Direction.UP, new ArrayList<PImage>());
        frames.put(Direction.DOWN, new ArrayList<PImage>());
        frames.put(Direction.LEFT, new ArrayList<PImage>());
        frames.put(Direction.RIGHT, new ArrayList<PImage>());

        String path = RESOURCEPATH + "/player/player";

        for (int i = 1; i < 5; i++) {
            frames.get(Direction.UP).add(app.loadImage(String.format("%s_up%d.png", path, i)));
            frames.get(Direction.DOWN).add(app.loadImage(String.format("%s%d.png", path, i)));
            frames.get(Direction.LEFT).add(app.loadImage(String.format("%s_left%d.png", path, i)));
            frames.get(Direction.RIGHT).add(app.loadImage(String.format("%s_right%d.png", path, i)));
        }

        return new Player(frames);

    }

    /**
     * Sets up the enemy animation frames.
     * @param app The app object.
     * @param graphicsHandler The GraphicsHandler object used in the app.
     * @return A new, empty list used for enemies.
     */
    public List<Character> enemies(App app, GraphicsHandler graphicsHandler) {

        graphicsHandler.redFrames = new HashMap<Direction, ArrayList<PImage>>();
        graphicsHandler.yellowFrames = new HashMap<Direction, ArrayList<PImage>>();

        graphicsHandler.redFrames.put(Direction.UP, new ArrayList<PImage>());
        graphicsHandler.redFrames.put(Direction.DOWN, new ArrayList<PImage>());
        graphicsHandler.redFrames.put(Direction.LEFT, new ArrayList<PImage>());
        graphicsHandler.redFrames.put(Direction.RIGHT, new ArrayList<PImage>());

        graphicsHandler.yellowFrames.put(Direction.UP, new ArrayList<PImage>());
        graphicsHandler.yellowFrames.put(Direction.DOWN, new ArrayList<PImage>());
        graphicsHandler.yellowFrames.put(Direction.LEFT, new ArrayList<PImage>());
        graphicsHandler.yellowFrames.put(Direction.RIGHT, new ArrayList<PImage>());

        String redPath = RESOURCEPATH + "/red_enemy/red_";
        String yellowPath = RESOURCEPATH + "/yellow_enemy/yellow_";

        for (int i = 1; i < 5; i++) {
            graphicsHandler.redFrames.get(Direction.UP).add(app.loadImage(String.format("%sup%d.png", redPath, i)));
            graphicsHandler.redFrames.get(Direction.DOWN).add(app.loadImage(String.format("%sdown%d.png", redPath, i)));
            graphicsHandler.redFrames.get(Direction.LEFT).add(app.loadImage(String.format("%sleft%d.png", redPath, i)));
            graphicsHandler.redFrames.get(Direction.RIGHT).add(app.loadImage(String.format("%sright%d.png", redPath, i)));
            graphicsHandler.yellowFrames.get(Direction.UP).add(app.loadImage(String.format("%sup%d.png", yellowPath, i)));
            graphicsHandler.yellowFrames.get(Direction.DOWN).add(app.loadImage(String.format("%sdown%d.png", yellowPath, i)));
            graphicsHandler.yellowFrames.get(Direction.LEFT).add(app.loadImage(String.format("%sleft%d.png", yellowPath, i)));
            graphicsHandler.yellowFrames.get(Direction.RIGHT).add(app.loadImage(String.format("%sright%d.png", yellowPath, i)));
        }

        return new ArrayList<Character>();

    }

    /**
     * Sets up the bomb animation frames.
     * @param app The app object.
     * @param graphicsHandler The GraphicsHandler object used in the app.
     * @return A new, empty list used for bombs.
     */
    public List<Bomb> bombs(App app, GraphicsHandler graphicsHandler) {
        graphicsHandler.bombFrames = new ArrayList<PImage>();
        graphicsHandler.bombFrames.add(app.loadImage(RESOURCEPATH + "/bomb/bomb.png"));

        for (int i = 1; i < 9; i++) {
            graphicsHandler.bombFrames.add(app.loadImage(String.format("%s/bomb/bomb%d.png", RESOURCEPATH, i)));
        }
        return new LinkedList<Bomb>();
    }

    /**
     * Sets up the explosion images.
     * @param app The app object.
     * @param graphicsHandler The GraphicsHandler object used in the app.
     * @return A new, empty list used for explosions.
     */
    public List<Explosion> explosions(App app, GraphicsHandler graphicsHandler) {
        String exPath = RESOURCEPATH + "/explosion/";

        graphicsHandler.explosionFrames = new HashMap<Direction, PImage>();
        graphicsHandler.explosionFrames.put(Direction.CENTRE, app.loadImage(exPath + "centre.png"));
        graphicsHandler.explosionFrames.put(Direction.VERTICAL, app.loadImage(exPath + "vertical.png"));
        graphicsHandler.explosionFrames.put(Direction.HORIZONTAL, app.loadImage(exPath + "horizontal.png"));
        graphicsHandler.explosionFrames.put(Direction.UP, app.loadImage(exPath + "end_top.png"));
        graphicsHandler.explosionFrames.put(Direction.DOWN, app.loadImage(exPath + "end_bottom.png"));
        graphicsHandler.explosionFrames.put(Direction.LEFT, app.loadImage(exPath + "end_left.png"));
        graphicsHandler.explosionFrames.put(Direction.RIGHT, app.loadImage(exPath + "end_right.png"));

        return new LinkedList<Explosion>();
    }

    /**
     * Loads the JSON data from the config.json file. This includes loading
     * level file names, level times and the player's lives.
     * @param player The Player object.
     * @param filePathExtension A path prefix for loading the config file. Only used in testing.
     * @return Whether the loading of the config file was successful.
     */
    public boolean parseJSON(Player player, String filePathExtension) {
        try {

            File f = new File(filePathExtension + "config.json");
            Scanner scan = new Scanner(f);
            String plainText = "";

            while (scan.hasNextLine()) {
                plainText += scan.nextLine() + "\n";
            }

            JSONObject data = JSONObject.parse(plainText);
            levelFiles = new ArrayList<String>();
            levelTimes = new ArrayList<Integer>();

            JSONArray levelsArray = data.getJSONArray("levels");
            JSONObject level;

            for (int i = 0; i < levelsArray.size(); i++) {
                level = levelsArray.getJSONObject(i);
                levelFiles.add(level.getString("path"));
                levelTimes.add((Integer) level.getInt("time"));
            }

            player.lives = data.getInt("lives");

            return true;

        } catch (FileNotFoundException e) {
            return false;
        }
    }

    /**
     * Loads other individual images used directly in graphicsHandler.
     * @param app The app object.
     * @param graphicsHandler The GraphicsHandler object used in the app.
     * @return A PFont object represting the font used in the game.
     */
    public PFont loadImages(App app, GraphicsHandler graphicsHandler) {
        graphicsHandler.tileImages = new HashMap<Tile, PImage>();
        graphicsHandler.tileImages.put(Tile.SOLID, app.loadImage(RESOURCEPATH + "/wall/solid.png"));
        graphicsHandler.tileImages.put(Tile.BROKEN, app.loadImage(RESOURCEPATH + "/broken/broken.png"));
        graphicsHandler.tileImages.put(Tile.GOAL, app.loadImage(RESOURCEPATH + "/goal/goal.png"));
        graphicsHandler.tileImages.put(Tile.EMPTY, app.loadImage(RESOURCEPATH + "/empty/empty.png"));

        graphicsHandler.livesIcon = app.loadImage(RESOURCEPATH + "/icons/player.png");
        graphicsHandler.timeIcon = app.loadImage(RESOURCEPATH + "/icons/clock.png");

        font = app.createFont(RESOURCEPATH + "/PressStart2P-Regular.ttf", App.FONTSIZE);
        return font;
    }

}
