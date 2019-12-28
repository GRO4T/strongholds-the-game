package com.strongholds.game.model.gameobject;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.strongholds.game.GameSingleton;

import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

public class MeleeUnit extends Unit implements IUnit{
    float range = 5.0f;
    TimerTask attackRequest;

    public MeleeUnit(World world, BodyDef bodyDef, float width, float height, GameSingleton.ObjectType type, String id) {
        super(world, bodyDef, width, height, type, id);
    }

    public void update(){
        if (isOnEnemySide())
            move(new Vector2(-1.0f, 0));
        else if (getState() == GameSingleton.ObjectState.IDLING)
            attack();
    }

    public void attack(){
        setState(GameSingleton.ObjectState.ATTACKING);
        AttackTask attackTask = new AttackTask();
        Timer timer = new Timer(true);
        timer.schedule(attackTask, 1000);
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    private class AttackTask extends TimerTask{
        @Override
        public void run() {
            Iterator it = getTargets().iterator();
            while (it.hasNext()){
                AnimatedActor unit = (AnimatedActor) it.next();
                if (unit.isOnEnemySide()){// && Math.abs(unit.getPosition().x - body.getPosition().x) < range){
                    unit.gotHit(10);
                }
            }
            setState(GameSingleton.ObjectState.IDLING);
            cancel();
        }
    }
}
