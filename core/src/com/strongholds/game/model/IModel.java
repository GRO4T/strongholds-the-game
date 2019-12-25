package com.strongholds.game.model;

import com.badlogic.gdx.math.Vector2;
import com.strongholds.game.GameSingleton.ObjectType;
import com.strongholds.game.model.gameobject.IViewAnimatedActor;
import com.strongholds.game.model.gameobject.IViewGameObject;

public interface IModel extends IReadOnlyModel{
    void update(float timeStep);
    void dispose();
    void createObject(String id, ObjectType objectType, Vector2 position, Vector2 size);
    void createActor(String id, ObjectType objectType, Vector2 position, Vector2 size);
}
