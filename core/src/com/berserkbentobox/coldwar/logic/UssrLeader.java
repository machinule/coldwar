package com.berserkbentobox.coldwar.logic;

import com.berserkbentobox.coldwar.Superpower.UssrLeaderState;
import com.berserkbentobox.coldwar.Superpower.UssrLeaderStateOrBuilder;
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
	
	static Status validateSettings(UssrLeaderSettingsOrBuilder settings) {
		return Status.OK;
	}
	
	static Status validateState(UssrLeaderStateOrBuilder state) {
		return Status.OK;
	}
}
