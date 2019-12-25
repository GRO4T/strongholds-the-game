package com.strongholds.game.controller;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import com.strongholds.game.GameSingleton;
import com.strongholds.game.GameSingleton.ObjectType;
import com.strongholds.game.model.IModel;
import com.strongholds.game.model.Model;
import com.strongholds.game.view.IView;
import com.strongholds.game.view.View;
import com.strongholds.game.view.ViewEvent;

import java.util.concurrent.LinkedBlockingQueue;

// It's our game controller

public class StrongholdsGame extends ApplicationAdapter implements IViewController, IModelController{
	GameSingleton gameSingleton;
	private AssetManager assetManager;

	final float Fps = 60.0f;
	private int screenWidth;
	private int screenHeight;

	int nextId = 0;

	private IModel model;
	private IView view;

	LinkedBlockingQueue<ViewEvent> viewEventsQueue;
	//PriorityQueue<ModelEvent> modelEvents;

	public StrongholdsGame(int screenWidth, int screenHeight) {
		gameSingleton = GameSingleton.getGameSingleton();
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
	}

	@Override
	public void create () {
		model = new Model();
		view = new View(model, this);

		viewEventsQueue = new LinkedBlockingQueue<>();

		assetManager = new AssetManager();
		loadAssets();
		view.loadTextures();

		createObject(ObjectType.BASE, new Vector2(0, 60));
		createObject(ObjectType.PLATFORM, new Vector2(0, 0));

		createActor(ObjectType.SWORDSMAN, new Vector2(200, 400));

		createActor("player", ObjectType.SWORDSMAN, new Vector2(600, 400));
		//createActor("player", ObjectType.DEBUG_NO_OBJECT, new Vector2(600, 400));
	}

	@Override
	public void render () {
		//debug
		if (Gdx.input.isKeyJustPressed(Input.Keys.C)){
			createActor(ObjectType.SWORDSMAN, new Vector2(700, 400));
		}
		earlyUpdate();
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

	private void earlyUpdate(){

	}

	private void update(){
		ViewEvent viewEvent;
		while (viewEventsQueue.size() > 0){
			viewEvent = viewEventsQueue.poll();
			if (viewEvent.toTrainUnit()){
				ObjectType unitType = viewEvent.getUnitType();
				long unitCost = gameSingleton.getCost(unitType);
				if (model.getMoney() >= unitCost){
					createActor(unitType, new Vector2(600, 400));
					model.addMoney(-unitCost);
				}
				else
					System.out.println("not enough money!");
			}
		}
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

	public void addEvent(ViewEvent viewEvent){
		viewEventsQueue.add(viewEvent);
	}
}
