package com.strongholds.game.model.gameobject;

import com.strongholds.game.GameSingleton;

public interface IAnimatedActor extends IReadOnlyAnimatedActor{
    void setState(GameSingleton.ObjectState newState);
}
