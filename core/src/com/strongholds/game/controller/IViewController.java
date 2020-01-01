package com.strongholds.game.controller;

import com.badlogic.gdx.assets.AssetManager;

public interface IViewController {
    int getScreenWidth();
    int getScreenHeight();
    AssetManager getAssetManager();
    void addEvent(ViewEvent event);
    //void startNetworkController();
}
