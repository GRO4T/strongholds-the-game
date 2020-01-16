package com.strongholds.game.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.*;

import com.strongholds.game.GameSingleton;
import com.strongholds.game.GameSingleton.ObjectType;
import com.strongholds.game.controller.IModelController;
import com.strongholds.game.event.ModelEvent;
import com.strongholds.game.gameobject.*;

public class Model implements IModel, DeathListener
{
    private World world;
    private final int velocityIterations = 6;
    private final int positionIterations = 2;
    private final float worldGravity = -15.0f;

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
    private IModelController controller;

    private final int baseInitialHealth = 100;

    public Model(IModelController controller)
    {
        this.controller = controller;
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

    @Override
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

    @Override
    public void dispose()
    {
        //TODO
    }

    @Override
    public void createObject(String id, ObjectType objectType, Vector2 position, Vector2 size, boolean isEnemy) {
        GameObject newObject = gameObjectsFactory.createObject(id, objectType, position, size, isEnemy);
        if (objectType == ObjectType.BASE){
            newObject.setHealth(baseInitialHealth);
            newObject.setMaxHealth(baseInitialHealth);
        }

        gameObjectsMap.put(id, newObject);
    }

    @Override
    public void createUnit(String id, ObjectType objectType, Vector2 position, Vector2 size, boolean isEnemy) {
        Unit newUnit = (Unit)gameObjectsFactory.createObject(id, objectType, position, size, isEnemy);
        newUnit.setDeathListener(this);
        newUnit.setModel(this);
        actorsMap.put(id, newUnit);
    }

    @Override
    public Object[] getGameObjects() {
        return gameObjectsMap.values().toArray();
    }

    @Override
    public Object[] getActors(){
        return actorsMap.values().toArray();
    }

    @Override
    public IReadOnlyAnimatedActor getActor(String id){ return actorsMap.get(id); }

    @Override
    public IReadOnlyGameObject getGameObject(String id){ return gameObjectsMap.get(id); }

    @Override
    public long getMoney(){ return money; }

    @Override
    public void addMoney(long value){
        money += value;
        if (money < 0L)
            money = 0L;
    }

    @Override
    public int getBaseHealth(){
        return gameObjectsMap.get("base").getHealth();
    }

    @Override
    public int getEnemyBaseHealth(){
        return gameObjectsMap.get("enemyBase").getHealth();
    }

    @Override
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

    public void unitHit(String id, int damage){
        if (id.equals("enemyBase")){
            gameObjectsMap.get("base").gotHit(damage);
        }
        else{
            actorsMap.get(id).gotHit(damage);
        }
    }
    public void enemyUnitHit(String id, int damage){
        controller.addEvent(new ModelEvent(true, id, damage));
    }
}
