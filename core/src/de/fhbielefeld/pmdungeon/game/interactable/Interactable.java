package de.fhbielefeld.pmdungeon.game.interactable;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import de.fhbielefeld.pmdungeon.game.characters.Character;
import de.fhbielefeld.pmdungeon.game.dungeon.dungeonconverter.Coordinate;

public interface Interactable extends Disposable {
    void update();

    void render(SpriteBatch batch);

    void interact(Character character);

    Coordinate getCoordinate();
}