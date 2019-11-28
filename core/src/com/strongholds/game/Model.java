package com.strongholds.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.*;

import com.strongholds.game.GameSingleton.ObjectType;
import com.strongholds.game.gameobject.GameObject;
import com.strongholds.game.gameobject.GameObjectsFactory;

public class Model
{
    private World world;
    private int velocityIterations;
    private int positionIterations;

    int nextId;
    GameObjectsFactory gameObjectsFactory;
    Map<String, GameObject> gameObjectsMap;
    Map<String, GameObject> actorsMap;

    public Model(int velocityIterations, int positionIterations)
    {
        this.velocityIterations = velocityIterations;
        this.positionIterations = positionIterations;
        world = new World(new Vector2(0, -15), true);

        gameObjectsFactory = new GameObjectsFactory(world);
        gameObjectsMap = new HashMap<>();
        actorsMap = new HashMap<>();
    }

    public void update(float timeStep)
    {
        world.step(timeStep, velocityIterations, positionIterations);
    }

    public void dispose()
    {
        //TODO
    }

    // this may be rewritten to some Factory
    public void createObject(ObjectType objectType, Vector2 position, Vector2 size) {
        GameObject newObject = gameObjectsFactory.createObject(objectType, position, size);
        gameObjectsMap.put(Integer.toString(nextId++), newObject);
    }

    public void createActor(ObjectType objectType, Vector2 position, Vector2 size) {
        GameObject newObject = gameObjectsFactory.createObject(objectType, position, size);
        actorsMap.put(Integer.toString(nextId++), newObject);
    }

    public Object[] getGameObjects() {
        return gameObjectsMap.values().toArray();
    }

    public Object[] getActors(){
        return actorsMap.values().toArray();
    }
}
