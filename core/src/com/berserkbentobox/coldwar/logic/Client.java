package com.berserkbentobox.coldwar.logic;

import java.util.concurrent.Future;

import com.berserkbentobox.coldwar.GameSettingsOuterClass.GameSettings;
import com.berserkbentobox.coldwar.GameStateOuterClass.Crisis;
import com.berserkbentobox.coldwar.GameStateOuterClass.GameState;
import com.berserkbentobox.coldwar.GameSettingsOuterClass.ProvinceSettings;
import com.berserkbentobox.coldwar.GameSettingsFactory;
import com.berserkbentobox.coldwar.Logger;
import com.berserkbentobox.coldwar.EventOuterClass.BerlinBlockadeEvent;
import com.berserkbentobox.coldwar.EventOuterClass.Event;
import com.berserkbentobox.coldwar.MoveOuterClass.MoveList;
import com.berserkbentobox.coldwar.logic.mechanics.Heat;
import com.berserkbentobox.coldwar.logic.mechanics.Policy;
import com.berserkbentobox.coldwar.logic.mechanics.Province;
import com.berserkbentobox.coldwar.logic.mechanics.Pseudorandom;
import com.berserkbentobox.coldwar.logic.mechanics.Superpower;
import com.berserkbentobox.coldwar.logic.mechanics.Technology;
import com.berserkbentobox.coldwar.logic.mechanics.Treaty;

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
		GameSettings settings = new GameSettingsFactory("game_settings").newGameSettings();
		GameState.Builder state = GameState.newBuilder()
				.setSettings(settings)
				.setSuperpowerState(Superpower.buildInitialState(settings.getSuperpowerSettings()))
				.setPseudorandomState(Pseudorandom.buildInitialState(settings.getPseudorandomSettings()))
				.setPolicyState(Policy.buildInitialState(settings.getPolicySettings()))
				.setTreatyState(Treaty.buildInitialState(settings.getTreatySettings()))
				.setTechnologyState(Technology.buildInitialState(settings.getTechnologySettings()))
				.setProvinceState(Province.buildInitialState(settings.getProvinceSettings()))
				.setTurn(0);

		Heat initHeat = new Heat(settings);
		if (initHeat.validate().ok()) {
			state.setHeatState(initHeat.state());
		} else {
			Logger.Info("Initial heat state invalid.");
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
		Logger.Dbg("Net party unity: " + computedState.getNetPartyUnity());
		Logger.Dbg("Net patriotism: " + computedState.getNetPatriotism());
		for (String msg : ComputedGameState.getEventMessages(this.state, Player.USA)) {
			Logger.Info(msg);
		}		
	}

	public abstract Future<Boolean> endTurn();
	public abstract MoveList getUSAMove();
	public abstract MoveList getUSSRMove();
	public abstract MoveBuilder getMoveBuilder();	
}