package com.berserkbentobox.coldwar.logic;

import java.util.concurrent.Future;

import com.berserkbentobox.coldwar.GameSettingsOuterClass.GameSettings;
import com.berserkbentobox.coldwar.GameStateOuterClass.GameState;
import com.berserkbentobox.coldwar.GameSettingsFactory;
import com.berserkbentobox.coldwar.Logger;
import com.berserkbentobox.coldwar.MoveOuterClass.MoveList;
import com.berserkbentobox.coldwar.logic.mechanics.Conflict;
import com.berserkbentobox.coldwar.logic.mechanics.Leader;
import com.berserkbentobox.coldwar.logic.mechanics.Policy;

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
				.setPolicyState(Policy.buildInitialState(settings.getPolicySettings()))
				.setConflictState(Conflict.buildInitialState(settings.getConflictSettings()))
				.setLeadersState(Leader.buildInitialState(settings.getLeaderSettings()))
				.setTurn(0);


		if (!this.settings.getProvinces().validate().ok()) {
			Logger.Err("Initial settings invalid.");			
		} else {
			state.setProvinceState(this.settings.getProvinces().initialState());
		}
		
		if (!this.settings.getHeat().validate().ok()) {
			Logger.Err("Initial settings invalid.");			
		} else {
			state.setHeatState(this.settings.getHeat().initialState());
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

		if (!this.settings.getSuperpower().validate().ok()) {
			Logger.Err("Initial settings invalid.");			
		} else {
			state.setSuperpowerState(this.settings.getSuperpower().initialState());
		}

		if (!this.settings.getTreaty().validate().ok()) {
			Logger.Err("Initial settings invalid.");			
		} else {
			state.setTreatyState(this.settings.getTreaty().initialState());
		}
		
		if (!this.settings.getCrisis().validate().ok()) {
			Logger.Err("Initial settings invalid.");			
		} else {
			state.setCrisisState(this.settings.getCrisis().initialState());
		}
		
		if (!this.settings.getYear().validate().ok()) {
			Logger.Err("Initial settings invalid.");			
		} else {
			state.setYearState(this.settings.getYear().initialState());
		}
		
		this.initialGameState = state.build();
		//Logger.Vrb("Initial game state: " + this.initialGameState);
		return state;
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	public boolean isReady() {
		if(getMoveBuilder().getMechanics().getSuperpower().isReady(this.getMoveBuilder().getYear(), this.getPlayer())) {
			return true;
		}
		return false;
	}
	
	public void nextTurn() {
		Logger.Info("Proceeding to the next turn.");
		GameStateManager manager = new GameStateManager(this.settings, this.state);
		Mechanics mechanics = manager.computeDeterministicMechanics(this.getUSAMove(), this.getUSSRMove());
		ComputedGameState computedState = new ComputedGameState(this.state, this.getUSAMove(), this.getUSSRMove(), this.settings, mechanics);
		// Hack while a bunch of stuff is still in ComputedGameState:
		GameState managedGameState = manager.computeNextGameState(mechanics);
		GameState.Builder nextGameState = computedState.nextState.toBuilder();
		nextGameState.setProvinceState(managedGameState.getProvinceState());
		nextGameState.setHeatState(managedGameState.getHeatState());
		nextGameState.setTechnologyState(managedGameState.getTechnologyState());
		nextGameState.setPseudorandomState(managedGameState.getPseudorandomState());
		nextGameState.setSuperpowerState(managedGameState.getSuperpowerState());
		nextGameState.setDeterrenceState(managedGameState.getDeterrenceState());
		nextGameState.setTreatyState(managedGameState.getTreatyState());
		nextGameState.setInfluenceState(managedGameState.getInfluenceState());
		nextGameState.setCrisisState(managedGameState.getCrisisState());
		nextGameState.setYearState(managedGameState.getYearState());
		
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