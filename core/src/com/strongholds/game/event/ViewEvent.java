package com.strongholds.game.event;

import com.strongholds.game.GameSingleton;

import java.io.Serializable;

public class ViewEvent implements Serializable {
    private boolean trainUnit = false;
    private GameSingleton.ObjectType unitType = GameSingleton.ObjectType.SWORDSMAN;
    private boolean isEnemy = false;

    private

    public ViewEvent(boolean trainUnit, GameSingleton.ObjectType unitType) {
        this.trainUnit = trainUnit;
        this.unitType = unitType;
    }

    public void setIsEnemy(boolean value){
        isEnemy = value;
    }


    public boolean toTrainUnit(){ return trainUnit; }
    public GameSingleton.ObjectType getUnitType(){ return unitType; }
    public boolean getIsEnemy(){ return isEnemy; }
}
