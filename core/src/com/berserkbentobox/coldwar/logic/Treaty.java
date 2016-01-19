package com.berserkbentobox.coldwar.logic;

import java.util.stream.Collectors;

import com.berserkbentobox.coldwar.Treaty.TreatyGameSettingsOrBuilder;
import com.berserkbentobox.coldwar.Treaty.TreatyGameState;
import com.berserkbentobox.coldwar.Treaty.TreatyGameStateOrBuilder;
import com.berserkbentobox.coldwar.Treaty.TreatySettingsOrBuilder;
import com.berserkbentobox.coldwar.Treaty.TreatyState;

public class Treaty {

	static Status validateSettings(TreatyGameSettingsOrBuilder settings) {
		return Status.OK;
	}

	static Status validateState(TreatyGameStateOrBuilder state) {
		return Status.OK;
	}
	
	static TreatyState.Builder buildInitialState(TreatySettingsOrBuilder settings) {
		TreatyState.Builder state = TreatyState.newBuilder();
		state
			.setId(settings.getId())
			.setSigned(settings.getInitSigned());
		return state;
	}

	static TreatyGameState.Builder buildInitialState(TreatyGameSettingsOrBuilder settings) {
		TreatyGameState.Builder state = TreatyGameState.newBuilder();
		state
			.addAllTreaty(settings.getTreatyList().stream().map(s -> buildInitialState(s).build()).collect(Collectors.toList()));
		return state;
	}
}
