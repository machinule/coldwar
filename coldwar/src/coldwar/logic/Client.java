package coldwar.logic;

import coldwar.GameStateOuterClass.GameState;
import coldwar.Logger;
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
		
	protected GameState.Builder getInitialGameState() {
		return GameState.newBuilder()
				.setVersion("0.0.1")
				.setHeat(Settings.getConstInt("Starting Heat"))
				.setTurn(0);
	}
	
	public Player getPlayer() {
		return this.player;
	}
		
	public void nextTurn() {
		Logger.Info("Proceeding to the next turn.");
		ComputationCache cache = new ComputationCache(this.state, this.getUSAMove(), this.getUSSRMove());
		this.state = Computations.getNextGameState(cache);
		Logger.Info("Next game state: " + this.state.toString());
	}

	public abstract void endTurn();
	public abstract MoveList getUSAMove();
	public abstract MoveList getUSSRMove();
	public abstract MoveBuilder getMoveBuilder();	
}