package com.strongholds.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Model extends java.util.Observable{
    /* Represents scale between the model and
        it's visual representation on the screen.

        position_on_the_screen = pos_in_box2d_world * scale;
     */
    private final float Scale = 10.0f;

    private World world;
    private int velocityIterations;
    private int positionIterations;

    //test
    public Body ball;
    public Body platform;

    public Model(){}
    public Model(int velocityIterations, int positionIterations){
        this.velocityIterations = velocityIterations;
        this.positionIterations = positionIterations;
        world = new World(new Vector2(0, -10), true);

        //how to create a object in jbox2d
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(200, 600);
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(128, 128);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.3f;
        fixtureDef.restitution = 0.5f;

        ball = world.createBody(bodyDef);
        ball.createFixture(fixtureDef);

        bodyDef.position.set(300, 0);
        bodyDef.type = BodyDef.BodyType.StaticBody;
        polygonShape.setAsBox(256, 32);

        platform = world.createBody(bodyDef);
        platform.createFixture(fixtureDef);
    }
    public void update(float timeStep){
        world.step(timeStep, velocityIterations, positionIterations);
        System.out.println(ball.getLinearVelocity().y);
    }

    public void dispose(){

    }

}
