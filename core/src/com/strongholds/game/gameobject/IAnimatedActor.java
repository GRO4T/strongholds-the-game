package com.strongholds.game.gameobject;

import com.strongholds.game.GameSingleton;

public interface IAnimatedActor extends IReadOnlyAnimatedActor, IGameObject{
    void setState(GameSingleton.ObjectState newState);
}
