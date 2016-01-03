package coldwar.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import coldwar.ColdWarGame;
import coldwar.Logger;

public abstract class AbstractScreen implements Screen {
	protected final ColdWarGame game;

	protected Stage stage;
	private final Viewport viewport;

	public AbstractScreen(final ColdWarGame game) {
		this.game = game;
		this.viewport = new ScreenViewport();
		this.stage = new Stage(this.viewport);
	}

	@Override
	public void dispose() {
		Logger.Dbg("Disposing screen: " + this.getName());
		this.stage.dispose();
	}

	protected String getName() {
		return this.getClass().getSimpleName();
	}

	@Override
	public void hide() {
		Logger.Dbg("Hiding screen: " + this.getName());
		this.dispose();
	}

	@Override
	public void pause() {
		Logger.Dbg("Pausing screen: " + this.getName());
	}

	@Override
	public void render(final float delta) {
		this.stage.act(delta);
		Gdx.gl.glClearColor(1.0f, 1.0f, 1.0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		this.stage.draw();
	}

	@Override
	public void resize(final int width, final int height) {
		Logger.Dbg("Resizing screen: " + this.getName() + " to: " + width + " x " + height);
	}

	@Override
	public void resume() {
		Logger.Dbg("Resuming screen: " + this.getName());
	}
	
	@Override
	public void show() {
		Logger.Dbg("Showing screen: " + this.getName());
		Gdx.input.setInputProcessor(this.stage);
	}
}