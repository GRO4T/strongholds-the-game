package com.strongholds.game.gameobject;

import com.badlogic.gdx.math.Vector2;

public interface IUnit extends IAnimatedActor{
    void attack();
    void move(Vector2 direction);
    void update();
}
