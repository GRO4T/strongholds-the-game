package com.strongholds.game.gameobject;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.strongholds.game.GameSingleton;

public class RangedUnit extends Unit implements IUnit{
    /**
     * Creates a unit
     *
     * @param bodyDef box2d body definition
     * @param width   unit width (in meters)
     * @param height  unit height (in meters)
     * @param type    unit type
     * @param id      id
     * @param isEnemy whether unit is an enemy
     */
    public RangedUnit(BodyDef bodyDef, float width, float height, GameSingleton.ObjectType type, String id, boolean isEnemy) {
        super(bodyDef, width, height, type, id, isEnemy);
    }
}
