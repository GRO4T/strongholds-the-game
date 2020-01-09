package com.strongholds.game.controller;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import com.strongholds.game.GameSingleton;
import com.strongholds.game.GameSingleton.ObjectType;
import com.strongholds.game.event.ErrorEvent;
import com.strongholds.game.event.ViewEvent;
import com.strongholds.game.model.IModel;
import com.strongholds.game.model.Model;
import com.strongholds.game.gameobject.GameObject;
import com.strongholds.game.net.INetworkController;
import com.strongholds.game.net.ObjectReceivedListener;
import com.strongholds.game.net.TcpServer;
import com.strongholds.game.view.IGameView;
import com.strongholds.game.view.GameView;
import com.strongholds.game.view.MenuView;

import java.util.Timer;
import java.util.TimerTask;
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
	private MenuView menuView;

	private String message = "";
	Timer clearMessageTimer;

	private LinkedBlockingQueue<ViewEvent> queueOfViewEvents;
	//private LinkedBlockingQueue<ModelEvent> modelEventsQueue;

	private INetworkController networkController;
	private Thread networkThread;

	private Vector2 friendlyBaseSpawnPoint;
	private Vector2 enemyBaseSpawnPoint;
	private Vector2 friendliesSpawnPoint;
	private Vector2 enemiesSpawnPoint;

	public StrongholdsGame(int screenWidth, int screenHeight) {
		gameSingleton = GameSingleton.getGameSingleton();
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		clearMessageTimer = new Timer(true);
	}

	@Override
	public void create () {
		queueOfViewEvents = new LinkedBlockingQueue<>();

		assetManager = new AssetManager();
		loadAssets();

		menuView = new MenuView(this, assetManager, screenWidth, screenHeight);
		model = new Model();
		gameView = new GameView(model, this);
		gameView.loadTextures();

		createMap();

		networkController = new TcpServer();
		networkController.registerController(this);
		networkThread = new Thread(networkController);

		menuView.init();

		//test
		//connect();
		//startGame();
	}

	private void createMap(){
		friendlyBaseSpawnPoint = new Vector2(0, 60);
		enemyBaseSpawnPoint = new Vector2(1072, 60);
		friendliesSpawnPoint = new Vector2(friendlyBaseSpawnPoint);
		friendliesSpawnPoint.add(new Vector2(gameView.getTextureSize(ObjectType.BASE).x - 60,0));
		enemiesSpawnPoint = new Vector2((enemyBaseSpawnPoint));
		enemiesSpawnPoint.add(new Vector2(20, 0));
		createObject("base", ObjectType.BASE, friendlyBaseSpawnPoint);
		createObject("enemyBase", ObjectType.BASE, enemyBaseSpawnPoint);
		GameObject base = (GameObject)model.getGameObject("base");
		base.setHealth(100);
		GameObject enemyBase = (GameObject)model.getGameObject("enemyBase");
		enemyBase.setIsOnEnemySide(true);
		enemyBase.setHealth(100);
		createObject(ObjectType.PLATFORM, new Vector2(0, 0));
	}

	@Override
	public void render () {
		if (!startGame){
			menuView.draw();
			return;
		}

		//if (!paused){
			earlyUpdate();
			gameView.update();
			update();
			model.update(1.0f / Fps);
		//}

		gameView.draw(Gdx.graphics.getDeltaTime());
	}

	@Override
	public void dispose () {
		model.dispose();
		assetManager.dispose();
		networkThread.interrupt();
		networkController.dispose();
		menuView.dispose();
	}

	private void earlyUpdate(){

	}

	private void update(){
		ViewEvent viewEvent;
		while (queueOfViewEvents.size() > 0){
			viewEvent = queueOfViewEvents.poll();
			if (viewEvent.toTrainUnit()){
				ObjectType unitType = viewEvent.getUnitType();

				boolean isEnemy = viewEvent.getIsEnemy();
				if (isEnemy){
					createUnit(unitType, enemiesSpawnPoint, isEnemy);
				}
				else{
					long unitCost = gameSingleton.getCost(unitType);
					if (model.getMoney() >= unitCost) {
						createUnit(unitType, friendliesSpawnPoint, isEnemy);
						//notify the opponent that you trained the unit
						viewEvent.setIsEnemy(true);
						networkController.addObjectRequest(viewEvent);
						model.addMoney(-unitCost);
					}
					else
						setMessage("NOT ENOUGH MONEY");

				}
			}
		}

	}


	private void loadAssets(){
		for (String filename : GameSingleton.getGameSingleton().getTextureFilenames()){
			assetManager.load(filename, Texture.class);
		}
		assetManager.finishLoading();
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

	/* IViewController */

	public void addEvent(ViewEvent viewEvent){
		queueOfViewEvents.add(viewEvent);
	}

	public String getMessage() {
		return message;
	}

	private void setMessage(String message){
		if (this.message.equals(message))
			return;

		this.message = message;
		ClearMessageTask clearMessageTask = new ClearMessageTask();
		int clearMessageInterval = 2000;
		clearMessageTimer.schedule(clearMessageTask, clearMessageInterval);
	}

	private class ClearMessageTask extends TimerTask {
		@Override
		public void run() {
			message = "";
		}
	}

	/* ObjectReceivedListener interface */

	public void notify(LinkedBlockingQueue<Object> receivedObjects) {
		while (receivedObjects.size() > 0){
			Object receivedObj = receivedObjects.poll();
			if (receivedObj instanceof ViewEvent){
				queueOfViewEvents.add((ViewEvent)receivedObj);
			}
		}
	}

	public void notifyOnError(ErrorEvent errorEvent) {
		if (errorEvent.isOpponentDisconnected()){
			setMessage("OPPONENT DISCONNECTED");
		}
	}

	/* IMenuController */

	public void startGame() {
		startGame = true;
		gameView.init();
		startNetworkController();

	}

	private void startNetworkController(){
		networkThread = new Thread(networkController);
		networkThread.start();
	}

	public boolean connect(){
		return networkController.connect();
	}

	/* GETTERS AND SETTERS */

	public void setIp(String ip) {
		networkController.setTargetIp(ip);
	}

	public void setInPort(int port) {
		networkController.setInPort(port);
	}

	public void setOutPort(int port) {
		networkController.setOutPort(port);
	}

	public void setUsername(String username) {

	}

	public int getScreenWidth() {
		return screenWidth;
	}

	public int getScreenHeight() {
		return screenHeight;
	}

	public AssetManager getAssetManager() {
		return assetManager;
	}
}

