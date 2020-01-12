package com.strongholds.game.event;

import java.io.Serializable;

public class ModelEvent implements Serializable {
    private boolean unitHit;
    private String unitId;
    private int damage;

    public ModelEvent(boolean unitHit, String unitId, int damage) {
        this.unitHit = unitHit;
        this.unitId = unitId;
        this.damage = damage;
    }

    public boolean isUnitHit() {
        return unitHit;
    }

    public String getUnitId() {
        return unitId;
    }

    public int getDamage() {
        return damage;
    }
}
