package com.berserkbentobox.coldwar.logic.mechanics;

import com.berserkbentobox.coldwar.Pseudorandom.PseudorandomMechanicSettingsOrBuilder;
import com.berserkbentobox.coldwar.Pseudorandom.PseudorandomMechanicState;
import com.berserkbentobox.coldwar.Pseudorandom.PseudorandomMechanicStateOrBuilder;
import com.berserkbentobox.coldwar.logic.Status;

public class Pseudorandom {

	public static PseudorandomMechanicState.Builder buildInitialState(PseudorandomMechanicSettingsOrBuilder settings) {
		PseudorandomMechanicState.Builder state = PseudorandomMechanicState.newBuilder();
		state
			.setSeed(settings.getInitSeed());
		return state;
	}
	
	static Status validateSettings(PseudorandomMechanicSettingsOrBuilder settings) {
		return Status.OK;
	}
	
	static Status validateState(PseudorandomMechanicStateOrBuilder state) {
		return Status.OK;
	}
}
