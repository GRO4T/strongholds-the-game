package com.strongholds.game.model.gameobject;

import com.strongholds.game.GameSingleton.ObjectState;

public interface IReadOnlyAnimatedActor extends IReadOnlyGameObject {
   ObjectState getState();
}
