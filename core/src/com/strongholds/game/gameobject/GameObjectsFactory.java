package com.strongholds.game.gameobject;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.strongholds.game.GameSingleton;
import com.strongholds.game.GameSingleton.ObjectType;

import static com.strongholds.game.GameSingleton.getGameSingleton;
import com.strongholds.game.GameSingleton.ObjectState;

public class GameObjectsFactory {
    World world;

    public GameObjectsFactory(World world) {
        this.world = world;
    }

    public GameObject createObject(ObjectType objectType, Vector2 position, Vector2 size){
        float pixels_per_meter = getGameSingleton().getPixels_per_meter();
        Vector2 bodySize = new Vector2(size.x / (2*pixels_per_meter), size.y / (2*pixels_per_meter));
        Vector2 bodyPos = new Vector2(position.x / pixels_per_meter + bodySize.x,
                position.y / pixels_per_meter + bodySize.y);

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(bodyPos.x, bodyPos.y);

        if (objectType == ObjectType.PLATFORM || objectType == ObjectType.BASE){
            bodyDef.type = BodyDef.BodyType.StaticBody;
            return new GameObject(world, bodyDef, bodySize.x, bodySize.y, objectType);
        }
        else if (objectType == ObjectType.SWORDSMAN){
            bodyDef.type = BodyDef.BodyType.DynamicBody;
            AnimatedActor actor = new AnimatedActor(world, bodyDef, bodySize.x, bodySize.y, objectType);
            actor.setState(ObjectState.IDLING);
            return actor;
        }
        throw new ObjectTypeNotDefinedException("ObjectType not handled by GameObjectsFactory: ObjectType = " + objectType);
    }
}
