package demolition;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import processing.core.PImage;

public class MovementTest {

    Tile[][] levelMap;
    Map<Direction, ArrayList<PImage>> dummyFrames;

    @BeforeEach
    public void setupObjects() {
        levelMap = new Tile[4][4];
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                levelMap[i][j] = Tile.EMPTY;
        levelMap[2][1] = Tile.SOLID;

        dummyFrames = new HashMap<Direction, ArrayList<PImage>>();
        ArrayList<PImage> tempList = new ArrayList<PImage>();
        tempList.add(new PImage());
        dummyFrames.put(Direction.DOWN, tempList);
        dummyFrames.put(Direction.UP, tempList);
        dummyFrames.put(Direction.LEFT, tempList);
        dummyFrames.put(Direction.RIGHT, tempList);
    }

    @Test
    public void testPlayerMove() {
        Player player = new Player(dummyFrames);
        player.x = 1;
        player.y = 1;
        assertFalse(player.move(Direction.DOWN, levelMap));
        assertEquals(player.x, 1);
        assertEquals(player.y, 1);
        assertTrue(player.move(Direction.RIGHT, levelMap));
        assertEquals(player.x, 2);
        assertEquals(player.y, 1);
        assertFalse(player.autoMove(levelMap));
    }

    @Test
    public void testRedEnemy() {
        RedEnemy redEnemy = new RedEnemy(dummyFrames, 1, 1);
        assertFalse(redEnemy.move(Direction.DOWN, levelMap));
        assertEquals(redEnemy.x, 1);
        assertEquals(redEnemy.y, 1);
        assertTrue(redEnemy.move(Direction.RIGHT, levelMap));
        assertEquals(redEnemy.x, 2);
        assertEquals(redEnemy.y, 1);

        redEnemy.gameTime = 30;
        assertFalse(redEnemy.autoMove(levelMap));

        redEnemy.gameTime = 60;
        assertTrue(redEnemy.autoMove(levelMap));

        switch (redEnemy.dir) {
            case UP:
                assertEquals(redEnemy.x, 2);
                assertEquals(redEnemy.y, 0);
                break;

            case DOWN:
                assertEquals(redEnemy.x, 2);
                assertEquals(redEnemy.y, 2);
                break;

            case LEFT:
                assertEquals(redEnemy.x, 1);
                assertEquals(redEnemy.y, 1);
                break;

            case RIGHT:
                assertEquals(redEnemy.x, 3);
                assertEquals(redEnemy.y, 1);
                break;
        }

        redEnemy.x = 1;
        redEnemy.y = 1;
        redEnemy.dir = Direction.DOWN;
        assertTrue(redEnemy.autoMove(levelMap));
        assertFalse(redEnemy.dir == Direction.DOWN);

        switch (redEnemy.dir) {
            case UP:
                assertEquals(redEnemy.x, 1);
                assertEquals(redEnemy.y, 0);
                break;

            case LEFT:
                assertEquals(redEnemy.x, 0);
                assertEquals(redEnemy.y, 1);
                break;

            case RIGHT:
                assertEquals(redEnemy.x, 2);
                assertEquals(redEnemy.y, 1);
                break;
        }
    }

    @Test
    public void testYellowEnemy() {
        YellowEnemy yellowEnemy = new YellowEnemy(dummyFrames, 1, 1);
        assertFalse(yellowEnemy.move(Direction.DOWN, levelMap));
        assertEquals(yellowEnemy.x, 1);
        assertEquals(yellowEnemy.y, 1);
        assertTrue(yellowEnemy.move(Direction.RIGHT, levelMap));
        assertEquals(yellowEnemy.x, 2);
        assertEquals(yellowEnemy.y, 1);

        yellowEnemy.gameTime = 30;
        assertFalse(yellowEnemy.autoMove(levelMap));

        yellowEnemy.gameTime = 60;
        assertTrue(yellowEnemy.autoMove(levelMap));

        yellowEnemy.x = 1;
        yellowEnemy.y = 1;
        yellowEnemy.dir = Direction.DOWN;
        assertTrue(yellowEnemy.autoMove(levelMap));
        assertEquals(yellowEnemy.dir, Direction.LEFT);
        assertEquals(yellowEnemy.x, 0);
        assertEquals(yellowEnemy.y, 1);
    }

}
