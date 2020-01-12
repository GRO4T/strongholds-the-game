package com.strongholds.game.gameobject;

import com.badlogic.gdx.math.Vector2;
import com.strongholds.game.model.DeathListener;

public interface IUnit extends IReadOnlyUnit, IAnimatedActor{
    void attack();
    void move(Vector2 direction);
    void update();
    void dispose();
    void setDeathListener(DeathListener deathListener);
}
