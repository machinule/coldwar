package com.berserkbentobox.coldwar.logic.mechanics.superpower;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.berserkbentobox.coldwar.Superpower.UsaSettings;
import com.berserkbentobox.coldwar.Superpower.UsaSettingsOrBuilder;
import com.berserkbentobox.coldwar.Superpower.UsaState;
import com.berserkbentobox.coldwar.Superpower.UsaLeaderSettings;
import com.berserkbentobox.coldwar.logic.Status;

public class Usa {
	public static class Settings {
		
		private UsaSettings settings;
		private Map<String, UsaLeader.Settings> usaLeaderSettings;
		
		public Settings(UsaSettings settings) {
			this.settings = settings;
			this.usaLeaderSettings = new HashMap<String, UsaLeader.Settings>();
			for (UsaLeaderSettings l : this.settings.getLeaderSettingsList()) {
				UsaLeader.Settings ls = new UsaLeader.Settings(l);
				this.usaLeaderSettings.put(ls.getSettings().getName(), ls);
			}
		}
		
		public Status validate() {
			return Status.OK;
		}
		
		public UsaSettings getSettings() {
			return this.settings;
		}
		
		public Collection<UsaLeader.Settings> getLeaderSettings() {
			return this.usaLeaderSettings.values();
		}
		
		public UsaState initialState() {
			UsaState.Builder state = UsaState.newBuilder();
			state
				.setPresident(settings.getPresidencySettings().getInitPresident())
				.setVicePresident(settings.getPresidencySettings().getInitVicePresident())
				.setPatriotism(settings.getInitPatriotism())
				.setInfluenceStore(InfluenceStore.buildInitialState(settings.getInfluenceStoreSettings()));;
				for (UsaLeader.Settings ls : this.getLeaderSettings()) {
					state.addLeader(ls.initialState());
				}
/*
			state
				.getUssrStateBuilder()
					.setPremier(settings.getUssrSettings().getSecretariatSettings().getInitPremier())
					.addAllTroika(settings.getUssrSettings().getSecretariatSettings().getInitTroikaList())
					.addAllLeader(settings.getUssrSettings().getLeaderSettingsList().stream().map(s -> UssrLeader.buildInitialState(s).build()).collect(Collectors.toList()))
					.setPartyUnity(settings.getUssrSettings().getInitPartyUnity())
					.setInfluenceStore(InfluenceStore.buildInitialState(settings.getUssrSettings().getInfluenceStoreSettings()));
			
*/
			return state.build();
		}
	}
	
	private Settings settings;
	private UsaState.Builder state;
	
	public Usa(Settings settings, UsaState.Builder state) {
		this.settings = settings;
		this.state = state;
	}
	
	public UsaState.Builder getState() {
		return this.state;
	}

	public Settings getSettings() {
		return this.settings;
	}
}
