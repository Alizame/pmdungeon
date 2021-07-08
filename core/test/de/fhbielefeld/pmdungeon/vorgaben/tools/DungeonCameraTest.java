package de.fhbielefeld.pmdungeon.vorgaben.tools;

import com.badlogic.gdx.graphics.Texture;
import de.fhbielefeld.pmdungeon.vorgaben.HeadlessSetup;
import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IDrawable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class DungeonCameraTest extends HeadlessSetup {
    private DungeonCamera dc;

    @BeforeEach
    public void setUp() {
        super.setUp();
        float vw = Constants.VIRTUALHEIGHT * Constants.WIDTH / (float) Constants.HEIGHT;
        float vh = Constants.VIRTUALHEIGHT;
        dc = new DungeonCamera(null, vw, vh);
    }

    @Test
    void normalConstructor() {
        float vw = Constants.VIRTUALHEIGHT * Constants.WIDTH / (float) Constants.HEIGHT;
        float vh = Constants.VIRTUALHEIGHT;

        TestObject drawable = new TestObject();
        drawable.setPosition(new Point(1, 2));
        dc = new DungeonCamera(drawable, vw, vh);

        dc.position.set(0, 0, 0);
        dc.zoom += 1;
        dc.update();

        assertEquals(dc.viewportWidth, vw);
        assertEquals(dc.viewportHeight, vh);
        assertEquals(dc.getFollowedObject(), drawable);
    }

    @DisplayName("setFocusPoint")
    @Test
    void setFocusPoint() throws NoSuchFieldException, IllegalAccessException {
        Point newfocus = new Point(1, 2);
        dc.setFocusPoint(newfocus);
        dc.update(); // Update camera coords

        // Check if private IDrawable follows is null
        assertNull(dc.getFollowedObject());

        // Check if correct focus point is set
        Field focusPointField = DungeonCamera.class.getDeclaredField("focusPoint");
        focusPointField.setAccessible(true);
        assertEquals(focusPointField.get(this.dc), newfocus);

        // Compare point and camera coordinates
        assertEquals(dc.position.x, newfocus.x);
        assertEquals(dc.position.y, newfocus.y);
    }

    @DisplayName("setFocusPoint - Point is moving")
    @Test
    void setFocusPointPointMoves() {
        Point newfocus = new Point(1, 2);
        dc.setFocusPoint(newfocus);
        dc.update(); // Update camera coords

        // Compare point and camera coordinates
        assertEquals(dc.position.x, newfocus.x);
        assertEquals(dc.position.y, newfocus.y);

        newfocus.x = 5;
        newfocus.y = 7;

        dc.update(); // Update camera coords

        // Compare point and camera coordinates
        assertEquals(dc.position.x, newfocus.x);
        assertEquals(dc.position.y, newfocus.y);
    }

    @DisplayName("setFocusPoint - Focus Point after drawable was focused")
    @Test
    void setFocusPointAfterFollowingObject() {
        TestObject drawable = new TestObject();
        drawable.setPosition(new Point(3, 4));
        Point newfocus = new Point(1, 2);

        dc.follow(drawable);
        dc.update(); // Update camera coords

        dc.setFocusPoint(newfocus);
        dc.update(); // Update camera coords

        // Compare point and camera coordinates
        assertEquals(dc.position.x, newfocus.x);
        assertEquals(dc.position.y, newfocus.y);

        newfocus.x = 5;
        newfocus.y = 7;

        dc.update(); // Update camera coords

        // Compare point and camera coordinates
        assertEquals(dc.position.x, newfocus.x);
        assertEquals(dc.position.y, newfocus.y);
    }

    @DisplayName("follow - Object followed properly")
    @Test
    void follow() {
        TestObject drawable = new TestObject();
        Point newfocus = new Point(3, 4);
        drawable.setPosition(newfocus);
        dc.follow(drawable);
        dc.update();
        assertEquals(dc.position.x, newfocus.x);
        assertEquals(dc.position.y, newfocus.y);

        // Move drawable and check again
        newfocus.x = 5;
        newfocus.y = 7;
        drawable.setPosition(newfocus);
        dc.update();
        assertEquals(dc.position.x, newfocus.x);
        assertEquals(dc.position.y, newfocus.y);
    }

    @DisplayName("follow - Center camera to (0,0) if nothing is followed")
    @Test
    void followNull() {
        dc.follow(null);
        dc.update();
        assertEquals(dc.position.x, 0);
        assertEquals(dc.position.y, 0);
    }

    @DisplayName("getFollowedObject - Initialized DungeonCamera has no followed drawable")
    @Test
    void getFollowedObjectInitializedCamera() {
        assertNull(dc.getFollowedObject());
    }

    @DisplayName("getFollowedObject - Initialized DungeonCamera has no followed drawable")
    @Test
    void getFollowedObject() {
        TestObject drawable = new TestObject();
        dc.follow(drawable);
        assertEquals(dc.getFollowedObject(), drawable);
    }

    @DisplayName("update - Center camera to (0,0) if nothing is followed")
    @Test
    void update() {
        assertNull(dc.getFollowedObject());
        dc.update();
        assertEquals(dc.position.x, 0);
        assertEquals(dc.position.y, 0);
    }

    @DisplayName("update - Check camera position after following a drawable")
    @Test
    void updateAfterFollowingObject() {
        TestObject drawable = new TestObject();
        Point newfocus = new Point(3, 4);
        drawable.setPosition(newfocus);
        dc.follow(drawable);
        dc.update();
        assertEquals(dc.position.x, newfocus.x);
        assertEquals(dc.position.y, newfocus.y);
    }

    @DisplayName("update - Check camera position after following a point")
    @Test
    void updateAfterFollowingPoint() {
        Point newfocus = new Point(3, 4);
        dc.setFocusPoint(newfocus);
        dc.update();
        assertEquals(dc.position.x, newfocus.x);
        assertEquals(dc.position.y, newfocus.y);
    }

    static class TestObject implements IDrawable {
        private Point p;

        @Override
        public Point getPosition() {
            return p;
        }

        public void setPosition(Point p) {
            this.p = p;
        }

        @Override
        public Texture getTexture() {
            return null;
        }
    }
}
