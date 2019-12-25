package com.strongholds.game.model;

import com.strongholds.game.model.gameobject.IViewAnimatedActor;
import com.strongholds.game.model.gameobject.IViewGameObject;

public interface IReadOnlyModel {
    Object[] getGameObjects();
    Object[] getActors();
    IViewAnimatedActor getActor(String id);
    IViewGameObject getGameObject(String id);
}
