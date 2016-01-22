package com.berserkbentobox.coldwar.logic.mechanics;

import com.berserkbentobox.coldwar.Policy.PolicyMechanicState;
import com.berserkbentobox.coldwar.Policy.PolicyMechanicStateOrBuilder;
import com.berserkbentobox.coldwar.Policy.PolicySettingsOrBuilder;
import com.berserkbentobox.coldwar.Policy.PolicyState;
import com.berserkbentobox.coldwar.logic.Status;

import java.util.stream.Collectors;

import com.berserkbentobox.coldwar.Policy.PolicyMechanicSettingsOrBuilder;

public class Policy {

	static Status validateSettings(PolicyMechanicSettingsOrBuilder settings) {
		return Status.OK;
	}

	static Status validateState(PolicyMechanicStateOrBuilder state) {
		return Status.OK;
	}
	
	static PolicyState.Builder buildInitialState(PolicySettingsOrBuilder settings) {
		PolicyState.Builder state = PolicyState.newBuilder();
		state
			.setId(settings.getId())
			.setActive(settings.getInitActive())
			.setAvailable(settings.getInitAvailable());
		return state;
	}

	public static PolicyMechanicState.Builder buildInitialState(PolicyMechanicSettingsOrBuilder settings) {
		PolicyMechanicState.Builder state = PolicyMechanicState.newBuilder();
		state
			.addAllUsaPolicy(settings.getUsaPolicyList().stream().map(s -> buildInitialState(s).build()).collect(Collectors.toList()))
			.addAllUssrPolicy(settings.getUssrPolicyList().stream().map(s -> buildInitialState(s).build()).collect(Collectors.toList()));
		return state;
	}
}
