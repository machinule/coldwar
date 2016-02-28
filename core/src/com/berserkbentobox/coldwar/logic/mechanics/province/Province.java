package com.berserkbentobox.coldwar.logic.mechanics.province;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.berserkbentobox.coldwar.DissidentsOuterClass.Dissidents;
import com.berserkbentobox.coldwar.DissidentsOuterClass.Government;
import com.berserkbentobox.coldwar.Id.ProvinceId;
import com.berserkbentobox.coldwar.Logger;
import com.berserkbentobox.coldwar.Province.ProvinceMechanicSettingsOrBuilder;
import com.berserkbentobox.coldwar.Province.ProvinceSettings;
import com.berserkbentobox.coldwar.Province.ProvinceSettingsOrBuilder;
import com.berserkbentobox.coldwar.Province.ProvinceMechanicState;
import com.berserkbentobox.coldwar.Province.ProvinceMechanicStateOrBuilder;
import com.berserkbentobox.coldwar.Province.ProvinceState;
import com.berserkbentobox.coldwar.logic.Client.Player;
import com.berserkbentobox.coldwar.logic.Status;


public class Province {
	public static class Settings {
		
		private ProvinceSettings settings;
		
		public Settings(ProvinceSettings settings) {
			this.settings = settings;
		}
		
		public Status validate() {
			return Status.OK;
		}
		
		// Getters
		
		public ProvinceSettings getSettings() {
			return this.settings;
		}
		
		public ProvinceState initialState() {
			ProvinceState.Builder state = ProvinceState.newBuilder();
			state
				.setId(this.settings.getId())
				.setInfluence(this.settings.getInitInfluence())
				.setAlly(this.settings.getInitAlly())
				.setDissidents(this.settings.getInitDissidents())
				.setGov(this.settings.getInitGovernment())
				.setOccupier(this.settings.getInitOccupier());
			return state.build();
		}
	}
	
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
			.setDissidents(settings.getInitDissidents());
		return state;
	}

	public static ProvinceMechanicState.Builder buildInitialState(ProvinceMechanicSettingsOrBuilder settings) {
		ProvinceMechanicState.Builder state = ProvinceMechanicState.newBuilder();
		state
			.addAllProvinceState(settings.getProvinceList().stream().map(s -> buildInitialState(s).build()).collect(Collectors.toList()));;
		return state;
	}
	
	private Settings settings;
	private ProvinceMechanic parent;
	private ProvinceState.Builder state;
	
	public Province(ProvinceMechanic parent, Settings settings, ProvinceState.Builder state) {
		this.settings = settings;
		this.state = state;
		this.parent = parent;
	}
	
	// Private
	
	private int getAllianceThreshold() {
		return getNetStability();
	}
	
	// Getters
	
	public ProvinceState.Builder getState() {
		return this.state;
	}

	public Settings getSettings() {
		return this.settings;
	}
	
	public int getInfluence() {
		return this.state.getInfluence();
	}
	
	public int getNetStability() {
		int stab = this.settings.getSettings().getStabilityBase();
		if(this.hasStrongGov())
			stab++;
		if(this.state.hasDissidents())
			stab--;
// Province Leader mechanic needed
//		if(this.state.hasLeader())
//			mod++;
		return stab;
	}
	
	public boolean hasStrongGov() {
		if(this.state.getGov() == Government.DEMOCRACY ||
		   this.state.getGov() == Government.AUTOCRACY ||
		   this.state.getGov() == Government.COMMUNISM) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean hasDissidents() {
		return this.state.hasDissidents();
	}
	
	public Player getAlly() {
		int infl = this.state.getInfluence();
		if(infl > this.getAllianceThreshold() &&
		   this.state.getGov() != Government.COMMUNISM &&
		   this.state.getGov() != Government.COLONY)
			return Player.USA;
		else if(infl < -this.getAllianceThreshold() &&
			    this.state.getGov() != Government.DEMOCRACY &&
			    this.state.getGov() != Government.COLONY)
			return Player.USSR;
		return null;
	}
	
	public boolean isAdj(Player player) {
		for(ProvinceId id : this.settings.settings.getAdjacencyList()) {
			Province p = parent.getProvince(id);
			if((p.getInfluence() > 0 && player == Player.USA) ||
			   (p.getInfluence() < 0 && player == Player.USSR)) {
				return true;
			}
		}
		return false;
	}
	
	// Logic
	
	public void influence(int magnitude) {
		this.state.setInfluence(this.getState().getInfluence() + magnitude);
	}
	
	public void addDissidents(Dissidents.Builder diss) {
		this.state.setDissidents(diss);
	}
}
