package com.berserkbentobox.coldwar.logic.mechanics.victory;

import com.berserkbentobox.coldwar.GameSettingsOuterClass.GameSettingsOrBuilder;
import com.berserkbentobox.coldwar.GameStateOuterClass.GameStateOrBuilder;
import com.berserkbentobox.coldwar.Victory.VictoryMechanicSettings;
import com.berserkbentobox.coldwar.Victory.VictoryMechanicState;
import com.berserkbentobox.coldwar.logic.Mechanic;
import com.berserkbentobox.coldwar.logic.Mechanics;
import com.berserkbentobox.coldwar.logic.Status;

public class VictoryMechanic extends Mechanic {
	
	public static class Settings {
		
		private GameSettingsOrBuilder gameSettings;
		private VictoryMechanicSettings settings;
		
		public Settings(GameSettingsOrBuilder gameSettings) {
			this.gameSettings = gameSettings;
			this.settings = gameSettings.getVictorySettings();
		}
		
		public Status validate() {
			return Status.OK;
		}
		
		public VictoryMechanicSettings getSettings() {
			return this.settings;
		}
		
		public VictoryMechanicState initialState() {
			VictoryMechanicState.Builder state = VictoryMechanicState.newBuilder();
			return state.build();
		}
	}
	
	private Settings settings;
	private VictoryMechanicState.Builder state;
	
	public VictoryMechanic(Mechanics mechanics, Settings settings, GameStateOrBuilder state) {
		super(mechanics);
		this.settings = settings;
		this.state = state.getVictoryState().toBuilder();
	}
		
	public Status validate() {
		return Status.OK;
	}
	
	public Settings getSettings() {
		return this.settings;
	}
	
	public VictoryMechanicState buildState() {
		return this.state.build();
	}

}
