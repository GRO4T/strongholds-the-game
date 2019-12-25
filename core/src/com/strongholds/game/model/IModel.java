package com.strongholds.game.model;

import com.badlogic.gdx.math.Vector2;
import com.strongholds.game.GameSingleton.ObjectType;

public interface IModel extends IReadOnlyModel{
    void update(float timeStep);
    void dispose();
    void createObject(String id, ObjectType objectType, Vector2 position, Vector2 size);
    void createActor(String id, ObjectType objectType, Vector2 position, Vector2 size);

    void addMoney(long value);
}
