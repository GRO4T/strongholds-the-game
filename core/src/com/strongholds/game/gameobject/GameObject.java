package com.strongholds.game.gameobject;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.strongholds.game.GameSingleton;

/**
 * Represents static game object. Base class for AnimatedActor
 */
public class GameObject implements IGameObject{
    /**
     * reference to an instance of global GameSingleton
     */
    private GameSingleton gameSingleton;
    /**
     * reference to an instance of box2d world
     */
    private World world;
    /**
     * instance of the box2d body
     */
    protected Body body;
    /**
     * object type
     */
    private GameSingleton.ObjectType type;
    /**
     * object width (in meters) (width of the first fixture)
     */
    private float width;
    /**
     * object height (in meters) (height of the first fixture)
     */
    private float height;

    /**
     * object id
     */
    private String id;

    /**
     * object current health
     */
    protected int health;
    /**
     * object max health
     */
    protected int maxHealth;

    /**
     * flag telling whether object is on enemy side
     */
    private boolean isEnemy;

    /**
     * Creates a new GameObject
     * @param bodyDef box2d body definition
     * @param width object width (in meters)
     * @param height object height (in meters)
     * @param type object type
     * @param id object id
     * @param isEnemy whether object is on enemy side
     */
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

    /**
     * Creates a new fixture and sets its collision filter
     */
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

    /**
     * Sets fixture's collision filter
     * @param fixture fixture that needs to have its collision filter set
     */
    private void setCollisionFilter(Fixture fixture){
        Filter filter = new Filter();

        if (type == GameSingleton.ObjectType.BASE){
            if (isEnemy){
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
                filter.categoryBits = GameSingleton.ENEMY_ACTOR_COLLISION_MASK;
                filter.maskBits = GameSingleton.GAME_OBJECT_COLLISION_MASK
                        | GameSingleton.SENSOR_COLLISION_MASK
                        | GameSingleton.ACTOR_COLLISION_MASK
                        | GameSingleton.ENEMY_ACTOR_COLLISION_MASK
                        | GameSingleton.BASE_COLLISION_MASK;
            }
            else{
                filter.categoryBits = GameSingleton.ACTOR_COLLISION_MASK;
                filter.maskBits = GameSingleton.GAME_OBJECT_COLLISION_MASK
                        | GameSingleton.SENSOR_COLLISION_MASK
                        | GameSingleton.ENEMY_ACTOR_COLLISION_MASK
                        | GameSingleton.ACTOR_COLLISION_MASK
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

    public int getHealth() {
        return health;
    }

    public int getMaxHealth(){
        return maxHealth;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    /**
     * Sets object max health
     * @param value max health
     */
    public void setMaxHealth(int value) {
        maxHealth = value;
    }
}
