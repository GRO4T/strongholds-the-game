package com.strongholds.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AssetLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.strongholds.game.GameSingleton;
import com.strongholds.game.controller.IMenuController;

public class MenuView extends AbstractView{
    private AssetManager assetManager;
    private Sprite background;
    private IMenuController controller;

    public MenuView(IMenuController controller, AssetManager assetManager, int screenX, int screenY){
        this.assetManager = assetManager;
        this.controller = controller;
        this.screenX = screenX;
        this.screenY = screenY;

        stage = new Stage();

        spriteBatch = new SpriteBatch();

        background = new Sprite((Texture)assetManager.get(GameSingleton.getGameSingleton().menuBackgroundTexture));

        font = new BitmapFont();
        skin = new Skin(Gdx.files.internal("Craftacular_UI_Skin/craftacular-ui.json"));

        buttonAtlas = new TextureAtlas(Gdx.files.internal("Craftacular_UI_Skin/craftacular-ui.atlas"));
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.up = skin.getDrawable("button");
        textButtonStyle.down = skin.getDrawable("button-hover");
        textButtonStyle.checked = skin.getDrawable("button");

        createUI();
    }

    private void createUI(){
        createButton(screenX / 2 - 100,  screenY - 300, 200, 80,  "START GAME",
                new ClickListener(){
                    @Override
                    public void clicked(InputEvent event, float x, float y){
                        startGame();
                    }
                });
        createButton(screenX / 2 - 80,  screenY - 410, 160, 50,  "CONNECT",
                new ClickListener(){
                    @Override
                    public void clicked(InputEvent event, float x, float y){
                        System.out.println("connecting");
                    }
                });

        TextField ipField = new TextField("", skin);
        ipField.setPosition(screenX / 2 - 100, screenY - 355);
        ipField.setSize(200, 50);
        stage.addActor(ipField);
    }

    private void startGame(){
        controller.startGame();
    }

    public void init(){
        Gdx.input.setInputProcessor(stage);
    }

    public void draw(){
        spriteBatch.begin();
        spriteBatch.draw(background, 0, 0);
        font.draw(spriteBatch, "IP ADDRESS", screenX / 2 + 110, screenY - 325);
        spriteBatch.end();
        stage.draw();
    }

    public void dispose(){
        stage.dispose();
    }


}
