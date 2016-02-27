package com.berserkbentobox.coldwar.logic.mechanics.victory;

import com.berserkbentobox.coldwar.Victory.VictoryPointStoreSettings;
import com.berserkbentobox.coldwar.Victory.VictoryPointStoreState;
import com.berserkbentobox.coldwar.logic.Status;

public class VictoryPointStore {
	public static class Settings {
		
		private VictoryMechanic.Settings parent;
		private VictoryPointStoreSettings settings;
		
		public Settings(VictoryMechanic.Settings parent, VictoryPointStoreSettings settings) {
			this.parent = parent;
			this.settings = settings;
		}
				
		public Status validate() {
			return Status.OK;
		}
		
		public VictoryPointStoreSettings getSettings() {
			return this.settings;
		}
		
		public VictoryPointStoreState initialState() {
			VictoryPointStoreState.Builder state = VictoryPointStoreState.newBuilder();
			state.setVictoryPoints(this.settings.getVictoryPointsInit());
			return state.build();
		}
	}
	
	private Settings settings;
	private VictoryMechanic parent;
	private VictoryPointStoreState.Builder state;
	
	public VictoryPointStore(VictoryMechanic parent, Settings settings, VictoryPointStoreState.Builder state) {
		this.parent = parent;
		this.settings = settings;
		this.state = state;
	}
	
	public VictoryPointStoreState.Builder getState() {
		return this.state;
	}

	public Settings getSettings() {
		return this.settings;
	}
	
	// Logic
	
	public void addVictoryPoints(int magnitude) {
		this.getState().setVictoryPoints(this.getState().getVictoryPoints());
	}

	public boolean hasWon() {
		return this.getState().getVictoryPoints() >= this.getSettings().getSettings().getVictoryPointsMax();
	}
}
