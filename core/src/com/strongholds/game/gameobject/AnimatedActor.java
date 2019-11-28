package com.strongholds.game.gameobject;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.strongholds.game.GameSingleton;

public class AnimatedActor extends GameObject{
    private AnimatedActorState state;

    public AnimatedActor(World world, BodyDef bodyDef, float width, float height, GameSingleton.ObjectType type) {
        super(world, bodyDef, width, height, type);
        state = new AnimatedActorState();
    }

    public GameSingleton.ObjectState getState(){
        return state.getState();
    }

    public void setState(GameSingleton.ObjectState newState){
        state.setState(newState);
    }
}
