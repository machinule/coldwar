package com.berserkbentobox.coldwar.logic;

import java.util.concurrent.Future;

import com.badlogic.gdx.Gdx;
import com.google.protobuf.TextFormat;

import com.berserkbentobox.coldwar.GameSettingsOuterClass.GameSettings;
import com.berserkbentobox.coldwar.GameStateOuterClass.Crisis;
import com.berserkbentobox.coldwar.GameStateOuterClass.GameState;
import com.berserkbentobox.coldwar.GameSettingsOuterClass.ProvinceSettings;
import com.berserkbentobox.coldwar.InfluenceStoreOuterClass.InfluenceStore;
import com.berserkbentobox.coldwar.LeaderOuterClass.Culture;
import com.berserkbentobox.coldwar.LeaderOuterClass.Leader;
import com.berserkbentobox.coldwar.Logger;
import com.berserkbentobox.coldwar.DissidentsOuterClass.Dissidents;
import com.berserkbentobox.coldwar.DissidentsOuterClass.Government;
import com.berserkbentobox.coldwar.EventOuterClass.BerlinBlockadeEvent;
import com.berserkbentobox.coldwar.EventOuterClass.Event;
import com.berserkbentobox.coldwar.MoveOuterClass.MoveList;
import com.berserkbentobox.coldwar.ProvinceOuterClass.Conflict;
import com.berserkbentobox.coldwar.ProvinceOuterClass.LeaderList;
import com.berserkbentobox.coldwar.ProvinceOuterClass.Province;
import com.berserkbentobox.coldwar.SovietUnionOuterClass.SovietUnion;
import com.berserkbentobox.coldwar.UnitedStatesOuterClass.UnitedStates;

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
	protected Boolean isWaitingOnPlayer = false;
		
	public Boolean isWaitingOnPlayer() {
		return this.isWaitingOnPlayer;
	}
	protected GameState.Builder getInitialGameState() {
		// Just use the defaults in the proto for now.
		GameSettings.Builder settings = GameSettings.newBuilder();
		if(!populateLeaders(settings)) return null;
		if(!populateTechs(settings)) return null;
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
						.build())
				.setTechs(settings.getTechInit());
		for (ProvinceSettings p : settings.getProvincesList()) {
			Province.Builder builder = state.addProvincesBuilder()
				.setId(p.getId())
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
				Logger.Dbg("Leader init - " + p.getLeaderInit());
				builder.setLeader(getLeader(settings, p.getLeaderInit()));
			}
			if (p.hasDissidentsInit()) {
				builder.setDissidents(p.getDissidentsInit());
			}
			if (p.hasConflictInit()) {
				builder.setConflict(p.getConflictInit());
			}
		}
		
		//Berlin Blockade
		Crisis.Builder c = Crisis.newBuilder();
		c.setBerlinBlockade(true);
		c.setInfo("Blockade of Berlin");
		c.setUsaOption1("Begin the Berlin Airlift");
		c.setUssrOption1("End the Blockade");
		state.setCrises(c.build());
		
		state.getTurnLogBuilder()
		.addEvents(Event.newBuilder()
			.setBerlinBlockadeEvent(BerlinBlockadeEvent.newBuilder()
				.build())
			.build());
		
		this.initialGameState = state.build();
		Logger.Dbg("Initial game state: " + this.initialGameState);
		return state;
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	protected Leader getLeader(GameSettings.Builder settings, String name) {
		for(LeaderList leadList : settings.getCandidatesList()) {
			for(Leader l : leadList.getLeadersList()) {
				if (l.getName().equals(name)) {
					Logger.Dbg("Setting leader " + l.getName());
					return l;
				}
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
		for (String msg : ComputedGameState.getEventMessages(this.state, Player.USA)) {
			Logger.Info(msg);
		}		
	}
	
	protected boolean populateLeaders(GameSettings.Builder settings) {
        Logger.Dbg("Reading leader file");
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

	protected boolean populateTechs(GameSettings.Builder settings) {
        Logger.Dbg("Reading technology file");
        String input = new String(Gdx.files.internal("Techs.txt").readString());
        
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
			.setGovernmentInit(Government.DEMOCRACY)
			.addAdjacency(Province.Id.MEXICO)
			.addAdjacency(Province.Id.CUBA);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.USSR)
			.setRegion(Province.Region.SUPERPOWERS)
			.setLabel("Soviet Union")
			.setInfluenceInit(-1)
			.setGovernmentInit(Government.COMMUNISM)
			.addAdjacency(Province.Id.ROMANIA)
			.addAdjacency(Province.Id.POLAND)
			.addAdjacency(Province.Id.FINLAND)
			.addAdjacency(Province.Id.TURKEY);
			//.addAdjacency(Province.Id.NORTH_KOREA);
		
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
			.addAdjacency(Province.Id.HONDURAS)
			.addAdjacency(Province.Id.EL_SALVADOR);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.EL_SALVADOR)
			.setCulture(Culture.SPANISH)
			.setRegion(Province.Region.CENTRAL_AMERICA)
			.setLabel("El Salvador")
			.addAdjacency(Province.Id.GUATEMALA)
			.addAdjacency(Province.Id.HONDURAS);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.HONDURAS)
			.setCulture(Culture.SPANISH)
			.setRegion(Province.Region.CENTRAL_AMERICA)
			.setLabel("Honduras")
			.addAdjacency(Province.Id.MEXICO)
			.addAdjacency(Province.Id.GUATEMALA)
			.addAdjacency(Province.Id.NICARAGUA)
			.addAdjacency(Province.Id.EL_SALVADOR);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.NICARAGUA)
			.setCulture(Culture.SPANISH)
			.setRegion(Province.Region.CENTRAL_AMERICA)
			.setLabel("Nicaragua")
			.setInfluenceInit(1)
			.setGovernmentInit(Government.AUTOCRACY)
			.setLeaderInit("Anastasio Somoza Garcia")
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
			.setGovernmentInit(Government.DEMOCRACY)
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
			.setDissidentsInit(Dissidents.newBuilder()
				.setGov(Government.COMMUNISM)
				.setLeaderInit("Fidel Castro"))
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
			.setLeaderInit("Rafael Trujillo")
			.setGovernmentInit(Government.AUTOCRACY)
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
			.setGovernmentInit(Government.AUTOCRACY)
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
			.setGovernmentInit(Government.DEMOCRACY)
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
			.setLeaderInit("Juan Peron")
			.addAdjacency(Province.Id.BOLIVIA)
			.addAdjacency(Province.Id.URUGUAY)
			.addAdjacency(Province.Id.BRAZIL)
			.addAdjacency(Province.Id.CHILE);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.URUGUAY)
			.setCulture(Culture.SPANISH)
			.setRegion(Province.Region.SOUTH_AMERICA)
			.setLabel("Uruguay")
			.setStabilityBase(2)
			.addAdjacency(Province.Id.ARGENTINA)
			.addAdjacency(Province.Id.BRAZIL);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.BRAZIL)
			.setCulture(Culture.PORTUGUESE)
			.setRegion(Province.Region.SOUTH_AMERICA)
			.setLabel("Brazil")
			.setStabilityBase(2)
			.addAdjacency(Province.Id.URUGUAY)
			.addAdjacency(Province.Id.VENEZUELA)
			.addAdjacency(Province.Id.ARGENTINA)
			.addAdjacency(Province.Id.GUYANA);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.VENEZUELA)
			.setCulture(Culture.SPANISH)
			.setRegion(Province.Region.SOUTH_AMERICA)
			.setLabel("Venezuela")
			.setStabilityBase(2)
			.addAdjacency(Province.Id.COLOMBIA)
			.addAdjacency(Province.Id.BRAZIL)
			.addAdjacency(Province.Id.LESS_ANTILLES)
			.addAdjacency(Province.Id.GUYANA);

		settings.addProvincesBuilder()
			.setId(Province.Id.GUYANA)
			.setCulture(Culture.SPANISH)
			.setRegion(Province.Region.SOUTH_AMERICA)
			.setLabel("Guyana")
			.addAdjacency(Province.Id.VENEZUELA)
			.addAdjacency(Province.Id.BRAZIL)
			.addAdjacency(Province.Id.LESS_ANTILLES);
		
		// WESTERN EUROPE
		
		settings.addProvincesBuilder()
		.setId(Province.Id.CANADA)
			.setCulture(Culture.ENGLISH)
			.setRegion(Province.Region.WESTERN_EUROPE)
			.setLabel("Canada")
			.setStabilityBase(3)
			.setInfluenceInit(1)
			.setGovernmentInit(Government.DEMOCRACY)
			.addAdjacency(Province.Id.USA)
			.addAdjacency(Province.Id.GREAT_BRITAIN);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.GREAT_BRITAIN)
			.setCulture(Culture.ENGLISH)
			.setRegion(Province.Region.WESTERN_EUROPE)
			.setLabel("United Kingdom")
			.setStabilityBase(3)
			.setInfluenceInit(2)
			.setGovernmentInit(Government.DEMOCRACY)
			.addAdjacency(Province.Id.FRANCE)
			.addAdjacency(Province.Id.CANADA)
			.addAdjacency(Province.Id.BENELUX)
			.addAdjacency(Province.Id.NORWAY);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.FRANCE)
			.setCulture(Culture.FRENCH)
			.setRegion(Province.Region.WESTERN_EUROPE)
			.setLabel("France")
			.setStabilityBase(3)
			.setGovernmentInit(Government.DEMOCRACY)
			.addAdjacency(Province.Id.WEST_GERMANY)
			.addAdjacency(Province.Id.GREAT_BRITAIN)
			.addAdjacency(Province.Id.SPAIN)
			//.addAdjacency(Province.Id.ALGERIA)
			.addAdjacency(Province.Id.ITALY);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.WEST_GERMANY)
			.setCulture(Culture.GERMAN)
			.setRegion(Province.Region.WESTERN_EUROPE)
			.setLabel("West Germany")
			.setStabilityBase(3)
			.setGovernmentInit(Government.OCCUPIED)
			.setOccupierInit(Province.Id.USA)
			.addAdjacency(Province.Id.FRANCE)
			.addAdjacency(Province.Id.EAST_GERMANY)
			.addAdjacency(Province.Id.BENELUX)
			.addAdjacency(Province.Id.DENMARK);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.SPAIN)
			.setCulture(Culture.SPANISH)
			.setRegion(Province.Region.WESTERN_EUROPE)
			.setLabel("Spain")
			.setStabilityBase(2)
			//.setLeaderInit("Francisco Franco")
			.setGovernmentInit(Government.AUTOCRACY)
			.setOccupierInit(Province.Id.USA)
			//.addAdjacency(Province.Id.MOROCCO)
			.addAdjacency(Province.Id.PORTUGAL)
			.addAdjacency(Province.Id.FRANCE);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.PORTUGAL)
			.setCulture(Culture.PORTUGUESE)
			.setRegion(Province.Region.WESTERN_EUROPE)
			.setLabel("Portugal")
			.setStabilityBase(2)
			//.setLeaderInit("Ant�nio de Oliveira Salazar")
			.setGovernmentInit(Government.AUTOCRACY)
			.setOccupierInit(Province.Id.USA)
			//.addAdjacency(Province.Id.MOROCCO)
			.addAdjacency(Province.Id.SPAIN);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.BENELUX)
			.setCulture(Culture.FRENCH) // TODO: Dutch
			.setRegion(Province.Region.WESTERN_EUROPE)
			.setLabel("Low Countries")
			.setStabilityBase(3)
			.setGovernmentInit(Government.AUTOCRACY)
			.addAdjacency(Province.Id.WEST_GERMANY)
			.addAdjacency(Province.Id.GREAT_BRITAIN);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.DENMARK)
			.setCulture(Culture.GERMAN) // TODO: Culture
			.setRegion(Province.Region.WESTERN_EUROPE)
			.setLabel("Denmark")
			.setStabilityBase(3)
			.setGovernmentInit(Government.DEMOCRACY)
			.addAdjacency(Province.Id.WEST_GERMANY)
			.addAdjacency(Province.Id.NORWAY)
			.addAdjacency(Province.Id.SWEDEN);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.NORWAY)
			.setCulture(Culture.GERMAN) // TODO: Culture
			.setRegion(Province.Region.WESTERN_EUROPE)
			.setLabel("Norway")
			.setStabilityBase(3)
			.setGovernmentInit(Government.DEMOCRACY)
			.addAdjacency(Province.Id.DENMARK)
			.addAdjacency(Province.Id.SWEDEN)
			.addAdjacency(Province.Id.GREAT_BRITAIN);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.ITALY)
			//.setCulture(Culture.ITALIAN)
			.setRegion(Province.Region.WESTERN_EUROPE)
			.setLabel("Italy")
			.setStabilityBase(3)
			.addAdjacency(Province.Id.FRANCE)
			//.addAdjacency(Province.Id.LIBYA)
			.addAdjacency(Province.Id.YUGOSLAVIA);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.SWEDEN)
			.setCulture(Culture.GERMAN) // TODO: Culture
			.setRegion(Province.Region.WESTERN_EUROPE)
			.setLabel("Sweden")
			.setStabilityBase(3)
			.setGovernmentInit(Government.DEMOCRACY)
			.addAdjacency(Province.Id.DENMARK)
			.addAdjacency(Province.Id.NORWAY)
			.addAdjacency(Province.Id.FINLAND);
		
		// EASTERN EUROPE
		
		settings.addProvincesBuilder()
			.setId(Province.Id.EAST_GERMANY)
			.setCulture(Culture.GERMAN)
			.setRegion(Province.Region.EASTERN_EUROPE)
			.setLabel("East Germany")
			.setStabilityBase(3)
			.setGovernmentInit(Government.OCCUPIED)
			.setOccupierInit(Province.Id.USSR)
			.addAdjacency(Province.Id.WEST_GERMANY)
			.addAdjacency(Province.Id.CZECHOSLOVAKIA)
			.addAdjacency(Province.Id.POLAND);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.POLAND)
			//.setCulture(Culture.POLISH)
			.setRegion(Province.Region.EASTERN_EUROPE)
			.setLabel("Poland")
			.setStabilityBase(3)
			.setInfluenceInit(-1)
			.setGovernmentInit(Government.COMMUNISM)
			.addAdjacency(Province.Id.WEST_GERMANY)
			.addAdjacency(Province.Id.CZECHOSLOVAKIA)
			.addAdjacency(Province.Id.USSR);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.HUNGARY)
			//.setCulture(Culture.HUNGARIAN)
			.setRegion(Province.Region.EASTERN_EUROPE)
			.setLabel("Hungary")
			.setStabilityBase(2)
			.setInfluenceInit(-1)
			.setGovernmentInit(Government.COMMUNISM)
			.addAdjacency(Province.Id.ROMANIA)
			.addAdjacency(Province.Id.CZECHOSLOVAKIA);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.CZECHOSLOVAKIA)
			//.setCulture(Culture.CZECH)
			.setRegion(Province.Region.EASTERN_EUROPE)
			.setLabel("Czechoslovakia")
			.setStabilityBase(2)
			.setInfluenceInit(-1)
			.setGovernmentInit(Government.COMMUNISM)
			.addAdjacency(Province.Id.EAST_GERMANY)
			.addAdjacency(Province.Id.POLAND)
			.addAdjacency(Province.Id.HUNGARY);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.ROMANIA)
			//.setCulture(Culture.ROMANIAN)
			.setRegion(Province.Region.EASTERN_EUROPE)
			.setLabel("Romania")
			.setStabilityBase(2)
			.setInfluenceInit(-1)
			.setGovernmentInit(Government.COMMUNISM)
			.addAdjacency(Province.Id.USSR)
			.addAdjacency(Province.Id.HUNGARY)
			.addAdjacency(Province.Id.BULGARIA);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.BULGARIA)
			//.setCulture(Culture.BULGARIAN)
			.setRegion(Province.Region.EASTERN_EUROPE)
			.setLabel("Bulgaria")
			.setStabilityBase(2)
			.setInfluenceInit(-1)
			.setGovernmentInit(Government.COMMUNISM)
			.addAdjacency(Province.Id.TURKEY)
			.addAdjacency(Province.Id.ROMANIA);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.YUGOSLAVIA)
			//.setCulture(Culture.YUGOSLAV)
			.setRegion(Province.Region.EASTERN_EUROPE)
			.setLabel("Yugoslavia")
			.setStabilityBase(2)
			//.setLeaderInit("Josip Broz Tito")
			.setGovernmentInit(Government.COMMUNISM)
			.addAdjacency(Province.Id.ITALY)
			.addAdjacency(Province.Id.GREECE);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.GREECE)
			//.setCulture(Culture.GREEK)
			.setRegion(Province.Region.EASTERN_EUROPE)
			.setLabel("Greece")
			.setStabilityBase(2)
			.setGovernmentInit(Government.DEMOCRACY)
			.setConflictInit(Conflict.newBuilder()
				.setActive(true)
				.setLength(1)
				.setGoal(3)
				.setDefenderProgress(2)
				.setDefenderSupporter(Province.Id.USA)
				.setDefChanceMod(100000)
				.setRebels(Dissidents.newBuilder()
						.setGov(Government.COMMUNISM)
						.build())
				.setName("Greek Civil War")
				.build())
			.addAdjacency(Province.Id.YUGOSLAVIA)
			.addAdjacency(Province.Id.TURKEY);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.TURKEY)
			//.setCulture(Culture.TURKISH)
			.setRegion(Province.Region.EASTERN_EUROPE)
			.setLabel("Turkey")
			.setStabilityBase(2)
			.addAdjacency(Province.Id.USSR)
			.addAdjacency(Province.Id.BULGARIA)
			//.addAdjacency(Province.Id.SYRIA)
			.addAdjacency(Province.Id.GREECE);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.FINLAND)
			//.setCulture(Culture.FINNISH)
			.setRegion(Province.Region.EASTERN_EUROPE)
			.setLabel("Finland")
			.setStabilityBase(3)
			.setGovernmentInit(Government.DEMOCRACY)
			.addAdjacency(Province.Id.USSR)
			.addAdjacency(Province.Id.SWEDEN);
		
		// TODO: adjust settings after this point.
		settings.addProvincesBuilder()
			.setId(Province.Id.LIBYA)
			.setRegion(Province.Region.MIDDLE_EAST)
			.setLabel("Libya")
			.setStabilityBase(3000)
			.setGovernmentInit(Government.DEMOCRACY);

		settings.addProvincesBuilder()
			.setId(Province.Id.EGYPT)
			.setRegion(Province.Region.MIDDLE_EAST)
			.setLabel("Egypt")
			.setStabilityBase(3000)
			.setGovernmentInit(Government.DEMOCRACY);

		settings.addProvincesBuilder()
			.setId(Province.Id.ISRAEL)
			.setRegion(Province.Region.MIDDLE_EAST)
			.setLabel("Israel")
			.setStabilityBase(3000)
			.setGovernmentInit(Government.DEMOCRACY);

		settings.addProvincesBuilder()
			.setId(Province.Id.SYRIA)
			.setRegion(Province.Region.MIDDLE_EAST)
			.setLabel("Syria")
			.setStabilityBase(3000)
			.setGovernmentInit(Government.DEMOCRACY);

		settings.addProvincesBuilder()
			.setId(Province.Id.IRAQ)
			.setRegion(Province.Region.MIDDLE_EAST)
			.setLabel("Iraq")
			.setStabilityBase(3000)
			.setGovernmentInit(Government.DEMOCRACY);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.IRAN)
			.setRegion(Province.Region.MIDDLE_EAST)
			.setLabel("Iran")
			.setStabilityBase(3000)
			.setGovernmentInit(Government.DEMOCRACY);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.GULF_STATES)
			.setRegion(Province.Region.MIDDLE_EAST)
			.setLabel("Gulf States")
			.setStabilityBase(3000)
			.setGovernmentInit(Government.DEMOCRACY);

		settings.addProvincesBuilder()
			.setId(Province.Id.SAUDI_ARABIA)
			.setRegion(Province.Region.MIDDLE_EAST)
			.setLabel("Saudi Arabia")
			.setStabilityBase(3000)
			.setGovernmentInit(Government.DEMOCRACY);

		settings.addProvincesBuilder()
			.setId(Province.Id.YEMEN)
			.setRegion(Province.Region.MIDDLE_EAST)
			.setLabel("Yemen")
			.setStabilityBase(3000)
			.setGovernmentInit(Government.DEMOCRACY);

		settings.addProvincesBuilder()
			.setId(Province.Id.JORDAN)
			.setRegion(Province.Region.MIDDLE_EAST)
			.setLabel("Jordan")
			.setStabilityBase(3000)
			.setGovernmentInit(Government.DEMOCRACY);
		
		// Africa
		settings.addProvincesBuilder()
			.setId(Province.Id.MOROCCO)
			.setRegion(Province.Region.AFRICA)
			.setLabel("Morocco")
			.setStabilityBase(3000)
			.setGovernmentInit(Government.DEMOCRACY);

		settings.addProvincesBuilder()
			.setId(Province.Id.IVORY_GOLD_COAST)
			.setRegion(Province.Region.AFRICA)
			.setLabel("Ivory/Gold Coast")
			.setStabilityBase(3000)
			.setGovernmentInit(Government.DEMOCRACY);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.NIGERIA)
			.setRegion(Province.Region.AFRICA)
			.setLabel("Nigeria")
			.setStabilityBase(3000)
			.setGovernmentInit(Government.DEMOCRACY);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.ALGERIA)
			.setRegion(Province.Region.AFRICA)
			.setLabel("Algeria")
			.setStabilityBase(3000)
			.setGovernmentInit(Government.DEMOCRACY);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.WEST_AFRICA)
			.setRegion(Province.Region.AFRICA)
			.setLabel("West Africa")
			.setStabilityBase(3000)
			.setGovernmentInit(Government.DEMOCRACY);

		settings.addProvincesBuilder()
			.setId(Province.Id.ZAIRE)
			.setRegion(Province.Region.AFRICA)
			.setLabel("Zaire")
			.setStabilityBase(3000)
			.setGovernmentInit(Government.DEMOCRACY);

		settings.addProvincesBuilder()
			.setId(Province.Id.ANGOLA)
			.setRegion(Province.Region.AFRICA)
			.setLabel("Angola")
			.setStabilityBase(3000)
			.setGovernmentInit(Government.DEMOCRACY);

		settings.addProvincesBuilder()
			.setId(Province.Id.SOUTH_AFRICA)
			.setRegion(Province.Region.AFRICA)
			.setLabel("South Africa")
			.setStabilityBase(3000)
			.setGovernmentInit(Government.DEMOCRACY);

		settings.addProvincesBuilder()
			.setId(Province.Id.MOZAMBIQUE)
			.setRegion(Province.Region.AFRICA)
			.setLabel("Mozambique")
			.setStabilityBase(3000)
			.setGovernmentInit(Government.DEMOCRACY);
		
		settings.addProvincesBuilder()
			.setId(Province.Id.EAST_AFRICA)
			.setRegion(Province.Region.AFRICA)
			.setLabel("East Africa")
			.setStabilityBase(3000)
			.setGovernmentInit(Government.DEMOCRACY);

		settings.addProvincesBuilder()
			.setId(Province.Id.ETHIOPIA)
			.setRegion(Province.Region.AFRICA)
			.setLabel("Ethiopia")
			.setStabilityBase(3000)
			.setGovernmentInit(Government.DEMOCRACY);
		
		// East Asia
		settings.addProvincesBuilder()
			.setId(Province.Id.CHINA)
			.setRegion(Province.Region.EAST_ASIA)
			.setLabel("China")
			.setStabilityBase(3000)
			.setGovernmentInit(Government.DEMOCRACY);

		settings.addProvincesBuilder()
			.setId(Province.Id.NORTH_KOREA)
			.setRegion(Province.Region.EAST_ASIA)
			.setLabel("Best Korea")
			.setStabilityBase(3000)
			.setGovernmentInit(Government.DEMOCRACY);

		settings.addProvincesBuilder()
			.setId(Province.Id.SOUTH_KOREA)
			.setRegion(Province.Region.EAST_ASIA)
			.setLabel("South Korea")
			.setStabilityBase(3000)
			.setGovernmentInit(Government.DEMOCRACY);

		settings.addProvincesBuilder()
			.setId(Province.Id.JAPAN)
			.setRegion(Province.Region.EAST_ASIA)
			.setLabel("Japan")
			.setStabilityBase(3000)
			.setGovernmentInit(Government.DEMOCRACY);

		settings.addProvincesBuilder()
			.setId(Province.Id.TAIWAN)
			.setRegion(Province.Region.EAST_ASIA)
			.setLabel("Taiwan")
			.setStabilityBase(3000)
			.setGovernmentInit(Government.DEMOCRACY);

		settings.addProvincesBuilder()
			.setId(Province.Id.PHILIPPINES)
			.setRegion(Province.Region.EAST_ASIA)
			.setLabel("Philippines")
			.setStabilityBase(3000)
			.setGovernmentInit(Government.DEMOCRACY);

		settings.addProvincesBuilder()
			.setId(Province.Id.VIETNAM)
			.setRegion(Province.Region.EAST_ASIA)
			.setLabel("Vietnam")
			.setStabilityBase(3000)
			.setGovernmentInit(Government.DEMOCRACY);

		settings.addProvincesBuilder()
			.setId(Province.Id.LAOS)
			.setRegion(Province.Region.EAST_ASIA)
			.setLabel("Laos")
			.setStabilityBase(3000)
			.setGovernmentInit(Government.DEMOCRACY);

		settings.addProvincesBuilder()
			.setId(Province.Id.CAMBODIA)
			.setRegion(Province.Region.EAST_ASIA)
			.setLabel("Cambodia")
			.setStabilityBase(3000)
			.setGovernmentInit(Government.DEMOCRACY);

		settings.addProvincesBuilder()
			.setId(Province.Id.AFGHANISTAN)
			.setRegion(Province.Region.SOUTH_ASIA)
			.setLabel("Afghanistan")
			.setStabilityBase(3000)
			.setGovernmentInit(Government.DEMOCRACY);

		settings.addProvincesBuilder()
			.setId(Province.Id.PAKISTAN)
			.setRegion(Province.Region.EAST_ASIA)
			.setLabel("Pakistan")
			.setStabilityBase(3000)
			.setGovernmentInit(Government.DEMOCRACY);

		settings.addProvincesBuilder()
			.setId(Province.Id.BANGLADESH)
			.setRegion(Province.Region.EAST_ASIA)
			.setLabel("Bangladesh")
			.setStabilityBase(3000)
			.setGovernmentInit(Government.DEMOCRACY);

		settings.addProvincesBuilder()
			.setId(Province.Id.INDIA)
			.setRegion(Province.Region.EAST_ASIA)
			.setLabel("India")
			.setStabilityBase(3000)
			.setGovernmentInit(Government.DEMOCRACY);

		settings.addProvincesBuilder()
			.setId(Province.Id.BURMA)
			.setRegion(Province.Region.EAST_ASIA)
			.setLabel("Burma")
			.setStabilityBase(3000)
			.setGovernmentInit(Government.DEMOCRACY);

		settings.addProvincesBuilder()
			.setId(Province.Id.THAILAND)
			.setRegion(Province.Region.EAST_ASIA)
			.setLabel("Thailand")
			.setStabilityBase(3000)
			.setGovernmentInit(Government.DEMOCRACY);

		settings.addProvincesBuilder()
			.setId(Province.Id.MALAYSIA)
			.setRegion(Province.Region.EAST_ASIA)
			.setLabel("Mayalisa")
			.setStabilityBase(3000)
			.setGovernmentInit(Government.DEMOCRACY);

		settings.addProvincesBuilder()
			.setId(Province.Id.INDONESIA)
			.setRegion(Province.Region.EAST_ASIA)
			.setLabel("Indonesia")
			.setStabilityBase(3000)
			.setGovernmentInit(Government.DEMOCRACY);

		settings.addProvincesBuilder()
			.setId(Province.Id.AUSTRALIA)
			.setRegion(Province.Region.EAST_ASIA)
			.setLabel("Australia")
			.setStabilityBase(3000)
			.setGovernmentInit(Government.DEMOCRACY);

	}

	public abstract Future<Boolean> endTurn();
	public abstract MoveList getUSAMove();
	public abstract MoveList getUSSRMove();
	public abstract MoveBuilder getMoveBuilder();	
}