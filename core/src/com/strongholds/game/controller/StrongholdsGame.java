package com.strongholds.game.controller;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import com.strongholds.game.GameSingleton;
import com.strongholds.game.GameSingleton.ObjectType;
import com.strongholds.game.exception.CannotConnectException;
import com.strongholds.game.model.IModel;
import com.strongholds.game.model.Model;
import com.strongholds.game.gameobject.GameObject;
import com.strongholds.game.net.INetworkController;
import com.strongholds.game.net.ObjectReceivedListener;
import com.strongholds.game.net.TcpServer;
import com.strongholds.game.view.IGameView;
import com.strongholds.game.view.GameView;
import com.strongholds.game.view.MenuView;

import java.util.concurrent.LinkedBlockingQueue;

// It's our game controller

public class StrongholdsGame extends ApplicationAdapter implements IViewController, IModelController, IMenuController, ObjectReceivedListener {
	GameSingleton gameSingleton;
	private AssetManager assetManager;

	final float Fps = 60.0f;
	private int screenWidth;
	private int screenHeight;

	public boolean startGame;

	int nextId = 0;


	private IModel model;
	private IGameView gameView;

	private MenuView menu;

	private LinkedBlockingQueue<ViewEvent> viewEventsQueue;
	//private LinkedBlockingQueue<ModelEvent> modelEventsQueue;

	private INetworkController networkController;
	private Thread networkThread;

	public void startNetworkController(){
		Thread networkThread = new Thread(networkController);
		networkThread.start();
	}

	public StrongholdsGame(int screenWidth, int screenHeight) {
		gameSingleton = GameSingleton.getGameSingleton();
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
	}

	@Override
	public void create () {
		viewEventsQueue = new LinkedBlockingQueue<>();

		assetManager = new AssetManager();
		loadAssets();

		menu = new MenuView(this, assetManager, screenWidth, screenHeight);
		model = new Model();
		gameView = new GameView(model, this);
		gameView.loadTextures();

		createMap();

		networkController = new TcpServer();
		networkController.registerController(this);
		networkThread = new Thread(networkController);

		menu.init();

		networkThread.start();
	}

	private void createMap(){
		createObject("base", ObjectType.BASE, new Vector2(0, 60));
		createObject("enemyBase", ObjectType.BASE, new Vector2(1100, 60));
		GameObject base = (GameObject)model.getGameObject("enemyBase");
		base.setIsOnEnemySide(true);
		createObject(ObjectType.PLATFORM, new Vector2(0, 0));
	}

	@Override
	public void render () {
		if (!startGame){
			menu.draw();
			return;
		}

		earlyUpdate();
		gameView.update();
		update();
		model.update(1.0f / Fps);
		gameView.draw(Gdx.graphics.getDeltaTime());
	}


	@Override
	public void dispose () {
		model.dispose();
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

				boolean isEnemy = viewEvent.getIsEnemy();
				if (isEnemy){
					createUnit(unitType, new Vector2(1000, 60), isEnemy);
				}
				else{
					long unitCost = gameSingleton.getCost(unitType);
					if (model.getMoney() >= unitCost) {
						createUnit(unitType, new Vector2(150, 60), isEnemy);
						//notify the opponent that you trained the unit
						viewEvent.setIsEnemy(true);
						networkController.addObjectRequest(viewEvent);
						model.addMoney(-unitCost);
					}
					else
						System.out.println("not enough money!");
				}
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
		createObject(Integer.toString(nextId++), objectType, position);
	}

	private void createObject(String id, ObjectType objectType, Vector2 position){
		model.createObject(id, objectType, position, gameView.getTextureSize(objectType));
	}

	private void createUnit(ObjectType objectType, Vector2 position, boolean isEnemy){
		String id = Integer.toString(nextId++);
		createUnit(id, objectType, position, isEnemy);
	}

	private void createUnit(String id, ObjectType objectType, Vector2 position, boolean isEnemy){
		gameView.loadActorSprites(id, objectType);
		Vector2 unitSize = gameView.getTextureSize(id);
		model.createUnit(id, objectType, position, unitSize, isEnemy);
	}

	public void addEvent(ViewEvent viewEvent){
		viewEventsQueue.add(viewEvent);
	}

	@Override
	public void notify(LinkedBlockingQueue<Object> receivedObjects) {
		while (receivedObjects.size() > 0){
			Object receivedObj = receivedObjects.poll();
			if (receivedObj instanceof ViewEvent){
				System.out.println("new event added");
				viewEventsQueue.add((ViewEvent)receivedObj);
			}
		}
	}

	@Override
	public void startGame() {
		startGame = true;
		gameView.init();
	}

	@Override
	public boolean connect() {
		try{
			networkThread.start();
		}
		catch(CannotConnectException e){
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public void setIp(String ip) {
		networkController.setTargetIp(ip);
	}

	@Override
	public void setInPort(int port) {
		networkController.setInPort(port);
	}

	@Override
	public void setOutPort(int port) {
		networkController.setOutPort(port);
	}

	@Override
	public void setUsername(String username) {

	}
}
