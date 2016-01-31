package com.berserkbentobox.coldwar.logic.mechanics.superpower;

import com.berserkbentobox.coldwar.Superpower.UssrLeaderState;
import com.berserkbentobox.coldwar.logic.Status;
import com.berserkbentobox.coldwar.Superpower.UssrLeaderSettings;

public class UssrLeader {
	public static class Settings {
		
		private UssrLeaderSettings settings;
		
		public Settings(UssrLeaderSettings settings) {
			this.settings = settings;
		}
		
		public Status validate() {
			return Status.OK;
		}
		
		public UssrLeaderSettings getSettings() {
			return this.settings;
		}
		
		public UssrLeaderState initialState() {
			UssrLeaderState.Builder state = UssrLeaderState.newBuilder();
			state
				.setName(this.settings.getName())
				.setPartySupport(this.settings.getInitPartySupport());
			return state.build();
		}
	}
	
	private Settings settings;
	private Ussr parent;
	private UssrLeaderState.Builder state;
	
	public UssrLeader(Ussr parent, Settings settings, UssrLeaderState.Builder state) {
		this.settings = settings;
		this.state = state;
		this.parent = parent;
	}
	
	// Getters
	
	public UssrLeaderState.Builder getState() {
		return this.state;
	}

	public Settings getSettings() {
		return this.settings;
	}
}
