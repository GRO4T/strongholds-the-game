package com.strongholds.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

import com.strongholds.game.GameSingleton;
import com.strongholds.game.GameSingleton.ObjectType;
import com.strongholds.game.GameSingleton.ObjectState;
import com.strongholds.game.Model;
import com.strongholds.game.controller.StrongholdsGame;
import com.strongholds.game.gameobject.AnimatedActor;
import com.strongholds.game.gameobject.GameObject;

public class View implements PropertyChangeListener
{
    private Model model;
    private StrongholdsGame controller;

    private float pixels_per_meter;

    private OrthographicCamera cam;
    private final int cameraSpeed = 10;
    private final float cameraZoom = 1.0f;

    private SpriteBatch spriteBatch;
    Map<ObjectType, Texture> staticObjectsTextureMap;
    Map<ObjectType, Map<ObjectState, AnimationClip>> actorsTextureMap;

    public View(Model model, StrongholdsGame controller)
    {
        pixels_per_meter = GameSingleton.getGameSingleton().getPixels_per_meter();

        staticObjectsTextureMap = new HashMap<>();
        actorsTextureMap = new HashMap<>();

        this.model = model;
        this.controller = controller;
        spriteBatch = new SpriteBatch();

        cam = new OrthographicCamera(controller.getScreenWidth(), controller.getScreenHeight());
        cam.position.set(controller.getScreenWidth() / 2, controller.getScreenHeight() / 2, 0);
        cam.zoom = cameraZoom;
    }

    public void update()
    {
        //returns true (How to check class of the object)
        //System.out.println(this.getClass().equals(com.strongholds.game.view.View.class));

        handleInput();
        cam.update();
        spriteBatch.setProjectionMatrix(cam.combined);
    }

    public void draw(float deltaTime)
    {
        spriteBatch.begin();
        Texture backgroundTexture = staticObjectsTextureMap.get(ObjectType.BACKGROUND_IMAGE);
        spriteBatch.draw(backgroundTexture, 0, 0);
        spriteBatch.draw(backgroundTexture, -1200, 0);
        spriteBatch.draw(backgroundTexture, 1200, 0);

        // draw non-animated objects
        for (Object gameObject : model.getGameObjects())
        {
            drawGameObject((GameObject)gameObject);
        }
        //draw actors
        for (Object actor : model.getActors()){
            drawGameObject((AnimatedActor)actor, deltaTime);
        }
        spriteBatch.end();
    }

    private void drawGameObject(GameObject gameObject){
        Texture texture = staticObjectsTextureMap.get(gameObject.getType());
        float x = (gameObject.getPosition().x - gameObject.getWidth()) * pixels_per_meter;
        float y = (gameObject.getPosition().y - gameObject.getHeight()) * pixels_per_meter;
        spriteBatch.draw(texture, x, y);
    }

    private void drawGameObject(AnimatedActor gameObject, float deltaTime){
        ObjectType objectType = gameObject.getType();
        ObjectState objectState = gameObject.getState();
        AnimationClip clip = actorsTextureMap.get(objectType).get(objectState);
        TextureRegion textureRegion = clip.getCurrentFrame();
        clip.update(deltaTime);
        spriteBatch.draw(textureRegion, (gameObject.getPosition().x - gameObject.getWidth()) * pixels_per_meter,
                (gameObject.getPosition().y - gameObject.getHeight()) * pixels_per_meter);
    }

    public void dispose(){

    }

    private Texture getTexture(String filename){
        return (Texture)controller.getAssetManager().get(filename);
    }

    public void loadTextures(){
        staticObjectsTextureMap.put(ObjectType.BACKGROUND_IMAGE, getTexture("background-textures.png"));
        staticObjectsTextureMap.put(ObjectType.PLATFORM, getTexture("platform.png"));
        staticObjectsTextureMap.put(ObjectType.BASE, getTexture("base.png"));

        actorsTextureMap.put(ObjectType.SWORDSMAN, new HashMap<ObjectState, AnimationClip>());
        AnimationClip clip = new AnimationClip(getTexture("swordsman_idling.png"), 7, 1, 7, 0.1f);
        actorsTextureMap.get(ObjectType.SWORDSMAN).put(ObjectState.IDLING, clip);

        AnimationClip clip2 = new AnimationClip(getTexture("swordsman_attacking.png"), 9, 1, 9, 0.1f);
        actorsTextureMap.get(ObjectType.SWORDSMAN).put(ObjectState.ATTACKING, clip2);
    }

    public Vector2 getTextureSize(ObjectType objectType){
        Texture texture = staticObjectsTextureMap.get(objectType);
        return new Vector2(texture.getWidth(), texture.getHeight());
    }

    public Vector2 getTextureSize(ObjectType objectType, ObjectState objectState){
        TextureRegion texture = actorsTextureMap.get(objectType).get(objectState).getCurrentFrame();
        return new Vector2(texture.getRegionWidth(), texture.getRegionHeight());
    }

    private void handleInput(){
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            cam.translate(-cameraSpeed, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            cam.translate(cameraSpeed, 0, 0);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {

    }
}

/* TODO
    write AnimatedTexture class for animating objects
    it would go sth like that view asks model for current state of the object and then decides what to do
 */