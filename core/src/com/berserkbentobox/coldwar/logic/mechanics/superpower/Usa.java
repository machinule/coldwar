package com.berserkbentobox.coldwar.logic.mechanics.superpower;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.berserkbentobox.coldwar.Superpower.UsaSettings;
import com.berserkbentobox.coldwar.Superpower.UsaSettingsOrBuilder;
import com.berserkbentobox.coldwar.Superpower.UsaState;
import com.berserkbentobox.coldwar.Technology.TechnologyState;
import com.berserkbentobox.coldwar.Superpower.UsaLeaderSettings;
import com.berserkbentobox.coldwar.Superpower.UsaLeaderState;
import com.berserkbentobox.coldwar.logic.Status;
import com.berserkbentobox.coldwar.logic.mechanics.technology.Technology;
import com.berserkbentobox.coldwar.logic.mechanics.technology.TechnologyMechanic;

public class Usa {
	public static class Settings {

		private SuperpowerMechanic.Settings parent;
		private UsaSettings settings;
		private Map<String, UsaLeader.Settings> leaderSettings;
		
		public Settings(SuperpowerMechanic.Settings parent, UsaSettings settings) {
			this.settings = settings;
			this.parent = parent;
			this.leaderSettings = new HashMap<String, UsaLeader.Settings>();
			for (UsaLeaderSettings l : this.settings.getLeaderSettingsList()) {
				UsaLeader.Settings ls = new UsaLeader.Settings(l);
				this.leaderSettings.put(ls.getSettings().getName(), ls);
			}
		}
		
		public Status validate() {
			return Status.OK;
		}
		
		public UsaSettings getSettings() {
			return this.settings;
		}
		
		public Collection<UsaLeader.Settings> getLeaderSettings() {
			return this.leaderSettings.values();
		}
		
		public UsaLeader.Settings getLeaderSettings(String name) {
			return this.leaderSettings.get(name);
		}
		
		public UsaState initialState() {
			UsaState.Builder state = UsaState.newBuilder();
			state
				.setPresident(settings.getPresidencySettings().getInitPresident())
				.setVicePresident(settings.getPresidencySettings().getInitVicePresident())
				.setPatriotism(settings.getInitPatriotism());
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
	private SuperpowerMechanic parent;
	private UsaState.Builder state;
	private Map<String, UsaLeader> leaders;
	
	public Usa(SuperpowerMechanic parent, Settings settings, UsaState.Builder state) {
		this.settings = settings;
		this.state = state;
		this.parent = parent;
		this.leaders = new LinkedHashMap<String, UsaLeader>();
		for (UsaLeaderState.Builder ls : this.state.getLeaderBuilderList()) {
			UsaLeader l = new UsaLeader(this, parent.getSettings().getUsaSettings().getLeaderSettings(ls.getName()), ls);
			this.leaders.put(l.getState().getName(), l);
		}
	}
	
	// Getters
	
	public UsaState.Builder getState() {
		return this.state;
	}

	public Settings getSettings() {
		return this.settings;
	}

	public Collection<UsaLeader> getLeaders() {
		return this.leaders.values();
	}
	
	public UsaLeader getLeader(String name) {
		return this.leaders.get(name);
	}
}
