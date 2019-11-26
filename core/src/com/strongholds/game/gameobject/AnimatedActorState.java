package com.strongholds.game.gameobject;

import com.strongholds.game.GameSingleton;
import com.strongholds.game.gameobject.ListenableState;
import com.strongholds.game.gameobject.StateChangedListener;

import java.util.List;

public class AnimatedActorState implements ListenableState {
    private List<StateChangedListener> listenerList;
    private GameSingleton.ObjectState state;

    @Override
    public void addListener(StateChangedListener stateChangedListener) {
        listenerList.add(stateChangedListener);
    }

    @Override
    public GameSingleton.ObjectState getState() {
        return state;
    }

    @Override
    public void setState(GameSingleton.ObjectState newState) {
        if (newState != state){
            for (StateChangedListener listener : listenerList)
                listener.stateChanged(newState);
        }
        state = newState;
    }
}
