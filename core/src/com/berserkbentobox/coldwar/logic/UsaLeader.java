package com.berserkbentobox.coldwar.logic;

import com.berserkbentobox.coldwar.Superpower.SuperpowerSettingsOrBuilder;
import com.berserkbentobox.coldwar.Superpower.SuperpowerStateOrBuilder;
import com.berserkbentobox.coldwar.Superpower.UsaLeaderState;
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
	
	static Status validateSettings(SuperpowerSettingsOrBuilder settings) {
		return Status.OK;
	}
	
	static Status validateState(SuperpowerStateOrBuilder state) {
		return Status.OK;
	}
}

