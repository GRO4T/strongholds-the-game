package com.strongholds.game.model.gameobject;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.strongholds.game.GameSingleton;

public class Unit extends AnimatedActor{
    float speed = 200.0f;
    boolean canAttack = true;
    int attackSpeed = 700;
    int timeBetweenAttacks = 1000;

    public Unit(World world, BodyDef bodyDef, float width, float height, GameSingleton.ObjectType type, String id) {
        super(world, bodyDef, width, height, type, id);
    }

    public void move(Vector2 direction) {
        direction.nor();
        direction.scl(speed);
        //body.applyLinearImpulse(direction, body.getPosition(), false);
        body.applyForce(direction, body.getPosition(), true);
    }
}
