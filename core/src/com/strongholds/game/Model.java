package com.strongholds.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.*;

import com.strongholds.game.GameSingleton.ObjectType;
import com.strongholds.game.gameobject.AnimatedActor;
import com.strongholds.game.gameobject.GameObject;
import com.strongholds.game.gameobject.GameObjectsFactory;

public class Model
{
    private World world;
    private final int velocityIterations = 6;
    private final int positionIterations = 2;
    private final float worldGravity = -15.0f;

    Queue events;

    int nextId;
    GameObjectsFactory gameObjectsFactory;
    Map<String, GameObject> gameObjectsMap;
    Map<String, AnimatedActor> actorsMap;

    public Model()
    {
        world = new World(new Vector2(0, worldGravity), true);

        gameObjectsFactory = new GameObjectsFactory(world);
        gameObjectsMap = new HashMap<>();
        actorsMap = new HashMap<>();
    }

    public void update(float timeStep)
    {
        world.step(timeStep, velocityIterations, positionIterations);

        for (AnimatedActor actor : actorsMap.values()){
            actor.setState(GameSingleton.ObjectState.ATTACKING);
        }
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
        AnimatedActor newObject = (AnimatedActor)gameObjectsFactory.createObject(objectType, position, size);
        actorsMap.put(Integer.toString(nextId++), newObject);
    }

    public Object[] getGameObjects() {
        return gameObjectsMap.values().toArray();
    }

    public Object[] getActors(){
        return actorsMap.values().toArray();
    }
}
