package com.berserkbentobox.coldwar.logic.mechanics.influencestore;

import com.berserkbentobox.coldwar.GameSettingsOuterClass.GameSettingsOrBuilder;
import com.berserkbentobox.coldwar.GameStateOuterClass.GameStateOrBuilder;
import com.berserkbentobox.coldwar.Influence.InfluenceMechanicSettings;
import com.berserkbentobox.coldwar.Influence.InfluenceMechanicState;
import com.berserkbentobox.coldwar.logic.Status;
import com.berserkbentobox.coldwar.logic.Client.Player;

public class InfluenceStoreMechanic {
	
	public static class Settings {
		
		private GameSettingsOrBuilder gameSettings;
		private InfluenceMechanicSettings settings;
		private InfluenceStore.Settings usaSettings;
		private InfluenceStore.Settings ussrSettings;
		
		public Settings(GameSettingsOrBuilder gameSettings) {
			this.gameSettings = gameSettings;
			this.settings = gameSettings.getInfluenceSettings();
			this.usaSettings = new InfluenceStore.Settings(this, this.settings.getUsaSettings());
			this.ussrSettings = new InfluenceStore.Settings(this, this.settings.getUssrSettings());
		}
		
		public Status validate() {
			return Status.OK;
		}
		
		public InfluenceStore.Settings getSettings(Player player) {
			if (player == Player.USA) {
				return this.getUsaSettings();
			} else {
				return this.getUssrSettings();
			}
		}
				
		private InfluenceStore.Settings getUssrSettings() {
			return this.usaSettings;
		}

		private InfluenceStore.Settings getUsaSettings() {
			return this.ussrSettings;
		}

		public InfluenceMechanicState initialState() {
			InfluenceMechanicState.Builder state = InfluenceMechanicState.newBuilder();
			state.setUsaState(this.usaSettings.initialState());
			state.setUssrState(this.ussrSettings.initialState());
			return state.build();
		}
	}
	
	private Settings settings;
	private InfluenceMechanicState.Builder state;
	private InfluenceStore usaStore;
	private InfluenceStore ussrStore;
	
	public InfluenceStoreMechanic(Settings settings, GameStateOrBuilder state) {
		this.settings = settings;
		this.state = state.getInfluenceState().toBuilder();
		this.usaStore = new InfluenceStore(this, this.getSettings().getUsaSettings(), this.state.getUsaStateBuilder());
		this.ussrStore = new InfluenceStore(this, this.getSettings().getUssrSettings(), this.state.getUssrStateBuilder());
	}
		
	public Status validate() {
		return Status.OK;
	}
	
	public Settings getSettings() {
		return this.settings;
	}
	
	public InfluenceMechanicState buildState() {
		return this.state.build();
	}
	
	private InfluenceStore getInfluenceStore(Player player) {
		if (player == Player.USA) {
			return this.usaStore;
		} else {
			return this.ussrStore;
		}
	}
	
	// Logic
	
}
