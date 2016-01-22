package com.berserkbentobox.coldwar.logic.mechanics;

import com.berserkbentobox.coldwar.Technology.TechnologyMechanicSettingsOrBuilder;
import com.berserkbentobox.coldwar.Technology.TechnologyMechanicState;
import com.berserkbentobox.coldwar.Technology.TechnologyMechanicStateOrBuilder;
import com.berserkbentobox.coldwar.logic.Status;

public class Technology {
	static Status validateSettings(TechnologyMechanicSettingsOrBuilder settings) {
		return Status.OK;
	}

	static Status validateState(TechnologyMechanicStateOrBuilder state) {
		return Status.OK;
	}
	
	public static TechnologyMechanicState.Builder buildInitialState(TechnologyMechanicSettingsOrBuilder settings) {
		TechnologyMechanicState.Builder state = TechnologyMechanicState.newBuilder();
		state
			.addAllUsaState(settings.getInitUsaTechnologyGroupList())
			.addAllUssrState(settings.getInitUssrTechnologyGroupList());
		
		return state;
	}
}
