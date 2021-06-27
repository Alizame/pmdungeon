package de.fhbielefeld.pmdungeon.vorgaben.game.Controller;

import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EntityControllerTest {
    private final int num = 1000;
    private EntityController ec;

    @BeforeEach
    void setUp() {
        ec = new EntityController();
    }

    @AfterEach
    void tearDown() {
    }

    @DisplayName(/* it */ "is not null")
    @Test
    void initialisable() {
        assertNotNull(ec);
    }

    @DisplayName("is initially empty")
    @Test
    void initialEmpty() {
        assertTrue(ec.getList().isEmpty());
    }

    @DisplayName("getList returns list")
    @Test
    void getList() {
        assertNotNull(ec.getList());
        assertTrue(ec.getList() instanceof List);
    }

    @DisplayName("can add a single entity")
    @Test
    void addEntityNormal() {
        IEntity entity = new TestEntity();

        ec.addEntity(entity);
        assertFalse(ec.getList().isEmpty(), "list is not emtpy");
        assertTrue(ec.getList().contains(entity), "list contains given entity");
        assertEquals(ec.getList().size(), 1, "list contains one entity");
    }

    @DisplayName("can add multiple entities")
    @Test
    void addEntityMultiple() {
        IEntity entity1 = new TestEntity();
        IEntity entity2 = new TestEntity();
        IEntity entity3 = new TestEntity();
        IEntity entity4 = new TestEntity();

        ec.addEntity(entity1);
        ec.addEntity(entity2);
        ec.addEntity(entity3);
        ec.addEntity(entity4);

        assertFalse(ec.getList().isEmpty(), "list is not emtpy");
        assertTrue(ec.getList().contains(entity1), "list contains given entity1");
        assertTrue(ec.getList().contains(entity2), "list contains given entity2");
        assertTrue(ec.getList().contains(entity3), "list contains given entity3");
        assertTrue(ec.getList().contains(entity4), "list contains given entity4");
        assertEquals(ec.getList().size(), 4, "list contains 4 entities");
    }

    @DisplayName("can add multiple entities")
    @Test
    void addEntityMany() {
        ArrayList<IEntity> entities = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            TestEntity entity = new TestEntity();
            entities.add(entity);
            ec.addEntity(entity);
        }

        assertFalse(ec.getList().isEmpty(), "list is not emtpy");
        assertEquals(ec.getList().size(), num, "list contains all entities");
        for (IEntity entity : entities) {
            boolean check = ec.getList().contains(entity);
            assertTrue(check, "list contains given entity");
        }
    }

    @DisplayName("Adding entity twice only inserts it once")
    @Test
    void addEntityTwiceSame() {
        IEntity entity = new TestEntity();

        ec.addEntity(entity);
        ec.addEntity(entity);

        assertFalse(ec.getList().isEmpty(), "list is not emtpy");
        assertTrue(ec.getList().contains(entity), "list contains given entity");
        assertEquals(ec.getList().size(), 1, "list contains only one entity");
    }

    @DisplayName("can remove a single entity")
    @Test
    void removeEntityNormal() {
        IEntity entity = new TestEntity();

        ec.addEntity(entity);
        ec.removeEntity(entity);
        assertTrue(ec.getList().isEmpty(), "list is emtpy");
    }

    @Test
    void removeEntityDoesntContain() {
        IEntity entity = new TestEntity();
        IEntity entity2 = new TestEntity();

        ec.addEntity(entity);
        ec.removeEntity(entity2);

        assertFalse(ec.getList().isEmpty(), "list is emtpy");
        assertTrue(ec.getList().contains(entity), "list contains given entity");
        assertFalse(ec.getList().contains(entity2), "list doesnt contain removed entity");
    }

    @DisplayName("can remove multiple entities")
    @Test
    void removeEntityMultiple() {
        ArrayList<IEntity> entities = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            TestEntity entity = new TestEntity();
            entities.add(entity);
            ec.addEntity(entity);
        }
        assertFalse(ec.getList().isEmpty(), "list is not emtpy");

        // remove
        for (int i = 0; i < num; i++) {
            ec.removeEntity(entities.get(i));
        }
        assertTrue(ec.getList().isEmpty(), "list is emtpy");
    }

    @Test
    void removeEntityNotInList() {
        IEntity entity = new TestEntity();
        IEntity entity2 = new TestEntity();

        ec.addEntity(entity);
        ec.removeEntity(entity2);
        assertFalse(ec.getList().isEmpty(), "list is not emtpy");
        assertTrue(ec.getList().contains(entity), "list containts non-removed entity");
    }

    @DisplayName("can remove all entities")
    @Test
    void removeAllNormal() {
        IEntity entity = new TestEntity();

        ec.addEntity(entity);
        assertFalse(ec.getList().isEmpty(), "list is not empty");
        assertTrue(ec.getList().contains(entity));
        ec.removeAll();
        assertTrue(ec.getList().isEmpty(), "list is emtpy");
    }

    @Test
    void removeAllEmpty() {
        assertTrue(ec.getList().isEmpty(), "list is empty before");
        ec.removeAll();
        assertTrue(ec.getList().isEmpty(), "list is emtpy after");
    }

    @DisplayName("can remove all entities")
    @Test
    void removeAllMultiple() {
        for (int i = 0; i < num; i++) {
            TestEntity entity = new TestEntity();
            ec.addEntity(entity);
        }
        assertFalse(ec.getList().isEmpty(), "list is not emtpy");

        ec.removeAll();
        assertTrue(ec.getList().isEmpty(), "list is emtpy");
    }

    @DisplayName("can remove all entities")
    @Test
    void removeAllFromNormal() {
        IEntity entity = new TestEntity();

        ec.addEntity(entity);
        assertFalse(ec.getList().isEmpty(), "list contains given element");
        ec.removeAllFrom(TestEntity.class);
        assertTrue(ec.getList().isEmpty(), "list is emtpy");
    }

    @Test
    void removeAllFromEmpty() {
        assertTrue(ec.getList().isEmpty(), "list is empty before");
        ec.removeAllFrom(TestEntity.class);
        assertTrue(ec.getList().isEmpty(), "list is emtpy after");
    }

    @Test
    void removeAllNotInList() {
        IEntity entity = new TestEntity();

        ec.addEntity(entity);
        ec.removeAllFrom(DeletableTestEntity.class);
        assertFalse(ec.getList().isEmpty(), "list is not emtpy");
        assertTrue(ec.getList().contains(entity), "list containts non-removed entity");
    }

    @DisplayName("can remove all entities")
    @Test
    void removeAllFromMultiple() {
        for (int i = 0; i < num; i++) {
            TestEntity entity = new TestEntity();
            DeletableTestEntity entity2 = new DeletableTestEntity();
            ec.addEntity(entity);
            ec.addEntity(entity2);
        }
        assertFalse(ec.getList().isEmpty(), "list is not emtpy");
        assertEquals(ec.getList().size(), num * 2);

        ec.removeAllFrom(DeletableTestEntity.class);
        assertFalse(ec.getList().isEmpty(), "list is not emtpy");
        assertEquals(ec.getList().size(), num);

        ec.removeAllFrom(TestEntity.class);
        assertTrue(ec.getList().isEmpty(), "list is emtpy");
    }

    /* update:
        NormalEntity only
        - update once with single entity    => size=1, cnt=1
        - update one entity 1000 times      => size=1, cnt=1000
        - update 100 times with 100 entities  => size=100, cnt=100
    */
    @DisplayName("update once with single entity")
    @Test
    void updateNormalOnce() {
        TestEntity entity = new TestEntity();
        ec.addEntity(entity);

        ec.update();

        assertEquals(ec.getList().size(), 1, "entity still in list");
        assertEquals(entity.cnt, 1, "entity updated once");
    }

    @DisplayName("update multiple times with single entity")
    @Test
    void updateNormalMultiple() {
        TestEntity entity = new TestEntity();
        ec.addEntity(entity);

        for (int i = 0; i < num; i++) {
            ec.update();
        }

        assertEquals(ec.getList().size(), 1, "entity still in list");
        assertEquals(entity.cnt, num, "entity updated once");
    }

    @DisplayName("update multiple times with multiple entities")
    @Test
    void updateNormalMultiple2() {
        ArrayList<TestEntity> entities = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            TestEntity entity = new TestEntity();
            entities.add(entity);
            ec.addEntity(entity);
        }

        for (int i = 0; i < num; i++) {
            ec.update();
        }

        assertEquals(ec.getList().size(), num, "entity still in list");
        for (TestEntity entity : entities) {
            assertEquals(entity.cnt, num, "entity updated");
        }
    }

    /*
        DeletableEntity only
        - update twice with single deletable entity      => size=0, cnt=0 -> cnt=0
        - update once with not deletable, update once with deletable, update once again    => size=0, cnt=1
        - update once with 1000 deletable entities      => size=0, cnt=0
        - update once with 1000 not deletable, update once with 1000 deletable              => size=0, cnt=1
    */
    @DisplayName("update once with single entity")
    @Test
    void updateDeletableSingle() {
        DeletableTestEntity entity = new DeletableTestEntity(true);
        ec.addEntity(entity);

        ec.update();
        assertEquals(ec.getList().size(), 0, "entity not in list");
        assertEquals(entity.cnt, 0, "entity not updated");

        ec.update();
        assertEquals(ec.getList().size(), 0, "entity not in list");
        assertEquals(entity.cnt, 0, "entity still not updated");
    }

    @DisplayName("once not deletable, then deletable")
    @Test
    void updateDeletableSingle2() {
        DeletableTestEntity entity = new DeletableTestEntity(false);
        ec.addEntity(entity);

        ec.update();
        assertEquals(ec.getList().size(), 1, "entity in list");
        assertEquals(entity.cnt, 1, "entity updated");

        entity.deletable = true;
        ec.update();
        assertEquals(ec.getList().size(), 0, "entity not in list");
        assertEquals(entity.cnt, 1, "entity still not updated again");
    }

    @DisplayName("update multiple times with single deletable entity")
    @Test
    void updateDeletableMultiple() {
        DeletableTestEntity entity = new DeletableTestEntity(true);
        ec.addEntity(entity);

        for (int i = 0; i < num; i++) {
            ec.update();
            assertEquals(ec.getList().size(), 0, "entity not in list");
            assertEquals(entity.cnt, 0, "entity not updated");
        }
    }

    @DisplayName("update once with multiple deletable entity")
    @Test
    void updateDeletableMultiple2() {
        ArrayList<DeletableTestEntity> entities = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            DeletableTestEntity entity = new DeletableTestEntity(true);
            entities.add(entity);
            ec.addEntity(entity);
        }

        ec.update();

        assertEquals(ec.getList().size(), 0, "list empty");
        for (TestEntity entity : entities) {
            assertEquals(entity.cnt, 0, "entity not updated");
        }
    }

    @DisplayName("update multiple times with multiple deletable entity")
    @Test
    void updateDeletableMultiple3() {
        ArrayList<DeletableTestEntity> entities = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            DeletableTestEntity entity = new DeletableTestEntity(true);
            entities.add(entity);
            ec.addEntity(entity);
        }

        for (int i = 0; i < num; i++) {
            ec.update();
            assertEquals(ec.getList().size(), 0, "entity not in list");
        }

        for (TestEntity entity : entities) {
            assertEquals(entity.cnt, 0, "entity not updated");
        }
    }

    @DisplayName("update once with multiple not deletable, update once with deletable")
    @Test
    void updateDeletableMultiple4() {
        ArrayList<DeletableTestEntity> entities = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            DeletableTestEntity entity = new DeletableTestEntity(false);
            entities.add(entity);
            ec.addEntity(entity);
        }

        ec.update();
        assertEquals(ec.getList().size(), num, "list still full");

        for (DeletableTestEntity entity : entities) {
            assertEquals(entity.cnt, 1, "entity updated");
            entity.deletable = true;
        }

        ec.update();
        assertEquals(ec.getList().size(), 0, "list now empty");

        for (TestEntity entity : entities) {
            assertEquals(entity.cnt, 1, "entity only updated once");
        }
    }

    @DisplayName("update once with both entity types")
    @Test
    void updateBothSingle() {
        TestEntity entity = new TestEntity();
        DeletableTestEntity entity2 = new DeletableTestEntity(true);
        ec.addEntity(entity);
        ec.addEntity(entity2);
        assertEquals(ec.getList().size(), 2, "both in list");

        ec.update();
        assertTrue(ec.getList().contains(entity), "normal entity in list");
        assertEquals(ec.getList().size(), 1, "normal entity in list");
        assertEquals(entity.cnt, 1, "normal entity updated");

        ec.update();
        assertEquals(ec.getList().size(), 1, "normal entity in list");
        assertEquals(entity.cnt, 2, "normal entity updated");
        assertEquals(entity2.cnt, 0, "entity still not updated");
    }

    @DisplayName("update multiple times with multiple normal and deletable entity")
    @Test
    void updateBothMultiple() {
        ArrayList<TestEntity> entities = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            DeletableTestEntity entity = new DeletableTestEntity(true);
            TestEntity entity2 = new TestEntity();
            entities.add(entity);
            entities.add(entity2);
            ec.addEntity(entity);
            ec.addEntity(entity2);
        }

        for (int i = 0; i < num; i++) {
            ec.update();
        }

        for (TestEntity entity : entities) {
            if (entity instanceof DeletableTestEntity) {
                assertEquals(entity.cnt, 0, "entity not updated");
            } else {
                assertEquals(entity.cnt, num, "entity updated");
            }
        }
        assertEquals(ec.getList().size(), num, "entity list size only normal entities");
    }

    /*
        both
        - update once with NE und deletable DE              => size=1, cnt=1 bei NE, cnt=0 bei DE
        - update once with 1000 NE und 1000 deletable DE    => size=1000, cnt=1 bei NE, cnt=0 bei DE
        - update 100 times with 1000 NE und 1000 deletable DE   => size=1000, cnt=100 bei NE, cnt=0 bei DE
        - update 50 times with 1000 NE und 1000 DE, update 5 times with deletable DEs   => size=1000, cnt=100 bei NE, cnt=50 bei DE
    */

    @DisplayName("update multiple times with multiple normal and deletable entity 2")
    @Test
    void updateBothMultiple2() {
        ArrayList<TestEntity> entities = new ArrayList<>();
        ArrayList<DeletableTestEntity> entities2 = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            TestEntity entity = new TestEntity();
            DeletableTestEntity entity2 = new DeletableTestEntity(false);
            entities.add(entity);
            entities2.add(entity2);
            ec.addEntity(entity);
            ec.addEntity(entity2);
        }

        for (int i = 0; i < num; i++) {
            ec.update();
        }
        assertEquals(ec.getList().size(), num * 2, "entity list size all entities");


        for (DeletableTestEntity entity : entities2) {
            entity.setDeletable(true);
        }
        for (int i = 0; i < num; i++) {
            ec.update();
        }

        // check:
        for (TestEntity entity : entities) {
            assertEquals(entity.cnt, num * 2, "entity updated");
        }
        for (TestEntity entity : entities2) {
            assertEquals(entity.cnt, num, "entity not updated");
        }
        assertEquals(ec.getList().size(), num, "entity list size only normal entities");
    }

    static class TestEntity implements IEntity {
        public int cnt = 0;

        @Override
        public void update() {
            cnt++;
        }

        @Override
        public boolean deleteable() {
            return false;
        }
    }

    static class DeletableTestEntity extends TestEntity {
        public boolean deletable = false;

        DeletableTestEntity() {
        }

        DeletableTestEntity(boolean initialState) {
            deletable = initialState;
        }

        @Override
        public boolean deleteable() {
            return deletable;
        }

        public void setDeletable(boolean b) {
            deletable = b;
        }
    }
}
