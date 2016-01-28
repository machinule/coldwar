package com.berserkbentobox.coldwar.logic.mechanics;

import java.util.Random;

import com.berserkbentobox.coldwar.GameSettingsOuterClass.GameSettingsOrBuilder;
import com.berserkbentobox.coldwar.GameStateOuterClass.GameStateOrBuilder;
import com.berserkbentobox.coldwar.Pseudorandom.PseudorandomMechanicSettings;
import com.berserkbentobox.coldwar.Pseudorandom.PseudorandomMechanicState;
import com.berserkbentobox.coldwar.logic.Status;

public class PseudorandomMechanic {

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
	
	public PseudorandomMechanic(Settings settings, GameStateOrBuilder state) {
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
	
	public void reseed() {
		this.state.setSeed(this.random.nextLong());
	}
	
	public PseudorandomMechanicState buildState() {
		return this.state.build();
	}
}
