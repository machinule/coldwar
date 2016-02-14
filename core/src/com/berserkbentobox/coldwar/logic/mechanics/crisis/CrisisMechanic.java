package com.berserkbentobox.coldwar.logic.mechanics.crisis;

import com.berserkbentobox.coldwar.Crisis.CrisisMechanicSettings;
import com.berserkbentobox.coldwar.Crisis.CrisisMechanicState;
import com.berserkbentobox.coldwar.GameSettingsOuterClass.GameSettingsOrBuilder;
import com.berserkbentobox.coldwar.GameStateOuterClass.GameStateOrBuilder;
import com.berserkbentobox.coldwar.logic.Status;

public class CrisisMechanic {
	public static class Settings {
		
		private GameSettingsOrBuilder gameSettings;
		private CrisisMechanicSettings settings;
		
		public Settings(GameSettingsOrBuilder gameSettings) {
			this.gameSettings = gameSettings;
			this.settings = gameSettings.getCrisisSettings();
		}
		
		public Status validate() {
			return Status.OK;
		}
		
		public CrisisMechanicSettings getSettings() {
			return this.settings;
		}
		
		public CrisisMechanicState initialState() {
			CrisisMechanicState.Builder state = CrisisMechanicState.newBuilder();
			//state.setCrisis(this.settings.getInitCrisis());
			return state.build();
		}
	}
	
	private Settings settings;
	private CrisisMechanicState.Builder state;
	
	public CrisisMechanic(Settings settings, GameStateOrBuilder state) {
		this.settings = settings;
		this.state = state.getCrisisState().toBuilder();
	}
		
	public Status validate() {
		return Status.OK;
	}
	
	public Settings getSettings() {
		return this.settings;
	}
	
	public CrisisMechanicState.Builder getState() {
		return this.state;
	}
	
	public CrisisMechanicState buildState() {
		return this.state.build();
	}

	public boolean isActiveCrisis() {
		if(this.getState().hasJoint() || this.getState().hasUsa() || this.getState().hasUssr())
			return true;
		return false;
	}
	
}


/*

// Crises
if (move.hasUsaBerlinBlockadeAirliftMove()) {
	int cost = 3; // TODO: Real value; starting MIL point cost
	milStoreMap.compute(player,  (p, mil) -> mil == null ? -cost : mil - cost);
	crisis.setUsaActed1(true);
}
if (move.hasUssrBerlinBlockadeLiftBlockadeMove()) {
	int cost = 100; // TODO: Real value
	milStoreMap.compute(player,  (p, mil) -> mil == null ? -cost : mil - cost);
	crisis.setUssrActed1(true);
}







		//Berlin Blockade
		Crisis.Builder c = Crisis.newBuilder();
		c.setBerlinBlockade(true);
		c.setInfo("Blockade of Berlin");
		c.setUsaOption1("Begin the Berlin Airlift");
		c.setUssrOption1("End the Blockade");
		state.setCrises(c.build());
		
		state.getTurnLogBuilder()
		.addEvents(Event.newBuilder()
			.setBerlinBlockadeEvent(BerlinBlockadeEvent.newBuilder()
				.build())
			.build());












// CRISIS HANDLING

if(crisis.getBerlinBlockade()) {
	Logger.Dbg("Resolving Berlin Blockade");
	boolean airlift = crisis.getUsaActed1();
	boolean blockade = !crisis.getUssrActed1();
	if(airlift) {
		if(blockade) { // Airlift while blockade maintained
			patriotismCounter += 5; // TODO: Settings value
		} else { // Airlift while blockade lifted
			totalInfluenceMap.compute(ProvinceId.EAST_GERMANY, (i, infl) -> infl == null ? -1 : infl - 1);
		}
	} else {
		if(blockade) { // Blockade with no airlift
			patriotismCounter -= 5;
			// Berlin flag unset
		} else { // No blockade and no airlift
			totalInfluenceMap.compute(ProvinceId.EAST_GERMANY, (i, infl) -> infl == null ? -1 : infl - 1);
		}
	}
	Logger.Dbg("Berlin airlift enacted: " + airlift);
	Logger.Dbg("Berlin blockade maintained: " + blockade);
}





// Crises

public boolean isBerlinBlockadeActive() {
	if(state.getCrises().getBerlinBlockade()) {
		return true;
	}
	return false;
}







public String getCrisisInfo() {
return state.getCrises().getInfo();
}

public String getCrisisUsaOption1() {
return state.getCrises().getUsaOption1();
}

public String getCrisisUssrOption1() {
return state.getCrises().getUssrOption1();
}



*/