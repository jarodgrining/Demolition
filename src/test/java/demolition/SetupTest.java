package demolition;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import processing.core.PApplet;
import processing.core.PImage;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

public class SetupTest {

    Setup setup;
    App app;
    GraphicsHandler graphicsHandler;

    @BeforeEach
    public void setupObject() {
        app = new App();
        setup = new Setup();
        app.noLoop();
        PApplet.runSketch(new String[] {"App"}, app);
        app.delay(1000);
        graphicsHandler = new GraphicsHandler(app);
    }

    @Test
    public void testPlayerSetup() {
        assertNotNull(setup.player(app));
    }

    @Test
    public void testJSONReading() {
        Map<Direction, ArrayList<PImage>> dummyFrames = new HashMap<Direction, ArrayList<PImage>>();
        ArrayList<PImage> dummyList = new ArrayList<PImage>();
        dummyList.add(new PImage());
        dummyFrames.put(Direction.DOWN, dummyList);
        Player player = new Player(dummyFrames);
        assertTrue(setup.parseJSON(player, "src/test/resources/"));
        assertFalse(setup.parseJSON(player, "nonexistent/file/path"));
    }

    @Test
    public void testEnemySetup() {
        assertNotNull(setup.enemies(app, graphicsHandler));
    }

    @Test
    public void testBombSetup() {
        assertNotNull(setup.bombs(app, graphicsHandler));
    }

    @Test
    public void testExplosionSetup() {
        assertNotNull(setup.explosions(app, graphicsHandler));
    }

    @Test
    public void testImagesAndFontSetup() {
        assertNotNull(setup.loadImages(app, graphicsHandler));
    }

}
