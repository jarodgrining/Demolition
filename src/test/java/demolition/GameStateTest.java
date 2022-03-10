package demolition;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import processing.core.PImage;
import processing.core.PApplet;

public class GameStateTest {

    Setup setup;
    App app;
    GraphicsHandler graphicsHandler;
    GameState gameState;
    Map<Direction, ArrayList<PImage>> dummyFrames;

    @BeforeEach
    public void setupObject() {
        app = new App();
        setup = new Setup();
        gameState = new GameState();
        app.noLoop();
        PApplet.runSketch(new String[] {"App"}, app);
        app.delay(1000);
        graphicsHandler = new GraphicsHandler(app);
        dummyFrames = new HashMap<Direction, ArrayList<PImage>>();
        ArrayList<PImage> dummyList = new ArrayList<PImage>();
        dummyList.add(new PImage());
        dummyFrames.put(Direction.DOWN, dummyList);
    }

    @Test
    public void testLevelMapInitialisation() {
        setup.levelTimes = new ArrayList<Integer>();
        setup.levelTimes.add(100);
        setup.levelFiles = new ArrayList<String>();
        setup.levelFiles.add("level1.txt");
        gameState.player = new Player(dummyFrames);
        gameState.enemies = new ArrayList<Character>();
        graphicsHandler.redFrames = dummyFrames;
        graphicsHandler.yellowFrames = dummyFrames;
        assertNotNull(gameState.initialiseLevelMap(setup, graphicsHandler, "src/test/resources/"));
        assertNotNull(gameState.initialiseLevelMap(setup, graphicsHandler, "nonexistent/file/path"));
    }

    @Test
    public void testEntityUpdates() {
        RedEnemy redEnemy = new RedEnemy(dummyFrames, 0, 0);
        gameState.player = new Player(dummyFrames);
        gameState.player.x = 0;
        gameState.player.y = 0;
        assertTrue(gameState.checkEnemyKill(redEnemy));
        gameState.player.x = 1;
        assertFalse(gameState.checkEnemyKill(redEnemy));

        gameState.levelMap = new Tile[4][4];
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                gameState.levelMap[i][j] = Tile.EMPTY;
        gameState.levelMap[2][1] = Tile.SOLID;

        gameState.enemies = new ArrayList<Character>();
        assertFalse(gameState.updateEnemies());
        redEnemy.gameTime = 30;
        gameState.enemies.add(redEnemy);
        assertFalse(gameState.updateEnemies());
        gameState.player.x = redEnemy.x + redEnemy.dir.getXOffset();
        gameState.player.y = redEnemy.y + redEnemy.dir.getYOffset();
        redEnemy.gameTime = 60;
        assertTrue(gameState.updateEnemies());

        gameState.timeLeft = 1000;

        gameState.bombs = new ArrayList<Bomb>();
        gameState.explosions = new ArrayList<Explosion>();
        assertFalse(gameState.updateExplosions());
        gameState.explosions.add(new Explosion(new PImage(), 1, 1));
        assertFalse(gameState.updateExplosions());
        assertFalse(gameState.updateAllEntities());
        gameState.player.x = redEnemy.x + redEnemy.dir.getXOffset();
        gameState.player.y = redEnemy.y + redEnemy.dir.getYOffset();
        redEnemy.gameTime = 60;
        assertTrue(gameState.updateAllEntities());
        Explosion explosion = new Explosion(new PImage(), gameState.player.x, gameState.player.y);
        gameState.explosions.add(explosion);
        assertTrue(gameState.updateExplosions());
    }

    @Test
    public void testBombUpdates() {
        gameState.levelMap = new Tile[5][5];
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 5; j++)
                gameState.levelMap[i][j] = Tile.EMPTY;
        gameState.levelMap[2][1] = Tile.SOLID;
        gameState.bombs = new ArrayList<Bomb>();

        ArrayList<PImage> dummyImgs = new ArrayList<PImage>();
        dummyImgs.add(new PImage());
        Map<Direction, PImage> dummyMap = new HashMap<Direction, PImage>();
        dummyMap.put(Direction.DOWN, new PImage());

        Bomb bomb1 = new Bomb(dummyImgs, dummyMap, 2, 2);
        bomb1.gameTime = 30;
        assertNull(bomb1.checkExplode(gameState.levelMap));
        bomb1.gameTime = 200;
        assertNotNull(bomb1.checkExplode(gameState.levelMap));

        Bomb bomb2 = new Bomb(dummyImgs, dummyMap, 3, 3);
        bomb2.gameTime = 200;
        assertNotNull(bomb2.checkExplode(gameState.levelMap));

        gameState.bombs.add(bomb1);
        gameState.bombs.add(bomb2);
        gameState.explosions = new ArrayList<Explosion>();
        assertTrue(gameState.updateBombs());
    }

    @Test
    public void testEndGameMethods() {
        gameState.player = new Player(dummyFrames);
        gameState.player.x = 0;
        gameState.player.y = 0;
        gameState.goalCoords = new Coordinates(0, 0);
        gameState.player.lives = -1;
        gameState.currentLevel = 10;
        setup.levelFiles = new ArrayList<String>();
        assertTrue(gameState.checkWin(setup, graphicsHandler));
        assertTrue(gameState.doLoss(setup, graphicsHandler));

        setup.levelTimes = new ArrayList<Integer>();
        setup.levelTimes.add(100);
        setup.levelFiles = new ArrayList<String>();
        setup.levelFiles.add("level1.txt");
        gameState.enemies = new ArrayList<Character>();
        graphicsHandler.redFrames = dummyFrames;
        graphicsHandler.yellowFrames = dummyFrames;

        gameState.explosions = new ArrayList<Explosion>();
        gameState.bombs = new ArrayList<Bomb>();
        setup.levelFiles.add("new/made/up/file");
        setup.levelFiles.add("made/up/two/eletric/boogaloo");
        setup.levelTimes.add(100);
        setup.levelTimes.add(420);
        gameState.currentLevel = 0;
        PImage dummyPic = app.loadImage("src/test/resources/dummyFrame.png");
        graphicsHandler.livesIcon = dummyPic;
        graphicsHandler.timeIcon = dummyPic;
        graphicsHandler.tileImages = new HashMap<Tile, PImage>();
        graphicsHandler.tileImages.put(Tile.EMPTY, dummyPic);
        assertFalse(gameState.checkWin(setup, graphicsHandler));

        gameState.player.lives = 100;
        assertFalse(gameState.doLoss(setup, graphicsHandler));

        gameState.enemies.add(new RedEnemy(dummyFrames, -1, -1));
        gameState.explosions.add(new Explosion(dummyPic, -1, -1));
        assertFalse(gameState.checkLoss(setup, graphicsHandler));
        gameState.enemies.add(new RedEnemy(dummyFrames, gameState.player.x, gameState.player.y));
        assertTrue(gameState.checkLoss(setup, graphicsHandler));
        gameState.explosions.add(new Explosion(dummyPic, gameState.player.x, gameState.player.y));
        assertTrue(gameState.checkLoss(setup, graphicsHandler));
    }

    @Test
    public void testCheckExplosionKill() {
        gameState.player = new Player(dummyFrames);
        gameState.player.x = 0;
        gameState.player.y = 0;
        gameState.enemies = new ArrayList<Character>();
        gameState.enemies.add(new RedEnemy(dummyFrames, 1, 1));
        Explosion explosion = new Explosion(new PImage(), 1, 1);
        gameState.checkExplosionKill(explosion);
    }

}
