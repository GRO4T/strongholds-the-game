package com.strongholds.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Observable;
import java.util.Observer;

public class View implements Observer {
    private Model model;
    private StrongholdsGame controller;


    private SpriteBatch spriteBatch;
    private Texture background;

    public View(Model model, StrongholdsGame controller) {
        this.model = model;
        this.controller = controller;
        spriteBatch = new SpriteBatch();
    }

    @Override
    public void update(Observable observable, Object o) {

    }

    public void draw(){
        spriteBatch.begin();
        spriteBatch.draw(background, 0, 0);
        spriteBatch.end();
    }

    public void dispose(){

    }

    public void init(){

        background = controller.getAssetManager().get("background-textures.png");
    }
}
