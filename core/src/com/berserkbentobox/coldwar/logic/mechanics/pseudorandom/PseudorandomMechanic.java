package com.berserkbentobox.coldwar.logic.mechanics.pseudorandom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
	
	public Object howIRoll(Map<Object, Integer> possibilities) {
		Object[] idx = new Object[possibilities.size()];
		int[] pdf = new int[possibilities.size()];

		int i = 0;
		int sum = 0;
		for (Map.Entry<Object, Integer> entry : possibilities.entrySet()) {
			if (entry.getValue() <= 0) continue;
			sum += entry.getValue();
			pdf[i] = sum;
			idx[i] = entry.getKey();
			i++;
		}
		if (sum == 0) return null;
		int choice = Arrays.binarySearch(pdf, 0, i, random.nextInt(sum) + 1);
		if (choice >= 0) return idx[choice];
		return idx[-(choice + 1)];
	}
	
	public Object roll(LinkedHashMap<Object, Integer> chances) {
		List<Integer> weightedChances = new ArrayList<Integer>();
		int total = 0;
		weightedChances.add(0);
		for (int c : chances.values()) {
			weightedChances.add(total+c);
			total = total + c;
		}
		List<Object> keys = new ArrayList<Object>(chances.keySet());
		int result = random.nextInt(total);
		for (int i = 1; i<weightedChances.size()-1; i++) {
			if (result >= weightedChances.get(i-1) && result < weightedChances.get(i)) {
				return keys.get(i-1);
			}
		}
		return keys.get(weightedChances.size()-2);
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
