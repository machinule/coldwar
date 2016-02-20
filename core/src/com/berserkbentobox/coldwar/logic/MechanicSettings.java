package com.berserkbentobox.coldwar.logic;

import com.berserkbentobox.coldwar.GameSettingsOuterClass.GameSettings;
import com.berserkbentobox.coldwar.logic.mechanics.crisis.CrisisMechanic;
import com.berserkbentobox.coldwar.logic.mechanics.deterrance.DeterrenceMechanic;
import com.berserkbentobox.coldwar.logic.mechanics.heat.HeatMechanic;
import com.berserkbentobox.coldwar.logic.mechanics.influencestore.InfluenceStoreMechanic;
import com.berserkbentobox.coldwar.logic.mechanics.province.ProvinceMechanic;
import com.berserkbentobox.coldwar.logic.mechanics.pseudorandom.PseudorandomMechanic;
import com.berserkbentobox.coldwar.logic.mechanics.superpower.SuperpowerMechanic;
import com.berserkbentobox.coldwar.logic.mechanics.technology.TechnologyMechanic;
import com.berserkbentobox.coldwar.logic.mechanics.treaty.TreatyMechanic;
import com.berserkbentobox.coldwar.logic.year.YearMechanic;

// MechanicSettings is a container for various MechanicSettings abstractions over the raw settings proto.
public class MechanicSettings {

	private ProvinceMechanic.Settings provinces;
	private HeatMechanic.Settings heat;
	private TechnologyMechanic.Settings technology;
	private PseudorandomMechanic.Settings pseudorandom;
	private SuperpowerMechanic.Settings superpower;
	private TreatyMechanic.Settings treaty;
	private DeterrenceMechanic.Settings deterrence;
	private InfluenceStoreMechanic.Settings influence;
	private CrisisMechanic.Settings crisis;
	private YearMechanic.Settings year;
	
	public MechanicSettings(GameSettings settings) {
		this.provinces = new ProvinceMechanic.Settings(settings);
		this.technology = new TechnologyMechanic.Settings(settings);
		this.pseudorandom = new PseudorandomMechanic.Settings(settings);
		this.superpower = new SuperpowerMechanic.Settings(settings);
		this.heat = new HeatMechanic.Settings(settings);
		this.deterrence = new DeterrenceMechanic.Settings(settings);
		this.treaty = new TreatyMechanic.Settings(settings);
		this.influence = new InfluenceStoreMechanic.Settings(settings);
		this.crisis = new CrisisMechanic.Settings(settings);
		this.year = new YearMechanic.Settings(settings);
	}
	
	public ProvinceMechanic.Settings getProvinces() {
		return this.provinces;
	}
	
	public TechnologyMechanic.Settings getTechnology() {
		return this.technology;
	}
	
	public PseudorandomMechanic.Settings getPseudorandom() {
		return this.pseudorandom;
	}
	
	public SuperpowerMechanic.Settings getSuperpower() {
		return this.superpower;
	}

	public HeatMechanic.Settings getHeat() {
		return this.heat;
	}

	public TreatyMechanic.Settings getTreaty() {
		return this.treaty;
	}
	
	public DeterrenceMechanic.Settings getDeterrence() {
		return this.deterrence;
	}

	public InfluenceStoreMechanic.Settings getInfluence() {
		return this.influence;
	}
	
	public CrisisMechanic.Settings getCrisis() {
		return this.crisis;
	}
	
	public YearMechanic.Settings getYear() {
		return this.year;
	}
}
