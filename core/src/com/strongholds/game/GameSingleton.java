package com.strongholds.game;

import com.badlogic.gdx.physics.box2d.World;

import java.util.HashMap;

public class GameSingleton {
    private static volatile GameSingleton INSTANCE;

    private final float pixels_per_meter = 16.0f;
    private String textureFilenames[] = {
            "platform.png", "base.png", "background-textures.png",
            "Knight/idle.png", "Knight/move.png", "Knight/attack.png"};
    private HashMap<ObjectType, TextureInfo[]> actorsTextureInfo;

    public final String menuBackgroundTexture = "background-textures.png";

    private HashMap<ObjectType, Long> costLedger;

    private World world;
    public enum ObjectType{
        PLATFORM, BACKGROUND_IMAGE, BASE, SWORDSMAN;
    }

    public enum ObjectState{
        IDLING, MOVING, ATTACKING;
    }

    public class TextureInfo{
        public String filename;
        public int cols; // cols of the spriteSheet
        public int rows; // rows of the spriteSheet
        public int filledFrames; // non empty frames of the Spritesheet
        public float interval;

        public TextureInfo(String filename, int cols, int rows, int filledFrames, float interval) {
            this.filename = filename;
            this.rows = rows;
            this.cols = cols;
            this.filledFrames = filledFrames;
            this.interval = interval;
        }
    }

    public static final short GAME_OBJECT_COLLISION_MASK = 0x0001;
    public static final short ACTOR_COLLISION_MASK = 0x0002;
    public static final short SENSOR_COLLISION_MASK = 0x0004;
    public static final short BASE_COLLISION_MASK = 0x0008;
    public static final short ENEMY_BASE_COLLISION_MASK = 0x0010;
    public static final short ENEMY_ACTOR_COLLISION_MASK = 0x0020;

    public float getPixels_per_meter(){ return pixels_per_meter; }
    public String[] getTextureFilenames(){ return textureFilenames; }

    private GameSingleton(){
        actorsTextureInfo = new HashMap<>();
        TextureInfo textureInfo[] = {
                new TextureInfo("Knight/idle.png", 4, 1, 4, 0.1f),
                new TextureInfo("Knight/move.png", 8, 1, 8, 0.1f),
                new TextureInfo("Knight/attack.png", 10, 1, 10, 0.1f),
            };
        actorsTextureInfo.put(ObjectType.SWORDSMAN, textureInfo);

        costLedger = new HashMap<>();
        costLedger.put(ObjectType.SWORDSMAN, 100L);
    }

    public static GameSingleton getGameSingleton(){
        if (INSTANCE == null){
            INSTANCE = new GameSingleton();
        }
        return INSTANCE;
    }

    public TextureInfo[] getActorTextureInfo(ObjectType objectType){
        return actorsTextureInfo.get(objectType);
    }

    public static String toString(ObjectType objectType){
        if (objectType == ObjectType.SWORDSMAN)
            return "ObjectType.SWORDSMAN";
        return "toString not defined for this objectType";
    }

    public long getCost(ObjectType objectType){
        return costLedger.get(objectType).longValue();
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }
}
