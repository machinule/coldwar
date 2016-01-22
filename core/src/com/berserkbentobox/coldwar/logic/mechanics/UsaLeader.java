package com.berserkbentobox.coldwar.logic.mechanics;

import com.berserkbentobox.coldwar.Superpower.UsaLeaderState;
import com.berserkbentobox.coldwar.Superpower.UsaLeaderStateOrBuilder;
import com.berserkbentobox.coldwar.logic.Status;
import com.berserkbentobox.coldwar.Superpower.UsaLeaderSettingsOrBuilder;

	
public class UsaLeader {
	static UsaLeaderState.Builder buildInitialState(UsaLeaderSettingsOrBuilder settings) {
		UsaLeaderState.Builder state = UsaLeaderState.newBuilder();
		state
			.setId(settings.getId())
			.setAvailable(settings.getInitAvailable())
			.setNumTermsAsPresident(settings.getInitNumTermsAsPresident())
			.setNumTermsAsVicePresident(settings.getInitNumTermsAsVicePresident());
		return state;
	}
	
	static Status validateSettings(UsaLeaderSettingsOrBuilder settings) {
		return Status.OK;
	}
	
	static Status validateState(UsaLeaderStateOrBuilder state) {
		return Status.OK;
	}
}

