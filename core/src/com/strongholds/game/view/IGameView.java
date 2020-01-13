package com.strongholds.game.view;

import com.badlogic.gdx.math.Vector2;
import com.strongholds.game.GameSingleton.ObjectType;

public interface IGameView {
    void update(float deltaTime);
    void draw();
    void loadTextures();
    void loadActorSprites(String id, ObjectType objectType);
    Vector2 getTextureSize(ObjectType objectType);
    Vector2 getTextureSize(String id);
    void init();
    void gameFinished();
}
