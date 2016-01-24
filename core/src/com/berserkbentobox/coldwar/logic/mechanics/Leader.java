package com.berserkbentobox.coldwar.logic.mechanics;

import java.util.stream.Collectors;

import com.berserkbentobox.coldwar.Leader.LeaderMechanicSettingsOrBuilder;
import com.berserkbentobox.coldwar.Leader.LeaderMechanicState;
import com.berserkbentobox.coldwar.Leader.LeaderMechanicStateOrBuilder;
import com.berserkbentobox.coldwar.Leader.LeaderSettingsOrBuilder;
import com.berserkbentobox.coldwar.Leader.LeaderState;
import com.berserkbentobox.coldwar.Policy.PolicyMechanicSettingsOrBuilder;
import com.berserkbentobox.coldwar.Policy.PolicyMechanicState;
import com.berserkbentobox.coldwar.Policy.PolicyMechanicStateOrBuilder;
import com.berserkbentobox.coldwar.logic.Status;

public class Leader {

	static Status validateSettings(LeaderMechanicSettingsOrBuilder settings) {
		return Status.OK;
	}

	static Status validateState(LeaderMechanicStateOrBuilder state) {
		return Status.OK;
	}
	
	static LeaderState.Builder buildInitialState(LeaderSettingsOrBuilder settings) {
		LeaderState.Builder state = LeaderState.newBuilder();
		state
			.setId(settings.getId())
			.setProvince(settings.getProvinceRestriction())
			.setDead(false);
		return state;
	}	

	public static LeaderMechanicState.Builder buildInitialState(LeaderMechanicSettingsOrBuilder settings) {
		LeaderMechanicState.Builder state = LeaderMechanicState.newBuilder();
		state
			.addAllLeader(settings.getInitLeaderList().stream().map(s -> buildInitialState(s).build()).collect(Collectors.toList()));
		return state;
	}
}
