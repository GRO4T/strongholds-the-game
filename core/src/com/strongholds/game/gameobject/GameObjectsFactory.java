package com.strongholds.game.gameobject;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.strongholds.game.GameSingleton;
import com.strongholds.game.GameSingleton.ObjectType;

import static com.strongholds.game.GameSingleton.getGameSingleton;
import com.strongholds.game.GameSingleton.ObjectState;
import com.strongholds.game.exception.ObjectTypeNotDefinedException;

public class GameObjectsFactory {
    World world;
    GameSingleton gameSingleton;

    public GameObjectsFactory(World world) {
        this.world = world;
        gameSingleton = getGameSingleton();
    }

    public GameObject createObject(String id, ObjectType objectType, Vector2 position, Vector2 size, boolean isEnemy){
        float pixels_per_meter = gameSingleton.getPixels_per_meter();
        Vector2 bodySize = new Vector2(size.x / (2*pixels_per_meter), size.y / (2*pixels_per_meter));
        Vector2 bodyPos = new Vector2(position.x / pixels_per_meter + bodySize.x,
                position.y / pixels_per_meter + bodySize.y);

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(bodyPos.x, bodyPos.y);

        if (objectType == ObjectType.PLATFORM || objectType == ObjectType.BASE){
            bodyDef.type = BodyDef.BodyType.StaticBody;
            return new GameObject(bodyDef, bodySize.x, bodySize.y, objectType, id, isEnemy);
        }
        else if (objectType == ObjectType.SWORDSMAN){
            bodyDef.type = BodyDef.BodyType.DynamicBody;
            MeleeUnit meleeUnit = new MeleeUnit(bodyDef, bodySize.x, bodySize.y, objectType, id, isEnemy);
            meleeUnit.setState(ObjectState.IDLING);
            return meleeUnit;
        }
        throw new ObjectTypeNotDefinedException("ObjectType not handled by GameObjectsFactory: ObjectType = " +
                gameSingleton.toString(objectType));
    }
}
