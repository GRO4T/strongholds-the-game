package com.strongholds.game.model.gameobject;

import com.badlogic.gdx.math.Vector2;
import com.strongholds.game.GameSingleton;

public interface IReadOnlyGameObject {
    Vector2 getPosition();
    Vector2 getVelocity();
    boolean isOnEnemySide();
    float getWidth();
    float getHeight();
    GameSingleton.ObjectType getType();
    String getId();
}
