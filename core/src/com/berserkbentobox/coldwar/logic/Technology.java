package com.berserkbentobox.coldwar.logic;

import com.berserkbentobox.coldwar.Technology.TechnologyMechanicSettingsOrBuilder;
import com.berserkbentobox.coldwar.Technology.TechnologyMechanicState;
import com.berserkbentobox.coldwar.Technology.TechnologyMechanicStateOrBuilder;

public class Technology {
	static Status validateSettings(TechnologyMechanicSettingsOrBuilder settings) {
		return Status.OK;
	}

	static Status validateState(TechnologyMechanicStateOrBuilder state) {
		return Status.OK;
	}
	
	static TechnologyMechanicState.Builder buildInitialState(TechnologyMechanicSettingsOrBuilder settings) {
		TechnologyMechanicState.Builder state = TechnologyMechanicState.newBuilder();
		state
			.addAllUsaState(settings.getInitUsaTechnologyGroupList())
			.addAllUssrState(settings.getInitUssrTechnologyGroupList());
		
		return state;
	}
}
