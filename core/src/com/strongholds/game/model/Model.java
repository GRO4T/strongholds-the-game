package com.strongholds.game.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.*;

import com.strongholds.game.GameSingleton;
import com.strongholds.game.GameSingleton.ObjectType;
import com.strongholds.game.gameobject.*;

public class Model implements IModel, DeathListener
{
    private World world;
    private final int velocityIterations = 6;
    private final int positionIterations = 2;
    private final float worldGravity = -15.0f;

    //Queue events;
    private Timer taskScheduler;
    private boolean addMoney = true;
    private int incomeInterval = 1000;
    private int moneyGain = 10;
    private long money;
    private final long startCash = 200L;

    private GameObjectsFactory gameObjectsFactory;
    private Map<String, GameObject> gameObjectsMap;
    private Map<String, IUnit> actorsMap;
    private LinkedList<String> listOfDeadUnitsIds;

    private MyContactListener contactListener;

    public Model()
    {
        money = startCash;

        world = new World(new Vector2(0, worldGravity), true);
        GameSingleton.getGameSingleton().setWorld(world);
        contactListener = new MyContactListener();
        world.setContactListener(contactListener);

        gameObjectsFactory = new GameObjectsFactory(world);
        gameObjectsMap = new HashMap<>();
        actorsMap = new HashMap<>();
        listOfDeadUnitsIds = new LinkedList<>();

        taskScheduler = new Timer(true);
    }

    public void update(float timeStep)
    {
        for (IUnit actor : actorsMap.values()){
            actor.update();
        }

        if (addMoney){
            addMoney = false;
            AddMoneyTask addMoneyTask = new AddMoneyTask();
            taskScheduler.schedule(addMoneyTask, incomeInterval);
        }

        world.step(timeStep, velocityIterations, positionIterations);

        if (listOfDeadUnitsIds.size() > 0){
            String id = listOfDeadUnitsIds.poll();
            IUnit deadActor = actorsMap.get(id);
            if (deadActor != null){
                actorsMap.get(id).dispose();
                actorsMap.remove(id);
            }
        }
    }

    public void dispose()
    {
        //TODO
    }

    public void createObject(String id, ObjectType objectType, Vector2 position, Vector2 size) {
        GameObject newObject = gameObjectsFactory.createObject(id, objectType, position, size, false);
        gameObjectsMap.put(id, newObject);
    }

    public void createUnit(String id, ObjectType objectType, Vector2 position, Vector2 size, boolean isEnemy) {
        Unit newObject = (Unit)gameObjectsFactory.createObject(id, objectType, position, size, isEnemy);
        newObject.setDeathListener(this);
        actorsMap.put(id, (IUnit)newObject);
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

    public int getBaseHealth(){
        return gameObjectsMap.get("base").getHealth();
    }

    public int getEnemyBaseHealth(){
        return gameObjectsMap.get("enemyBase").getHealth();
    }

    public void notifyDeadUnit(String unitId) {
        listOfDeadUnitsIds.add(unitId);
    }

    private class AddMoneyTask extends TimerTask {

        @Override
        public void run() {
            addMoney(moneyGain);
            addMoney = true;
        }
    }
}
