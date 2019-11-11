package com.mygdx.strongholds;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Observable;
import java.util.Observer;

public class View implements Observer {
    private Model model;
    private GameController controller;
    private SpriteBatch spriteBatch;

    public void draw(){

    }
    @Override
    public void update(Observable observable, Object o) {

    }
}
