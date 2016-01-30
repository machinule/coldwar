package com.berserkbentobox.coldwar.logic;

import com.berserkbentobox.coldwar.GameStateOuterClass.GameState;
import com.berserkbentobox.coldwar.logic.mechanics.HeatMechanic;
import com.berserkbentobox.coldwar.logic.mechanics.PseudorandomMechanic;
import com.berserkbentobox.coldwar.logic.mechanics.Superpower.SuperpowerMechanic;
import com.berserkbentobox.coldwar.logic.mechanics.deterrance.DeterrenceMechanic;
import com.berserkbentobox.coldwar.logic.mechanics.technology.TechnologyMechanic;
import com.berserkbentobox.coldwar.logic.mechanics.treaty.TreatyMechanic;

public class Mechanics {
	private HeatMechanic heat;
	private TechnologyMechanic technology;
	private SuperpowerMechanic superpower;
	private PseudorandomMechanic pseudorandom;
	private TreatyMechanic treaty;
	private DeterrenceMechanic deterrence;
	
	public Mechanics(MechanicSettings settings, GameState state) {
		this.technology = new TechnologyMechanic(settings.getTechnology(), state);
		this.pseudorandom = new PseudorandomMechanic(settings.getPseudorandom(), state);
		this.superpower = new SuperpowerMechanic(settings.getSuperpower(), state);
		this.heat = new HeatMechanic(settings.getHeat(), state);
		this.treaty = new TreatyMechanic(settings.getTreaty(), state);
		this.deterrence = new DeterrenceMechanic(settings.getDeterrence(), state);
	}
	
	public TechnologyMechanic getTechnology() {
		return this.technology;
	}	
	
	public PseudorandomMechanic getPseudorandom() {
		return this.pseudorandom;
	}
	
	public SuperpowerMechanic getSuperpower() {
		return this.superpower;
	
	}
	
	public HeatMechanic getHeat() {
		return this.heat;
	}

	public DeterrenceMechanic getDeterrence() {
		return this.deterrence;
	}

	public TreatyMechanic getTreaty() {
		return this.treaty;
	}

	public GameState buildState() {
		GameState.Builder state = GameState.newBuilder();
		state.setTechnologyState(this.getTechnology().buildState());
		state.setPseudorandomState(this.getPseudorandom().buildState());
		state.setHeatState(this.getHeat().buildState());
		state.setSuperpowerState(this.getSuperpower().buildState());
		state.setDeterrenceState(this.getDeterrence().buildState());
		state.setTreatyState(this.getTreaty().buildState());
		return state.build();
	}
}
