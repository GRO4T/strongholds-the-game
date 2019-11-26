package com.strongholds.game.gameobject;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.strongholds.game.GameSingleton;

public class GameObject {
    private Body body;
    private GameSingleton.ObjectType type;
    private float width;
    private float height;

    public GameObject(World world, BodyDef bodyDef, float width, float height, GameSingleton.ObjectType type) {
        this.type = type;
        this.width = width;
        this.height = height;

        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(width, height);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.3f;
        fixtureDef.restitution = 0.5f;

        body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
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
}
