package com.strongholds.game.controller;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import com.strongholds.game.GameSingleton;
import com.strongholds.game.GameSingleton.ObjectType;
import com.strongholds.game.event.ErrorEvent;
import com.strongholds.game.event.ModelEvent;
import com.strongholds.game.event.ViewEvent;
import com.strongholds.game.model.IModel;
import com.strongholds.game.model.Model;
import com.strongholds.game.net.INetworkController;
import com.strongholds.game.net.ObjectReceivedListener;
import com.strongholds.game.net.TcpServer;
import com.strongholds.game.view.IGameView;
import com.strongholds.game.view.GameView;
import com.strongholds.game.view.MenuView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Game Controller
 * Has references
 */
public class StrongholdsGame extends ApplicationAdapter implements IViewController, IModelController, IMenuController, ObjectReceivedListener {
	private GameSingleton gameSingleton;
	private AssetManager assetManager;

	private final float Fps = 60.0f;
	private int screenWidth;
	private int screenHeight;

	private boolean startGame;
	private boolean opponentStartGame;

	private boolean running = true;

	private int nextId = 0;
	private int playerId;

	private IModel model;
	private IGameView gameView;
	private IMenuView menuView;

	private String message = "";
	private Timer clearMessageTimer;

	private LinkedBlockingQueue<ViewEvent> queueOfViewEvents;
	private LinkedBlockingQueue<ModelEvent> queueOfModelEvents;

	private INetworkController networkController;
	private Thread networkThread;

	private String username = "";
	private String opponentUsername = "";

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
		queueOfModelEvents = new LinkedBlockingQueue<>();

		assetManager = new AssetManager();
		loadAssets();

		menuView = new MenuView(this, assetManager, screenWidth, screenHeight);

		networkController = new TcpServer();
		networkController.registerController(this);

		menuView.init();

		//test
		//connect();
		//startGame();
	}

	private void initGame(){
		running = false;
		startGame = false;
		opponentStartGame = false;
		message = "";
		model = new Model(this);
		gameView = new GameView(model, this);
		gameView.loadTextures();
		createMap();
	}

	private void createMap(){
		friendlyBaseSpawnPoint = new Vector2(0, 60);
		enemyBaseSpawnPoint = new Vector2(1072, 60);
		friendliesSpawnPoint = new Vector2(friendlyBaseSpawnPoint);
		friendliesSpawnPoint.add(new Vector2(gameView.getTextureSize(ObjectType.BASE).x - 60,0));
		enemiesSpawnPoint = new Vector2((enemyBaseSpawnPoint));
		enemiesSpawnPoint.add(new Vector2(20, 0));
		createObject("base", ObjectType.BASE, friendlyBaseSpawnPoint, false);
		createObject("enemyBase", ObjectType.BASE, enemyBaseSpawnPoint, true);
		createObject(ObjectType.PLATFORM, new Vector2(0, 0), false);
	}

	@Override
	public void render () {
		if (!startGame){
			menuView.draw();
			return;
		}

		if (running){
			float deltaTime = Gdx.graphics.getDeltaTime();
			earlyUpdate();
			gameView.update(deltaTime);
			update();
			model.update(1.0f / Fps);
		}
		else{
			handleViewEvents();
			setMessageAndClearAfterTime(getMessage());
		}

		gameView.draw();
	}

	@Override
	public void dispose () {
		if (model != null)
			model.dispose();
		assetManager.dispose();
		if (networkThread != null)
			networkThread.interrupt();
		networkController.dispose();
		menuView.dispose();
	}

	private void earlyUpdate(){
		if (model.getEnemyBaseHealth() <= 0){
			running = false;
			message = username + " WON!";
			gameView.gameFinished();
		}
		else if (model.getBaseHealth() <= 0){
			running = false;
			message = opponentUsername + " WON!";
			gameView.gameFinished();
		}
	}

	private void update(){
		handleViewEvents();
		handleModelEvents();
	}

	private void handleViewEvents(){
		ViewEvent viewEvent;
		while (queueOfViewEvents.size() > 0){
			viewEvent = queueOfViewEvents.poll();
			if (opponentStartGame){
				if (viewEvent.toTrainUnit() && running){
					handleUnitTraining(viewEvent);
				}
				if (viewEvent.isTogglePaused()){
					if (!viewEvent.isFromNetwork()){
						viewEvent.setFromNetwork();
						networkController.addObjectRequest(viewEvent);
					}
					if (running){
						running = false;
						message = "GAME PAUSED";
					}
					else{
						running = true;
						message = "";
					}
				}
				if (viewEvent.isRestart()){
					startGame = false;
					networkController.stop();
					menuView.init();
				}
			}
			else{
				if (viewEvent.isSetUsername()){
					setOpponentUsername(viewEvent.getUsername());
				}
				if (viewEvent.isStart()){
					running = true;
					startGame = true;
					opponentStartGame = true;
					message = "";
				}
			}
		}
	}

	private void handleUnitTraining(ViewEvent viewEvent){
		ObjectType unitType = viewEvent.getUnitType();

		boolean isEnemy = viewEvent.getIsEnemy();
		if (isEnemy){
			createUnit(viewEvent.getUnitId(), unitType, enemiesSpawnPoint, true);
		}
		else{
			long unitCost = gameSingleton.getCost(unitType);
			if (model.getMoney() >= unitCost) {
				String id = createUnit(unitType, friendliesSpawnPoint, false);
				//notify the opponent that you trained the unit
				viewEvent.setEnemy(true);
				viewEvent.setUnitId(id);
				networkController.addObjectRequest(viewEvent);
				model.addMoney(-unitCost);
			}
			else
				setMessageAndClearAfterTime("NOT ENOUGH MONEY");

		}
	}

	private void handleModelEvents(){
		ModelEvent modelEvent;
		while (queueOfModelEvents.size() > 0){
			modelEvent = queueOfModelEvents.poll();
			if (modelEvent.isUnitHit()){
				networkController.addObjectRequest(modelEvent);
			}
		}
	}


	private void loadAssets(){
		for (String filename : GameSingleton.getGameSingleton().getTextureFilenames()){
			assetManager.load(filename, Texture.class);
		}
		assetManager.finishLoading();
	}


	private void createObject(ObjectType objectType, Vector2 position, boolean isEnemy){
		String id = Integer.toString(nextId++) + playerId;
		createObject(id, objectType, position, isEnemy);
	}

	private void createObject(String id, ObjectType objectType, Vector2 position, boolean isEnemy){
		model.createObject(id, objectType, position, gameView.getTextureSize(objectType), isEnemy);
	}

	private String createUnit(ObjectType objectType, Vector2 position, boolean isEnemy){
		String id = Integer.toString(nextId++) + playerId;
		return createUnit(id, objectType, position, isEnemy);
	}

	private String createUnit(String id, ObjectType objectType, Vector2 position, boolean isEnemy){
		gameView.loadActorSprites(id, objectType);
		Vector2 unitSize = gameView.getTextureSize(id);
		model.createUnit(id, objectType, position, unitSize, isEnemy);
		return id;
	}

	/* IViewController */

	public void addEvent(ViewEvent viewEvent){
		queueOfViewEvents.add(viewEvent);
	}

	public void addEvent(ModelEvent modelEvent){
		queueOfModelEvents.add(modelEvent);
	}

	public String getMessage() {
		return message;
	}

	private void setMessageAndClearAfterTime(String message){
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
			else if (receivedObj instanceof ModelEvent){
				ModelEvent modelEvent = (ModelEvent) receivedObj;
				model.unitHit(modelEvent.getUnitId(), modelEvent.getDamage());
			}
		}
	}

	public void notifyOnError(ErrorEvent errorEvent) {
		if (errorEvent.isOpponentDisconnected()){
			if (opponentStartGame){
				setMessageAndClearAfterTime("OPPONENT DISCONNECTED");
				running = false;
				gameView.gameFinished();
			}
		}
	}

	/* IMenuController */

	public void startGame() {
		startNetworkController();

		initGame();
		Random random = new Random();
		int idRange = 60000;
		playerId = random.nextInt(idRange);

		gameView.init();

		ViewEvent startGameEvent = new ViewEvent();
		startGameEvent.setStart();
		networkController.addObjectRequest(startGameEvent);

		startGame = true;
		message = "Waiting for the opponent...";
	}

	private void startNetworkController(){
		networkThread = new Thread(networkController);
		networkThread.start();
		networkController.start();
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
		this.username = username;
		networkController.addObjectRequest(new ViewEvent(true, username));
	}

	public void setOpponentUsername(String opponentUsername){
		this.opponentUsername = opponentUsername;
	}

	public String getUsername() {
		return username;
	}

	public String getOpponentUsername() {
		return opponentUsername;
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

