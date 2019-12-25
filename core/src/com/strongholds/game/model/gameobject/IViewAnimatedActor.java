package com.strongholds.game.model.gameobject;

import com.strongholds.game.GameSingleton.ObjectState;

public interface IViewAnimatedActor extends IViewGameObject{
   ObjectState getState();
   void setState(ObjectState newState);
}
