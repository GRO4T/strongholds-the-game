package com.strongholds.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.*;

import com.strongholds.game.GameSingleton.ObjectType;
import com.strongholds.game.gameobject.GameObject;

public class Model{
    private World world;
    private int velocityIterations;
    private int positionIterations;

    Map<String, GameObject> gameObjectsMap;

    public Model(){}
    public Model(int velocityIterations, int positionIterations){
        gameObjectsMap = new HashMap<>();

        this.velocityIterations = velocityIterations;
        this.positionIterations = positionIterations;
        world = new World(new Vector2(0, -15), true);
    }
    public void update(float timeStep){
        world.step(timeStep, velocityIterations, positionIterations);
    }

    public void dispose(){ //TODO
    }

    // this may be rewritten to some Factory
    public void createObject(String objectId, Vector2 position, Vector2 size){
        gameObjectsMap.put(objectId, )
    }

    public Map<String, GameObject> getForegroundObjectsMap() {
        return foregroundObjectsMap;
    }
}
