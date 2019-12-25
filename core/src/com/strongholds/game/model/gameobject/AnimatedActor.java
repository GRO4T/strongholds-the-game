package com.strongholds.game.model.gameobject;

import com.badlogic.gdx.physics.box2d.*;
import com.strongholds.game.GameSingleton;
import com.strongholds.game.GameSingleton.ObjectType;
import com.strongholds.game.GameSingleton.ObjectState;

import java.util.LinkedList;

public class AnimatedActor extends GameObject implements IAnimatedActor {
    //private AnimatedActorState state;
    private ObjectState state;
    private LinkedList<AnimatedActor> targets;

    private boolean isEnemy;

    public AnimatedActor(World world, BodyDef bodyDef, float width, float height, ObjectType type, String id) {
        super(world, bodyDef, width, height, type, id);
        //state = new AnimatedActorState();
        targets = new LinkedList<>();
        isEnemy = false;

        //set main fixture's collisionFilter
        Filter filter = new Filter();
        filter.categoryBits = GameSingleton.ACTOR_COLLISION_MASK; // 0x0002
        filter.maskBits = GameSingleton.GAME_OBJECT_COLLISION_MASK
                | GameSingleton.ACTOR_COLLISION_MASK
                | GameSingleton.SENSOR_COLLISION_MASK; // 0x0007 = 0x0004 OR 0x0002 OR 0x0001
        body.getFixtureList().first().setFilterData(filter);

        //create sensor definition
        CircleShape sensorShape = new CircleShape();
        sensorShape.setRadius(4);
        FixtureDef sensorDef = new FixtureDef();
        sensorDef.isSensor = true;
        sensorDef.shape = sensorShape;

        //create sensor, set user its userData and collisionFilter
        Fixture sensor  = body.createFixture(sensorDef);
        sensor.setUserData(this);
        Filter sensorFilter = new Filter();
        sensorFilter.categoryBits = GameSingleton.SENSOR_COLLISION_MASK;
        sensorFilter.maskBits = GameSingleton.ACTOR_COLLISION_MASK;
        sensor.setFilterData(sensorFilter);
    }

    public GameSingleton.ObjectState getState(){
        return state;
    }

    public void setState(GameSingleton.ObjectState newState){
        state = newState;
    }


    @Override
    public void gotHit(int damage){
        //System.out.println("got hit for " + damage);
    }

    public void addTarget(AnimatedActor target){
        if (!targets.contains(target)){
            targets.add(target);
        }
    }

    public void removeTarget(AnimatedActor target){
        if (targets.contains(target)){
            targets.remove(target);
        }
    }

    public LinkedList<AnimatedActor> getTargets() {
        return targets;
    }
}
