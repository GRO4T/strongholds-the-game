package com.strongholds.game.gameobject;

import com.strongholds.game.GameSingleton;

public interface ListenableState {
    void addListener(StateChangedListener stateChangedListener);
    GameSingleton.ObjectState getState();
    void setState(GameSingleton.ObjectState newState);
}
