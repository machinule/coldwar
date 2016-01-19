package com.berserkbentobox.coldwar.logic;

import com.berserkbentobox.coldwar.Pseudorandom.PseudorandomGameSettingsOrBuilder;
import com.berserkbentobox.coldwar.Pseudorandom.PseudorandomGameState;
import com.berserkbentobox.coldwar.Pseudorandom.PseudorandomGameStateOrBuilder;

public class Pseudorandom {

	static PseudorandomGameState.Builder buildInitialState(PseudorandomGameSettingsOrBuilder settings) {
		PseudorandomGameState.Builder state = PseudorandomGameState.newBuilder();
		state
			.setSeed(settings.getInitSeed());
		return state;
	}
	
	static Status validateSettings(PseudorandomGameSettingsOrBuilder settings) {
		return Status.OK;
	}
	
	static Status validateState(PseudorandomGameStateOrBuilder state) {
		return Status.OK;
	}
}
