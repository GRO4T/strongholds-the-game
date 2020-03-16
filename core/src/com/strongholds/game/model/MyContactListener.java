package com.strongholds.game.model;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.strongholds.game.GameSingleton;
import com.strongholds.game.gameobject.AnimatedActor;
import com.strongholds.game.gameobject.GameObject;
import com.strongholds.game.gameobject.unit.Unit;

/**
 * Custom contact listener
 */
public class MyContactListener implements ContactListener {
    /**
     * Called when two fixtures begin to touch
     * @param contact object containing information about contact
     */
    @Override
    public void beginContact(Contact contact) {
        GameObject objectA = (GameObject) (contact.getFixtureA().getUserData());
        GameObject objectB = (GameObject) (contact.getFixtureB().getUserData());

        //objectA.setCollision(true);
        //objectB.setCollision(true);

        if (contact.getFixtureA().isSensor()){
            AnimatedActor actorA = (AnimatedActor) objectA;
            if (actorA.isEnemy() != objectB.isEnemy()){
                actorA.addTarget(objectB);
            }
            else if (actorA instanceof Unit && objectB instanceof Unit){
                if (actorA.isEnemy()){
                    if (actorA.getPosition().x < objectB.getPosition().x){
                        ((Unit) objectB).setMaxVelocity(((Unit) actorA).getMaxVelocity());
                    }
                    else{
                        ((Unit) actorA).setMaxVelocity(((Unit) objectB).getMaxVelocity());
                    }
                }
                else{
                    if (actorA.getPosition().x > objectB.getPosition().x){
                        ((Unit) objectB).setMaxVelocity(((Unit) actorA).getMaxVelocity());
                    }
                    else{
                        ((Unit) actorA).setMaxVelocity(((Unit) objectB).getMaxVelocity());
                    }
                }
            }
        }
        if (contact.getFixtureB().isSensor()){
            AnimatedActor actorB = (AnimatedActor) objectB;
            if (actorB.isEnemy() != objectA.isEnemy()){
                actorB.addTarget(objectA);
            }
            else if (actorB instanceof Unit && objectA instanceof Unit){
                if (actorB.isEnemy()){
                    if (actorB.getPosition().x < objectA.getPosition().x){
                        ((Unit) objectA).setMaxVelocity(((Unit) actorB).getMaxVelocity());
                    }
                    else{
                        ((Unit) actorB).setMaxVelocity(((Unit) objectA).getMaxVelocity());
                    }
                }
                else{
                    if (actorB.getPosition().x > objectA.getPosition().x){
                        ((Unit) objectA).setMaxVelocity(((Unit) actorB).getMaxVelocity());
                    }
                    else{
                        ((Unit) actorB).setMaxVelocity(((Unit) objectA).getMaxVelocity());
                    }
                }
            }
        }
        // not sensors, instances of Unit and same side
        if (!contact.getFixtureA().isSensor() && !contact.getFixtureB().isSensor() &&
                objectA instanceof Unit && objectB instanceof Unit &&
                objectA.isEnemy() == objectB.isEnemy()){
            if (objectA.isEnemy()){
                if (objectA.getPosition().x < objectB.getPosition().x)
                    ((Unit) objectB).addFriendly(objectA);
                else
                    ((Unit) objectA).addFriendly(objectB);
            }
            else{
                if (objectA.getPosition().x > objectB.getPosition().x)
                    ((Unit) objectB).addFriendly(objectA);
                else
                    ((Unit) objectA).addFriendly(objectB);
            }
        }
    }

    /**
     * Called when two fixtures cease to touch
     * @param contact object containing information about contact
     */
    @Override
    public void endContact(Contact contact) {

        GameObject objectA = (GameObject) (contact.getFixtureA().getUserData());
        GameObject objectB = (GameObject) (contact.getFixtureB().getUserData());

        //objectA.setCollision(false);
        //objectB.setCollision(false);

        if (contact.getFixtureA().isSensor()){
            AnimatedActor actorA = (AnimatedActor) objectA;
            if (actorA.isEnemy() != objectB.isEnemy()){
                actorA.removeTarget(objectB);
            }
            if (objectB instanceof Unit ||
                    (objectB.isEnemy() && objectB.getType() == GameSingleton.ObjectType.BASE)){
                Unit unitB = (Unit) objectB;
                unitB.setContactingDamageable(false);
            }
        }
        if (contact.getFixtureB().isSensor()){
            AnimatedActor actorB = (AnimatedActor) objectB;
            if (actorB.isEnemy() != objectA.isEnemy()){
                actorB.removeTarget(objectA);
            }
            if (objectA instanceof Unit){
                Unit unitA = (Unit) objectB;
                unitA.setContactingDamageable(false);
            }
        }

        if (!contact.getFixtureA().isSensor() && !contact.getFixtureB().isSensor() &&
                objectA instanceof Unit && objectB instanceof Unit &&
                objectA.isEnemy() == objectB.isEnemy()){
            // both are enemies
            if (objectA.isEnemy()){
                if (objectA.getPosition().x < objectB.getPosition().x)
                    ((Unit) objectB).removeFriendly(objectA);
                else
                    ((Unit) objectA).removeFriendly(objectB);
            }
            else{
                if (objectA.getPosition().x > objectB.getPosition().x)
                    ((Unit) objectB).removeFriendly(objectA);
                else
                    ((Unit) objectA).removeFriendly(objectB);
            }
        }
    }

    /**
     * not used
     * @param contact
     * @param oldManifold
     */
    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    /**
     * not used
     * @param contact
     * @param impulse
     */
    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
