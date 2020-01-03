package com.strongholds.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.strongholds.game.GameSingleton;
import com.strongholds.game.controller.IMenuController;

public class MenuView extends AbstractView{
    private AssetManager assetManager;
    private Sprite background;
    private IMenuController controller;

    private TextField ipField;
    private TextField outPortField;
    private TextField inPortField;

    String errorMessage = "";

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
        Table table = new Table();
        table.setPosition(screenX / 2 - 100, screenY / 2);
        stage.addActor(table);

        table.addActor(createButton(0,  5, 200, 80,  "START GAME",
                new ClickListener(){
                    @Override
                    public void clicked(InputEvent event, float x, float y){
                        startGame();
                    }
                }));

        ipField = new TextField("", skin);
        ipField.setMaxLength(15);
        ipField.setPosition(-50, -50);
        ipField.setSize(300, 50);
        table.addActor(ipField);

        Label ipLabel = new Label("IP ADDRESS", skin);
        ipLabel.setPosition(270, -45);
        table.addActor(ipLabel);

        outPortField = new TextField("", skin);
        outPortField.setMaxLength(5);
        outPortField.setPosition(30, -105);
        outPortField.setSize(140, 50);
        table.addActor(outPortField);

        Label outPortLabel = new Label("OUT PORT", skin);
        outPortLabel.setPosition(270, -100);
        table.addActor(outPortLabel);

        inPortField = new TextField("", skin);
        inPortField.setMaxLength(5);
        inPortField.setPosition(30, -160);
        inPortField.setSize(140, 50);
        table.addActor(inPortField);

        Label inPortLabel = new Label("IN PORT", skin);
        inPortLabel.setPosition(270, -150);
        table.addActor(inPortLabel);

        table.addActor(createButton(20,-215 , 160, 50,  "CONNECT",
                new ClickListener(){
                    @Override
                    public void clicked(InputEvent event, float x, float y){
                        System.out.println("connecting");
                        int outPort = Integer.parseInt(outPortField.getText());
                        /*
                        if (GameSingleton.getGameSingleton().basicCommunicationPort == outPort){
                            System.out.println("restricted port!");
                            errorMessage = "RESTRICTED PORT!";
                            return;
                        }*/
                        int inPort = Integer.parseInt(inPortField.getText());
                        controller.setOutPort(outPort);
                        controller.setInPort(inPort);
                        controller.setIp(ipField.getText());
                    }
                }));
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
        //font.draw(spriteBatch, "IP ADDRESS", screenX / 2 + 160, screenY - 325);
        //font.draw(spriteBatch, "PORT", screenX / 2 + 160, screenY - 380);

        if (errorMessage != ""){
            font.draw(spriteBatch, errorMessage, screenX / 2 - errorMessage.length() * 5, screenY - 500);
        }

            spriteBatch.end();
        stage.draw();
    }

    public void dispose(){
        stage.dispose();
    }


}
