package com.strongholds.game.view;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.strongholds.game.controller.IViewController;

public class AbstractView {
    protected Stage stage;

    protected BitmapFont font;
    protected TextButton.TextButtonStyle textButtonStyle;
    protected Skin skin;
    protected TextureAtlas buttonAtlas;

    protected SpriteBatch spriteBatch;

    protected float screenX;
    protected float screenY;

    protected TextButton createButton(float x, float y, int width, int height, String label, ClickListener clickListener){
        TextButton newButton = new TextButton(label, textButtonStyle);
        newButton.setSize(width, height);
        newButton.setPosition(x, y);
        if (clickListener != null){
            newButton.addListener(clickListener);
        }
        return newButton;
    }
}
