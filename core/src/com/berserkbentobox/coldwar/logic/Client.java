package com.berserkbentobox.coldwar.logic;

import java.util.concurrent.Future;

import com.badlogic.gdx.Gdx;
import com.google.protobuf.TextFormat;

import com.berserkbentobox.coldwar.GameSettingsOuterClass.GameSettings;
import com.berserkbentobox.coldwar.GameStateOuterClass.Crisis;
import com.berserkbentobox.coldwar.GameStateOuterClass.GameState;
import com.berserkbentobox.coldwar.GameSettingsOuterClass.ProvinceSettings;
import com.berserkbentobox.coldwar.GameSettingsOuterClass.SingleProvinceSettings;
import com.berserkbentobox.coldwar.LeaderOuterClass.Leader;
import com.berserkbentobox.coldwar.GameSettingsFactory;
import com.berserkbentobox.coldwar.Logger;
import com.berserkbentobox.coldwar.DissidentsOuterClass.Dissidents;
import com.berserkbentobox.coldwar.DissidentsOuterClass.Government;
import com.berserkbentobox.coldwar.EventOuterClass.BerlinBlockadeEvent;
import com.berserkbentobox.coldwar.EventOuterClass.Event;
import com.berserkbentobox.coldwar.MoveOuterClass.MoveList;
import com.berserkbentobox.coldwar.ProvinceOuterClass.Conflict;
import com.berserkbentobox.coldwar.ProvinceOuterClass.Province;

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
		GameSettings settings = new GameSettingsFactory("game_settings").New();
		
		GameState.Builder state = GameState.newBuilder()
				.setSettings(settings)
				.setHeat(settings.getHeatSettings().getHeatInit())
				.setSeed(settings.getSoftwareSettings().getSeedInit())
				.setTurn(0);
//				.setUsa(UnitedStates.newBuilder()
//						.setInfluenceStore(InfluenceStore.newBuilder()
//								.setPolitical(settings.getUsaPolStoreInit())
//								.setMilitary(settings.getUsaMilStoreInit())
//								.setCovert(settings.getUsaCovStoreInit())
//								.build())
//						.setPatriotism(settings.getUsaInitPatriotism())
//						.build())
//				.setUssr(SovietUnion.newBuilder()
//						.setInfluenceStore(InfluenceStore.newBuilder()
//								.setPolitical(settings.getUssrPolStoreInit())
//								.setMilitary(settings.getUssrMilStoreInit())
//								.setCovert(settings.getUssrCovStoreInit())
//								.build())
//						.setPartyUnity(settings.getUssrInitPartyUnity())
//						.build())
//				.setTechs(settings.getTechInit());
		for (SingleProvinceSettings p : settings.getProvinceSettings().getProvinceList()) {
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
//				builder.setLeader(getLeader(settings, p.getLeaderInit()));
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
	
//	protected Leader getLeader(GameSettings.Builder settings, String name) {
//		for(LeaderList leadList : settings.getCandidatesList()) {
//			for(Leader l : leadList.getLeadersList()) {
//				if (l.getName().equals(name)) {
//					Logger.Dbg("Setting leader " + l.getName());
//					return l;
//				}
//			}
//		}
//		return null;
//	}
//	
	public void nextTurn() {
		Logger.Info("Proceeding to the next turn.");
		ComputedGameState computedState = new ComputedGameState(this.state, this.getUSAMove(), this.getUSSRMove());
		this.state = computedState.nextState;
		Logger.Info("Next game state: " + this.state.toString());
//		Logger.Dbg("Net party unity: " + computedState.getNetPartyUnity());
//		Logger.Dbg("Net patriotism: " + computedState.getNetPatriotism());
		for (String msg : ComputedGameState.getEventMessages(this.state, Player.USA)) {
			Logger.Info(msg);
		}		
	}

	public abstract Future<Boolean> endTurn();
	public abstract MoveList getUSAMove();
	public abstract MoveList getUSSRMove();
	public abstract MoveBuilder getMoveBuilder();	
}