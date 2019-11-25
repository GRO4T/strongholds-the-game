package com.strongholds.game;

public class GameSingleton {
    private static volatile GameSingleton INSTANCE;

    private final float pixels_per_meter = 16.0f;
    private String textureFilenames[] = {"platform.png", "base.png", "background-textures.png", "troop.png"};

    public enum ObjectType{
        PLATFORM, BACKGROUND_IMAGE, BASE, SWORDSMAN;
    }
    public enum ObjectState{
        MOVING, IDLING, ATTACKING;
    }

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
