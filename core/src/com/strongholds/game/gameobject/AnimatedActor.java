package com.strongholds.game.gameobject;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.strongholds.game.GameSingleton;
import com.strongholds.game.GameSingleton.ObjectType;
import com.strongholds.game.GameSingleton.ObjectState;

import java.util.LinkedList;

public class AnimatedActor extends GameObject implements IAnimatedActor {
    //private AnimatedActorState state;
    private ObjectState state;
    private LinkedList<GameObject> targets;

    public AnimatedActor(BodyDef bodyDef, float width, float height, ObjectType type, String id, boolean isEnemy) {
        super(bodyDef, width, height, type, id, isEnemy);
        //state = new AnimatedActorState();
        targets = new LinkedList<>();
/*
        //set main fixture's collisionFilter
        Filter filter = new Filter();
        filter.categoryBits = GameSingleton.ACTOR_COLLISION_MASK; // 0x0002
        filter.maskBits = GameSingleton.GAME_OBJECT_COLLISION_MASK
                | GameSingleton.ACTOR_COLLISION_MASK
                | GameSingleton.SENSOR_COLLISION_MASK; // 0x0007 = 0x0004 OR 0x0002 OR 0x0001
        body.getFixtureList().first().setFilterData(filter);

 */

        //create sensor definition
        CircleShape sensorShape = new CircleShape();
        sensorShape.setRadius(1.5f);
        FixtureDef sensorDef = new FixtureDef();
        sensorDef.isSensor = true;
        sensorDef.shape = sensorShape;

        //create sensor, set user its userData and collisionFilter
        Fixture sensor  = body.createFixture(sensorDef);
        sensor.setUserData(this);
        Filter sensorFilter = new Filter();
        sensorFilter.categoryBits = GameSingleton.SENSOR_COLLISION_MASK;
        if (isEnemy){
            sensorFilter.maskBits = GameSingleton.ACTOR_COLLISION_MASK
                    | GameSingleton.BASE_COLLISION_MASK;

        }
        else{
            sensorFilter.maskBits = GameSingleton.ENEMY_ACTOR_COLLISION_MASK
                    | GameSingleton.ENEMY_BASE_COLLISION_MASK;
        }
        sensor.setFilterData(sensorFilter);
    }

    @Override
    public void dispose(){
        super.dispose();
    }

    public GameSingleton.ObjectState getState(){
        return state;
    }

    public void setState(GameSingleton.ObjectState newState){
        state = newState;
    }


    @Override
    public void gotHit(int damage){
        System.out.println(getId() + " got hit for " + damage);
        Vector2 impulse;
        float knockbackMultiplier = 15.0f;
        if (isEnemy()){
            impulse = new Vector2(damage * knockbackMultiplier, 0);
        }
        else
            impulse = new Vector2(-damage * knockbackMultiplier, 0);
        body.applyLinearImpulse(impulse, body.getPosition(), true);
    }

    public void addTarget(GameObject target){
        if (!targets.contains(target)){
            targets.add(target);
        }
    }

    public void removeTarget(GameObject target){
        if (targets.contains(target)){
            targets.remove(target);
        }
    }

    public LinkedList<GameObject> getTargets() {
        return targets;
    }
}
