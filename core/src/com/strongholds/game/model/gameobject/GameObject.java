package com.strongholds.game.model.gameobject;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.strongholds.game.GameSingleton;

public class GameObject {
    protected Body body;
    private GameSingleton.ObjectType type;
    private float width;
    private float height;

    public GameObject(World world, BodyDef bodyDef, float width, float height, GameSingleton.ObjectType type) {
        this.type = type;
        this.width = width;
        this.height = height;
        //create main fixture's definition
        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(width, height);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.3f;
        fixtureDef.restitution = 0.5f;
        //create body, main fixture and its userData and collisionFilter(collisionFilter can be overridden by inheriting classes)
        body = world.createBody(bodyDef);
        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData(this);
        Filter filter = new Filter();
        filter.categoryBits = GameSingleton.GAME_OBJECT_COLLISION_MASK; // 0x0001
        filter.maskBits = GameSingleton.GAME_OBJECT_COLLISION_MASK
                | GameSingleton.ACTOR_COLLISION_MASK
                | GameSingleton.SENSOR_COLLISION_MASK; // 0x0007 = 0x0004 OR 0x0002 OR 0x0001
        fixture.setFilterData(filter);
    }

    public Vector2 getPosition(){
        return body.getPosition();
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public GameSingleton.ObjectType getType() {
        return type;
    }

    public Body getBody(){
        return body;
    }

    public void gotHit(int damage){

    }
}
