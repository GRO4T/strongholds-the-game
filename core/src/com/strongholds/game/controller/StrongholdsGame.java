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
import com.strongholds.game.view.IMenuView;
import com.strongholds.game.view.MenuView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * game controller
 */
public class StrongholdsGame extends ApplicationAdapter implements IViewController, IModelController, IMenuController, ObjectReceivedListener {
	/**
	 * reference to global GameSingleton
	 */
	private GameSingleton gameSingleton;
	/**
	 * instance of libgdx's AssetManager
	 */
	private AssetManager assetManager;

	private final float Fps = 60.0f;
	private int screenWidth;
	private int screenHeight;

	/**
	 * flag saying that we started the game (not necessarily our opponent)
	 */
	private boolean startGame;
	/**
	 * flag saying that our opponent started the game
	 */
	private boolean opponentStartGame;

	/**
	 * flag saying that the game is running
	 */
	private boolean running = true;

	/**
	 * integer used to create unique object ids
	 */
	private int nextId = 0;
	/**
	 * unique player id
	 */
	private int playerId;

	private IModel model;
	private IGameView gameView;
	private IMenuView menuView;

	/**
	 * current game message displayed on screen
	 * It is also used to display game errors.
	 */
	private String message = "";
	/**
	 * used to clear message after some time
	 */
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

	/**
	 * Constructor
	 * @param screenWidth screen width
	 * @param screenHeight screen height
	 */
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

	/**
	 * Initializes a new game
	 * Called every time player clicks start game in the menu.
	 */
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

	/**
	 * Handles changes made by model.update() in previous frame.
	 * Called before gameView.update().
	 */
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

	/**
	 * Handles events that came from views and network controller.
	 * Called after gameView.update().
	 */
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
				if (viewEvent.isSetUsername() && viewEvent.isFromNetwork()){
					setOpponentUsername(viewEvent.getUsername());
				}
				if (viewEvent.isStart() && viewEvent.isFromNetwork()){
					running = true;
					startGame = true;
					opponentStartGame = true;
					message = "";
				}
			}
		}
	}

	/**
	 * Helper method. Handles ViewEvents considering unit training.
	 * @param viewEvent viewEvent with trainUnit set to true
	 */
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

	/**
	 * Handles ModelEvents
	 */
	private void handleModelEvents(){
		ModelEvent modelEvent;
		while (queueOfModelEvents.size() > 0){
			modelEvent = queueOfModelEvents.poll();
			if (modelEvent.isUnitHit()){
				networkController.addObjectRequest(modelEvent);
			}
		}
	}

	/**
	 * Preloads the assets. (doesn't initialise view's textures)
	 */
	private void loadAssets(){
		for (String filename : GameSingleton.getGameSingleton().getTextureFilenames()){
			assetManager.load(filename, Texture.class);
		}
		assetManager.finishLoading();
	}

	/**
	 * Tells model to create a game object (random id)
	 * @param objectType object type
	 * @param position position
	 * @param isEnemy whether game object is on enemy side
	 */
	private void createObject(ObjectType objectType, Vector2 position, boolean isEnemy){
		String id = Integer.toString(nextId++) + playerId;
		createObject(id, objectType, position, isEnemy);
	}

	/**
	 * Tells model to create a game object (defined id)
	 * @param id object's id
	 * @param objectType object type
	 * @param position position
	 * @param isEnemy whether game object is on enemy side
	 */
	private void createObject(String id, ObjectType objectType, Vector2 position, boolean isEnemy){
		model.createObject(id, objectType, position, gameView.getTextureSize(objectType), isEnemy);
	}

	/**
	 * Tells model to create a unit (random id)
	 * @param objectType object type
	 * @param position position
	 * @param isEnemy whether is enemy
	 * @return the random id that was set for the unit
	 */
	private String createUnit(ObjectType objectType, Vector2 position, boolean isEnemy){
		String id = Integer.toString(nextId++) + playerId;
		createUnit(id, objectType, position, isEnemy);
		return id;
	}

	/**
	 * Tells model to create a unit (defined id)
	 * @param id unit id
	 * @param objectType object type
	 * @param position position
	 * @param isEnemy whether is enemy
	 */
	private void createUnit(String id, ObjectType objectType, Vector2 position, boolean isEnemy){
		gameView.loadActorSprites(id, objectType);
		Vector2 unitSize = gameView.getTextureSize(id);
		model.createUnit(id, objectType, position, unitSize, isEnemy);
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
		startGameEvent.setFromNetwork();
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

	/**
	 * Sets target ip address
	 * @param ip value of the ip address
	 */
	public void setIp(String ip) {
		networkController.setTargetIp(ip);
	}

	/**
	 * Sets input port
	 * @param port value of the input port
	 */
	public void setInPort(int port) {
		networkController.setInPort(port);
	}

	/**
	 * Sets output port
	 * @param port value of the output port
	 */
	public void setOutPort(int port) {
		networkController.setOutPort(port);
	}

	/**
	 * Sets host username
	 * @param username username
	 */
	public void setUsername(String username) {
		this.username = username;
		ViewEvent setUsernameEvent = new ViewEvent(username);
		setUsernameEvent.setFromNetwork();
		networkController.addObjectRequest(setUsernameEvent);
	}

	/**
	 * Sets opponent username
	 * @param opponentUsername
	 */
	public void setOpponentUsername(String opponentUsername){
		this.opponentUsername = opponentUsername;
	}

	/**
	 * Returns host username
	 * @return username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Returns opponent username
	 * @return username
	 */
	public String getOpponentUsername() {
		return opponentUsername;
	}

	/**
	 * Returns screen width
	 * @return screen width
	 */
	public int getScreenWidth() {
		return screenWidth;
	}

	/**
	 * Returns screen height
	 * @return screen height
	 */
	public int getScreenHeight() {
		return screenHeight;
	}

	/**
	 * Returns instance of libgdx AssetManager
	 * @return asset manager
	 */
	public AssetManager getAssetManager() {
		return assetManager;
	}
}

