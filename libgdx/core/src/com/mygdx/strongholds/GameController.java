package com.mygdx.strongholds;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.assets.AssetManager;

public class GameController extends ApplicationAdapter {
	final float Fps = 60.0f;

	private Model model;
	private View view;

	@Override
	public void create () {
		model = new Model();
		model.init(6, 2);
		view = new View();
		System.out.println("create called");
	}

	@Override
	public void render () {
		model.update(1.0f / Fps);
		view.draw();
	}
	
	@Override
	public void dispose () {

	}
}
