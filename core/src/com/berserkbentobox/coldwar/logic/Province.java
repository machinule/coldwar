package com.berserkbentobox.coldwar.logic;

import java.util.stream.Collectors;

import com.berserkbentobox.coldwar.Logger;
import com.berserkbentobox.coldwar.GameSettingsOuterClass.ProvinceGameSettingsOrBuilder;
import com.berserkbentobox.coldwar.GameSettingsOuterClass.ProvinceSettings;
import com.berserkbentobox.coldwar.GameSettingsOuterClass.ProvinceSettingsOrBuilder;
import com.berserkbentobox.coldwar.Province.ProvinceGameState;
import com.berserkbentobox.coldwar.Province.ProvinceGameStateOrBuilder;
import com.berserkbentobox.coldwar.Province.ProvinceState;


public class Province {
	static Status validateSettings(ProvinceGameSettingsOrBuilder settings) {
		return Status.OK;
	}

	static Status validateState(ProvinceGameStateOrBuilder state) {
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
			.setConflict(settings.getInitConflict());
		return state;
	}

	static ProvinceGameState.Builder buildInitialState(ProvinceGameSettingsOrBuilder settings) {
		ProvinceGameState.Builder state = ProvinceGameState.newBuilder();
		state
			.addAllProvinceState(settings.getProvinceList().stream().map(s -> buildInitialState(s).build()).collect(Collectors.toList()));;
		return state;
	}
}
