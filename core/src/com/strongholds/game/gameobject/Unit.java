package com.strongholds.game.gameobject;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.strongholds.game.GameSingleton;
import com.strongholds.game.model.DeathListener;
import com.strongholds.game.model.Model;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Represents a unit
 */
public class Unit extends AnimatedActor implements IUnit{
    /**
     * reference to the object implementing DeathListener interface
     */
    private DeathListener deathListener;
    protected Model model;

    /**
     * unit speed
     */
    protected float speed = 2000.0f; // prev 200
    /**
     * flag telling whether unit can perform an attack
     */
    protected boolean canAttack = true;
    /**
     * unit attack speed (in milliseconds)
     */
    protected int attackSpeed = 700;
    /**
     * time after successful attack when unit cannot start another one (in milliseconds)
     */
    protected int timeBetweenAttacks = 1000;

    /**
     * unit damage
     */
    protected int damage = 10;

    /**
     * timer used to schedule attack and suspend attack tasks
     */
    protected Timer attackTimer;

    /**
     * Creates a unit
     * @param bodyDef box2d body definition
     * @param width unit width (in meters)
     * @param height unit height (in meters)
     * @param type unit type
     * @param id id
     * @param isEnemy whether unit is an enemy
     */
    public Unit(BodyDef bodyDef, float width, float height, GameSingleton.ObjectType type, String id, boolean isEnemy) {
        super(bodyDef, width, height, type, id, isEnemy);
        attackTimer = new Timer(true);
        maxHealth = 100;
        health = maxHealth;
    }

    /**
     * Sets unit model reference
     * @param model reference to model
     */
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

    /**
     * Task used to schedule attack suspension
     */
    protected class SuspendAttackTask extends TimerTask {
        @Override
        public void run() {
            canAttack = true;
        }
    }

    /**
     * Called when unit has been hit.
     * Notifies death listener when (health<=0)
     * @param damage damage dealt to the unit
     */
    public void gotHit(int damage){
        health -= damage;
        if (health <= 0){
            health = 0;
            deathListener.notifyDeadUnit(getId());
        }
    }
}
