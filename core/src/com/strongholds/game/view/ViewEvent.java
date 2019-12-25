package com.strongholds.game.view;

import com.strongholds.game.GameSingleton;
import com.strongholds.game.controller.IEvent;

public class ViewEvent implements IEvent {
    private boolean trainUnit = false;
    private GameSingleton.ObjectType unitType = GameSingleton.ObjectType.SWORDSMAN;

    public ViewEvent(boolean trainUnit, GameSingleton.ObjectType unitType) {
        this.trainUnit = trainUnit;
        this.unitType = unitType;
    }

    public boolean toTrainUnit(){ return trainUnit; }
    public GameSingleton.ObjectType getUnitType(){ return unitType; }
}
