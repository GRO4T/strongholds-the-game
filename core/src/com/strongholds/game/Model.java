package com.strongholds.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class Model extends java.util.Observable{
    private World world;
    private int velocityIterations;
    private int positionIterations;

    public void init(int velocityIterations, int positionIterations){
        this.velocityIterations = velocityIterations;
        this.positionIterations = positionIterations;
        world = new World(new Vector2(0, -10), true);
    }
    public void update(float timeStep){
        world.step(timeStep, velocityIterations, positionIterations);
    }

    public void dispose(){

    }
}
