package com.mygdx.strongholds;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class Model extends java.util.Observable{
    private AssetManager assetManager;

    private World world;
    private GameObject player;
    private int velocityIterations;
    private int positionIterations;

    public void init(int velocityIterations, int positionIterations){
        player.texture = new Texture(Gdx.files.internal)


        this.velocityIterations = velocityIterations;
        this.positionIterations = positionIterations;
        world = new World(new Vector2(0, -10), true);
        player = new GameObject();
    }
    public void update(float timeStep){
        world.step(timeStep, velocityIterations, positionIterations);
    }
}
