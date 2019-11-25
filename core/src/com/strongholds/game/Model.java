package com.strongholds.game;

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
        BALL, PLATFORM, BACKGROUND_IMAGE, BASE;
    }

    private World world;
    private int velocityIterations;
    private int positionIterations;

    List<GameObject> projectiles;
    List<GameObject> backgroundObjects;
    Map<ObjectType, GameObject> foregroundObjectsMap;

    public Model(){}
    public Model(int velocityIterations, int positionIterations){
        projectiles = new LinkedList<>();
        backgroundObjects = new ArrayList<>();
        foregroundObjectsMap = new HashMap<>();

        this.velocityIterations = velocityIterations;
        this.positionIterations = positionIterations;
        world = new World(new Vector2(0, -15), true);
    }
    public void update(float timeStep){
        world.step(timeStep, velocityIterations, positionIterations);
    }

    public void dispose(){
        //TODO
    }

    // this may be rewritten to some Factory
    public void createObject(ObjectType objectType, Vector2 position, Vector2 size){
        float pixels_per_meter = GameSingleton.getGameSingleton().getPixels_per_meter();
        Vector2 bodySize = new Vector2(size.x / (2*pixels_per_meter), size.y / (2*pixels_per_meter));
        Vector2 bodyPos = new Vector2(position.x / pixels_per_meter + bodySize.x,
                position.y / pixels_per_meter + bodySize.y);

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(bodyPos.x, bodyPos.y);

        if (objectType == ObjectType.PLATFORM){
            bodyDef.type = BodyDef.BodyType.StaticBody;
            foregroundObjectsMap.put(objectType, new GameObject(world, bodyDef,
                    bodySize.x, bodySize.y, objectType));
        }
        else if (objectType == ObjectType.BASE){
            bodyDef.type = BodyDef.BodyType.StaticBody;
            foregroundObjectsMap.put(objectType, new GameObject(world, bodyDef,
                    bodySize.x, bodySize.y, objectType));
        }
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
