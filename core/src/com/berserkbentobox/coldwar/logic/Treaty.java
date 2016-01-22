package com.berserkbentobox.coldwar.logic;

import java.util.stream.Collectors;

import com.berserkbentobox.coldwar.Treaty.TreatyMechanicSettingsOrBuilder;
import com.berserkbentobox.coldwar.Treaty.TreatyMechanicState;
import com.berserkbentobox.coldwar.Treaty.TreatyMechanicStateOrBuilder;
import com.berserkbentobox.coldwar.Treaty.TreatySettingsOrBuilder;
import com.berserkbentobox.coldwar.Treaty.TreatyState;

public class Treaty {

	static Status validateSettings(TreatyMechanicSettingsOrBuilder settings) {
		return Status.OK;
	}

	static Status validateState(TreatyMechanicStateOrBuilder state) {
		return Status.OK;
	}
	
	static TreatyState.Builder buildInitialState(TreatySettingsOrBuilder settings) {
		TreatyState.Builder state = TreatyState.newBuilder();
		state
			.setId(settings.getId())
			.setSigned(settings.getInitSigned());
		return state;
	}

	static TreatyMechanicState.Builder buildInitialState(TreatyMechanicSettingsOrBuilder settings) {
		TreatyMechanicState.Builder state = TreatyMechanicState.newBuilder();
		state
			.addAllTreaty(settings.getTreatyList().stream().map(s -> buildInitialState(s).build()).collect(Collectors.toList()));
		return state;
	}
}
