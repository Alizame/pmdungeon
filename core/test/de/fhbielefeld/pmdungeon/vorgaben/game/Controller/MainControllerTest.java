package de.fhbielefeld.pmdungeon.vorgaben.game.Controller;

import de.fhbielefeld.pmdungeon.vorgaben.HeadlessSetup;
import de.fhbielefeld.pmdungeon.vorgaben.dungeonCreator.DungeonWorld;
import de.fhbielefeld.pmdungeon.vorgaben.graphic.HUD;
import de.fhbielefeld.pmdungeon.vorgaben.graphic.TextStage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockConstruction;


class MainControllerTest extends HeadlessSetup {
    private MainController mc;

    @DisplayName("render() calls firstFrame() once")
    @Test
    void testRender() {
        try (
                MockedConstruction<HUD> mockedHUD = mockConstruction(HUD.class);
                MockedConstruction<TextStage> mockedTextStage = mockConstruction(TextStage.class)
        ) {
            mc = new MainController();

            // render calls firstFrame, which creates the LevelController and loads the dungeon
            mc.render(0);
            assertNotNull(mc.levelController);
            DungeonWorld dungeonBefore = mc.levelController.getDungeon();
            assertNotNull(dungeonBefore);

            // if render calls firstFrame again, the dungeon would be replaced, so if it isn't replaced it didnt call firstFrame() again
            mc.render(0);

            assertEquals(dungeonBefore, mc.levelController.getDungeon());
        }
    }

    @DisplayName("calls firstFrame() - finishedSetup = false")
    @Test
    void testFirstFrame() throws IllegalAccessException, NoSuchFieldException, NoSuchMethodException, InvocationTargetException {
        try (
                MockedConstruction<HUD> mockedHUD = mockConstruction(HUD.class);
                MockedConstruction<TextStage> mockedTextStage = mockConstruction(TextStage.class)
        ) {
            Field f = MainController.class.getDeclaredField("finishedSetup");
            f.setAccessible(true);
            Method m = MainController.class.getDeclaredMethod("firstFrame");
            m.setAccessible(true);

            mc = new MainController();

            assertTrue(mc.firstFrame);
            assertFalse(f.getBoolean(mc));

            m.invoke(mc);

            assertTrue(f.getBoolean(mc));
            assertFalse(mc.firstFrame); // this means firstFrame() was called, as it sets firstFrame to false
        }
    }

    @DisplayName("calls firstFrame() - finishedSetup = true")
    @Test
    void testFirstFrame2() throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        try (
                MockedConstruction<HUD> mockedHUD = mockConstruction(HUD.class);
                MockedConstruction<TextStage> mockedTextStage = mockConstruction(TextStage.class)
        ) {
            Field f = MainController.class.getDeclaredField("finishedSetup");
            f.setAccessible(true);
            Method m = MainController.class.getDeclaredMethod("firstFrame");
            m.setAccessible(true);

            mc = new MainController();
            assertFalse(f.getBoolean(mc));
            m.invoke(mc); // initialize everything by calling firstFrame()
            assertTrue(f.getBoolean(mc));
            // finishedSetup wird gesetzt von firstFrame(), welches von render aufgerufen wird.
            DungeonWorld dungeonBefore = mc.levelController.getDungeon();

            m.invoke(mc); // call firstFrame() again, should replace dungeon

            DungeonWorld dungeonAfter = mc.levelController.getDungeon();
            assertNotEquals(dungeonBefore, dungeonAfter);
        }
    }

    @DisplayName("calls setupWorldController")
    @Test
    void testSetupWorldController() {
        try (
                MockedConstruction<HUD> mockedHUD = mockConstruction(HUD.class);
                MockedConstruction<TextStage> mockedTextStage = mockConstruction(TextStage.class)
        ) {
            mc = new MainController();
            assertNull(mc.levelController);
            mc.render(0); // render calls firstFrame, which calls setupWorldController
            assertNotNull(mc.levelController);
        }
    }

    @DisplayName("calls setupCamera")
    @Test
    void testSetupCamera() {
        try (
                MockedConstruction<HUD> mockedHUD = mockConstruction(HUD.class);
                MockedConstruction<TextStage> mockedTextStage = mockConstruction(TextStage.class)
        ) {
            mc = new MainController();
            assertNull(mc.camera);
            mc.render(0); // render calls firstFrame, which calls setupCamera
            assertNotNull(mc.camera);
        }
    }
}
