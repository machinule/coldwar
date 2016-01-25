package com.berserkbentobox.coldwar.logic;

import java.util.concurrent.Future;

import com.berserkbentobox.coldwar.GameSettingsOuterClass.GameSettings;
import com.berserkbentobox.coldwar.GameStateOuterClass.Crisis;
import com.berserkbentobox.coldwar.GameStateOuterClass.GameState;
import com.berserkbentobox.coldwar.GameSettingsFactory;
import com.berserkbentobox.coldwar.Logger;
import com.berserkbentobox.coldwar.EventOuterClass.BerlinBlockadeEvent;
import com.berserkbentobox.coldwar.EventOuterClass.Event;
import com.berserkbentobox.coldwar.MoveOuterClass.MoveList;
import com.berserkbentobox.coldwar.logic.mechanics.Conflict;
import com.berserkbentobox.coldwar.logic.mechanics.Heat;
import com.berserkbentobox.coldwar.logic.mechanics.Leader;
import com.berserkbentobox.coldwar.logic.mechanics.Policy;
import com.berserkbentobox.coldwar.logic.mechanics.Province;
import com.berserkbentobox.coldwar.logic.mechanics.Superpower;
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
	protected MechanicSettings settings;
		
	public Boolean isWaitingOnPlayer() {
		return this.isWaitingOnPlayer;
	}
	
	public MechanicSettings getSettings() {
		return this.settings;
	}
	
	protected GameState.Builder getInitialGameState() {
		GameSettings settings = new GameSettingsFactory("game_settings").newGameSettings();
		this.settings = new MechanicSettings(settings);
		
		GameState.Builder state = GameState.newBuilder()
				.setSettings(settings)
				.setSuperpowerState(Superpower.buildInitialState(settings.getSuperpowerSettings()))
				.setPolicyState(Policy.buildInitialState(settings.getPolicySettings()))
				.setTreatyState(Treaty.buildInitialState(settings.getTreatySettings()))
				.setProvinceState(Province.buildInitialState(settings.getProvinceSettings()))
				.setConflictState(Conflict.buildInitialState(settings.getConflictSettings()))
				.setLeadersState(Leader.buildInitialState(settings.getLeaderSettings()))
				.setTurn(0);

		Heat initHeat = new Heat(settings);
		if (initHeat.validate().ok()) {
			state.setHeatState(initHeat.state());
		} else {
			Logger.Info("Initial heat state invalid.");
		}

		if (!this.settings.getTechnology().validate().ok()) {
			Logger.Err("Initial settings invalid.");			
		} else {
			state.setTechnologyState(this.settings.getTechnology().initialState());
		}

		if (!this.settings.getPseudorandom().validate().ok()) {
			Logger.Err("Initial settings invalid.");			
		} else {
			state.setPseudorandomState(this.settings.getPseudorandom().initialState());
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
		//Logger.Dbg("Initial game state: " + this.initialGameState);
		return state;
	}
	
	public Player getPlayer() {
		return this.player;
	}

	public void nextTurn() {
		Logger.Info("Proceeding to the next turn.");
		ComputedGameState computedState = new ComputedGameState(this.state, this.getUSAMove(), this.getUSSRMove(), this.settings);
		// Hack while a bunch of stuff is still in ComputedGameState:
		GameStateManager manager = new GameStateManager(this.settings, this.state);
		GameState managedGameState = manager.computeNextGameState(manager.computeDeterministicMechanics(this.getUSAMove(), this.getUSSRMove()));
		GameState.Builder nextGameState = computedState.nextState.toBuilder();
		nextGameState.setTechnologyState(managedGameState.getTechnologyState());
		nextGameState.setPseudorandomState(managedGameState.getPseudorandomState());
		
		this.state = nextGameState.build();
		for (String msg : ComputedGameState.getEventMessages(this.state, Player.USA)) {
			Logger.Info(msg);
		}		
	}

	public abstract Future<Boolean> endTurn();
	public abstract MoveList getUSAMove();
	public abstract MoveList getUSSRMove();
	public abstract MoveBuilder getMoveBuilder();	
}