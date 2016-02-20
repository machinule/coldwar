package com.berserkbentobox.coldwar.logic.mechanics;

import com.berserkbentobox.coldwar.Conflict.ConflictMechanicSettingsOrBuilder;
import com.berserkbentobox.coldwar.Conflict.ConflictMechanicState;
import com.berserkbentobox.coldwar.Conflict.ConflictMechanicStateOrBuilder;
import com.berserkbentobox.coldwar.logic.Status;

public class Conflict {
	static Status validateSettings(ConflictMechanicSettingsOrBuilder settings) {
		return Status.OK;
	}

	static Status validateState(ConflictMechanicStateOrBuilder state) {
		return Status.OK;
	}

	public static ConflictMechanicState.Builder buildInitialState(ConflictMechanicSettingsOrBuilder settings) {
		ConflictMechanicState.Builder state = ConflictMechanicState.newBuilder();
		state
			.addAllActive(settings.getInitActiveList())
			.addAllDormant(settings.getInitDormantList())
			.addAllPossible(settings.getInitDormantList());
		return state;
	}
}
