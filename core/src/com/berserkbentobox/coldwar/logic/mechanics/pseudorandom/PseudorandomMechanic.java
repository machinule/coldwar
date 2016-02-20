package com.berserkbentobox.coldwar.logic.mechanics.pseudorandom;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.berserkbentobox.coldwar.Logger;
import com.berserkbentobox.coldwar.GameSettingsOuterClass.GameSettingsOrBuilder;
import com.berserkbentobox.coldwar.GameStateOuterClass.GameStateOrBuilder;
import com.berserkbentobox.coldwar.Pseudorandom.PseudorandomMechanicSettings;
import com.berserkbentobox.coldwar.Pseudorandom.PseudorandomMechanicState;
import com.berserkbentobox.coldwar.logic.Mechanic;
import com.berserkbentobox.coldwar.logic.Mechanics;
import com.berserkbentobox.coldwar.logic.Status;

public class PseudorandomMechanic extends Mechanic {

	public static class Settings {
		
		private GameSettingsOrBuilder gameSettings;
		private PseudorandomMechanicSettings settings;
		
		public Settings(GameSettingsOrBuilder gameSettings) {
			this.gameSettings = gameSettings;
			this.settings = gameSettings.getPseudorandomSettings();
		}
		
		public Status validate() {
			return Status.OK;
		}
		
		public PseudorandomMechanicState initialState() {
			PseudorandomMechanicState.Builder state = PseudorandomMechanicState.newBuilder();
			state.setSeed(this.settings.getInitSeed());
			return state.build();
		}
	}
	
	private Settings settings;
	private PseudorandomMechanicState.Builder state;
	private Random random;
	
	public PseudorandomMechanic(Mechanics mechanics, Settings settings, GameStateOrBuilder state) {
		super(mechanics);
		this.settings = settings;
		this.state = state.getPseudorandomState().toBuilder();
		this.random = new Random(this.state.getSeed());
	}
		
	public Status validate() {
		return Status.OK;
	}
	
	public Settings getSettings() {
		return this.settings;
	}
	
	public boolean happens(int chance) {
		return random.nextInt(1000000) < chance;
	}
	
	public int roll(List<Integer> chances) {
		Logger.Dbg("Rolling on " + chances.size() + " choices");
		int total = 0;
		List<Integer> weightedChances = new ArrayList<Integer>();
		weightedChances.add(0);
		for (int c : chances) {
			weightedChances.add(total+c);
			total = total + c;
		}
		int result = random.nextInt(total);
		Logger.Dbg("Roll result: " + result);
		for (int i = 1; i<weightedChances.size()-1; i++) {
			if (result >= weightedChances.get(i-1) && result < weightedChances.get(i)) {
				Logger.Dbg("Returning " + (i-1));
				return i-1;
			}
		}
		Logger.Dbg("Returning " + (weightedChances.size()-2));
		return weightedChances.get(weightedChances.size()-2);
	}
	
	// Leader-specific
	// TODO: Move to more appropriate location?
	public boolean dies(int age) {
		int start = 45;
		int escalate = 60;
		if(age < escalate && age > start) {
			return this.happens(10000);
		} else if(age >= escalate) {
			int exp = ((age - escalate) / 5) + 2;
			Logger.Dbg(exp + " at age " + age);
			return this.happens(10000*exp);
		} else {
			return false;
		}
	}
	
	public void reseed() {
		this.state.setSeed(this.random.nextLong());
	}
	
	public PseudorandomMechanicState buildState() {
		return this.state.build();
	}
}
