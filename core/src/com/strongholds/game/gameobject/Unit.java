package com.strongholds.game.gameobject;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.strongholds.game.GameSingleton;
import com.strongholds.game.model.DeathListener;
import com.strongholds.game.model.Model;

import java.util.Timer;
import java.util.TimerTask;

public class Unit extends AnimatedActor implements IUnit{
    DeathListener deathListener;
    protected Model model;

    float speed = 500.0f; // prev 200
    boolean canAttack = true;
    int attackSpeed = 700;
    int timeBetweenAttacks = 1000;

    int damage = 10;
    int health = 100;
    int maxHealth = 100;

    Timer attackTimer;

    public Unit(BodyDef bodyDef, float width, float height, GameSingleton.ObjectType type, String id, boolean isEnemy) {
        super(bodyDef, width, height, type, id, isEnemy);
        attackTimer = new Timer(true);
    }

    public void setModel(Model model){
        this.model = model;
    }

    public void dispose(){
        super.dispose();
    }

    public void setDeathListener(DeathListener deathListener) {
        this.deathListener = deathListener;
    }

    public void attack() {

    }

    public void move(Vector2 direction) {
        direction.nor();
        direction.scl(speed);
        body.applyForce(direction, body.getPosition(), true);
    }

    @Override
    public void update() {

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
        if (health <= 0){
            health = 0;
            deathListener.notifyDeadUnit(getId());
        }
    }

    public int getHealth(){ return health; }
    public int getMaxHealth(){ return maxHealth; }
}
