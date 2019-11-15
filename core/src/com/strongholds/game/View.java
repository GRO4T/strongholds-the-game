package com.strongholds.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class View implements Observer{
    private Model model;
    private StrongholdsGame controller;

    private final float pixels_per_meter = 16.0f;

    private OrthographicCamera cam;
    private final int cameraSpeed = 10;

    private SpriteBatch spriteBatch;
    Map<Model.ObjectType, Texture> textureMap;

    public View(Model model, StrongholdsGame controller) {
        textureMap = new HashMap<>();

        this.model = model;
        this.controller = controller;
        spriteBatch = new SpriteBatch();

        cam = new OrthographicCamera(controller.getScreenWidth(), controller.getScreenHeight());
        cam.position.set(600, 300, 0);
        cam.zoom = 1.0f;
    }

    @Override
    public void update(Observable observable, Object o) {

    }

    public void draw(){
        handleInput();
        cam.update();
        spriteBatch.setProjectionMatrix(cam.combined);

        spriteBatch.begin();
            Texture backgroundTexture = textureMap.get(Model.ObjectType.BACKGROUND_IMAGE);
            spriteBatch.draw(backgroundTexture, 0, 0);
            spriteBatch.draw(backgroundTexture, -1200, 0);
            spriteBatch.draw(backgroundTexture, 1200, 0);

            drawGameObject(model.ball, textureMap.get(Model.ObjectType.BALL));
            drawGameObject(model.platform, textureMap.get(Model.ObjectType.PLATFORM));
        spriteBatch.end();
    }

    private void drawGameObject(Model.GameObject gameObject, Texture texture){
        spriteBatch.draw(texture, (gameObject.getPosition().x - gameObject.getWidth()) * pixels_per_meter,
                (gameObject.getPosition().y - gameObject.getHeight()) * pixels_per_meter);
    }

    public void dispose(){

    }

    public void setTextures(){
        textureMap.put(Model.ObjectType.BACKGROUND_IMAGE, (Texture)controller.getAssetManager().get("background-textures.png"));
        textureMap.put(Model.ObjectType.BALL, (Texture)controller.getAssetManager().get("badlogic.jpg"));
        textureMap.put(Model.ObjectType.PLATFORM, (Texture)controller.getAssetManager().get("platform.jpg"));
    }

    public void handleInput(){
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            cam.translate(-cameraSpeed, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            cam.translate(cameraSpeed, 0, 0);
        }
    }
}

/* TODO
    write AnimatedTexture class for animating objects
    it would go sth like that view asks model for current state of the object and then decides what to do
 */