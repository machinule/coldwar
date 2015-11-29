package coldwar.logic;

import coldwar.GameStateOuterClass.GameSettings;
import coldwar.GameStateOuterClass.GameState;
import coldwar.GameStateOuterClass.ProvinceSettings;
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
		// Just use the defaults in the proto for now.
		GameSettings.Builder settings = GameSettings.newBuilder();
		populateProvinces(settings);
		
		GameState.Builder state = GameState.newBuilder()
				.setSettings(settings.build())
				.setHeat(settings.getHeatInit())
				.setSeed(settings.getSeedInit())
				.setTurn(0)
				.setUsa(UnitedStates.newBuilder()
						.setInfluenceStore(InfluenceStore.newBuilder()
								.setPolitical(settings.getUsaPolStoreInit())
								.setMilitary(settings.getUsaMilStoreInit())
								.setCovert(settings.getUsaCovStoreInit())
								.build())
						.build())
				.setUssr(SovietUnion.newBuilder()
						.setInfluenceStore(InfluenceStore.newBuilder()
								.setPolitical(settings.getUssrPolStoreInit())
								.setMilitary(settings.getUssrMilStoreInit())
								.setCovert(settings.getUssrCovStoreInit())
								.build())
						.build());
		for (ProvinceSettings p : settings.getProvincesList()) {
			Province.Builder builder = state.addProvincesBuilder()
				.setId(p.getId())
				.setDissidents(p.getDissidentsInit())
				.setGov(p.getGovernmentInit())
				.setInfluence(p.getInfluenceInit())
				.setRegion(p.getRegionInit());
			if (p.hasAllyInit()) {
				builder.setAlly(p.getAllyInit());
			}
			if (p.hasMilitaryBaseInit()) {
				builder.setBase(p.getMilitaryBaseInit());
			}
			if (p.hasOccupierInit()) {
				builder.setOccupier(p.getOccupierInit());
			}
		}
		this.initialGameState = state.build();
		Logger.Dbg("Initial game state: " + this.initialGameState);
		return state;
	}
	
	public Player getPlayer() {
		return this.player;
	}
		
	public void nextTurn() {
		Logger.Info("Proceeding to the next turn.");
		ComputedGameState computedState = new ComputedGameState(this.state, this.getUSAMove(), this.getUSSRMove());
		this.state = computedState.nextState;
		Logger.Info("Next game state: " + this.state.toString());
	}
	
	protected void populateProvinces(GameSettings.Builder settings) {
		
		// SUPERPOWERS
		
		settings.addProvincesBuilder()
			.setId(Province.Id.USA)
			.setRegionInit(Province.Region.SUPERPOWERS)
			.setLabel("United States")
			.setGovernmentInit(Province.Government.DEMOCRACY)
			.addAdjacency(Province.Id.MEXICO)
			.addAdjacency(Province.Id.CUBA);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.USSR)
			.setRegionInit(Province.Region.SUPERPOWERS)
			.setLabel("Soviet Union")
			.setGovernmentInit(Province.Government.COMMUNISM);
		
		// CENTRAL AMERICA
		
		settings.addProvincesBuilder()
			.setId(Province.Id.MEXICO)
			.setRegionInit(Province.Region.CENTRAL_AMERICA)
			.setLabel("Mexico")
			.setStabilityBase(2)
			.addAdjacency(Province.Id.USA)
			.addAdjacency(Province.Id.GUATEMALA)
			.addAdjacency(Province.Id.HONDURAS)
			.addAdjacency(Province.Id.CUBA);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.GUATEMALA)
			.setRegionInit(Province.Region.CENTRAL_AMERICA)
			.setLabel("Guatemala")
			.addAdjacency(Province.Id.MEXICO)
			.addAdjacency(Province.Id.HONDURAS)
			.addAdjacency(Province.Id.NICARAGUA);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.HONDURAS)
			.setRegionInit(Province.Region.CENTRAL_AMERICA)
			.setLabel("Honduras")
			.addAdjacency(Province.Id.MEXICO)
			.addAdjacency(Province.Id.GUATEMALA)
			.addAdjacency(Province.Id.NICARAGUA);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.NICARAGUA)
			.setRegionInit(Province.Region.CENTRAL_AMERICA)
			.setLabel("Nicaragua")
			.addAdjacency(Province.Id.CUBA)
			.addAdjacency(Province.Id.HONDURAS)
			.addAdjacency(Province.Id.GUATEMALA)
			.addAdjacency(Province.Id.PANAMA)
			.addAdjacency(Province.Id.COSTA_RICA);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.COSTA_RICA)
			.setRegionInit(Province.Region.CENTRAL_AMERICA)
			.setLabel("Costa Rica")
			.setStabilityBase(2)
			.setGovernmentInit(Province.Government.DEMOCRACY)
			.addAdjacency(Province.Id.NICARAGUA)
			.addAdjacency(Province.Id.PANAMA);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.PANAMA)
			.setRegionInit(Province.Region.CENTRAL_AMERICA)
			.setLabel("Panama")
			.setInfluenceInit(2)
			.setStabilityBase(2)
			.setMilitaryBaseInit(Province.Id.USA)
			.addAdjacency(Province.Id.NICARAGUA)
			.addAdjacency(Province.Id.COSTA_RICA)
			.addAdjacency(Province.Id.ECUADOR);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.CUBA)
			.setRegionInit(Province.Region.CENTRAL_AMERICA)
			.setLabel("Cuba")
			.setStabilityBase(2)
			.setDissidentsInit(true)
			.addAdjacency(Province.Id.USA)
			.addAdjacency(Province.Id.NICARAGUA)
			.addAdjacency(Province.Id.MEXICO)
			.addAdjacency(Province.Id.HAITI);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.HAITI)
			.setRegionInit(Province.Region.CENTRAL_AMERICA)
			.setLabel("Haiti")
			.addAdjacency(Province.Id.CUBA)
			.addAdjacency(Province.Id.DOMINICAN_REP);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.DOMINICAN_REP)
			.setRegionInit(Province.Region.CENTRAL_AMERICA)
			.setLabel("Dominican Rep.")
			.setGovernmentInit(Province.Government.AUTOCRACY)
			.addAdjacency(Province.Id.COLUMBIA)
			.addAdjacency(Province.Id.HAITI)
			.addAdjacency(Province.Id.LESS_ANTILLES);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.LESS_ANTILLES)
			.setRegionInit(Province.Region.CENTRAL_AMERICA)
			.setLabel("Lesser Antilles")
			.addAdjacency(Province.Id.DOMINICAN_REP)
			.addAdjacency(Province.Id.VENEZUELA)
			.addAdjacency(Province.Id.GUYANA);
		
		// SOUTH AMERICA
		
		settings.addProvincesBuilder()
			.setId(Province.Id.COLUMBIA)
			.setRegionInit(Province.Region.SOUTH_AMERICA)
			.setLabel("Columbia")
			.addAdjacency(Province.Id.DOMINICAN_REP)
			.addAdjacency(Province.Id.VENEZUELA)
			.addAdjacency(Province.Id.ECUADOR)
			.addAdjacency(Province.Id.GUYANA);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.ECUADOR)
			.setRegionInit(Province.Region.SOUTH_AMERICA)
			.setLabel("Ecuador")
			.setStabilityBase(2)
			.addAdjacency(Province.Id.COLUMBIA)
			.addAdjacency(Province.Id.PANAMA)
			.addAdjacency(Province.Id.PERU);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.PERU)
			.setRegionInit(Province.Region.SOUTH_AMERICA)
			.setLabel("Peru")
			.setStabilityBase(2)
			.setGovernmentInit(Province.Government.AUTOCRACY)
			.addAdjacency(Province.Id.ECUADOR)
			.addAdjacency(Province.Id.BOLIVIA)
			.addAdjacency(Province.Id.CHILE);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.CHILE)
			.setRegionInit(Province.Region.SOUTH_AMERICA)
			.setLabel("Chile")
			.setStabilityBase(2)
			.setGovernmentInit(Province.Government.DEMOCRACY)
			.addAdjacency(Province.Id.PERU)
			.addAdjacency(Province.Id.ARGENTINA)
			.addAdjacency(Province.Id.BOLIVIA);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.BOLIVIA)
			.setRegionInit(Province.Region.SOUTH_AMERICA)
			.setLabel("Bolivia")
			.setStabilityBase(2)
			.addAdjacency(Province.Id.PERU)
			.addAdjacency(Province.Id.ARGENTINA)
			.addAdjacency(Province.Id.CHILE);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.ARGENTINA)
			.setRegionInit(Province.Region.SOUTH_AMERICA)
			.setLabel("Argentina")
			.setStabilityBase(2)
			.addAdjacency(Province.Id.BOLIVIA)
			.addAdjacency(Province.Id.BRAZIL)
			.addAdjacency(Province.Id.CHILE);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.BRAZIL)
			.setRegionInit(Province.Region.SOUTH_AMERICA)
			.setLabel("Brazil")
			.setStabilityBase(2)
			.addAdjacency(Province.Id.GUYANA)
			.addAdjacency(Province.Id.VENEZUELA)
			.addAdjacency(Province.Id.ARGENTINA);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.GUYANA)
			.setRegionInit(Province.Region.SOUTH_AMERICA)
			.setLabel("Guyana")
			.setGovernmentInit(Province.Government.COLONY)
			.setOccupierInit(Province.Id.GREAT_BRITAIN)
			.addAdjacency(Province.Id.COLUMBIA)
			.addAdjacency(Province.Id.VENEZUELA)
			.addAdjacency(Province.Id.BRAZIL)
			.addAdjacency(Province.Id.LESS_ANTILLES);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.VENEZUELA)
			.setRegionInit(Province.Region.SOUTH_AMERICA)
			.setLabel("Venezuela")
			.setStabilityBase(2)
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