package com.strongholds.game.model;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.strongholds.game.model.gameobject.AnimatedActor;
import com.strongholds.game.model.gameobject.GameObject;

public class MyContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        if (contact.getFixtureA().isSensor()){
            AnimatedActor actorA = (AnimatedActor)(contact.getFixtureA().getUserData());
            AnimatedActor actorB = (AnimatedActor)(contact.getFixtureB().getUserData());
            actorA.addTarget(actorB);
            System.out.println(actorA.getTargets());
            return;
        }
        if (contact.getFixtureB().isSensor()){
            AnimatedActor actorA = (AnimatedActor)(contact.getFixtureA().getUserData());
            AnimatedActor actorB = (AnimatedActor)(contact.getFixtureB().getUserData());
            actorB.addTarget(actorA);
            System.out.println(actorB.getTargets());
            return;
        }
        GameObject objectA = (GameObject) (contact.getFixtureA().getUserData());
        GameObject objectB = (GameObject) (contact.getFixtureB().getUserData());
        objectB.gotHit(10);
    }

    @Override
    public void endContact(Contact contact) {
        if (contact.getFixtureA().isSensor()){
            System.out.println("ended contact with sensor");
            return;
        }

        if (contact.getFixtureB().isSensor()){
            System.out.println("ended contact with sensor");
            return;
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
