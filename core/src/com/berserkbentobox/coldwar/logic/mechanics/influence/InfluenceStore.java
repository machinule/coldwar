package com.berserkbentobox.coldwar.logic.mechanics.influence;

import com.berserkbentobox.coldwar.Influence.InfluenceStoreSettings;
import com.berserkbentobox.coldwar.Influence.InfluenceStoreState;
import com.berserkbentobox.coldwar.Technology.TechnologyState;
import com.berserkbentobox.coldwar.logic.Status;
import com.berserkbentobox.coldwar.logic.mechanics.technology.TechnologyGroup;
import com.berserkbentobox.coldwar.logic.mechanics.technology.Technology.Settings;



public class InfluenceStore {
	public static class Settings {
		
		private InfluenceMechanic.Settings parent;
		private InfluenceStoreSettings settings;
		
		public Settings(InfluenceMechanic.Settings parent, InfluenceStoreSettings settings) {
			this.parent = parent;
			this.settings = settings;
		}
				
		public Status validate() {
			return Status.OK;
		}
		
		public InfluenceStoreSettings getSettings() {
			return this.settings;
		}
		
		public InfluenceStoreState initialState() {
			InfluenceStoreState.Builder state = InfluenceStoreState.newBuilder();
			state.setPolitical(this.settings.getPoliticalStoreInit());
			state.setMilitary(this.settings.getMilitaryStoreInit());
			state.setCovert(this.settings.getCovertStoreInit());
			return state.build();
		}
	}
	
	private Settings settings;
	private InfluenceMechanic parent;
	private InfluenceStoreState.Builder state;
	
	public InfluenceStore(InfluenceMechanic parent, Settings settings, InfluenceStoreState.Builder state) {
		this.parent = parent;
		this.settings = settings;
		this.state = state;
	}
	
	public InfluenceStoreState.Builder getState() {
		return this.state;
	}

	public Settings getSettings() {
		return this.settings;
	}
}
