package com.strongholds.game.view;

import com.badlogic.gdx.InputAdapter;

public class MyInputProcessor extends InputAdapter {
    @Override
    public boolean keyDown (int keycode) {
        return true;
    }

    @Override
    public boolean mouseMoved (int x, int y) {
        return true;
    }
}
