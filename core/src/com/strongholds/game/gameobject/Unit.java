package com.strongholds.game.gameobject;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.strongholds.game.GameSingleton;

import java.util.Timer;
import java.util.TimerTask;

public class Unit extends AnimatedActor implements IReadOnlyUnit{
    float speed = 200.0f;
    boolean canAttack = true;
    int attackSpeed = 700;
    int timeBetweenAttacks = 1000;

    int damage = 10;
    int health = 100;
    int maxHealth = 100;

    Timer attackTimer;

    public Unit(World world, BodyDef bodyDef, float width, float height, GameSingleton.ObjectType type, String id) {
        super(world, bodyDef, width, height, type, id);
        attackTimer = new Timer(true);
    }

    public void move(Vector2 direction) {
        direction.nor();
        direction.scl(speed);
        body.applyForce(direction, body.getPosition(), true);
    }

    protected class SuspendAttackTask extends TimerTask {
        @Override
        public void run() {
            canAttack = true;
        }
    }

    @Override
    public void gotHit(int damage){
        health -= damage;
        if (health < 0)
            health = 0;
    }

    public int getHealth(){ return health; }
    public int getMaxHealth(){ return maxHealth; }
}
