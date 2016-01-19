package com.berserkbentobox.coldwar.logic;

import com.berserkbentobox.coldwar.Heat.HeatGameSettingsOrBuilder;
import com.berserkbentobox.coldwar.Heat.HeatGameState;
import com.berserkbentobox.coldwar.Heat.HeatGameStateOrBuilder;

public class Heat {

	static Status validateSettings(HeatGameSettingsOrBuilder settings) {
		return Status.OK;
	}

	static Status validateState(HeatGameStateOrBuilder state) {
		return Status.OK;
	}
	
	static HeatGameState.Builder buildInitialState(HeatGameSettingsOrBuilder settings) {
		HeatGameState.Builder state = HeatGameState.newBuilder();
		state
			.setHeat(settings.getInitHeat());
		return state;
	}
}
