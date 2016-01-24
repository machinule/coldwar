package com.berserkbentobox.coldwar.logic;

import com.berserkbentobox.coldwar.GameStateOuterClass.GameState;
import com.berserkbentobox.coldwar.logic.mechanics.TechnologyMechanic;

public class Mechanics {
	private TechnologyMechanic technology;
	
	public Mechanics(MechanicSettings settings, GameState state) {
		this.technology = new TechnologyMechanic(settings.getTechnology(), state);
	}
	
	public TechnologyMechanic getTechnology() {
		return this.technology;
	}
}
