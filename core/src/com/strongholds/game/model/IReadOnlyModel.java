package com.strongholds.game.model;

import com.strongholds.game.model.gameobject.IReadOnlyAnimatedActor;
import com.strongholds.game.model.gameobject.IReadOnlyGameObject;

public interface IReadOnlyModel {
    Object[] getGameObjects();
    Object[] getActors();
    IReadOnlyAnimatedActor getActor(String id);
    IReadOnlyGameObject getGameObject(String id);
    long getMoney();
    int getBaseHealth();
    int getEnemyBaseHealth();
}
