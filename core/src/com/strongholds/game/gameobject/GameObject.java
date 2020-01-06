package com.strongholds.game.gameobject;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.strongholds.game.GameSingleton;

import java.io.Serializable;

public class GameObject implements IGameObject{
    World world;
    protected Body body;
    private GameSingleton.ObjectType type;
    private float width;
    private float height;

    private String id;

    int health = 1000;

    private boolean isOnEnemySide = false;

    public GameObject(World world, BodyDef bodyDef, float width, float height, GameSingleton.ObjectType type, String id) {
        this.world = world;
        this.id = id;
        this.type = type;
        this.width = width;
        this.height = height;
        //create main fixture's definition
        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(width, height);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;
        fixtureDef.density = 3.0f;
        fixtureDef.friction = 0.6f;
        fixtureDef.restitution = 0.0f;
        //create body, main fixture and its userData and collisionFilter(collisionFilter can be overridden by inheriting classes)
        body = world.createBody(bodyDef);
        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData(this);
        Filter filter = new Filter();

        if (type == GameSingleton.ObjectType.BASE)
            filter.categoryBits = GameSingleton.ACTOR_COLLISION_MASK;
        else
            filter.categoryBits = GameSingleton.GAME_OBJECT_COLLISION_MASK; // 0x0001

        filter.maskBits = GameSingleton.GAME_OBJECT_COLLISION_MASK
                | GameSingleton.ACTOR_COLLISION_MASK
                | GameSingleton.SENSOR_COLLISION_MASK; // 0x0007 = 0x0004 OR 0x0002 OR 0x0001
        fixture.setFilterData(filter);
    }

    public void dispose(){
        world.destroyBody(body);
    }

    public Vector2 getPosition(){
        return body.getPosition();
    }

    public Vector2 getVelocity(){ return body.getLinearVelocity(); }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public GameSingleton.ObjectType getType() {
        return type;
    }

    public void gotHit(int damage){
        System.out.println("base hit for " + damage);
        health -= damage;
        if (health < 0)
            health = 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isOnEnemySide(){ return isOnEnemySide; }

    public void setIsOnEnemySide(boolean isOnEnemySide){ this.isOnEnemySide = isOnEnemySide; }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }
}
