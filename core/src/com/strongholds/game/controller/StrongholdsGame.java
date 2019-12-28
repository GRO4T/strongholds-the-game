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
import com.strongholds.game.model.gameobject.AnimatedActor;
import com.strongholds.game.model.gameobject.IAnimatedActor;
import com.strongholds.game.model.gameobject.IReadOnlyAnimatedActor;
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

		createUnit("player", ObjectType.SWORDSMAN, new Vector2(100, 100));
		//createActor("player", ObjectType.DEBUG_NO_OBJECT, new Vector2(600, 400));
	}

	@Override
	public void render () {
		//debug
		if (Gdx.input.isKeyJustPressed(Input.Keys.C)){
			createUnit(ObjectType.SWORDSMAN, new Vector2(700, 100));
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
			String id = "enemy" + nextId++;
			createUnit(id, ObjectType.SWORDSMAN, new Vector2( 800, 100));
			AnimatedActor enemy = (AnimatedActor)model.getActor(id);
			enemy.setIsOnEnemySide(true);
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
					createUnit(unitType, new Vector2(600, 400));
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

	private void createUnit(ObjectType objectType, Vector2 position){
		String id = Integer.toString(nextId++);
		createUnit(id, objectType, position);
	}

	private void createUnit(String id, ObjectType objectType, Vector2 position){
		view.loadActorSprites(id, objectType);
		Vector2 unitSize = view.getTextureSize(id);
		model.createUnit(id, objectType, position, unitSize);
	}

	public void addEvent(ViewEvent viewEvent){
		viewEventsQueue.add(viewEvent);
	}
}
