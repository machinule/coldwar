package com.berserkbentobox.coldwar.logic.mechanics.year;

import com.berserkbentobox.coldwar.GameSettingsOuterClass.GameSettingsOrBuilder;
import com.berserkbentobox.coldwar.GameStateOuterClass.GameStateOrBuilder;
import com.berserkbentobox.coldwar.Year.YearMechanicSettings;
import com.berserkbentobox.coldwar.Year.YearMechanicState;
import com.berserkbentobox.coldwar.logic.Mechanic;
import com.berserkbentobox.coldwar.logic.Mechanics;
import com.berserkbentobox.coldwar.logic.Status;

public class YearMechanic extends Mechanic {
	
	public static class Settings {
		
		private GameSettingsOrBuilder gameSettings;
		private YearMechanicSettings settings;
		
		public Settings(GameSettingsOrBuilder gameSettings) {
			this.gameSettings = gameSettings;
			this.settings = gameSettings.getYearSettings();
		}
		
		public Status validate() {
			return Status.OK;
		}
		
		public YearMechanicSettings getSettings() {
			return this.settings;
		}
		
		public YearMechanicState initialState() {
			YearMechanicState.Builder state = YearMechanicState.newBuilder();
			state.setYear(this.settings.getInitYear());
			return state.build();
		}
	}
	
	private Settings settings;
	private YearMechanicState.Builder state;
	
	public YearMechanic(Mechanics mechanics, Settings settings, GameStateOrBuilder state) {
		super(mechanics);
		this.settings = settings;
		this.state = state.getYearState().toBuilder();
	}
		
	public Status validate() {
		return Status.OK;
	}
	
	public Settings getSettings() {
		return this.settings;
	}
	
	public YearMechanicState buildState() {
		return this.state.build();
	}
	
	public int getYear() {
		return this.state.getYear();
	}
	
	public void incrementYear() {
		this.state.setYear(this.state.getYear() + 1);
	}

}
