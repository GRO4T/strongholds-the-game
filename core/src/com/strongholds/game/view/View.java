package com.strongholds.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

import com.strongholds.game.GameSingleton;
import com.strongholds.game.GameSingleton.ObjectType;
import com.strongholds.game.Model;
import com.strongholds.game.StrongholdsGame;
import com.strongholds.game.gameobject.GameObject;

public class View implements PropertyChangeListener {
    private Model model;
    private StrongholdsGame controller;

    private float pixels_per_meter;

    private OrthographicCamera cam;
    private final int cameraSpeed = 10;
    private final float cameraZoom = 1.0f;

    private SpriteBatch spriteBatch;
    Map<ObjectType, Texture> staticObjectsTextureMap;
    //Map<I, Map<ObjectState, AnimationClip>> animatedObjectsMap;

    public View(Model model, StrongholdsGame controller) {
        pixels_per_meter = GameSingleton.getGameSingleton().getPixels_per_meter();

        staticObjectsTextureMap = new HashMap<>();
        //animatedObjectsMap =

        this.model = model;
        this.controller = controller;
        spriteBatch = new SpriteBatch();

        cam = new OrthographicCamera(controller.getScreenWidth(), controller.getScreenHeight());
        cam.position.set(controller.getScreenWidth() / 2, controller.getScreenHeight() / 2, 0);
        cam.zoom = cameraZoom;
    }

    public void update(){
        //returns true (How to check class of the object)
        //System.out.println(this.getClass().equals(com.strongholds.game.view.View.class));

        handleInput();
        cam.update();
        spriteBatch.setProjectionMatrix(cam.combined);
    }

    public void draw(){
        spriteBatch.begin();
        Texture backgroundTexture = staticObjectsTextureMap.get(ObjectType.BACKGROUND_IMAGE);
        spriteBatch.draw(backgroundTexture, 0, 0);
        spriteBatch.draw(backgroundTexture, -1200, 0);
        spriteBatch.draw(backgroundTexture, 1200, 0);

        //drawGameObject(model.ball, textureMap.get(Model.ObjectType.BALL));
        for (Map.Entry<ObjectType, GameObject> entry :
                model.getForegroundObjectsMap().entrySet())
        {
            drawGameObject(entry.getValue(), staticObjectsTextureMap.get(entry.getKey()));
        }
        spriteBatch.end();
    }

    private void drawGameObject(GameObject gameObject, Texture texture){
        spriteBatch.draw(texture, (gameObject.getPosition().x - gameObject.getWidth()) * pixels_per_meter,
                (gameObject.getPosition().y - gameObject.getHeight()) * pixels_per_meter);
    }

    public void dispose(){

    }

    public void setTextures(){
        staticObjectsTextureMap.put(ObjectType.BACKGROUND_IMAGE, (Texture)controller.getAssetManager().get("background-textures.png"));
        staticObjectsTextureMap.put(ObjectType.PLATFORM, (Texture)controller.getAssetManager().get("platform.png"));
        staticObjectsTextureMap.put(ObjectType.BASE, (Texture)controller.getAssetManager().get("base.png"));
        staticObjectsTextureMap.put(ObjectType.SWORDSMAN, (Texture)controller.getAssetManager().get("troop.png"));
    }

    public Vector2 getTextureSize(ObjectType objectType){
        Texture texture = staticObjectsTextureMap.get(objectType);
        return new Vector2(texture.getWidth(), texture.getHeight());
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