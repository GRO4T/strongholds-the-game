package com.strongholds.game.gameobject;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.strongholds.game.GameSingleton;

import java.util.Iterator;
import java.util.TimerTask;

public class MeleeUnit extends Unit implements IUnit{
    float range = 5.0f;

    public MeleeUnit(World world, BodyDef bodyDef, float width, float height, GameSingleton.ObjectType type, String id) {
        super(world, bodyDef, width, height, type, id);
    }

    public void update(){
        if (getState() != GameSingleton.ObjectState.ATTACKING){
            if (getTargets().size() != 0 && canAttack){
                attack();
            }
            else{
                if (Math.abs(getVelocity().x) < 0.5f){
                    setState(GameSingleton.ObjectState.IDLING);
                }
                else{
                    setState(GameSingleton.ObjectState.MOVING);
                }
            }
        }

        if (isOnEnemySide())
            move(new Vector2(-1.0f, 0));
        else
            move(new Vector2(1.0f, 0));
    }

    public void attack(){
        setState(GameSingleton.ObjectState.ATTACKING);
        canAttack = false;
        AttackTask attackTask = new AttackTask();
        SuspendAttackTask suspendAttackTask = new SuspendAttackTask();
        attackTimer.schedule(attackTask, attackSpeed);
        attackTimer.schedule(suspendAttackTask, timeBetweenAttacks);
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    private class AttackTask extends TimerTask{
        @Override
        public void run() {
            Iterator it = getTargets().iterator();
            while (it.hasNext()){
                GameObject unit = (GameObject) it.next();
                if (isOnEnemySide() != unit.isOnEnemySide()){
                    unit.gotHit(damage);
                }
            }
            if (Math.abs(getVelocity().x) < 0.5f){
                setState(GameSingleton.ObjectState.IDLING);
            }
            else{
                setState(GameSingleton.ObjectState.MOVING);
            }
        }
    }

}
