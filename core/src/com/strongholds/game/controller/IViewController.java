package com.strongholds.game.controller;

import com.badlogic.gdx.assets.AssetManager;
import com.strongholds.game.view.ViewEvent;

public interface IViewController {
    int getScreenWidth();
    int getScreenHeight();
    AssetManager getAssetManager();
    void addEvent(ViewEvent event);
}
