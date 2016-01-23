package com.berserkbentobox.coldwar.logic.mechanics;

import com.berserkbentobox.coldwar.GameSettingsOuterClass.GameSettingsOrBuilder;
import com.berserkbentobox.coldwar.GameStateOuterClass.GameStateOrBuilder;
import com.berserkbentobox.coldwar.Heat.HeatMechanicSettings;
import com.berserkbentobox.coldwar.Heat.HeatMechanicState;
import com.berserkbentobox.coldwar.logic.Status;

public class Heat {

	private HeatMechanicState.Builder state;
	private HeatMechanicSettings settings;
	
	public Heat(GameSettingsOrBuilder settings) {
		this.settings = settings.getHeatSettings();
		this.state = HeatMechanicState.newBuilder();
		this.state.setHeat(settings.getHeatSettings().getInitHeat());
	}
	
	public Heat(GameStateOrBuilder state) {
		this.state = state.getHeatState().toBuilder();
		this.settings = state.getSettings().getHeatSettings();
	}
	
	enum Error {
		MIN_HEAT_GREATER_THAN_MAX_HEAT,
		HEAT_GREATER_THAN_MAX_HEAT,
		HEAT_LESS_THAN_MIN_HEAT
	}
	
	public Status validate() {
		if (this.settings.getMinHeat() > this.settings.getMaxHeat()) {
			return new Status(false, Error.MIN_HEAT_GREATER_THAN_MAX_HEAT.ordinal());
		}
		if (this.heat() > this.max()) {
			return new Status(false, Error.HEAT_GREATER_THAN_MAX_HEAT.ordinal());
		}
		if (this.heat() < this.min()) {
			return new Status(false, Error.HEAT_LESS_THAN_MIN_HEAT.ordinal());
		}
		return Status.OK;
	}

	public HeatMechanicState state() {
		return this.state.build();
	}
	
	public int heat() {
		return this.state.getHeat();
	}
	
	public int min() {
		return this.settings.getMinHeat();
	}
	
	public int max() {
		return this.settings.getMaxHeat();
	}
	
	public int decay() {
		return this.settings.getHeatDecay();
	}
	
	public synchronized void increase(int i) {
		this.state.setHeat(this.state.getHeat() + i);
	}
	
	public void decrease(int i) {
		increase(-i);
	}
	
	public synchronized void normalize() {
		this.state.setHeat(Math.min(Math.max(this.state.getHeat(), this.min()), this.max()));
	}
}
