package com.strongholds.game.model.gameobject;

public class ObjectTypeNotDefinedException extends RuntimeException{
    public ObjectTypeNotDefinedException(String message){
        super(message);
    }
}
