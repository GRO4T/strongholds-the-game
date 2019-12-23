package com.strongholds.game.model.gameobject;

import com.badlogic.gdx.physics.box2d.*;
import com.strongholds.game.GameSingleton;

import java.util.LinkedList;
import java.util.List;

public class AnimatedActor extends GameObject{
    private AnimatedActorState state;
    private LinkedList<AnimatedActor> targets;

    private String id;

    public AnimatedActor(World world, BodyDef bodyDef, float width, float height, GameSingleton.ObjectType type) {
        super(world, bodyDef, width, height, type);
        state = new AnimatedActorState();
        targets = new LinkedList<>();
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
        return state.getState();
    }

    public void setState(GameSingleton.ObjectState newState){
        state.setState(newState);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
