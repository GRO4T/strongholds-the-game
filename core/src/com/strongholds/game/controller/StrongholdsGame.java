package com.strongholds.game.controller;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import com.strongholds.game.GameSingleton;
import com.strongholds.game.GameSingleton.ObjectType;
import com.strongholds.game.model.Model;
import com.strongholds.game.view.View;

import java.util.Queue;

// It's our game controller

public class StrongholdsGame extends ApplicationAdapter {
	private AssetManager assetManager;

	final float Fps = 60.0f;
	private int screenWidth;
	private int screenHeight;

	int nextId = 0;

	private Model model;
	private View view;

	Queue events;

	public StrongholdsGame(int screenWidth, int screenHeight) {
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
	}

	@Override
	public void create () {
		model = new Model();
		view = new View(model, this);

		assetManager = new AssetManager();
		loadAssets();
		view.loadTextures();

		createObject(ObjectType.BASE, new Vector2(0, 60));
		createObject(ObjectType.PLATFORM, new Vector2(0, 0));

		createActor(ObjectType.SWORDSMAN, new Vector2(200, 400));

		createActor("player", ObjectType.SWORDSMAN, new Vector2(600, 400));
	}

	@Override
	public void render () {
		view.update();
		update();
		model.update(1.0f / Fps);
		view.draw(Gdx.graphics.getDeltaTime());
	}

	@Override
	public void dispose () {
		model.dispose();
		view.dispose();
		assetManager.dispose();
	}

	private void update(){

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

	private void createObject(ObjectType objectType, Vector2 position){
		model.createObject(Integer.toString(nextId++), objectType, position, view.getTextureSize(objectType));
	}

	private void createActor(ObjectType objectType, Vector2 position){
		String id = Integer.toString(nextId++);
		createActor(id, objectType, position);
	}

	private void createActor(String id, ObjectType objectType, Vector2 position){
		view.loadActorSprites(id, objectType);
		Vector2 actorSize = view.getTextureSize(id);
		model.createActor(id, objectType, position, actorSize);
	}
}
