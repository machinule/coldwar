package coldwar.logic;

import coldwar.GameStateOuterClass.GameState;
import coldwar.InfluenceStoreOuterClass.InfluenceStore;
import coldwar.Logger;
import coldwar.MoveListOuterClass.MoveList;
import coldwar.ProvinceOuterClass.Province;
import coldwar.Settings;
import coldwar.SovietUnionOuterClass.SovietUnion;
import coldwar.UnitedStatesOuterClass.UnitedStates;

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
		GameState.Builder state = GameState.newBuilder()
				.setVersion("0.0.1")
				.setHeat(Settings.getConstInt("Starting Heat"))
				.setTurn(0)
				.setUsa(UnitedStates.newBuilder()
						.setInfluenceStore(InfluenceStore.newBuilder()
								.setPolitical(Settings.getConstInt("starting_pol"))
								.setMilitary(Settings.getConstInt("starting_mil"))
								.setCovert(Settings.getConstInt("starting_cov"))
								.build())
						.build())
				.setUssr(SovietUnion.newBuilder()
						.setInfluenceStore(InfluenceStore.newBuilder()
								.setPolitical(Settings.getConstInt("starting_pol"))
								.setMilitary(Settings.getConstInt("starting_mil"))
								.setCovert(Settings.getConstInt("starting_cov"))
								.build())
						.build());
		for (final Province.Id id : Province.Id.values()) {
			state.addProvincesBuilder().setId(id).setInfluence(0).setDissidents(false);
		}
		return state;
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