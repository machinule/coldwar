package coldwar.logic;

import com.badlogic.gdx.Gdx;

import coldwar.MoveOuterClass.MoveList;
import coldwar.Logger;
import coldwar.Peer;
import coldwar.GameStateOuterClass.GameState;

public class RemoteClient extends Client {

	private MoveBuilder currentMoveBuilder;
	private Peer peer;
	private boolean isHost;
	private MoveList usa;
	private MoveList ussr;
	
	public RemoteClient(String host, int port) {
		super();
		this.peer = new Peer(Gdx.app.getNet());
		this.peer.Connect(host, port);
		this.isHost = false;
		this.player = Player.USSR;
		this.state = this.getInitialGameState().build();
		this.currentMoveBuilder = new MoveBuilder(this.player, this.state);
		Logger.Info("Starting game with initial game state:\n" + this.state.toString());
	}

	public RemoteClient(int port) {
		super();
		this.peer = new Peer(Gdx.app.getNet());
		this.peer.Host(port);
		this.isHost = true;
		this.player = Player.USA;
		this.state = this.getInitialGameState().build();
		this.currentMoveBuilder = new MoveBuilder(this.player, this.state);
		Logger.Info("Starting game with initial game state:\n" + this.state.toString());
	}
	
	protected GameState.Builder getInitialGameState() {
		if (this.isHost) {
			GameState.Builder state = super.getInitialGameState();
			this.peer.sendGameState(state.build());
			return state;
		} else {
			GameState state = this.peer.getGameState();
			this.initialGameState = state;
			return state.toBuilder();
		}
	}
	

	@Override
	public MoveList getUSAMove() {
		if (this.player == Player.USA) {
			return this.usa;
		} else {
			this.usa = this.peer.getMoveList();
			return this.usa;
		}
	}

	@Override
	public MoveList getUSSRMove() {
		if (this.player == Player.USSR) {
			return this.ussr;
		} else {
			this.ussr = this.peer.getMoveList();
			return this.ussr;
		}
	}

	@Override
	public MoveBuilder getMoveBuilder() {
		return this.currentMoveBuilder;
	}

	@Override
	public void endTurn() {
		Logger.Info("Ending turn for Player " + this.player);
		if (this.player == Player.USA) {
			this.usa = this.currentMoveBuilder.getMoveList();
			this.peer.sendMoveList(this.usa);
		} else {
			this.ussr = this.currentMoveBuilder.getMoveList();
			this.peer.sendMoveList(this.ussr);
		}
		this.nextTurn();
		this.currentMoveBuilder = new MoveBuilder(this.player, this.state);
	}

}
