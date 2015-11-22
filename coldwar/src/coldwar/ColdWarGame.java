package coldwar;

import com.badlogic.gdx.Game;

import coldwar.MoveListOuterClass.MoveList;
import coldwar.logic.MoveBuilder;
import coldwar.ui.SplashScreen;

public class ColdWarGame extends Game {

	public static final String LOG = ColdWarGame.class.getSimpleName();
	public Peer peer;

	public ColdWarGame(final Peer peer) {
		super();
		this.peer = peer;
	}

	public void connect(final String host, final int port) {
		this.peer.Connect(host, port);
	}

	@Override
	public void create() {
		this.setScreen(new SplashScreen(this));
	}

	@Override
	public void dispose() {
	}

	public void endTurn(final MoveBuilder moveBuilder) {
		final MoveList local = moveBuilder.getMoveList();
		this.peer.sendMoveList(local);
	}

	public void host(final int parseInt) {
		this.peer.Host(parseInt);
	}

	@Override
	public void render() {
		super.render(); // important!
	}

}
