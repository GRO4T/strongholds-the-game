package com.strongholds.game.controller;

import com.badlogic.gdx.assets.AssetManager;
import com.strongholds.game.event.ViewEvent;

/**
 *
 */
public interface IViewController {
    int getScreenWidth();
    int getScreenHeight();
    AssetManager getAssetManager();
    void addEvent(ViewEvent event);
    String getMessage();
    String getUsername();
    String getOpponentUsername();
}
