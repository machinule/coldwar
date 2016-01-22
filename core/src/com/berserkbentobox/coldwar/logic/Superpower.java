package com.berserkbentobox.coldwar.logic;

import java.util.stream.Collectors;

import com.berserkbentobox.coldwar.Superpower.SuperpowerMechanicSettingsOrBuilder;
import com.berserkbentobox.coldwar.Superpower.SuperpowerMechanicState;
import com.berserkbentobox.coldwar.Superpower.SuperpowerMechanicStateOrBuilder;
import com.berserkbentobox.coldwar.logic.UsaLeader;
import com.berserkbentobox.coldwar.logic.UssrLeader;
import com.berserkbentobox.coldwar.logic.InfluenceStore;

public class Superpower {
	
	static Status validateSettings(SuperpowerMechanicSettingsOrBuilder settings) {
		return Status.OK;
	}

	static Status validateState(SuperpowerMechanicStateOrBuilder state) {
		return Status.OK;
	}
	
	static SuperpowerMechanicState.Builder buildInitialState(SuperpowerMechanicSettingsOrBuilder settings) {
		SuperpowerMechanicState.Builder state = SuperpowerMechanicState.newBuilder();
		state
			.getUsaStateBuilder()
				.setPresident(settings.getUsaSettings().getPresidencySettings().getInitPresident())
				.setVicePresident(settings.getUsaSettings().getPresidencySettings().getInitVicePresident())
				.setPatriotism(settings.getUsaSettings().getInitPatriotism())
				.addAllLeader(settings.getUsaSettings().getLeaderSettingsList().stream().map(s -> UsaLeader.buildInitialState(s).build()).collect(Collectors.toList()))
				.setInfluenceStore(InfluenceStore.buildInitialState(settings.getUsaSettings().getInfluenceStoreSettings()));

		state
			.getUssrStateBuilder()
				.setPremier(settings.getUssrSettings().getSecretariatSettings().getInitPremier())
				.addAllTroika(settings.getUssrSettings().getSecretariatSettings().getInitTroikaList())
				.addAllLeader(settings.getUssrSettings().getLeaderSettingsList().stream().map(s -> UssrLeader.buildInitialState(s).build()).collect(Collectors.toList()))
				.setPartyUnity(settings.getUssrSettings().getInitPartyUnity())
				.setInfluenceStore(InfluenceStore.buildInitialState(settings.getUssrSettings().getInfluenceStoreSettings()));
		return state;
	}
}
