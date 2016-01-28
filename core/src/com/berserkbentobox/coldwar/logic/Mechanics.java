package com.berserkbentobox.coldwar.logic;

import com.berserkbentobox.coldwar.GameStateOuterClass.GameState;
import com.berserkbentobox.coldwar.logic.mechanics.PseudorandomMechanic;
import com.berserkbentobox.coldwar.logic.mechanics.SuperpowerMechanic;
import com.berserkbentobox.coldwar.logic.mechanics.TechnologyMechanic;

public class Mechanics {
	private TechnologyMechanic technology;
	private SuperpowerMechanic superpower;
	private PseudorandomMechanic pseudorandom;
	
	public Mechanics(MechanicSettings settings, GameState state) {
		this.technology = new TechnologyMechanic(settings.getTechnology(), state);
		this.pseudorandom = new PseudorandomMechanic(settings.getPseudorandom(), state);
		this.superpower = new SuperpowerMechanic(settings.getSuperpower(), state);
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
}
