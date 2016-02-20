package com.berserkbentobox.coldwar.logic.mechanics.deterrance;

import com.berserkbentobox.coldwar.Deterrence.DeterrenceMechanicSettings;
import com.berserkbentobox.coldwar.Deterrence.DeterrenceMechanicState;
import com.berserkbentobox.coldwar.GameSettingsOuterClass.GameSettingsOrBuilder;
import com.berserkbentobox.coldwar.GameStateOuterClass.GameStateOrBuilder;
import com.berserkbentobox.coldwar.logic.Mechanic;
import com.berserkbentobox.coldwar.logic.Mechanics;
import com.berserkbentobox.coldwar.logic.Status;

public class DeterrenceMechanic extends Mechanic {
	public static class Settings {
		
		private GameSettingsOrBuilder gameSettings;
		private DeterrenceMechanicSettings settings;
		
		public Settings(GameSettingsOrBuilder gameSettings) {
			this.gameSettings = gameSettings;
			this.settings = gameSettings.getDeterrenceSettings();
		}
		
		public Status validate() {
			return Status.OK;
		}
		
		public DeterrenceMechanicSettings getSettings() {
			return this.settings;
		}
		
		public DeterrenceMechanicState initialState() {
			DeterrenceMechanicState.Builder state = DeterrenceMechanicState.newBuilder();
			state.setDeterrence(this.settings.getInitDeterrence());
			return state.build();
		}
	}
	
	private Settings settings;
	private DeterrenceMechanicState.Builder state;
	
	public DeterrenceMechanic(Mechanics mechanics, Settings settings, GameStateOrBuilder state) {
		super(mechanics);
		this.settings = settings;
		this.state = state.getDeterrenceState().toBuilder();
	}
		
	public Status validate() {
		return Status.OK;
	}
	
	public Settings getSettings() {
		return this.settings;
	}
	
	public DeterrenceMechanicState buildState() {
		return this.state.build();
	}
	
	public synchronized void increase(int i) {
		this.state.setDeterrence(this.state.getDeterrence() + i);
	}
	
	public void decrease(int i) {
		increase(-i);
	}
	
	public int getDeterrence() {
		return this.state.getDeterrence();
	}
}
