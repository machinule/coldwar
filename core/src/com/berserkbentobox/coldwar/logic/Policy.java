package com.berserkbentobox.coldwar.logic;

import com.berserkbentobox.coldwar.Policy.PolicyGameState;
import com.berserkbentobox.coldwar.Policy.PolicyGameStateOrBuilder;
import com.berserkbentobox.coldwar.Policy.PolicySettingsOrBuilder;
import com.berserkbentobox.coldwar.Policy.PolicyState;

import java.util.stream.Collectors;

import com.berserkbentobox.coldwar.Policy.PolicyGameSettingsOrBuilder;

public class Policy {

	static Status validateSettings(PolicyGameSettingsOrBuilder settings) {
		return Status.OK;
	}

	static Status validateState(PolicyGameStateOrBuilder state) {
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

	static PolicyGameState.Builder buildInitialState(PolicyGameSettingsOrBuilder settings) {
		PolicyGameState.Builder state = PolicyGameState.newBuilder();
		state
			.addAllUsaPolicy(settings.getUsaPolicyList().stream().map(s -> buildInitialState(s).build()).collect(Collectors.toList()))
			.addAllUssrPolicy(settings.getUssrPolicyList().stream().map(s -> buildInitialState(s).build()).collect(Collectors.toList()));
		return state;
	}
}
