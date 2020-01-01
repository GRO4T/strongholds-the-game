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

    protected Sprite backgroundTexture;

    protected void createButton(float x, float y, String label, TextButton buttonRef, ClickListener clickListener){
        buttonRef = new TextButton(label, textButtonStyle);
        buttonRef.setPosition(x, y);
        if (clickListener != null){
            buttonRef.addListener(clickListener);
        }
        stage.addActor(buttonRef);
    }
}
