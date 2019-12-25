package com.strongholds.game.model.gameobject;

import com.strongholds.game.GameSingleton;

import java.util.LinkedList;
import java.util.List;
public class AnimatedActorState {//implements ListenableState {
    //private List<StateChangedListener> listenerList;
    private GameSingleton.ObjectState state;

    AnimatedActorState() {
      //  state = GameSingleton.ObjectState.IDLING;
     //   listenerList = new LinkedList<>();
    }
/*
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

 */
}
