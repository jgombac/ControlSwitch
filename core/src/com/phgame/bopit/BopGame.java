package com.phgame.bopit;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import states.GameStateManager;
import states.MainMenuState;

public class BopGame extends ApplicationAdapter {
	public static int WIDTH = 720;
	public static int HEIGHT = 1280;

	SpriteBatch sb;
	GameStateManager gsm;

	
	@Override
	public void create () {
		sb = new SpriteBatch();
		gsm = new GameStateManager();
//		WIDTH = Gdx.graphics.getWidth();
//		HEIGHT = Gdx.graphics.getHeight();
		gsm.push(new MainMenuState(gsm));

	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gsm.render(sb);
		gsm.update(Gdx.graphics.getDeltaTime());

	}

	@Override
	public void dispose(){
		gsm.dispose();
	}
}
