package com.strongholds.game.gameobject.unit;

import com.badlogic.gdx.math.Vector2;
import com.strongholds.game.GameSingleton;
import com.strongholds.game.gameobject.AnimatedActor;
import com.strongholds.game.gameobject.GameObjectDef;
import com.strongholds.game.model.DeathListener;
import com.strongholds.game.model.Model;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Represents a unit
 */
public class Unit extends AnimatedActor implements IUnit {
    private DeathListener deathListener;
    protected Model model;

    protected boolean canAttack = true;

    private UnitDef unitDef;

    protected int numberOfContactingDamageable = 0;

    /**
     * timer used to schedule attack and suspend attack tasks
     */
    protected Timer scheduler;

    private int velocityAdjustmentInterval = 500;

    /**
     * used to randomize damage
     */
    protected Random randomGenerator;

    public Unit(GameObjectDef objectDef, UnitDef unitDef) {
        super(objectDef, unitDef.range);
        scheduler = new Timer(true);

        this.unitDef = new UnitDef(unitDef);
        maxHealth = this.unitDef.maxHealth;
        health = maxHealth;
        randomGenerator = new Random();

        AdjustVelocityTask adjustVelocityTask = new AdjustVelocityTask();
        scheduler.schedule(adjustVelocityTask, velocityAdjustmentInterval);
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
        System.out.println("I shouldn't be called. Unit/attack");
    }

    public void move(Vector2 direction) {
        System.out.println(getMaxVelocity());

        direction.nor();
        direction.scl(getAcceleration());
        getBody().applyForce(direction, getBody().getPosition(), true);
        float clampedVelocityX = GameSingleton.clamp(getBody().getLinearVelocity().x, -getMaxVelocity(), getMaxVelocity());
        getBody().setLinearVelocity(clampedVelocityX, 0);
    }

    @Override
    public void update() {
        GameSingleton.ObjectState state = getState();
        int tSize = getTargets().size();
        int fSize = getFriendlies().size();

        if (state != GameSingleton.ObjectState.ATTACKING) {
            if (tSize != 0 && canAttack){
                attack();
                getBody().setLinearVelocity(new Vector2(0, 0));
            }
            else{
                System.out.println(getId() + " damages = " + isContactingDamageable());
                if (tSize == 0 && fSize == 0){
                    if (isEnemy())
                        move(new Vector2(-1.0f, 0));
                    else
                        move(new Vector2(1.0f, 0));
                }
                else{
                    getBody().setLinearVelocity(new Vector2(0, 0));
                }

                if (Math.abs(getVelocity().x) < 0.5f){
                    setState(GameSingleton.ObjectState.IDLING);
                }
                else{
                    setState(GameSingleton.ObjectState.MOVING);
                }
            }
        }
    }


    protected class AdjustVelocityTask extends TimerTask{
        @Override
        public void run() {
            if (getFriendlies().size() == 0){
                setMaxVelocity(Float.MAX_VALUE);
            }
        }
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

    protected float getAcceleration(){
        return unitDef.acceleration;
    }

    public float getMaxVelocity(){
        return unitDef.currentMaxVelocity;
    }

    public void setMaxVelocity(float maxVelocity){
        if (maxVelocity < unitDef.nativeMaxVelocity){
            unitDef.currentMaxVelocity = maxVelocity;
        }
        else{
            unitDef.currentMaxVelocity = unitDef.nativeMaxVelocity;
        }
    }

    protected int getAttackSpeed(){
        return unitDef.attackSpeed;
    }

    protected int getTimeBetweenAttacks(){
        return unitDef.timeBetweenAttacks;
    }

    protected int getDamage(){
        return unitDef.damage;
    }

    public boolean isContactingDamageable() {
        if (numberOfContactingDamageable > 0)
            return true;
        return false;
    }

    public void setContactingDamageable(boolean contactingDamageable) {
        if (contactingDamageable == true)
            numberOfContactingDamageable++;
        else if (numberOfContactingDamageable > 0)
            numberOfContactingDamageable--;
    }
}
