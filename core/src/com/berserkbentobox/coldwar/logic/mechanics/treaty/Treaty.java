package com.berserkbentobox.coldwar.logic.mechanics.treaty;

import com.berserkbentobox.coldwar.Treaty.TreatySettings;
import com.berserkbentobox.coldwar.Treaty.TreatyState;
import com.berserkbentobox.coldwar.logic.Status;
import com.berserkbentobox.coldwar.logic.mechanics.HeatMechanic;
import com.berserkbentobox.coldwar.logic.mechanics.deterrance.DeterrenceMechanic;

public class Treaty {
	public static class Settings {
		
		private TreatyMechanic.Settings parent;
		private TreatySettings settings;
		
		public Settings(TreatyMechanic.Settings parent, TreatySettings settings) {
			this.parent = parent;
			this.settings = settings;
		}
		
		public TreatyMechanic.Settings getParent() {
			return this.parent;
		}
		
		public Status validate() {
			return Status.OK;
		}
		
		public TreatySettings getSettings() {
			return this.settings;
		}
		
		public TreatyState initialState() {
			TreatyState.Builder state = TreatyState.newBuilder();
			state
				.setId(settings.getId())
				.setSigned(settings.getInitSigned());
			return state.build();
		}
	}
	
	private Settings settings;
	private TreatyMechanic parent;
	private TreatyState.Builder state;
	
	public Treaty(TreatyMechanic parent, Settings settings, TreatyState.Builder state) {
		this.parent = parent;
		this.settings = settings;
		this.state = state;
	}
	
	public TreatyState.Builder getState() {
		return this.state;
	}

	public Settings getSettings() {
		return this.settings;
	}
	
	public boolean isSigned() {
		return this.state.getSigned();
	}

	public void Sign(HeatMechanic heat, DeterrenceMechanic deterrence) {
		this.state.setSigned(true);
		heat.decrease(this.settings.getSettings().getHeatReduction());
		deterrence.decrease(this.settings.getSettings().getDeterrenceReduction());
	}
}
