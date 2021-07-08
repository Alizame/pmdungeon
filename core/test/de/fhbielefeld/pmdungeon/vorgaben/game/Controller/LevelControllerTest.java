package de.fhbielefeld.pmdungeon.vorgaben.game.Controller;

import de.fhbielefeld.pmdungeon.vorgaben.HeadlessSetup;
import de.fhbielefeld.pmdungeon.vorgaben.dungeonCreator.DungeonWorld;
import de.fhbielefeld.pmdungeon.vorgaben.dungeonCreator.tiles.Tile;
import de.fhbielefeld.pmdungeon.vorgaben.game.GameSetup;
import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static de.fhbielefeld.pmdungeon.vorgaben.game.Controller.LevelController.Stage;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class LevelControllerTest extends HeadlessSetup {
    private LevelController lc;
    private int cnt = 0;

    public void onLevelLoad() {
        cnt += 1;
    }

    private void onLevelLoadPrivate() {
    }

    public void onLevelLoadException() {
        throw new NullPointerException();
    }

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        cnt = 0;

        try {
            Method functionToPass = this.getClass().getMethod("onLevelLoad");
            Object[] arguments = new Object[0];
            lc = new LevelController(functionToPass, this, arguments);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            fail("test broken");
        }
    }

    @DisplayName("normal loadDungeon")
    @Test
    void loadDungeon() throws InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        Field f = LevelController.class.getDeclaredField("nextLevelTriggered");
        f.setAccessible(true);

        assertNull(lc.getDungeon(), "no DungeonWorld exists before loading one");

        DungeonWorld testDungeon = new DungeonWorld(0, 0);
        lc.loadDungeon(testDungeon);

        assertEquals(lc.getDungeon(), testDungeon, "LevelController has correct DungeonWorld instance");
        assertFalse(f.getBoolean(lc));
    }

    @DisplayName("loadDungeon with onLevelLoad=null")
    @Test
    void loadDungeon2() {
        Object[] arguments = new Object[0];
        lc = new LevelController(null, this, arguments);

        DungeonWorld testDungeon = new DungeonWorld(0, 0);
        assertThrows(NullPointerException.class, () -> lc.loadDungeon(testDungeon));
    }

    @DisplayName("loadDungeon with onLevelLoad")
    @Test
    void loadDungeon3() throws InvocationTargetException, IllegalAccessException {
        assertNull(lc.getDungeon(), "no DungeonWorld exists before loading one");

        DungeonWorld testDungeon = mock(DungeonWorld.class);
        lc.loadDungeon(testDungeon);
        assertEquals(cnt, 1, "onLevelLoad call count correct");
        lc.loadDungeon(testDungeon);
        assertEquals(cnt, 2, "onLevelLoad call count correct");

        assertEquals(lc.getDungeon(), testDungeon, "LevelController has correct DungeonWorld instance");
    }

    @DisplayName("normal loadDungeon twice")
    @Test
    void loadDungeon4() throws InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        Field f = LevelController.class.getDeclaredField("nextLevelTriggered");
        f.setAccessible(true);

        assertNull(lc.getDungeon(), "no DungeonWorld exists before loading one");

        DungeonWorld testDungeon = mock(DungeonWorld.class);
        lc.loadDungeon(testDungeon);

        assertEquals(lc.getDungeon(), testDungeon, "LevelController has correct DungeonWorld instance");

        DungeonWorld testDungeon2 = mock(DungeonWorld.class);
        lc.loadDungeon(testDungeon2);

        assertNotEquals(lc.getDungeon(), testDungeon);
        assertEquals(lc.getDungeon(), testDungeon2, "LevelController has correct DungeonWorld instance");
        assertFalse(f.getBoolean(lc)); // nextLevelTriggered = false

        verify(testDungeon2).makeConnections(); // makeConnections() has been called
    }


    @DisplayName("update with nextLevelTriggered=false")
    @Test
    void update() throws InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        Field f = LevelController.class.getDeclaredField("nextLevelTriggered");
        f.setAccessible(true);

        DungeonWorld testDungeon = mock(DungeonWorld.class);
        lc.loadDungeon(testDungeon);
        f.setBoolean(lc, false);

        lc.update();

        verify(testDungeon).renderFloor(GameSetup.batch);
        verify(testDungeon).renderWalls(testDungeon.getHeight() - 1, 0, GameSetup.batch);
    }

    @DisplayName("update with nextLevelTriggered=true")
    @Test
    void update2() throws IllegalAccessException, NoSuchFieldException {
        Field f = LevelController.class.getDeclaredField("nextLevelTriggered");
        f.setAccessible(true);
        f.setBoolean(lc, true);

        lc.update();

        assertNotNull(lc.getDungeon(), "next dungeon loaded");
    }

    @DisplayName("loadDungeon with onLevelLoad() private")
    @Test
    void update3() throws NoSuchFieldException, IllegalAccessException {
        Field f = LevelController.class.getDeclaredField("nextLevelTriggered");
        f.setAccessible(true);
        try {
            Method functionToPass = LevelControllerTest.class.getDeclaredMethod("onLevelLoadPrivate");
            lc = new LevelController(functionToPass, this, new Object[0]);
            f.setBoolean(lc, true);

            System.err.println("vvv IllegalAccessException Stacktrace expected vvv");
            lc.update();
            System.err.println("^^^ IllegalAccessException Stacktrace expected ^^^");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            fail("test broken");
        }
    }

    @DisplayName("loadDungeon with onLevelLoad() throwing runtime exception")
    @Test
    void update4() throws NoSuchFieldException, IllegalAccessException {
        Field f = LevelController.class.getDeclaredField("nextLevelTriggered");
        f.setAccessible(true);
        try {
            Method functionToPass = LevelControllerTest.class.getDeclaredMethod("onLevelLoadException");
            lc = new LevelController(functionToPass, this, new Object[0]);
            f.setBoolean(lc, true);

            System.err.println("vvv InvocationTargetException Stacktrace expected vvv");
            lc.update();
            System.err.println("^^^ InvocationTargetException Stacktrace expected ^^^");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            fail("test broken");
        }
    }


    @DisplayName("checkForTrigger with point on trigger")
    @Test
    void checkForTrigger() throws InvocationTargetException, IllegalAccessException {
        DungeonWorld testDungeon = mock(DungeonWorld.class);
        lc.loadDungeon(testDungeon);

        when(testDungeon.getNextLevelTrigger()).thenReturn(new Tile(Tile.Type.EMPTY, 0, 0));

        assertTrue(lc.checkForTrigger(new Point(0, 0)));
    }

    @DisplayName("checkForTrigger with point not on trigger")
    @Test
    void checkForTrigger2() throws InvocationTargetException, IllegalAccessException {
        DungeonWorld testDungeon = mock(DungeonWorld.class);
        lc.loadDungeon(testDungeon);

        when(testDungeon.getNextLevelTrigger()).thenReturn(new Tile(Tile.Type.EMPTY, 0, 0));

        assertFalse(lc.checkForTrigger(new Point(1, 0)));
    }


    @DisplayName("triggerNextStage with nextLevelTriggered=false")
    @Test
    void triggerNextStage() throws IllegalAccessException, NoSuchFieldException {
        Field f = LevelController.class.getDeclaredField("nextLevelTriggered");
        f.setAccessible(true);
        f.setBoolean(lc, false);

        lc.triggerNextStage();

        assertTrue(f.getBoolean(lc));
    }


    @DisplayName("triggerNextStage with nextLevelTriggered=true")
    @Test
    void triggerNextStage2() throws IllegalAccessException, NoSuchFieldException {
        Field f = LevelController.class.getDeclaredField("nextLevelTriggered");
        f.setAccessible(true);
        f.setBoolean(lc, true);

        lc.triggerNextStage();

        assertTrue(f.getBoolean(lc));
    }


    @DisplayName("normal draw")
    @Test
    void draw() throws InvocationTargetException, IllegalAccessException {
        DungeonWorld testDungeon = mock(DungeonWorld.class);
        lc.loadDungeon(testDungeon);

        lc.draw();

        verify(testDungeon).renderFloor(GameSetup.batch);
        verify(testDungeon).renderWalls(testDungeon.getHeight() - 1, 0, GameSetup.batch);
    }

    @DisplayName("draw without dungeonWorld")
    @Test
    void draw2() {
        lc.draw();
        // we only expect it not to throw an exception
    }

    @DisplayName("nextStage A -> B")
    @Test
    void nextStageAToB() throws IllegalAccessException, NoSuchFieldException, NoSuchMethodException, InvocationTargetException {
        Field f = LevelController.class.getDeclaredField("nextStage");
        f.setAccessible(true);
        Method m = LevelController.class.getDeclaredMethod("nextStage");
        m.setAccessible(true);
        lc.triggerNextStage();
        assertEquals(f.get(lc), Stage.A);

        m.invoke(lc);

        assertEquals(f.get(lc), Stage.B);
    }

    @DisplayName("nextStage B -> C")
    @Test
    void nextStageBToC() throws IllegalAccessException, NoSuchFieldException, NoSuchMethodException, InvocationTargetException {
        Field f = LevelController.class.getDeclaredField("nextStage");
        f.setAccessible(true);
        Method m = LevelController.class.getDeclaredMethod("nextStage");
        m.setAccessible(true);

        f.set(lc, Stage.B);
        lc.triggerNextStage();
        assertEquals(f.get(lc), Stage.B);

        m.invoke(lc);

        assertEquals(f.get(lc), Stage.C);
    }

    @DisplayName("nextStage C -> D")
    @Test
    void nextStageCToD() throws IllegalAccessException, NoSuchFieldException, NoSuchMethodException, InvocationTargetException {
        Field f = LevelController.class.getDeclaredField("nextStage");
        f.setAccessible(true);
        Method m = LevelController.class.getDeclaredMethod("nextStage");
        m.setAccessible(true);

        f.set(lc, Stage.C);
        lc.triggerNextStage();
        assertEquals(f.get(lc), Stage.C);

        m.invoke(lc);

        assertEquals(f.get(lc), Stage.D);
    }

    @DisplayName("nextStage D -> A")
    @Test
    void nextStageDToA() throws IllegalAccessException, NoSuchFieldException, NoSuchMethodException, InvocationTargetException {
        Field f = LevelController.class.getDeclaredField("nextStage");
        f.setAccessible(true);
        Method m = LevelController.class.getDeclaredMethod("nextStage");
        m.setAccessible(true);

        f.set(lc, Stage.D);
        lc.triggerNextStage();
        assertEquals(f.get(lc), Stage.D);

        m.invoke(lc);

        assertEquals(f.get(lc), Stage.A);
    }

}
