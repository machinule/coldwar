package com.berserkbentobox.coldwar.logic;

import com.berserkbentobox.coldwar.GameSettingsOuterClass.GameSettings;
import com.berserkbentobox.coldwar.logic.mechanics.PseudorandomMechanic;
import com.berserkbentobox.coldwar.logic.mechanics.TechnologyMechanic;

// MechanicSettings is a container for various MechanicSettings abstractions over the raw settings proto.
public class MechanicSettings {

	private TechnologyMechanic.Settings technology;
	private PseudorandomMechanic.Settings pseudorandom;
	
	public MechanicSettings(GameSettings settings) {
		this.technology = new TechnologyMechanic.Settings(settings);
		this.pseudorandom = new PseudorandomMechanic.Settings(settings);
	}
	
	public TechnologyMechanic.Settings getTechnology() {
		return this.technology;
	}
	
	public PseudorandomMechanic.Settings getPseudorandom() {
		return this.pseudorandom;
	}
}
