package de.fhbielefeld.pmdungeon.vorgaben.game.Controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import de.fhbielefeld.pmdungeon.vorgaben.dungeonCreator.dungeonconverter.DungeonConverter;
import de.fhbielefeld.pmdungeon.vorgaben.game.GameSetup;
import de.fhbielefeld.pmdungeon.vorgaben.graphic.HUD;
import de.fhbielefeld.pmdungeon.vorgaben.graphic.TextStage;
import de.fhbielefeld.pmdungeon.vorgaben.tools.Constants;
import de.fhbielefeld.pmdungeon.vorgaben.tools.DungeonCamera;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;

/**
 * Controls the game. Setup for all important objects. Contains Gameloop.
 */
public class MainController extends ScreenAdapter {

    /**
     * Controls all entity's
     */
    protected EntityController entityController;
    /**
     * The viewport for the dungeon
     */
    protected DungeonCamera camera;
    /**
     * Controls the level
     */
    protected LevelController levelController;
    /**
     * HUD
     */
    protected HUD hud;

    /**
     * Stage for Text
     */
    protected TextStage textHUD;

    /**
     * Marks if the firstFrame is already calculated or not (true= not calculated)
     */
    protected boolean firstFrame = true;

    // if you call a gdx function in setup this will call draw, so this boolean will
    // be used to stop loops of firstFrame
    private boolean finishedSetup = false;

    // ----------------------------- OWN IMPLEMENTATION
    // -----------------------------
    protected void setup() {
    }

    protected void beginFrame() {
    }

    protected void endFrame() {
    }

    public void onLevelLoad() {
    }
    // ----------------------------- END OWN IMPLEMENTATION
    // --------------------------

    /**
     * Setup for the MainController
     */
    private void firstFrame() {
        if (!finishedSetup) {
            this.entityController = new EntityController();
            this.hud = new HUD();
            this.textHUD = new TextStage(hud.getHudBatch());
            setupCamera();
            setupWorldController();
            finishedSetup = true;
            setup();
        }

        if (firstFrame) {
            // load first level
            firstFrame = false;
            try {
                levelController.loadDungeon(new DungeonConverter().dungeonFromJson(Constants.STARTLEVEL));
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Main Gameloop. Redraws the dungeon and calls all the update methods.
     *
     * @param delta Time since last loop. (since the PM-Dungeon is frame based, this
     *              isn't very useful)
     */
    @Override
    public final void render(float delta) {
        firstFrame();

        beginFrame();

        // clears the screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        // need to be called before stuff can be drawn
        GameSetup.batch.begin();

        // updates the level
        levelController.update();

        // need to be called after stuff has been drawn
        GameSetup.batch.end();

        // updates all objects in the dungeon
        entityController.update();

        // updates projectionsmatrix
        GameSetup.batch.setProjectionMatrix(camera.combined);

        // updates camera position
        camera.update();

        // updates and draw hud
        hud.draw();
        textHUD.draw();

        endFrame();

    }

    /**
     * Setting up the WorldController.
     */
    private void setupWorldController() {
        try {
            // this method will be called every time a new level gets load
            Method functionToPass = this.getClass().getMethod("onLevelLoad");
            System.out.println("DEBUG: " + functionToPass);
            // if you need parameter four your method, add them here
            Object[] arguments = new Object[0];
            this.levelController = new LevelController(functionToPass, this, arguments);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * Setting up the camera.
     */
    private void setupCamera() {
        camera = new DungeonCamera(null, Constants.VIRTUALHEIGHT * Constants.WIDTH / (float) Constants.HEIGHT,
                Constants.VIRTUALHEIGHT);
        camera.position.set(0, 0, 0);
        camera.zoom += 1;
        camera.update();
    }
}
