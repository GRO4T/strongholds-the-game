package com.strongholds.game.view;

public interface IMenuView {
    void init();

    /**
     * Draws the menu
     */
    void draw();

    /**
     * Called when view is destroyed
     */
    void dispose();
}
