package com.berserkbentobox.coldwar.logic.mechanics.superpower;

import com.berserkbentobox.coldwar.Superpower.UsaLeaderState;
import com.berserkbentobox.coldwar.logic.Status;
import com.berserkbentobox.coldwar.Superpower.UsaLeaderSettings;

public class UsaLeader {
	public static class Settings {
		
		private UsaLeaderSettings settings;
		
		public Settings(UsaLeaderSettings settings) {
			this.settings = settings;
		}
		
		public Status validate() {
			return Status.OK;
		}
		
		public UsaLeaderSettings getSettings() {
			return this.settings;
		}
		
		public UsaLeaderState initialState() {
			UsaLeaderState.Builder state = UsaLeaderState.newBuilder();
			state
				.setName(this.settings.getName())
				.setAvailable(this.settings.getInitAvailable())
				.setNumTermsAsPresident(this.settings.getInitNumTermsAsPresident())
				.setNumTermsAsVicePresident(this.settings.getInitNumTermsAsVicePresident());
			return state.build();
		}
	}
	
	private Settings settings;
	private Usa parent;
	private UsaLeaderState.Builder state;
	
	public UsaLeader(Usa parent, Settings settings, UsaLeaderState.Builder state) {
		this.settings = settings;
		this.state = state;
		this.parent = parent;
	}
	
	// Getters
	
	public UsaLeaderState.Builder getState() {
		return this.state;
	}

	public Settings getSettings() {
		return this.settings;
	}
}
