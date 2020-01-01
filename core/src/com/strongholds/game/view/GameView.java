package com.strongholds.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.strongholds.game.GameSingleton;
import com.strongholds.game.GameSingleton.ObjectType;
import com.strongholds.game.GameSingleton.ObjectState;
import com.strongholds.game.controller.IViewController;
import com.strongholds.game.controller.ViewEvent;
import com.strongholds.game.model.IReadOnlyModel;
import com.strongholds.game.gameobject.IReadOnlyAnimatedActor;
import com.strongholds.game.gameobject.IReadOnlyGameObject;

public class GameView extends AbstractView implements IGameView
{
    private IReadOnlyModel model;
    private IViewController controller;

    private float pixels_per_meter;

    private OrthographicCamera cam;
    //private final int cameraSpeed = 10;
    private final float cameraZoom = 1.0f;

    private Map<ObjectType, Sprite> staticObjectsTextureMap;
    private Map<String, Animator> actorsMap;

    public GameView(IReadOnlyModel model, final IViewController controller)
    {
        this.model = model;
        this.controller = controller;

        pixels_per_meter = GameSingleton.getGameSingleton().getPixels_per_meter();
        screenX = controller.getScreenWidth();
        screenY = controller.getScreenHeight();

        staticObjectsTextureMap = new HashMap<>();
        actorsMap = new HashMap<>();
        spriteBatch = new SpriteBatch();
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        cam = new OrthographicCamera(controller.getScreenWidth(), controller.getScreenHeight());
        cam.position.set(screenX / 2, screenY / 2, 0);
        cam.zoom = cameraZoom;

        //create button
        font = new BitmapFont();
        skin = new Skin();
        buttonAtlas = new TextureAtlas(Gdx.files.internal("buttons/buttons.pack"));
        skin.addRegions(buttonAtlas);
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.up = skin.getDrawable("button_on");
        textButtonStyle.down = skin.getDrawable("button_off");
        textButtonStyle.checked = skin.getDrawable("button_on");

        TextButton addSwordsmanButton = null;
        TextButton addArcherButton = null;

        createButton(-80, screenY - 200, "add Swordsman", addSwordsmanButton,
            new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y){
                    controller.addEvent(new ViewEvent(true, ObjectType.SWORDSMAN));
                }
            });
        createButton(300,  screenY - 200, "add Archer", addArcherButton,
            new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y){
                    System.out.println("archer added!");
                    //controller.addEvent()
                }
            });
    }

    public void update()
    {
        //handleInput();
        cam.update();
        spriteBatch.setProjectionMatrix(cam.combined);
    }

    public void draw(float deltaTime)
    {
        spriteBatch.begin();
        Sprite backgroundTexture = staticObjectsTextureMap.get(ObjectType.BACKGROUND_IMAGE);
        spriteBatch.draw(backgroundTexture, 0, 0);
        spriteBatch.draw(backgroundTexture, -1200, 0);
        spriteBatch.draw(backgroundTexture, 1200, 0);

        // draw non-animated objects
        for (Object gameObject : model.getGameObjects())
        {
            drawGameObject((IReadOnlyGameObject)gameObject);
        }
        //draw actors
        for (Object actor : model.getActors()){
            drawGameObject((IReadOnlyAnimatedActor)actor, deltaTime);
        }

        font.draw(spriteBatch, model.getMoney() + " $", screenX - 50, screenY - 20);
        font.draw(spriteBatch, "base health = " + model.getBaseHealth(), screenX - 200, screenY - 20);
        font.draw(spriteBatch, "enemy base health = " + model.getEnemyBaseHealth(), screenX - 400, screenY - 20);
        spriteBatch.end();

        //draw UI
        stage.draw();
    }

    private void drawGameObject(IReadOnlyGameObject gameObject){
        Sprite texture = staticObjectsTextureMap.get(gameObject.getType());
        float x = (gameObject.getPosition().x - gameObject.getWidth()) * pixels_per_meter;
        float y = (gameObject.getPosition().y - gameObject.getHeight()) * pixels_per_meter;
        if (gameObject.isOnEnemySide()){
            texture.flip(true, false);
            spriteBatch.draw(texture, x, y);
            texture.flip(true, false);
        }
        else {
            spriteBatch.draw(texture, x, y);
        }
    }

    private void drawGameObject(IReadOnlyAnimatedActor gameObject, float deltaTime){
        String id = gameObject.getId();
        ObjectState objectState = gameObject.getState();
        Animator animator = actorsMap.get(id);
        animator.update(objectState, deltaTime);
        TextureRegion textureRegion = animator.getCurrentFrame();
        float x = (gameObject.getPosition().x - gameObject.getWidth()) * pixels_per_meter;
        float y = (gameObject.getPosition().y - gameObject.getHeight()) * pixels_per_meter;

        if (gameObject.isOnEnemySide()){
            textureRegion.flip(true, false);
            spriteBatch.draw(textureRegion, x, y);
            textureRegion.flip(true, false);
        }
        else {
            spriteBatch.draw(textureRegion, x, y);
        }
    }

    public void loadTextures(){
        staticObjectsTextureMap.put(ObjectType.BACKGROUND_IMAGE, getSprite("background-textures.png"));
        staticObjectsTextureMap.put(ObjectType.PLATFORM, getSprite("platform.png"));
        staticObjectsTextureMap.put(ObjectType.BASE, getSprite("base.png"));
    }

    public void loadActorSprites(String id, ObjectType objectType){
        AnimationClip idle, move, attack;
        GameSingleton.TextureInfo textureInfos[] = GameSingleton.getGameSingleton().getActorTextureInfo(objectType);
        try{
            idle = new AnimationClip(
                    getTexture(textureInfos[0].filename),
                    textureInfos[0].cols,
                    textureInfos[0].rows,
                    textureInfos[0].filledFrames,
                    textureInfos[0].interval
            );
        }
        catch (NullPointerException e){
            System.out.println("IDLE TextureInfo not set " + GameSingleton.getGameSingleton().toString(objectType));
            idle = new AnimationClip();
        }
        try{
            move = new AnimationClip(
                    getTexture(textureInfos[1].filename),
                    textureInfos[1].cols,
                    textureInfos[1].rows,
                    textureInfos[1].filledFrames,
                    textureInfos[1].interval
            );
        }
        catch (NullPointerException e){
            System.out.println("MOVE TextureInfo not set " + GameSingleton.getGameSingleton().toString(objectType));
            move = new AnimationClip();
        }
        try{
            attack = new AnimationClip(
                    getTexture(textureInfos[2].filename),
                    textureInfos[2].cols,
                    textureInfos[2].rows,
                    textureInfos[2].filledFrames,
                    textureInfos[2].interval
            );
        }
        catch (NullPointerException e){
            System.out.println("ATTACK TextureInfo not set " + GameSingleton.getGameSingleton().toString(objectType));
            attack = new AnimationClip();
        }

        actorsMap.put(id, new Animator(idle, move, attack));
    }
    /*
    private void handleInput(){
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            cam.translate(-cameraSpeed, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            cam.translate(cameraSpeed, 0, 0);
        }
    }*/

        /* GETTERS AND SETTERS */

    public Vector2 getTextureSize(ObjectType objectType){
        Sprite texture = staticObjectsTextureMap.get(objectType);
        return new Vector2(texture.getWidth(), texture.getHeight());
    }

    public Vector2 getTextureSize(String id){
        TextureRegion texture = null;
        try{
            texture = actorsMap.get(id).getCurrentFrame();
        }
        catch(NullPointerException e){
            System.out.println("animation not set for actor of type " +
                    GameSingleton.getGameSingleton().toString(model.getActor(id).getType()));
        }
        return new Vector2(texture.getRegionWidth(), texture.getRegionHeight());
    }

    private Texture getTexture(String filename){

        return (Texture)controller.getAssetManager().get(filename);
    }

    private Sprite getSprite(String filename){

        return new Sprite((Texture)controller.getAssetManager().get(filename));
    }

}
