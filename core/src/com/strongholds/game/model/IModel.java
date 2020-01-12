package com.strongholds.game.model;

import com.badlogic.gdx.math.Vector2;
import com.strongholds.game.GameSingleton.ObjectType;

/** Model interface for Controller
 */
public interface IModel extends IReadOnlyModel{
    void update(float timeStep);
    void dispose();
    void createObject(String id, ObjectType objectType, Vector2 position, Vector2 size, boolean isEnemy);
    void createUnit(String id, ObjectType objectType, Vector2 position, Vector2 size, boolean isEnemy);
    void addMoney(long value);
}
