package com.strongholds.game.model.gameobject;

import com.badlogic.gdx.math.Vector2;
import com.strongholds.game.GameSingleton;

public interface IViewGameObject {
    Vector2 getPosition();
    float getWidth();
    float getHeight();
    GameSingleton.ObjectType getType();
    String getId();
}
