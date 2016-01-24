package com.berserkbentobox.coldwar.logic.mechanics;

import java.util.HashMap;
import java.util.Map;

import com.berserkbentobox.coldwar.Technology.TechnologyGroupState;
import com.berserkbentobox.coldwar.Technology.TechnologySettings;
import com.berserkbentobox.coldwar.Technology.TechnologyState;
import com.berserkbentobox.coldwar.logic.Status;
import com.berserkbentobox.coldwar.logic.mechanics.TechnologyGroup.Settings;

public class Technology {
	public static class Settings {
		
		private TechnologyGroup.Settings parent;
		private TechnologySettings settings;
		
		public Settings(TechnologyGroup.Settings parent, TechnologySettings settings) {
			this.parent = parent;
			this.settings = settings;
		}
		
		public Status validate() {
			return Status.OK;
		}
		
		public TechnologySettings getSettings() {
			return this.settings;
		}
	}
	
	private Settings settings;
	private TechnologyGroup parent;
	private TechnologyState.Builder state;
	
	public Technology(TechnologyGroup parent, Settings settings, TechnologyState.Builder state) {
		this.parent = parent;
		this.settings = settings;
		this.state = state;
	}
	
	public TechnologyState.Builder getState() {
		return this.state;
	}

	public Settings getSettings() {
		return this.settings;
	}
	
	public boolean isInProgress() {
		return this.state.getProgress() < this.settings.getSettings().getNumProgressions();
	}

	public synchronized void makeProgress() {
		this.state.setProgress(this.state.getProgress() + 1);
	}
}
