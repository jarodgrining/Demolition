package demolition;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class ConstantsTest {

    @Test
    public void app() {
        assertEquals(480, App.HEIGHT);
        assertEquals(480, App.WIDTH);
        assertEquals(13, App.MAPHEIGHT);
        assertEquals(15, App.MAPWIDTH);
        assertEquals(48, App.CHARHEIGHT);
        assertEquals(64, App.UPPEROFFSET);
        assertEquals(32, App.TILESIZE);
        assertEquals(16, App.FONTSIZE);
        assertEquals(60, App.FPS);
    }

    @Test
    public void graphicsHandler() {
        assertEquals(239, GraphicsHandler.ORANGE[0]);
        assertEquals(129, GraphicsHandler.ORANGE[1]);
        assertEquals(0, GraphicsHandler.ORANGE[2]);
        assertEquals(75, GraphicsHandler.GREEN[0]);
        assertEquals(105, GraphicsHandler.GREEN[1]);
        assertEquals(47, GraphicsHandler.GREEN[2]);
    }

    @Test
    public void setup() {
        assertEquals("src/main/resources", Setup.RESOURCEPATH);
    }

}
