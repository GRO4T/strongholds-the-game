package com.strongholds.game.event;

import com.strongholds.game.gameobject.GameObject;
import com.strongholds.game.gameobject.IUnit;

import java.io.Serializable;
import java.util.Map;

public class SyncEvent implements Serializable {
    private double gameTime; // it is used to determine whose game simulated more frames

    public SyncEvent(double gameTime) {
        this.gameTime = gameTime;
    }

    public double getGameTime() {
        return gameTime;
    }
}
