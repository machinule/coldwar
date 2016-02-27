package com.berserkbentobox.coldwar.logic.mechanics.event;

import com.berserkbentobox.coldwar.EventOuterClass.EventMechanicSettings;
import com.berserkbentobox.coldwar.EventOuterClass.EventMechanicState;
import com.berserkbentobox.coldwar.GameSettingsOuterClass.GameSettingsOrBuilder;
import com.berserkbentobox.coldwar.GameStateOuterClass.GameStateOrBuilder;
import com.berserkbentobox.coldwar.logic.Mechanic;
import com.berserkbentobox.coldwar.logic.Mechanics;
import com.berserkbentobox.coldwar.logic.Status;

public class EventMechanic extends Mechanic {
	
	public static class Settings {
		
		private GameSettingsOrBuilder gameSettings;
		private EventMechanicSettings settings;
		
		public Settings(GameSettingsOrBuilder gameSettings) {
			this.gameSettings = gameSettings;
			this.settings = gameSettings.getEventSettings();
		}
		
		public Status validate() {
			return Status.OK;
		}
		
		public EventMechanicState initialState() {
			EventMechanicState.Builder state = EventMechanicState.newBuilder();
			return state.build();
		}
	}
	
	private Settings settings;
	private EventMechanicState.Builder state;

	public EventMechanic(Mechanics mechanics, Settings settings, GameStateOrBuilder state) {
		super(mechanics);
		this.settings = settings;
		this.state = state.getEventState().toBuilder();
	}
		
	public Status validate() {
		return Status.OK;
	}
	
	public Settings getSettings() {
		return this.settings;
	}
	
	public EventMechanicState buildState() {
		return this.state.build();
	}
		
	// Logic
}
