package com.strongholds.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.*;

public class Model extends java.util.Observable{

    public class GameObject {
        private Body body;
        private ObjectType type;
        private float width;
        private float height;

        public GameObject(World world, BodyDef bodyDef, float width, float height, ObjectType type) {
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

        public ObjectType getType() {
            return type;
        }
    }

    public enum ObjectType{
        BALL, PLATFORM, BACKGROUND_IMAGE;
    }

    private World world;
    private int velocityIterations;
    private int positionIterations;

    List<GameObject> projectiles;
    List<GameObject> backgroundObjects;
    Map<ObjectType, GameObject> foregroundObjectsMap;

    public GameObject ball;
    public GameObject platform;

    public Model(){}
    public Model(int velocityIterations, int positionIterations){
        projectiles = new LinkedList<>();
        backgroundObjects = new ArrayList<>();
        foregroundObjectsMap = new HashMap<>();

        this.velocityIterations = velocityIterations;
        this.positionIterations = positionIterations;
        world = new World(new Vector2(0, -15), true);

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(12.5f, 37.5f);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        ball = new GameObject(world, bodyDef, 8, 8, ObjectType.BALL);

        bodyDef.position.set(18.75f, 0);
        bodyDef.type = BodyDef.BodyType.StaticBody;
        platform = new GameObject(world, bodyDef, 16, 2, ObjectType.BALL);
    }
    public void update(float timeStep){
        world.step(timeStep, velocityIterations, positionIterations);
    }

    public void dispose(){
        //TODO
    }

    public List<GameObject> getProjectiles() {
        return projectiles;
    }

    public List<GameObject> getBackgroundObjects() {
        return backgroundObjects;
    }

    public Map<ObjectType, GameObject> getForegroundObjectsMap() {
        return foregroundObjectsMap;
    }
}
