package com.strongholds.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MenuView extends AbstractView{
    AssetManager assetManager;

    public MenuView(AssetManager assetManager, int screenX, int screenY){
        this.assetManager = assetManager;
        this.screenX = screenX;
        this.screenY = screenY;

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

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

        TextButton connectButton = null;
        TextButton startButton = null;

        createButton(300,  screenY - 200, "add Archer", startButton,
                new ClickListener(){
                    @Override
                    public void clicked(InputEvent event, float x, float y){
                        System.out.println("archer added!");
                        //controller.addEvent()
                    }
                });
        createButton(300,  screenY - 400, "add Archer", connectButton,
                new ClickListener(){
                    @Override
                    public void clicked(InputEvent event, float x, float y){
                        System.out.println("archer added!");
                        //controller.addEvent()
                    }
                });

    }


}
