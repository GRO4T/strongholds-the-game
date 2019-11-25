package com.strongholds.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

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

		model.createObject(Model.ObjectType.BASE, new Vector2(0, 60),
				view.getTextureSize(Model.ObjectType.BASE));
		model.createObject(Model.ObjectType.PLATFORM, new Vector2(0, 0),
				view.getTextureSize(Model.ObjectType.PLATFORM));
	}

	@Override
	public void render () {
		view.update();
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
		for (String filename : GameSingleton.getGameSingleton().getTextureFilenames()){
			assetManager.load(filename, Texture.class);
		}
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
