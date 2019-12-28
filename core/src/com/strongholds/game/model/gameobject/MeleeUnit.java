package com.strongholds.game.model.gameobject;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.strongholds.game.GameSingleton;

import java.util.Iterator;

public class MeleeUnit extends Unit implements IUnit{
    float range = 100.0f;

    public MeleeUnit(World world, BodyDef bodyDef, float width, float height, GameSingleton.ObjectType type, String id) {
        super(world, bodyDef, width, height, type, id);
    }

    public void update(){
        attack();
    }

    public void attack(){
        setState(GameSingleton.ObjectState.ATTACKING);
        dealDamage();
    }

    private void dealDamage(){
        Iterator it = getTargets().iterator();
        while (it.hasNext()){
            AnimatedActor unit = (AnimatedActor) it.next();
            if (unit.isOnEnemySide() && Math.abs(unit.getPosition().x - body.getPosition().x) < range){
                unit.gotHit(10);
            }
        }
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
