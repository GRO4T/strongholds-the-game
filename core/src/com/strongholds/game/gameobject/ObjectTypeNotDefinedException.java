package com.strongholds.game.gameobject;

public class ObjectTypeNotDefinedException extends RuntimeException{
    public ObjectTypeNotDefinedException(String message){
        super(message);
    }
}
