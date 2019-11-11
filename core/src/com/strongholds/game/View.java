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
    private Texture ball;
    private Texture platform;

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
        spriteBatch.draw(ball, model.ball.getPosition().x - 128, model.ball.getPosition().y - 128);
        spriteBatch.draw(platform, model.platform.getPosition().x - 256, model.platform.getPosition().y - 32);
        spriteBatch.end();
    }

    public void dispose(){

    }

    public void setTextures(){
        background = controller.getAssetManager().get("background-textures.png");
        ball = controller.getAssetManager().get("badlogic.jpg");
        platform = controller.getAssetManager().get("platform.jpg");
    }
}

/* TODO
    write AnimatedTexture class for animating objects
    it would go sth like that view asks model for current state of the object and then decides what to do
 */