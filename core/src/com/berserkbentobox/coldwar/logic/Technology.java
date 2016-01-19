package com.berserkbentobox.coldwar.logic;

import com.berserkbentobox.coldwar.Technology.TechnologyGameSettingsOrBuilder;
import com.berserkbentobox.coldwar.Technology.TechnologyGameState;
import com.berserkbentobox.coldwar.Technology.TechnologyGameStateOrBuilder;

public class Technology {
	static Status validateSettings(TechnologyGameSettingsOrBuilder settings) {
		return Status.OK;
	}

	static Status validateState(TechnologyGameStateOrBuilder state) {
		return Status.OK;
	}
	
	static TechnologyGameState.Builder buildInitialState(TechnologyGameSettingsOrBuilder settings) {
		TechnologyGameState.Builder state = TechnologyGameState.newBuilder();
		state
			.addAllUsaState(settings.getInitUsaTechnologyGroupList())
			.addAllUssrState(settings.getInitUssrTechnologyGroupList());
		
		return state;
	}
}
