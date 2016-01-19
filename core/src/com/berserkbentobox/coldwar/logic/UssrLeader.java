package com.berserkbentobox.coldwar.logic;

import com.berserkbentobox.coldwar.Superpower.SuperpowerSettingsOrBuilder;
import com.berserkbentobox.coldwar.Superpower.SuperpowerStateOrBuilder;
import com.berserkbentobox.coldwar.Superpower.UssrLeaderState;
import com.berserkbentobox.coldwar.Superpower.UssrLeaderSettingsOrBuilder;

	
public class UssrLeader {
	static UssrLeaderState.Builder buildInitialState(UssrLeaderSettingsOrBuilder settings) {
		UssrLeaderState.Builder state = UssrLeaderState.newBuilder();
		state
			.setId(settings.getId())
			.setAvailable(settings.getInitAvailable())
			.setPartySupport(settings.getInitPartySupport());
		return state;
	}
	
	static Status validateSettings(SuperpowerSettingsOrBuilder settings) {
		return Status.OK;
	}
	
	static Status validateState(SuperpowerStateOrBuilder state) {
		return Status.OK;
	}
}

