package com.berserkbentobox.coldwar.logic.mechanics.victory;

import com.berserkbentobox.coldwar.GameSettingsOuterClass.GameSettingsOrBuilder;
import com.berserkbentobox.coldwar.GameStateOuterClass.GameStateOrBuilder;
import com.berserkbentobox.coldwar.Victory.VictoryMechanicSettings;
import com.berserkbentobox.coldwar.Victory.VictoryMechanicState;
import com.berserkbentobox.coldwar.logic.Mechanic;
import com.berserkbentobox.coldwar.logic.Mechanics;
import com.berserkbentobox.coldwar.logic.Status;
import com.berserkbentobox.coldwar.logic.Client.Player;

public class VictoryMechanic extends Mechanic {
	
	public static class Settings {
		
		private GameSettingsOrBuilder gameSettings;
		private VictoryMechanicSettings settings;
		private VictoryPointStore.Settings usaStoreSettings;
		private VictoryPointStore.Settings ussrStoreSettings;
		
		public Settings(GameSettingsOrBuilder gameSettings) {
			this.gameSettings = gameSettings;
			this.settings = gameSettings.getVictorySettings();
			this.usaStoreSettings = new VictoryPointStore.Settings(this, this.settings.getUsaStore());
			this.ussrStoreSettings = new VictoryPointStore.Settings(this, this.settings.getUssrStore());
		}
		
		public Status validate() {
			return Status.OK;
		}
		
		public VictoryMechanicSettings getSettings() {
			return this.settings;
		}
		
		public VictoryMechanicState initialState() {
			VictoryMechanicState.Builder state = VictoryMechanicState.newBuilder();
			state.setUsaStore(this.getUsaStoreSettings().initialState());
			state.setUssrStore(this.getUssrStoreSettings().initialState());
			return state.build();
		}

		public VictoryPointStore.Settings getStoreSettings(Player player) {
			if (player == Player.USA) {
				return this.getUsaStoreSettings();
			} else {
				return this.getUssrStoreSettings();
			}
		}
		
		public VictoryPointStore.Settings getUsaStoreSettings() {
			return this.usaStoreSettings;
		}

		public VictoryPointStore.Settings getUssrStoreSettings() {
			return this.ussrStoreSettings;
		}
	}
	
	private Settings settings;
	private VictoryMechanicState.Builder state;
	private VictoryPointStore usaStore;
	private VictoryPointStore ussrStore;
	
	public VictoryMechanic(Mechanics mechanics, Settings settings, GameStateOrBuilder state) {
		super(mechanics);
		this.settings = settings;
		this.state = state.getVictoryState().toBuilder();
		this.usaStore = new VictoryPointStore(this, this.settings.getUsaStoreSettings(), this.state.getUsaStoreBuilder());
		this.ussrStore = new VictoryPointStore(this, this.settings.getUssrStoreSettings(), this.state.getUssrStoreBuilder());
	}
		
	public Status validate() {
		return Status.OK;
	}
	
	public Settings getSettings() {
		return this.settings;
	}
	
	public VictoryMechanicState buildState() {
		return this.state.build();
	}
	
	public VictoryPointStore getStore(Player player) {
		if (player == Player.USA) {
			return this.getUsaStore();
		} else {
			return this.getUssrStore();
		}
	}

	public VictoryPointStore getUsaStore() {
		return this.usaStore;
	}
	
	public VictoryPointStore getUssrStore() {
		return this.ussrStore;
	}
	
	public void addVictoryPoints(Player player, int magnitude) {
		this.getStore(player).addVictoryPoints(magnitude);
	}

	public boolean hasUsaWon() {
		return this.usaStore.hasWon();
	}

	public boolean hasUssrWon() {
		return this.ussrStore.hasWon();
	}
	
	public boolean hasPlayerWon(Player player) {
		if (player == Player.USA) {
			return this.hasUsaWon();
		} else {
			return this.hasUssrWon();
		}
	}

	public boolean hasEitherPlayerWon() {
		return hasUsaWon() || hasUssrWon();
	}

}
