package coldwar;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;

import coldwar.MoveListOuterClass.MoveList;
import coldwar.logic.MoveBuilder;
import coldwar.ui.SplashScreen;

/**
 * ColdWarGame coordinates the application life cycle. It should not contain
 * game logic.
 * 
 * @see com.badlogix.gdx.Game
 */
public class ColdWarGame extends Game {

	public Peer peer;
	
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
		this.peer = new Peer(Gdx.app.getNet());
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

	/**
	 * End the current turn.
	 * 
	 * @param moveBuilder
	 */
	public void endTurn(final MoveBuilder moveBuilder) {
		final MoveList local = moveBuilder.getMoveList();
		this.peer.sendMoveList(local);
	}

	/**
	 * Host a game.
	 * 
	 * @param parseInt
	 */
	public void host(final int parseInt) {
		this.peer.Host(parseInt);
	}

	/**
	 * Connect to a remote game.
	 * 
	 * @param host
	 * @param port
	 */
	public void connect(final String host, final int port) {
		this.peer.Connect(host, port);
	}

}
