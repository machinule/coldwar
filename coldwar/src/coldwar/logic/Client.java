package coldwar.logic;

import com.badlogic.gdx.Gdx;
import com.google.protobuf.TextFormat;

import coldwar.GameSettingsOuterClass.GameSettings;
import coldwar.GameStateOuterClass.GameState;
import coldwar.GameSettingsOuterClass.ProvinceSettings;
import coldwar.InfluenceStoreOuterClass.InfluenceStore;
import coldwar.LeaderOuterClass.Culture;
import coldwar.LeaderOuterClass.Leader;
import coldwar.Logger;
import coldwar.MoveOuterClass.MoveList;
import coldwar.ProvinceOuterClass.Province;
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
		if(!populateLeaders(settings)) return null;
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
						.setPatriotism(settings.getUsaInitPatriotism())
						.build())
				.setUssr(SovietUnion.newBuilder()
						.setInfluenceStore(InfluenceStore.newBuilder()
								.setPolitical(settings.getUssrPolStoreInit())
								.setMilitary(settings.getUssrMilStoreInit())
								.setCovert(settings.getUssrCovStoreInit())
								.build())
						.setPartyUnity(settings.getUssrInitPartyUnity())
						.build());
		for (ProvinceSettings p : settings.getProvincesList()) {
			Province.Builder builder = state.addProvincesBuilder()
				.setId(p.getId())
				.setDissidents(p.getDissidentsInit())
				.setGov(p.getGovernmentInit())
				.setInfluence(p.getInfluenceInit());
			if (p.hasAllyInit()) {
				builder.setAlly(p.getAllyInit());
			}
			if (p.hasMilitaryBaseInit()) {
				builder.setBase(p.getMilitaryBaseInit());
			}
			if (p.hasOccupierInit()) {
				builder.setOccupier(p.getOccupierInit());
			}
			if (p.hasLeaderInit()) {
				state.addActiveLeaders(getLeader(settings, p.getLeaderInit()));
			}
		}
		this.initialGameState = state.build();
		Logger.Dbg("Initial game state: " + this.initialGameState);
		return state;
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	protected Leader getLeader(GameSettings.Builder settings, String name) {
		for(Leader l : settings.getLeaderBankList()) {
			if (l.getName().equals(name)) {
				Logger.Dbg("Setting leader " + l.getName());
				return l;
			}
		}
		return null;
	}
	
	public void nextTurn() {
		Logger.Info("Proceeding to the next turn.");
		ComputedGameState computedState = new ComputedGameState(this.state, this.getUSAMove(), this.getUSSRMove());
		this.state = computedState.nextState;
		Logger.Info("Next game state: " + this.state.toString());
		Logger.Dbg("Net party unity: " + computedState.getNetPartyUnity());
		Logger.Dbg("Net patriotism: " + computedState.getNetPatriotism());
		
	}
	
	protected boolean populateLeaders(GameSettings.Builder settings) {
        String file = "src/proto/Leaders.txt";
        
        Logger.Dbg("Reading leader file at " + file);
        String input = new String(Gdx.files.internal("Leaders.txt").readString());
        
        try {
        	TextFormat.merge(input, settings);
        }
        catch(TextFormat.ParseException ex) {
        	Logger.Err(ex.toString());
        	return false;
        }
        return true;
    }

	protected void populateProvinces(GameSettings.Builder settings) {
		
		// SUPERPOWERS
		
		settings.addProvincesBuilder()
			.setId(Province.Id.USA)
			.setRegion(Province.Region.SUPERPOWERS)
			.setLabel("United States")
			.setInfluenceInit(1)
			.setGovernmentInit(Province.Government.DEMOCRACY)
			.addAdjacency(Province.Id.MEXICO)
			.addAdjacency(Province.Id.CUBA);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.USSR)
			.setRegion(Province.Region.SUPERPOWERS)
			.setLabel("Soviet Union")
			.setInfluenceInit(-1)
			.setGovernmentInit(Province.Government.COMMUNISM);
		
		// CENTRAL AMERICA
		
		settings.addProvincesBuilder()
			.setId(Province.Id.MEXICO)
			.setCulture(Culture.SPANISH)
			.setRegion(Province.Region.CENTRAL_AMERICA)
			.setLabel("Mexico")
			.setStabilityBase(2)
			.addAdjacency(Province.Id.USA)
			.addAdjacency(Province.Id.GUATEMALA)
			.addAdjacency(Province.Id.HONDURAS)
			.addAdjacency(Province.Id.CUBA);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.GUATEMALA)
			.setCulture(Culture.SPANISH)
			.setRegion(Province.Region.CENTRAL_AMERICA)
			.setLabel("Guatemala")
			.addAdjacency(Province.Id.MEXICO)
			.addAdjacency(Province.Id.HONDURAS);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.HONDURAS)
			.setCulture(Culture.SPANISH)
			.setRegion(Province.Region.CENTRAL_AMERICA)
			.setLabel("Honduras")
			.addAdjacency(Province.Id.MEXICO)
			.addAdjacency(Province.Id.GUATEMALA)
			.addAdjacency(Province.Id.NICARAGUA);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.NICARAGUA)
			.setCulture(Culture.SPANISH)
			.setRegion(Province.Region.CENTRAL_AMERICA)
			.setLabel("Nicaragua")
			.setInfluenceInit(1)
			.setGovernmentInit(Province.Government.AUTOCRACY)
			.setLeaderInit("Anastasio Somoza García")
			.addAdjacency(Province.Id.CUBA)
			.addAdjacency(Province.Id.HONDURAS)
			.addAdjacency(Province.Id.PANAMA)
			.addAdjacency(Province.Id.COSTA_RICA);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.COSTA_RICA)
			.setCulture(Culture.SPANISH)
			.setRegion(Province.Region.CENTRAL_AMERICA)
			.setLabel("Costa Rica")
			.setStabilityBase(2)
			.setGovernmentInit(Province.Government.DEMOCRACY)
			.addAdjacency(Province.Id.NICARAGUA)
			.addAdjacency(Province.Id.PANAMA);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.PANAMA)
			.setCulture(Culture.SPANISH)
			.setRegion(Province.Region.CENTRAL_AMERICA)
			.setLabel("Panama")
			.setInfluenceInit(1)
			.setStabilityBase(2)
			.setMilitaryBaseInit(Province.Id.USA)
			.addAdjacency(Province.Id.NICARAGUA)
			.addAdjacency(Province.Id.COSTA_RICA)
			.addAdjacency(Province.Id.COLOMBIA);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.CUBA)
			.setCulture(Culture.SPANISH)
			.setRegion(Province.Region.CENTRAL_AMERICA)
			.setLabel("Cuba")
			.setStabilityBase(2)
			.setDissidentsInit(true)
			.addAdjacency(Province.Id.USA)
			.addAdjacency(Province.Id.NICARAGUA)
			.addAdjacency(Province.Id.MEXICO)
			.addAdjacency(Province.Id.HAITI);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.HAITI)
			.setCulture(Culture.FRENCH)
			.setRegion(Province.Region.CENTRAL_AMERICA)
			.setLabel("Haiti")
			.addAdjacency(Province.Id.CUBA)
			.addAdjacency(Province.Id.DOMINICAN_REP);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.DOMINICAN_REP)
			.setCulture(Culture.SPANISH)
			.setRegion(Province.Region.CENTRAL_AMERICA)
			.setLabel("Dominican Rep.")
			.setGovernmentInit(Province.Government.AUTOCRACY)
			.addAdjacency(Province.Id.HAITI)
			.addAdjacency(Province.Id.LESS_ANTILLES);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.LESS_ANTILLES)
			.setCulture(Culture.SPANISH)
			.setRegion(Province.Region.CENTRAL_AMERICA)
			.setLabel("Lesser Antilles")
			.addAdjacency(Province.Id.DOMINICAN_REP)
			.addAdjacency(Province.Id.VENEZUELA)
			.addAdjacency(Province.Id.GUYANA);
		
		// SOUTH AMERICA
		
		settings.addProvincesBuilder()
			.setId(Province.Id.COLOMBIA)
			.setCulture(Culture.SPANISH)
			.setRegion(Province.Region.SOUTH_AMERICA)
			.setLabel("Colombia")
			.addAdjacency(Province.Id.PANAMA)
			.addAdjacency(Province.Id.VENEZUELA)
			.addAdjacency(Province.Id.ECUADOR)
			.addAdjacency(Province.Id.PERU);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.ECUADOR)
			.setCulture(Culture.SPANISH)
			.setRegion(Province.Region.SOUTH_AMERICA)
			.setLabel("Ecuador")
			.setStabilityBase(2)
			.addAdjacency(Province.Id.COLOMBIA)
			.addAdjacency(Province.Id.PERU);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.PERU)
			.setCulture(Culture.SPANISH)
			.setRegion(Province.Region.SOUTH_AMERICA)
			.setLabel("Peru")
			.setStabilityBase(2)
			.setGovernmentInit(Province.Government.AUTOCRACY)
			.addAdjacency(Province.Id.ECUADOR)
			.addAdjacency(Province.Id.BOLIVIA)
			.addAdjacency(Province.Id.COLOMBIA)
			.addAdjacency(Province.Id.CHILE);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.CHILE)
			.setCulture(Culture.SPANISH)
			.setRegion(Province.Region.SOUTH_AMERICA)
			.setLabel("Chile")
			.setStabilityBase(2)
			.setGovernmentInit(Province.Government.DEMOCRACY)
			.addAdjacency(Province.Id.PERU)
			.addAdjacency(Province.Id.ARGENTINA)
			.addAdjacency(Province.Id.BOLIVIA);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.BOLIVIA)
			.setCulture(Culture.SPANISH)
			.setRegion(Province.Region.SOUTH_AMERICA)
			.setLabel("Bolivia")
			.setStabilityBase(2)
			.addAdjacency(Province.Id.PERU)
			.addAdjacency(Province.Id.ARGENTINA)
			.addAdjacency(Province.Id.CHILE);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.ARGENTINA)
			.setCulture(Culture.SPANISH)
			.setRegion(Province.Region.SOUTH_AMERICA)
			.setLabel("Argentina")
			.setStabilityBase(2)
			.setLeaderInit("Juan Perón")
			.addAdjacency(Province.Id.BOLIVIA)
			.addAdjacency(Province.Id.BRAZIL)
			.addAdjacency(Province.Id.CHILE);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.BRAZIL)
			.setCulture(Culture.PORTUGUESE)
			.setRegion(Province.Region.SOUTH_AMERICA)
			.setLabel("Brazil")
			.setStabilityBase(2)
			.addAdjacency(Province.Id.GUYANA)
			.addAdjacency(Province.Id.VENEZUELA)
			.addAdjacency(Province.Id.ARGENTINA);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.GUYANA)
			.setCulture(Culture.SPANISH)
			.setRegion(Province.Region.SOUTH_AMERICA)
			.setLabel("Guyana")
			.setGovernmentInit(Province.Government.COLONY)
			.setOccupierInit(Province.Id.GREAT_BRITAIN)
			.addAdjacency(Province.Id.VENEZUELA)
			.addAdjacency(Province.Id.BRAZIL)
			.addAdjacency(Province.Id.LESS_ANTILLES);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.VENEZUELA)
			.setCulture(Culture.SPANISH)
			.setRegion(Province.Region.SOUTH_AMERICA)
			.setLabel("Venezuela")
			.setStabilityBase(2)
			.addAdjacency(Province.Id.COLOMBIA)
			.addAdjacency(Province.Id.BRAZIL)
			.addAdjacency(Province.Id.GUYANA)
			.addAdjacency(Province.Id.LESS_ANTILLES);
	}

	public abstract void endTurn();
	public abstract MoveList getUSAMove();
	public abstract MoveList getUSSRMove();
	public abstract MoveBuilder getMoveBuilder();	
}