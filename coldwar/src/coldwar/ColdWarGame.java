package coldwar;

import com.badlogic.gdx.Game;

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

	private final Peer peer;

	public ColdWarGame(final Peer peer) {
		super();
		this.peer = peer;
	}

	/**
	 * Called when the application is created. Immediately enters the splash
	 * screen.
	 * 
	 * @see com.badlogic.gdx.ApplicationListener#create()
	 */
	@Override
	public void create() {
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
