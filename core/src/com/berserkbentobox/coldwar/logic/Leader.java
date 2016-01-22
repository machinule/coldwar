package com.berserkbentobox.coldwar.logic;

import com.berserkbentobox.coldwar.Leader.LeaderMechanicSettingsOrBuilder;
import com.berserkbentobox.coldwar.Leader.LeaderSettingsOrBuilder;
import com.berserkbentobox.coldwar.Leader.LeaderState;

public class Leader {

	static Status validateSettings(LeaderMechanicSettingsOrBuilder settings) {
		return Status.OK;
	}
	
	static LeaderState.Builder buildInitialState(LeaderSettingsOrBuilder settings) {
		LeaderState.Builder state = LeaderState.newBuilder();
		state
			.setId(settings.getId());
		return state;
	}	
}
