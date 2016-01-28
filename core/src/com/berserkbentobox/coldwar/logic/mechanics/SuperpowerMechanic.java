package com.berserkbentobox.coldwar.logic.mechanics;

import com.berserkbentobox.coldwar.GameSettingsOuterClass.GameSettingsOrBuilder;
import com.berserkbentobox.coldwar.GameStateOuterClass.GameStateOrBuilder;
import com.berserkbentobox.coldwar.Superpower.SuperpowerMechanicSettings;
import com.berserkbentobox.coldwar.Superpower.SuperpowerMechanicState;
import com.berserkbentobox.coldwar.logic.Status;

public class SuperpowerMechanic {

	public static class Settings {
		
		private GameSettingsOrBuilder gameSettings;
		private SuperpowerMechanicSettings settings;
		
		public Settings(GameSettingsOrBuilder gameSettings) {
			this.gameSettings = gameSettings;
			this.settings = gameSettings.getSuperpowerSettings();
		}
		public Status validate() {
			return Status.OK;
		}
		
		public SuperpowerMechanicState initialState() {
			SuperpowerMechanicState.Builder state = SuperpowerMechanicState.newBuilder();
			return state.build();
		}
	}
	
	private Settings settings;
	private SuperpowerMechanicState.Builder state;
	
	public SuperpowerMechanic(Settings settings, GameStateOrBuilder state) {
		this.settings = settings;
		this.state = state.getSuperpowerState().toBuilder();
	}
		
	public Status validate() {
		return Status.OK;
	}
	
	public Settings getSettings() {
		return this.settings;
	}
	
	public SuperpowerMechanicState buildState() {
		return this.state.build();
	}
}
