package com.strongholds.game;

import java.util.HashMap;
import java.util.Map;

public class GameSingleton {
    private static volatile GameSingleton INSTANCE;

    private final float pixels_per_meter = 16.0f;
    private String textureFilenames[] = {
            "platform.png", "base.png", "background-textures.png", "troop.png",
            "swordsman_idling.png", "swordsman_attacking.png"};

    public enum ObjectType{
        PLATFORM, BACKGROUND_IMAGE, BASE, SWORDSMAN;
    }
    public enum ObjectState{
        MOVING, IDLING, ATTACKING;
    }

    public static final short GAME_OBJECT_COLLISION_MASK = 0x0001;
    public static final short ACTOR_COLLISION_MASK = 0x0002;
    public static final short SENSOR_COLLISION_MASK = 0x0004;

    public float getPixels_per_meter(){ return pixels_per_meter; }
    public String[] getTextureFilenames(){ return textureFilenames; }

    private GameSingleton(){
    }

    public static GameSingleton getGameSingleton(){
        if (INSTANCE == null){
            INSTANCE = new GameSingleton();
        }
        return INSTANCE;
    }
}
