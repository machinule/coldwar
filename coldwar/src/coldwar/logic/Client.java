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
	public GameState initialGameState;
		
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
		populateProvinces(state);
		this.initialGameState = state.build();
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
	
	protected void populateProvinces(GameState.Builder state) {
		
		// CENTRAL AMERICA
		
		state.addProvincesBuilder()
			.setId(Province.Id.MEXICO)
			.setStability(3)
			.addAdjacency(Province.Id.GUATEMALA)
			.addAdjacency(Province.Id.HONDURAS);
		
		state.addProvincesBuilder()
			.setId(Province.Id.GUATEMALA)
			.addAdjacency(Province.Id.MEXICO)
			.addAdjacency(Province.Id.HONDURAS)
			.addAdjacency(Province.Id.NICARAGUA);
		
		state.addProvincesBuilder()
			.setId(Province.Id.HONDURAS)
			.addAdjacency(Province.Id.MEXICO)
			.addAdjacency(Province.Id.GUATEMALA)
			.addAdjacency(Province.Id.NICARAGUA);
		
		state.addProvincesBuilder()
			.setId(Province.Id.NICARAGUA)
			.addAdjacency(Province.Id.CUBA)
			.addAdjacency(Province.Id.HONDURAS)
			.addAdjacency(Province.Id.GUATEMALA)
			.addAdjacency(Province.Id.COSTA_RICA);
		
		state.addProvincesBuilder()
			.setId(Province.Id.COSTA_RICA)
			.setStability(2)
			.setGov(Province.Government.DEMOCRACY)
			.addAdjacency(Province.Id.NICARAGUA)
			.addAdjacency(Province.Id.PANAMA);
		
		state.addProvincesBuilder()
			.setId(Province.Id.PANAMA)
			.setInfluence(2)
			.setBase(Province.Id.US)
			.addAdjacency(Province.Id.NICARAGUA)
			.addAdjacency(Province.Id.ECUADOR);
		
		state.addProvincesBuilder()
			.setId(Province.Id.CUBA)
			.setInfluence(2)
			.setDissidents(true)
			.addAdjacency(Province.Id.US)
			.addAdjacency(Province.Id.NICARAGUA)
			.addAdjacency(Province.Id.MEXICO)
			.addAdjacency(Province.Id.HAITI);
		
		state.addProvincesBuilder()
			.setId(Province.Id.HAITI)
			.addAdjacency(Province.Id.CUBA)
			.addAdjacency(Province.Id.DOMINICAN_REP);
		
		state.addProvincesBuilder()
			.setId(Province.Id.DOMINICAN_REP)
			.setGov(Province.Government.AUTOCRACY)
			.addAdjacency(Province.Id.HAITI)
			.addAdjacency(Province.Id.LESS_ANTILLES);
		
		state.addProvincesBuilder()
			.setId(Province.Id.LESS_ANTILLES)
			.addAdjacency(Province.Id.DOMINICAN_REP)
			.addAdjacency(Province.Id.VENEZUELA)
			.addAdjacency(Province.Id.GUYANA);
		
		// SOUTH AMERICA
		
		state.addProvincesBuilder()
		.setId(Province.Id.COLUMBIA)
			.addAdjacency(Province.Id.DOMINICAN_REP)
			.addAdjacency(Province.Id.VENEZUELA)
			.addAdjacency(Province.Id.GUYANA);
		
		state.addProvincesBuilder()
			.setId(Province.Id.ECUADOR)
			.setStability(2)
			.addAdjacency(Province.Id.COLUMBIA)
			.addAdjacency(Province.Id.PERU);
		
		state.addProvincesBuilder()
			.setId(Province.Id.PERU)
			.setStability(2)
			.setGov(Province.Government.AUTOCRACY)
			.addAdjacency(Province.Id.ECUADOR)
			.addAdjacency(Province.Id.BOLIVIA)
			.addAdjacency(Province.Id.CHILE);
		
		state.addProvincesBuilder()
			.setId(Province.Id.CHILE)
			.setStability(2)
			.setGov(Province.Government.DEMOCRACY)
			.addAdjacency(Province.Id.PERU)
			.addAdjacency(Province.Id.ARGENTINA)
			.addAdjacency(Province.Id.BOLIVIA);
		
		state.addProvincesBuilder()
			.setId(Province.Id.BOLIVIA)
			.setStability(2)
			.addAdjacency(Province.Id.PERU)
			.addAdjacency(Province.Id.ARGENTINA)
			.addAdjacency(Province.Id.CHILE);
		
		state.addProvincesBuilder()
			.setId(Province.Id.ARGENTINA)
			.setStability(2)
			.addAdjacency(Province.Id.BOLIVIA)
			.addAdjacency(Province.Id.BRAZIL)
			.addAdjacency(Province.Id.CHILE);
		
		state.addProvincesBuilder()
			.setId(Province.Id.BRAZIL)
			.setStability(2)
			.addAdjacency(Province.Id.GUYANA)
			.addAdjacency(Province.Id.VENEZUELA)
			.addAdjacency(Province.Id.ARGENTINA);
		
		state.addProvincesBuilder()
			.setId(Province.Id.GUYANA)
			.setGov(Province.Government.COLONY)
			.setOccupier(Province.Id.GREAT_BRITAIN)
			.addAdjacency(Province.Id.VENEZUELA)
			.addAdjacency(Province.Id.BRAZIL)
			.addAdjacency(Province.Id.LESS_ANTILLES);
		
		state.addProvincesBuilder()
			.setId(Province.Id.VENEZUELA)
			.setStability(2)
			.addAdjacency(Province.Id.COLUMBIA)
			.addAdjacency(Province.Id.BRAZIL)
			.addAdjacency(Province.Id.GUYANA)
			.addAdjacency(Province.Id.LESS_ANTILLES);
	}

	public abstract void endTurn();
	public abstract MoveList getUSAMove();
	public abstract MoveList getUSSRMove();
	public abstract MoveBuilder getMoveBuilder();	
}