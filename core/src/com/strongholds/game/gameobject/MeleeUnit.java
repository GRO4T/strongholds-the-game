package com.strongholds.game.gameobject;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.strongholds.game.GameSingleton;
import com.strongholds.game.model.Model;

import java.util.Iterator;
import java.util.Random;
import java.util.TimerTask;

/**
 * Represents a melee unit
 */
public class MeleeUnit extends Unit implements IUnit{
    //float range = 5.0f;
    /**
     * random number generator
     */
    Random randomGenerator;

    /**
     * Creates a melee unit
     * @param bodyDef box2d body definition
     * @param width unit width (in meters)
     * @param height unit height (in meters)
     * @param type unit type
     * @param id id
     * @param isEnemy whether unit is an enemy
     */
    public MeleeUnit(BodyDef bodyDef, float width, float height, GameSingleton.ObjectType type, String id, boolean isEnemy) {
        super(bodyDef, width, height, type, id, isEnemy);
        randomGenerator = new Random();
    }


    public void update(){
        if (getState() != GameSingleton.ObjectState.ATTACKING){
            if (getTargets().size() != 0 && canAttack){
                attack();
            }
            else{
                if (Math.abs(getVelocity().x) < 0.5f){
                    setState(GameSingleton.ObjectState.IDLING);
                }
                else{
                    setState(GameSingleton.ObjectState.MOVING);
                }
            }
        }

        if (isEnemy())
            move(new Vector2(-1.0f, 0));
        else
            move(new Vector2(1.0f, 0));
    }

    public void attack(){
        setState(GameSingleton.ObjectState.ATTACKING);
        canAttack = false;
        AttackTask attackTask = new AttackTask();
        SuspendAttackTask suspendAttackTask = new SuspendAttackTask();
        attackTimer.schedule(attackTask, attackSpeed);
        attackTimer.schedule(suspendAttackTask, timeBetweenAttacks);
    }

    /**
     * Task called when unit is about to attack.
     * After attackSpeed milliseconds it checks target list
     * and if there are any enemies or enemy bases in it,
     * deals damage to them.
     */
    private class AttackTask extends TimerTask{
        @Override
        public void run() {
            boolean containsUnit = false;
            Iterator it = getTargets().iterator();
            while (it.hasNext()){
                GameObject unit = (GameObject) it.next();
                //check if there was a unit
                if (!containsUnit && unit instanceof Unit)
                    containsUnit = true;

                //attack only if it is an enemy unit or if it is an enemy base but there was no units before
                if (unit.isEnemy() && !(unit.getId().equals("enemyBase") && containsUnit)){
                    int sign = randomGenerator.nextBoolean() == true ? 1 : -1;
                    int finalDamage = damage + sign * randomGenerator.nextInt(4);
                    model.enemyUnitHit(unit.getId(), finalDamage);
                    unit.gotHit(finalDamage);
                    System.out.println("unit id = " + unit.getId() + " got hit for " + finalDamage);

                }
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
