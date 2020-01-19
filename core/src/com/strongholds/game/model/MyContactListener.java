package com.strongholds.game.model;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.strongholds.game.gameobject.AnimatedActor;
import com.strongholds.game.gameobject.GameObject;

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
        if (contact.getFixtureA().isSensor()){
            AnimatedActor actor = (AnimatedActor) (contact.getFixtureA().getUserData());
            GameObject object = (GameObject) (contact.getFixtureB().getUserData());
            if (actor.isEnemy() != object.isEnemy()){
                actor.addTarget(object);
            }
            return;
        }
        if (contact.getFixtureB().isSensor()){
            AnimatedActor actor = (AnimatedActor)(contact.getFixtureB().getUserData());
            GameObject object = (GameObject) (contact.getFixtureA().getUserData());
            if (actor.isEnemy() != object.isEnemy()){
                actor.addTarget(object);
            }
            return;
        }
    }

    /**
     * Called when two fixtures cease to touch
     * @param contact object containing information about contact
     */
    @Override
    public void endContact(Contact contact) {
        if (contact.getFixtureA().isSensor()){
            AnimatedActor actor = (AnimatedActor)(contact.getFixtureA().getUserData());
            GameObject object = (GameObject) (contact.getFixtureB().getUserData());
            if (actor.isEnemy() != object.isEnemy()){
                actor.removeTarget(object);
            }
            return;
        }

        if (contact.getFixtureB().isSensor()){
            AnimatedActor actor = (AnimatedActor)(contact.getFixtureB().getUserData());
            GameObject object = (GameObject) (contact.getFixtureA().getUserData());
            if (actor.isEnemy() != object.isEnemy()){
                actor.removeTarget(object);
            }
            return;
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
