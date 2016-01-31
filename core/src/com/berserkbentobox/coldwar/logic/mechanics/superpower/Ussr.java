package com.berserkbentobox.coldwar.logic.mechanics.superpower;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.berserkbentobox.coldwar.Superpower.UssrSettings;
import com.berserkbentobox.coldwar.Superpower.UssrState;
import com.berserkbentobox.coldwar.Superpower.UssrLeaderSettings;
import com.berserkbentobox.coldwar.Superpower.UssrLeaderState;
import com.berserkbentobox.coldwar.logic.Status;

public class Ussr {
	public static class Settings {

		private SuperpowerMechanic.Settings parent;
		private UssrSettings settings;
		private Map<String, UssrLeader.Settings> leaderSettings;
		
		public Settings(SuperpowerMechanic.Settings parent, UssrSettings settings) {
			this.settings = settings;
			this.parent = parent;
			this.leaderSettings = new HashMap<String, UssrLeader.Settings>();
			for (UssrLeaderSettings l : this.settings.getLeaderSettingsList()) {
				UssrLeader.Settings ls = new UssrLeader.Settings(l);
				this.leaderSettings.put(ls.getSettings().getName(), ls);
			}
		}
		
		public Status validate() {
			return Status.OK;
		}
		
		public UssrSettings getSettings() {
			return this.settings;
		}
		
		public Collection<UssrLeader.Settings> getLeaderSettings() {
			return this.leaderSettings.values();
		}
		
		public UssrLeader.Settings getLeaderSettings(String name) {
			return this.leaderSettings.get(name);
		}
		
		public UssrState initialState() {
			UssrState.Builder state = UssrState.newBuilder();
			state
					.setPremier(settings.getSecretariatSettings().getInitPremier())
					.addAllTroika(settings.getSecretariatSettings().getInitTroikaList())
					.setPartyUnity(settings.getInitPartyUnity());
					for (UssrLeader.Settings ls : this.getLeaderSettings()) {
						state.addLeader(ls.initialState());
					}
			return state.build();
		}
	}

	private Settings settings;
	private SuperpowerMechanic parent;
	private UssrState.Builder state;
	private Map<String, UssrLeader> leaders;
	
	public Ussr(SuperpowerMechanic parent, Settings settings, UssrState.Builder state) {
		this.settings = settings;
		this.state = state;
		this.parent = parent;
		this.leaders = new LinkedHashMap<String, UssrLeader>();
		for (UssrLeaderState.Builder ls : this.state.getLeaderBuilderList()) {
			UssrLeader l = new UssrLeader(this, parent.getSettings().getUssrSettings().getLeaderSettings(ls.getName()), ls);
			this.leaders.put(l.getState().getName(), l);
		}
	}
	
	// Getters
	
	public UssrState.Builder getState() {
		return this.state;
	}

	public Settings getSettings() {
		return this.settings;
	}

	public Collection<UssrLeader> getLeaders() {
		return this.leaders.values();
	}
	
	public UssrLeader getLeader(String name) {
		return this.leaders.get(name);
	}
}
