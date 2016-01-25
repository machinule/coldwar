package com.berserkbentobox.coldwar.logic;

import com.berserkbentobox.coldwar.GameStateOuterClass.GameState;
import com.berserkbentobox.coldwar.logic.mechanics.PseudorandomMechanic;
import com.berserkbentobox.coldwar.logic.mechanics.TechnologyMechanic;

public class Mechanics {
	private TechnologyMechanic technology;
	private PseudorandomMechanic pseudorandom;
	
	public Mechanics(MechanicSettings settings, GameState state) {
		this.technology = new TechnologyMechanic(settings.getTechnology(), state);
		this.pseudorandom = new PseudorandomMechanic(settings.getPseudorandom(), state);
	}
	
	public TechnologyMechanic getTechnology() {
		return this.technology;
	}
	
	public PseudorandomMechanic getPseudorandom() {
		return this.pseudorandom;
	}
}
