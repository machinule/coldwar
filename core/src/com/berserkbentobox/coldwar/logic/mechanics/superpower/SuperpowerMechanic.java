package com.berserkbentobox.coldwar.logic.mechanics.superpower;

import com.berserkbentobox.coldwar.GameSettingsOuterClass.GameSettingsOrBuilder;
import com.berserkbentobox.coldwar.GameStateOuterClass.GameStateOrBuilder;
import com.berserkbentobox.coldwar.Superpower.SuperpowerMechanicSettings;
import com.berserkbentobox.coldwar.Superpower.SuperpowerMechanicState;
import com.berserkbentobox.coldwar.Superpower.UsaState;
import com.berserkbentobox.coldwar.Superpower.UssrState;
import com.berserkbentobox.coldwar.logic.Status;

public class SuperpowerMechanic {

	public static class Settings {
		
		private GameSettingsOrBuilder gameSettings;
		private SuperpowerMechanicSettings settings;
		private Usa.Settings usaSettings;
		private Ussr.Settings ussrSettings;
		
		public Settings(GameSettingsOrBuilder gameSettings) {
			this.gameSettings = gameSettings;
			this.settings = gameSettings.getSuperpowerSettings();
		}
		public Status validate() {
			return Status.OK;
		}
		
		public Usa.Settings getUsaSettings() {
			return this.usaSettings;
		}
		
		public Ussr.Settings getUssrSettings() {
			return this.ussrSettings;
		}
		
		public SuperpowerMechanicState initialState() {
			SuperpowerMechanicState.Builder state = SuperpowerMechanicState.newBuilder();
			usaSettings = new Usa.Settings(this, settings.getUsaSettings());
			ussrSettings = new Ussr.Settings(this, settings.getUssrSettings());
			UsaState usaState = usaSettings.initialState();
			UssrState ussrState = ussrSettings.initialState();
			state.setUsaState(usaState);
			state.setUssrState(ussrState);
			return state.build();
		}
	}
	
	private Settings settings;
	private SuperpowerMechanicState.Builder state;
	private Usa usa;
	private Ussr ussr;
	
	public SuperpowerMechanic(Settings settings, GameStateOrBuilder state) {
		this.settings = settings;
		this.state = settings.initialState().toBuilder();
		this.usa = new Usa(this, this.getSettings().getUsaSettings(), this.state.getUsaStateBuilder());
		this.ussr = new Ussr(this, this.getSettings().getUssrSettings(), this.state.getUssrStateBuilder());
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
	
	// Getters
	
	public Usa getUsa() {
		return this.usa;
	}
	
	public Ussr getUssr() {
		return this.ussr;
	}
}
