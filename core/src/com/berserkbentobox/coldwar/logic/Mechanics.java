package com.berserkbentobox.coldwar.logic;

import com.berserkbentobox.coldwar.GameStateOuterClass.GameState;
import com.berserkbentobox.coldwar.logic.mechanics.crisis.CrisisMechanic;
import com.berserkbentobox.coldwar.logic.mechanics.deterrance.DeterrenceMechanic;
import com.berserkbentobox.coldwar.logic.mechanics.heat.HeatMechanic;
import com.berserkbentobox.coldwar.logic.mechanics.influencestore.InfluenceStoreMechanic;
import com.berserkbentobox.coldwar.logic.mechanics.pseudorandom.PseudorandomMechanic;
import com.berserkbentobox.coldwar.logic.mechanics.superpower.SuperpowerMechanic;
import com.berserkbentobox.coldwar.logic.mechanics.technology.TechnologyMechanic;
import com.berserkbentobox.coldwar.logic.mechanics.treaty.TreatyMechanic;

public class Mechanics {
	private HeatMechanic heat;
	private TechnologyMechanic technology;
	private SuperpowerMechanic superpower;
	private PseudorandomMechanic pseudorandom;
	private TreatyMechanic treaty;
	private DeterrenceMechanic deterrence;
	private InfluenceStoreMechanic influence;
	private CrisisMechanic crisis;
	
	public Mechanics(MechanicSettings settings, GameState state) {
		this.technology = new TechnologyMechanic(settings.getTechnology(), state);
		this.pseudorandom = new PseudorandomMechanic(settings.getPseudorandom(), state);
		this.superpower = new SuperpowerMechanic(settings.getSuperpower(), state);
		this.heat = new HeatMechanic(settings.getHeat(), state);
		this.treaty = new TreatyMechanic(settings.getTreaty(), state);
		this.deterrence = new DeterrenceMechanic(settings.getDeterrence(), state);
		this.influence = new InfluenceStoreMechanic(settings.getInfluence(), state);
		this.crisis = new CrisisMechanic(settings.getCrisis(), state);
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

	public InfluenceStoreMechanic getInfluence() {
		return this.influence;
	}
	
	public CrisisMechanic getCrisis() {
		return this.crisis;
	}

	public GameState buildState() {
		GameState.Builder state = GameState.newBuilder();
		state.setTechnologyState(this.getTechnology().buildState());
		state.setPseudorandomState(this.getPseudorandom().buildState());
		state.setHeatState(this.getHeat().buildState());
		state.setSuperpowerState(this.getSuperpower().buildState());
		state.setDeterrenceState(this.getDeterrence().buildState());
		state.setTreatyState(this.getTreaty().buildState());
		state.setInfluenceState(this.getInfluence().buildState());
		state.setCrisisState(this.getCrisis().buildState());
		return state.build();
	}
}
