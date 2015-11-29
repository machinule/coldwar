package coldwar;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;

import coldwar.ui.SplashScreen;

/**
 * ColdWarGame coordinates the application life cycle. It should not contain
 * game logic.
 * 
 * @see com.badlogic.gdx.Game
 */
public class ColdWarGame extends Game {
	
	/**
	 * Called when the application is created. Immediately enters the splash
	 * screen.
	 * 
	 * @see com.badlogic.gdx.ApplicationListener#create()
	 */
	@Override
	public void create() {
		// Set the logger to DEBUG for initialization. It will be reset based on preferences later.
		Logger.setLogLevel(Application.LOG_DEBUG);
		Settings.init();
		Logger.setLogLevel(Settings.getLogLevel());
		if (Settings.getConstBool("pack_textures")) {
			final TexturePacker.Settings settings = new TexturePacker.Settings();
			settings.maxWidth = 16384;
			settings.maxHeight = 8192;
			TexturePacker.process(settings, "assets", "textures", "pack");	
		}
		Gdx.app.getGraphics().setDisplayMode(
				Settings.consts.getInteger("splash_y"),
				Settings.consts.getInteger("splash_x"),
				Settings.prefs.getBoolean("fullscreen"));
		this.setScreen(new SplashScreen(this));
	}

	/**
	 * Called when the application is rendered.
	 * 
	 * @see com.badlogic.gdx.Game#render()
	 */
	@Override
	public void render() {
		super.render();
	}

	/**
	 * Called when the application is disposed.
	 * 
	 * @see com.badlogic.gdx.Game#dispose()
	 */
	@Override
	public void dispose() {
		// TODO: clean up application elements.
	}

	public void setRes() {
		Gdx.app.getGraphics().setDisplayMode(
				Settings.prefs.getInteger("resolution_width"),
				Settings.prefs.getInteger("resolution_height"),
				Settings.prefs.getBoolean("fullscreen"));
	}
	
	public void setRes(int x, int y) {
		Gdx.app.getGraphics().setDisplayMode(
				x,
				y,
				Settings.prefs.getBoolean("fullscreen"));
	}
	
}
