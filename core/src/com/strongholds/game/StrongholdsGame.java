package com.strongholds.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

// It's our game controller

public class StrongholdsGame extends ApplicationAdapter {
	private AssetManager assetManager;

	final float Fps = 60.0f;
	private int screenWidth;
	private int screenHeight;

	private Model model;
	private View view;

	public StrongholdsGame(int screenWidth, int screenHeight) {
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
	}

	@Override
	public void create () {
		model = new Model(6, 2);
		view = new View(model, this);

		assetManager = new AssetManager();
		loadAssets();
		view.setTextures();
	}

	@Override
	public void render () {
		model.update(1.0f / Fps);
		view.draw();
	}

	@Override
	public void dispose () {
		model.dispose();
		view.dispose();
		assetManager.dispose();
	}

	public AssetManager getAssetManager() {
		return assetManager;
	}

	private void loadAssets(){
		assetManager.load("background-textures.png", Texture.class);
		assetManager.load("badlogic.jpg", Texture.class);
		assetManager.load("platform.jpg", Texture.class);
		assetManager.finishLoading();
		System.out.println("finished loading assets");
	}

	public int getScreenWidth() {
		return screenWidth;
	}

	public int getScreenHeight() {
		return screenHeight;
	}
}