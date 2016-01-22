package com.berserkbentobox.coldwar.logic.mechanics;

import java.util.stream.Collectors;

import com.berserkbentobox.coldwar.Logger;
import com.berserkbentobox.coldwar.GameSettingsOuterClass.ProvinceMechanicSettingsOrBuilder;
import com.berserkbentobox.coldwar.GameSettingsOuterClass.ProvinceSettings;
import com.berserkbentobox.coldwar.GameSettingsOuterClass.ProvinceSettingsOrBuilder;
import com.berserkbentobox.coldwar.Province.ProvinceMechanicState;
import com.berserkbentobox.coldwar.Province.ProvinceMechanicStateOrBuilder;
import com.berserkbentobox.coldwar.Province.ProvinceState;
import com.berserkbentobox.coldwar.logic.Status;


public class Province {
	static Status validateSettings(ProvinceMechanicSettingsOrBuilder settings) {
		return Status.OK;
	}

	static Status validateState(ProvinceMechanicStateOrBuilder state) {
		return Status.OK;
	}
	
	static ProvinceState.Builder buildInitialState(ProvinceSettingsOrBuilder settings) {
		ProvinceState.Builder state = ProvinceState.newBuilder();
		state
			.setId(settings.getId())
			.setGov(settings.getInitGovernment())
			.setInfluence(settings.getInitInfluence())
			.setAlly(settings.getInitAlly())
			.setBase(settings.getInitMilitaryBase())
			.setOccupier(settings.getInitOccupier())
			.setDissidents(settings.getInitDissidents())
			//.setLeader(settings.getInitLeader())
			.setConflict(settings.getInitConflict());
		return state;
	}

	public static ProvinceMechanicState.Builder buildInitialState(ProvinceMechanicSettingsOrBuilder settings) {
		ProvinceMechanicState.Builder state = ProvinceMechanicState.newBuilder();
		state
			.addAllProvinceState(settings.getProvinceList().stream().map(s -> buildInitialState(s).build()).collect(Collectors.toList()));;
		return state;
	}
}
