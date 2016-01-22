package com.berserkbentobox.coldwar.logic;

import com.berserkbentobox.coldwar.Pseudorandom.PseudorandomMechanicSettingsOrBuilder;
import com.berserkbentobox.coldwar.Pseudorandom.PseudorandomMechanicState;
import com.berserkbentobox.coldwar.Pseudorandom.PseudorandomMechanicStateOrBuilder;

public class Pseudorandom {

	static PseudorandomMechanicState.Builder buildInitialState(PseudorandomMechanicSettingsOrBuilder settings) {
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
