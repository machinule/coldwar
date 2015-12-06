package coldwar.logic;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.CharBuffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.google.protobuf.TextFormat;

import coldwar.GameSettingsOuterClass.GameSettings;
import coldwar.GameStateOuterClass.GameState;
import coldwar.GameSettingsOuterClass.ProvinceSettings;
import coldwar.InfluenceStoreOuterClass.InfluenceStore;
import coldwar.LeaderOuterClass.LeaderList;
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
		populateLeaders(settings);
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
				.setLeader(p.getLeaderInit());
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
	
	protected void populateLeaders(GameSettings.Builder settings) {
        String file = "src/proto/Leaders.txt";
        String line = null;
        LeaderList.Builder leaderList = settings.getLeaders().newBuilder();
        
        Logger.Dbg("Reading leader file at " + file);
        String leaders = new String(Gdx.files.internal("Leaders.txt").readString());
        
        try {
        	TextFormat.merge(leaders, leaderList);
        }
        catch(TextFormat.ParseException ex) {
        	Logger.Err(ex.toString());
        }
    }

	protected void populateProvinces(GameSettings.Builder settings) {
		
		// SUPERPOWERS
		
		settings.addProvincesBuilder()
			.setId(Province.Id.USA)
			.setRegion(Province.Region.SUPERPOWERS)
			.setLabel("United States")
			.setGovernmentInit(Province.Government.DEMOCRACY)
			.addAdjacency(Province.Id.MEXICO)
			.addAdjacency(Province.Id.CUBA);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.USSR)
			.setRegion(Province.Region.SUPERPOWERS)
			.setLabel("Soviet Union")
			.setGovernmentInit(Province.Government.COMMUNISM);
		
		// CENTRAL AMERICA
		
		settings.addProvincesBuilder()
			.setId(Province.Id.MEXICO)
			.setCulture(Province.Culture.SPANISH)
			.setRegion(Province.Region.CENTRAL_AMERICA)
			.setLabel("Mexico")
			.setStabilityBase(2)
			.addAdjacency(Province.Id.USA)
			.addAdjacency(Province.Id.GUATEMALA)
			.addAdjacency(Province.Id.HONDURAS)
			.addAdjacency(Province.Id.CUBA);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.GUATEMALA)
			.setCulture(Province.Culture.SPANISH)
			.setRegion(Province.Region.CENTRAL_AMERICA)
			.setLabel("Guatemala")
			.addAdjacency(Province.Id.MEXICO)
			.addAdjacency(Province.Id.HONDURAS);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.HONDURAS)
			.setCulture(Province.Culture.SPANISH)
			.setRegion(Province.Region.CENTRAL_AMERICA)
			.setLabel("Honduras")
			.addAdjacency(Province.Id.MEXICO)
			.addAdjacency(Province.Id.GUATEMALA)
			.addAdjacency(Province.Id.NICARAGUA);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.NICARAGUA)
			.setCulture(Province.Culture.SPANISH)
			.setRegion(Province.Region.CENTRAL_AMERICA)
			.setLabel("Nicaragua")
			.setGovernmentInit(Province.Government.AUTOCRACY)
			.addAdjacency(Province.Id.CUBA)
			.addAdjacency(Province.Id.HONDURAS)
			.addAdjacency(Province.Id.PANAMA)
			.addAdjacency(Province.Id.COSTA_RICA);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.COSTA_RICA)
			.setCulture(Province.Culture.SPANISH)
			.setRegion(Province.Region.CENTRAL_AMERICA)
			.setLabel("Costa Rica")
			.setStabilityBase(2)
			.setGovernmentInit(Province.Government.DEMOCRACY)
			.addAdjacency(Province.Id.NICARAGUA)
			.addAdjacency(Province.Id.PANAMA);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.PANAMA)
			.setCulture(Province.Culture.SPANISH)
			.setRegion(Province.Region.CENTRAL_AMERICA)
			.setLabel("Panama")
			.setInfluenceInit(2)
			.setStabilityBase(2)
			.setMilitaryBaseInit(Province.Id.USA)
			.addAdjacency(Province.Id.NICARAGUA)
			.addAdjacency(Province.Id.COSTA_RICA)
			.addAdjacency(Province.Id.COLOMBIA);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.CUBA)
			.setCulture(Province.Culture.SPANISH)
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
			.setCulture(Province.Culture.FRENCH)
			.setRegion(Province.Region.CENTRAL_AMERICA)
			.setLabel("Haiti")
			.addAdjacency(Province.Id.CUBA)
			.addAdjacency(Province.Id.DOMINICAN_REP);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.DOMINICAN_REP)
			.setCulture(Province.Culture.SPANISH)
			.setRegion(Province.Region.CENTRAL_AMERICA)
			.setLabel("Dominican Rep.")
			.setGovernmentInit(Province.Government.AUTOCRACY)
			.addAdjacency(Province.Id.HAITI)
			.addAdjacency(Province.Id.LESS_ANTILLES);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.LESS_ANTILLES)
			.setCulture(Province.Culture.SPANISH)
			.setRegion(Province.Region.CENTRAL_AMERICA)
			.setLabel("Lesser Antilles")
			.addAdjacency(Province.Id.DOMINICAN_REP)
			.addAdjacency(Province.Id.VENEZUELA)
			.addAdjacency(Province.Id.GUYANA);
		
		// SOUTH AMERICA
		
		settings.addProvincesBuilder()
			.setId(Province.Id.COLOMBIA)
			.setCulture(Province.Culture.SPANISH)
			.setRegion(Province.Region.SOUTH_AMERICA)
			.setLabel("Colombia")
			.addAdjacency(Province.Id.PANAMA)
			.addAdjacency(Province.Id.VENEZUELA)
			.addAdjacency(Province.Id.ECUADOR)
			.addAdjacency(Province.Id.PERU);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.ECUADOR)
			.setCulture(Province.Culture.SPANISH)
			.setRegion(Province.Region.SOUTH_AMERICA)
			.setLabel("Ecuador")
			.setStabilityBase(2)
			.addAdjacency(Province.Id.COLOMBIA)
			.addAdjacency(Province.Id.PERU);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.PERU)
			.setCulture(Province.Culture.SPANISH)
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
			.setCulture(Province.Culture.SPANISH)
			.setRegion(Province.Region.SOUTH_AMERICA)
			.setLabel("Chile")
			.setStabilityBase(2)
			.setGovernmentInit(Province.Government.DEMOCRACY)
			.addAdjacency(Province.Id.PERU)
			.addAdjacency(Province.Id.ARGENTINA)
			.addAdjacency(Province.Id.BOLIVIA);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.BOLIVIA)
			.setCulture(Province.Culture.SPANISH)
			.setRegion(Province.Region.SOUTH_AMERICA)
			.setLabel("Bolivia")
			.setStabilityBase(2)
			.addAdjacency(Province.Id.PERU)
			.addAdjacency(Province.Id.ARGENTINA)
			.addAdjacency(Province.Id.CHILE);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.ARGENTINA)
			.setCulture(Province.Culture.SPANISH)
			.setRegion(Province.Region.SOUTH_AMERICA)
			.setLabel("Argentina")
			.setStabilityBase(2)
			.addAdjacency(Province.Id.BOLIVIA)
			.addAdjacency(Province.Id.BRAZIL)
			.addAdjacency(Province.Id.CHILE);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.BRAZIL)
			.setCulture(Province.Culture.PORTUGUESE)
			.setRegion(Province.Region.SOUTH_AMERICA)
			.setLabel("Brazil")
			.setStabilityBase(2)
			.addAdjacency(Province.Id.GUYANA)
			.addAdjacency(Province.Id.VENEZUELA)
			.addAdjacency(Province.Id.ARGENTINA);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.GUYANA)
			.setCulture(Province.Culture.SPANISH)
			.setRegion(Province.Region.SOUTH_AMERICA)
			.setLabel("Guyana")
			.setGovernmentInit(Province.Government.COLONY)
			.setOccupierInit(Province.Id.GREAT_BRITAIN)
			.addAdjacency(Province.Id.VENEZUELA)
			.addAdjacency(Province.Id.BRAZIL)
			.addAdjacency(Province.Id.LESS_ANTILLES);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.VENEZUELA)
			.setCulture(Province.Culture.SPANISH)
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