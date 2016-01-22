package com.berserkbentobox.coldwar.logic.mechanics;

import com.berserkbentobox.coldwar.Heat.HeatMechanicSettingsOrBuilder;
import com.berserkbentobox.coldwar.Heat.HeatMechanicState;
import com.berserkbentobox.coldwar.Heat.HeatMechanicStateOrBuilder;
import com.berserkbentobox.coldwar.logic.Status;

public class Heat {

	static Status validateSettings(HeatMechanicSettingsOrBuilder settings) {
		return Status.OK;
	}

	static Status validateState(HeatMechanicStateOrBuilder state) {
		return Status.OK;
	}
	
	public static HeatMechanicState.Builder buildInitialState(HeatMechanicSettingsOrBuilder settings) {
		HeatMechanicState.Builder state = HeatMechanicState.newBuilder();
		state
			.setHeat(settings.getInitHeat());
		return state;
	}
}
