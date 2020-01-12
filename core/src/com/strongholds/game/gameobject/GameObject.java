package com.strongholds.game.gameobject;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.strongholds.game.GameSingleton;

public class GameObject implements IGameObject{
    GameSingleton gameSingleton;
    World world;
    protected Body body;
    private GameSingleton.ObjectType type;
    private float width;
    private float height;

    private String id;

    int health = 1000;

    private boolean isEnemy = false;

    public GameObject(BodyDef bodyDef, float width, float height, GameSingleton.ObjectType type, String id, boolean isEnemy) {
        gameSingleton = GameSingleton.getGameSingleton();
        this.world = gameSingleton.getWorld();
        this.id = id;
        this.type = type;
        this.width = width;
        this.height = height;
        this.isEnemy = isEnemy;

        body = world.createBody(bodyDef);
        createFixture();
    }

    private void createFixture(){
        //create main fixture's definition
        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(width, height);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;
        fixtureDef.density = 3.0f;
        fixtureDef.friction = 0.6f;
        fixtureDef.restitution = 0.0f;
        //create body, main fixture and its userData and collisionFilter(collisionFilter can be overridden by inheriting classes)
        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData(this);

        setCollisionFilter(fixture);
    }

    private void setCollisionFilter(Fixture fixture){
        Filter filter = new Filter();

        if (type == GameSingleton.ObjectType.BASE){
            if (isEnemy){
                System.out.println("im an enemy base");
                filter.categoryBits = GameSingleton.ENEMY_BASE_COLLISION_MASK;
                filter.maskBits = GameSingleton.GAME_OBJECT_COLLISION_MASK
                        | GameSingleton.SENSOR_COLLISION_MASK
                        | GameSingleton.ACTOR_COLLISION_MASK;
            }
            else{
                filter.categoryBits = GameSingleton.BASE_COLLISION_MASK;
                filter.maskBits = GameSingleton.GAME_OBJECT_COLLISION_MASK
                        | GameSingleton.SENSOR_COLLISION_MASK
                        | GameSingleton.ENEMY_ACTOR_COLLISION_MASK;
            }
        }
        else if (type == GameSingleton.ObjectType.SWORDSMAN){
            if (isEnemy){
                System.out.println("hey");
                filter.categoryBits = GameSingleton.ENEMY_ACTOR_COLLISION_MASK;
                filter.maskBits = GameSingleton.GAME_OBJECT_COLLISION_MASK
                        | GameSingleton.SENSOR_COLLISION_MASK
                        | GameSingleton.ACTOR_COLLISION_MASK
                        | GameSingleton.BASE_COLLISION_MASK;
            }
            else{
                filter.categoryBits = GameSingleton.ACTOR_COLLISION_MASK;
                filter.maskBits = GameSingleton.GAME_OBJECT_COLLISION_MASK
                        | GameSingleton.SENSOR_COLLISION_MASK
                        | GameSingleton.ENEMY_ACTOR_COLLISION_MASK
                        | GameSingleton.ENEMY_BASE_COLLISION_MASK;
            }
        }
        else{
            filter.categoryBits = GameSingleton.GAME_OBJECT_COLLISION_MASK;
            filter.maskBits = GameSingleton.GAME_OBJECT_COLLISION_MASK
                    | GameSingleton.SENSOR_COLLISION_MASK
                    | GameSingleton.ACTOR_COLLISION_MASK
                    | GameSingleton.ENEMY_ACTOR_COLLISION_MASK
                    | GameSingleton.BASE_COLLISION_MASK
                    | GameSingleton.ENEMY_BASE_COLLISION_MASK;
        }

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

    public boolean isEnemy(){ return isEnemy; }

    public void setIsOnEnemySide(boolean isOnEnemySide){ this.isEnemy = isOnEnemySide; }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }
}
