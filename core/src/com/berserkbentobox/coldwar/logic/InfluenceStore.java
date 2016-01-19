package com.berserkbentobox.coldwar.logic;

import com.berserkbentobox.coldwar.InfluenceStore.InfluenceStoreSettingsOrBuilder;
import com.berserkbentobox.coldwar.InfluenceStore.InfluenceStoreState;
import com.berserkbentobox.coldwar.InfluenceStore.InfluenceStoreStateOrBuilder;


public class InfluenceStore {
	static InfluenceStoreState.Builder buildInitialState(InfluenceStoreSettingsOrBuilder settings) {
		InfluenceStoreState.Builder state = InfluenceStoreState.newBuilder();
		state
			.setPolitical(settings.getPoliticalStoreInit())
			.setMilitary(settings.getMilitaryStoreInit())
			.setCovert(settings.getCovertStoreInit());
		return state;
	}
	
	static Status validateSettings(InfluenceStoreSettingsOrBuilder settings) {
		return Status.OK;
	}
	
	static Status validateState(InfluenceStoreStateOrBuilder state) {
		return Status.OK;
	}

}
