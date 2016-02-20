package com.berserkbentobox.coldwar.logic.mechanics.heat;

import com.berserkbentobox.coldwar.GameSettingsOuterClass.GameSettingsOrBuilder;
import com.berserkbentobox.coldwar.GameStateOuterClass.GameStateOrBuilder;
import com.berserkbentobox.coldwar.Heat.HeatMechanicSettings;
import com.berserkbentobox.coldwar.Heat.HeatMechanicState;
import com.berserkbentobox.coldwar.logic.Mechanic;
import com.berserkbentobox.coldwar.logic.Mechanics;
import com.berserkbentobox.coldwar.logic.Status;

public class HeatMechanic extends Mechanic {
	public static class Settings {
		
		private GameSettingsOrBuilder gameSettings;
		private HeatMechanicSettings settings;
		
		public Settings(GameSettingsOrBuilder gameSettings) {
			this.gameSettings = gameSettings;
			this.settings = gameSettings.getHeatSettings();
		}
		
		public Status validate() {
			return Status.OK;
		}
		
		public HeatMechanicSettings getSettings() {
			return this.settings;
		}
		
		public HeatMechanicState initialState() {
			HeatMechanicState.Builder state = HeatMechanicState.newBuilder();
			state.setHeat(this.settings.getInitHeat());
			return state.build();
		}
	}
	
	private Settings settings;
	private HeatMechanicState.Builder state;
	
	public HeatMechanic(Mechanics mechanics, Settings settings, GameStateOrBuilder state) {
		super(mechanics);
		this.settings = settings;
		this.state = state.getHeatState().toBuilder();
	}
		
	public Status validate() {
		return Status.OK;
	}
	
	public Settings getSettings() {
		return this.settings;
	}
	
	public HeatMechanicState buildState() {
		return this.state.build();
	}
	
	public synchronized void increase(int i) {
		this.state.setHeat(this.state.getHeat() + i);
	}
	
	public void decrease(int i) {
		increase(-i);
	}
	
	public synchronized void normalize() {
		this.state.setHeat(Math.min(Math.max(this.state.getHeat(), this.getSettings().getSettings().getMinHeat()), this.getSettings().getSettings().getMaxHeat()));
	}
	
	public void decay() {
		this.decrease(this.getSettings().getSettings().getHeatDecay());
	}

	public int getHeat() {
		return this.state.getHeat();
	}
}
