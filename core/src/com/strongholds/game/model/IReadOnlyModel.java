package com.strongholds.game.model;

import com.strongholds.game.gameobject.IReadOnlyAnimatedActor;
import com.strongholds.game.gameobject.IReadOnlyGameObject;

/**
 * Model interface for View
 */
public interface IReadOnlyModel {
    Object[] getGameObjects();
    Object[] getActors();
    IReadOnlyAnimatedActor getActor(String id);
    IReadOnlyGameObject getGameObject(String id);
    long getMoney();
    int getBaseHealth();
    int getEnemyBaseHealth();
}
