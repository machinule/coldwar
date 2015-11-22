package coldwar.logic;

import coldwar.GameStateOuterClass.GameState;
import coldwar.MoveListOuterClass.MoveList;
import coldwar.Settings;

/**
 * Client manages the game state, making moves and taking turns.
 */
public abstract class Client {
	
	static public enum Player {
		USA, USSR
	}

	protected GameState state;
	protected Player player;
	ComputationCache cache;
	
	public Client() {
		this.state = this.getInitialGameState().build();
		this.cache = new ComputationCache(this.state, MoveList.getDefaultInstance(), MoveList.getDefaultInstance());
	}
	
	protected GameState.Builder getInitialGameState() {
		return GameState.newBuilder()
				.setVersion("0.0.1")
				.setHeat(Settings.getConstInt("Starting Heat"))
				.setTurn(0);
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	public void endTurn() {
		cache.setState(state);
		cache.setUSAMove(this.getUSAMove());
		cache.setUSSRMove(this.getUSSRMove());
	}
	
	public void nextTurn() {
		state = this.getNextState();
	}
	
	public abstract MoveList getUSAMove();
	public abstract MoveList getUSSRMove();
	public abstract MoveBuilder getMoveBuilder();
	
	public GameState getNextState() {
		return Computations.getNextGameState(this.cache);
	}
	
}