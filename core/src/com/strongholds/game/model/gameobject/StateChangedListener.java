package com.strongholds.game.model.gameobject;

import com.strongholds.game.GameSingleton;

public interface StateChangedListener{
    void stateChanged(GameSingleton.ObjectState newState);
}
