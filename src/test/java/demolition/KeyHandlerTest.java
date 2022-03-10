package demolition;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.HashMap;
import processing.core.PImage;
import java.util.Map;

public class KeyHandlerTest {

    GraphicsHandler graphicsHandler;
    KeyHandler keyHandler;
    Tile[][] levelMap;
    Map<Direction, ArrayList<PImage>> dummyFrames;

    @BeforeEach
    public void setupObjects() {

        dummyFrames = new HashMap<Direction, ArrayList<PImage>>();
        ArrayList<PImage> tempList = new ArrayList<PImage>();
        tempList.add(new PImage());
        dummyFrames.put(Direction.DOWN, tempList);
        dummyFrames.put(Direction.UP, tempList);
        dummyFrames.put(Direction.LEFT, tempList);
        dummyFrames.put(Direction.RIGHT, tempList);

        keyHandler = new KeyHandler();
        App app = new App();
        graphicsHandler = new GraphicsHandler(app);
        graphicsHandler.bombFrames = new ArrayList<PImage>();
        graphicsHandler.bombFrames.add(new PImage());
        graphicsHandler.explosionFrames = new HashMap<Direction, PImage>();

        levelMap = new Tile[5][5];
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 5; j++)
                levelMap[i][j] = Tile.EMPTY;

    }

    @Test
    public void testKeyPress() {
        Player player = new Player(dummyFrames);
        player.x = 2;
        player.y = 2;
        ArrayList<Bomb> bombs = new ArrayList<Bomb>();

        assertFalse(keyHandler.handlePress(100, player, bombs, graphicsHandler, levelMap));
        assertTrue(keyHandler.handlePress(37, player, bombs, graphicsHandler, levelMap));
        assertTrue(keyHandler.handlePress(38, player, bombs, graphicsHandler, levelMap));
        assertTrue(keyHandler.handlePress(39, player, bombs, graphicsHandler, levelMap));
        assertTrue(keyHandler.handlePress(40, player, bombs, graphicsHandler, levelMap));
        assertTrue(keyHandler.handlePress(32, player, bombs, graphicsHandler, levelMap));
        assertFalse(keyHandler.handlePress(37, player, bombs, graphicsHandler, levelMap));
        assertFalse(keyHandler.handlePress(38, player, bombs, graphicsHandler, levelMap));
        assertFalse(keyHandler.handlePress(39, player, bombs, graphicsHandler, levelMap));
        assertFalse(keyHandler.handlePress(40, player, bombs, graphicsHandler, levelMap));
        assertFalse(keyHandler.handlePress(32, player, bombs, graphicsHandler, levelMap));
    }

    @Test
    public void testKeyRelease() {
        assertFalse(keyHandler.handleRelease(100));
        assertTrue(keyHandler.handleRelease(32));
        assertTrue(keyHandler.handleRelease(37));
        assertTrue(keyHandler.handleRelease(38));
        assertTrue(keyHandler.handleRelease(39));
        assertTrue(keyHandler.handleRelease(40));
    }

}
