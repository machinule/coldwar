package com.berserkbentobox.coldwar.logic;

import com.berserkbentobox.coldwar.GameStateOuterClass.GameState;
import com.berserkbentobox.coldwar.logic.mechanics.crisis.CrisisMechanic;
import com.berserkbentobox.coldwar.logic.mechanics.deterrance.DeterrenceMechanic;
import com.berserkbentobox.coldwar.logic.mechanics.heat.HeatMechanic;
import com.berserkbentobox.coldwar.logic.mechanics.influencestore.InfluenceStoreMechanic;
import com.berserkbentobox.coldwar.logic.mechanics.province.ProvinceMechanic;
import com.berserkbentobox.coldwar.logic.mechanics.pseudorandom.PseudorandomMechanic;
import com.berserkbentobox.coldwar.logic.mechanics.superpower.SuperpowerMechanic;
import com.berserkbentobox.coldwar.logic.mechanics.technology.TechnologyMechanic;
import com.berserkbentobox.coldwar.logic.mechanics.treaty.TreatyMechanic;
import com.berserkbentobox.coldwar.logic.mechanics.victory.VictoryMechanic;
import com.berserkbentobox.coldwar.logic.mechanics.year.YearMechanic;

public class Mechanics {
	private ProvinceMechanic provinces;
	private HeatMechanic heat;
	private TechnologyMechanic technology;
	private SuperpowerMechanic superpower;
	private PseudorandomMechanic pseudorandom;
	private TreatyMechanic treaty;
	private DeterrenceMechanic deterrence;
	private InfluenceStoreMechanic influence;
	private CrisisMechanic crisis;
	private YearMechanic year;
	private VictoryMechanic victory;
	
	public Mechanics(MechanicSettings settings, GameState state) {
		this.pseudorandom = new PseudorandomMechanic(this, settings.getPseudorandom(), state);
		
		this.provinces = new ProvinceMechanic(this, settings.getProvinces(), state);
		this.technology = new TechnologyMechanic(this, settings.getTechnology(), state);
		this.superpower = new SuperpowerMechanic(this, settings.getSuperpower(), state);
		this.heat = new HeatMechanic(this, settings.getHeat(), state);
		this.treaty = new TreatyMechanic(this, settings.getTreaty(), state);
		this.deterrence = new DeterrenceMechanic(this, settings.getDeterrence(), state);
		this.influence = new InfluenceStoreMechanic(this, settings.getInfluence(), state);
		this.crisis = new CrisisMechanic(this, settings.getCrisis(), state);
		this.year = new YearMechanic(this, settings.getYear(), state);
		this.victory = new VictoryMechanic(this, settings.getVictory(), state);
	}
	
	public ProvinceMechanic getProvinces() {
		return this.provinces;
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
	
	public YearMechanic getYear() {
		return this.year;
	}
	
	public VictoryMechanic getVictory() {
		return this.victory;
	}

	public GameState buildState() {
		GameState.Builder state = GameState.newBuilder();
		state.setPseudorandomState(this.getPseudorandom().buildState());
		
		state.setProvinceState(this.getProvinces().buildState());
		state.setTechnologyState(this.getTechnology().buildState());
		state.setHeatState(this.getHeat().buildState());
		state.setSuperpowerState(this.getSuperpower().buildState());
		state.setDeterrenceState(this.getDeterrence().buildState());
		state.setTreatyState(this.getTreaty().buildState());
		state.setInfluenceState(this.getInfluence().buildState());
		state.setCrisisState(this.getCrisis().buildState());
		state.setYearState(this.getYear().buildState());
		state.setVictoryState(this.getVictory().buildState());
		return state.build();
	}
}
