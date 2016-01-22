package com.berserkbentobox.coldwar.logic;

import com.berserkbentobox.coldwar.Heat.HeatMechanicSettingsOrBuilder;
import com.berserkbentobox.coldwar.Heat.HeatMechanicState;
import com.berserkbentobox.coldwar.Heat.HeatMechanicStateOrBuilder;

public class Heat {

	static Status validateSettings(HeatMechanicSettingsOrBuilder settings) {
		return Status.OK;
	}

	static Status validateState(HeatMechanicStateOrBuilder state) {
		return Status.OK;
	}
	
	static HeatMechanicState.Builder buildInitialState(HeatMechanicSettingsOrBuilder settings) {
		HeatMechanicState.Builder state = HeatMechanicState.newBuilder();
		state
			.setHeat(settings.getInitHeat());
		return state;
	}
}
