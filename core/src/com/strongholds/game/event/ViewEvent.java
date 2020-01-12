package com.strongholds.game.event;

import com.strongholds.game.GameSingleton;

import java.io.Serializable;

public class ViewEvent implements Serializable {
    private boolean trainUnit = false;
    private GameSingleton.ObjectType unitType = GameSingleton.ObjectType.SWORDSMAN;
    private String unitId = "";
    private boolean isEnemy = false;

    private boolean setUsername = false;
    private String username = "";

    private boolean togglePaused = false;

    public ViewEvent(boolean trainUnit, GameSingleton.ObjectType unitType) {
        this.trainUnit = trainUnit;
        this.unitType = unitType;
    }

    public ViewEvent(boolean setUsername, String username){
        this.setUsername = setUsername;
        this.username = username;
    }

    public ViewEvent(boolean togglePaused){
        this.togglePaused = togglePaused;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public void setEnemy(boolean enemy) {
        isEnemy = enemy;
    }

    public boolean toTrainUnit(){ return trainUnit; }
    public GameSingleton.ObjectType getUnitType(){ return unitType; }
    public String getUnitId(){ return unitId; }
    public boolean getIsEnemy(){ return isEnemy; }

    public boolean isSetUsername() {
        return setUsername;
    }

    public String getUsername() {
        return username;
    }

    public boolean isTogglePaused() {
        return togglePaused;
    }
}
