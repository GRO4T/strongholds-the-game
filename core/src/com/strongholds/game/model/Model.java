package com.strongholds.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.*;

import com.strongholds.game.GameSingleton;
import com.strongholds.game.GameSingleton.ObjectType;
import com.strongholds.game.model.gameobject.*;

public class Model implements IModel
{
    private World world;
    private final int velocityIterations = 6;
    private final int positionIterations = 2;
    private final float worldGravity = -15.0f;

    //Queue events;

    long money;

    GameObjectsFactory gameObjectsFactory;
    Map<String, GameObject> gameObjectsMap;
    Map<String, AnimatedActor> actorsMap;

    MyContactListener contactListener;

    public Model()
    {
        money = 200L;

        world = new World(new Vector2(0, worldGravity), true);
        contactListener = new MyContactListener();
        world.setContactListener(contactListener);

        gameObjectsFactory = new GameObjectsFactory(world);
        gameObjectsMap = new HashMap<>();
        actorsMap = new HashMap<>();
    }

    public void update(float timeStep)
    {
        world.step(timeStep, velocityIterations, positionIterations);

        for (AnimatedActor actor : actorsMap.values()){
            //actor.setState(GameSingleton.ObjectState.ATTACKING);
        }

        AnimatedActor player = actorsMap.get("player");
        if (Gdx.input.isKeyPressed(Input.Keys.A)){
            player.getBody().applyLinearImpulse(new Vector2(-10, 0), player.getBody().getPosition(), true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)){
            player.getBody().applyLinearImpulse(new Vector2(10, 0), player.getBody().getPosition(), true);
        }
    }

    public void dispose()
    {
        //TODO
    }

    public void createObject(String id, ObjectType objectType, Vector2 position, Vector2 size) {
        GameObject newObject = gameObjectsFactory.createObject(id, objectType, position, size);
        gameObjectsMap.put(id, newObject);
    }
/*
    public void createActor(ObjectType objectType, Vector2 position, Vector2 size) {
        AnimatedActor newObject = (AnimatedActor)gameObjectsFactory.createObject(objectType, position, size);
        actorsMap.put(Integer.toString(nextId++), newObject);
    }

 */
    public void createActor(String id, ObjectType objectType, Vector2 position, Vector2 size) {
        AnimatedActor newObject = (AnimatedActor)gameObjectsFactory.createObject(id, objectType, position, size);
        actorsMap.put(id, newObject);
    }

    public Object[] getGameObjects() {
        return gameObjectsMap.values().toArray();
    }

    public Object[] getActors(){
        return actorsMap.values().toArray();
    }

    public IReadOnlyAnimatedActor getActor(String id){ return actorsMap.get(id); }

    public IReadOnlyGameObject getGameObject(String id){ return gameObjectsMap.get(id); }

    public long getMoney(){ return money; }

    public void addMoney(long value){
        money += value;
        if (money < 0L)
            money = 0L;
    }
}
