package com.berserkbentobox.coldwar.logic.mechanics.superpower;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.berserkbentobox.coldwar.Superpower.UsaSettings;
import com.berserkbentobox.coldwar.Superpower.UsaState;
import com.berserkbentobox.coldwar.Superpower.UsaLeaderSettings;
import com.berserkbentobox.coldwar.Superpower.UsaLeaderState;
import com.berserkbentobox.coldwar.logic.Status;
import com.berserkbentobox.coldwar.logic.mechanics.pseudorandom.PseudorandomMechanic;

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
			return state.build();
		}
	}

	private Settings settings;
	private SuperpowerMechanic parent;
	private UsaState.Builder state;
	private Map<String, UsaLeader> leaders;
	private ArrayList<UsaLeader> candidates;
	
	public Usa(SuperpowerMechanic parent, Settings settings, UsaState.Builder state) {
		this.settings = settings;
		this.state = state;
		this.parent = parent;
		this.leaders = new LinkedHashMap<String, UsaLeader>();
		for (UsaLeaderState.Builder ls : this.state.getLeaderBuilderList()) {
			UsaLeader l = new UsaLeader(this, parent.getSettings().getUsaSettings().getLeaderSettings(ls.getName()), ls);
			this.leaders.put(l.getState().getName(), l);
		}
		//sdfsd
		this.state.setPresident(settings.getSettings().getPresidencySettings().getInitPresident());
		this.state.setVicePresident(settings.getSettings().getPresidencySettings().getInitVicePresident());
		candidates = new ArrayList<UsaLeader>();
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
	
	public Collection<UsaLeader> getPresidentialEligible(int year) {
		Collection<UsaLeader> ret = new ArrayList<UsaLeader>();
		for (UsaLeader l : this.leaders.values()) {
			UsaLeaderSettings settings = l.getSettings().getSettings();
			UsaLeaderState.Builder state = l.getState();
			if (settings.getStartYear() < year &&
				settings.getEndYear() > year &&
				state.getNumTermsAsPresident() < 2) {
				ret.add(l);
			}
		}
		return ret;
	}

	public Collection<UsaLeader> getVicePresidentialEligible(int year) {
		Collection<UsaLeader> ret = new ArrayList<UsaLeader>();
		for (UsaLeader l : this.leaders.values()) {
			UsaLeaderSettings settings = l.getSettings().getSettings();
			UsaLeaderState.Builder state = l.getState();
			if (settings.getStartYear() - settings.getViceYears() < year &&
				settings.getEndYear() > year &&
				state.getNumTermsAsPresident() < this.settings.getSettings().getPresidencySettings().getMaxTerms()) {
				ret.add(l);
			}
		}
		return ret;
	}
	
	public UsaLeader getLeader(String name) {
		return this.leaders.get(name);
	}	
	
	// Logic
	
	public void clearCandidates() {
		candidates.clear();
	}
	
	public void setCandidate(String name) {
		candidates.add(leaders.get(name));
	}
	
	public UsaLeader electPresident(int year, ArrayList<UsaLeader> finalCandidates, PseudorandomMechanic pseudorandomMechanic) {
		UsaLeader ret;
		int base = 500000;
		int vp_effect = 50000; // TODO: Settings value
		int cand_0_vp = finalCandidates.get(0).getState().getNumTermsAsVicePresident() * vp_effect;
		int cand_1_vp = -(finalCandidates.get(1).getState().getNumTermsAsVicePresident() * vp_effect);
		if (pseudorandomMechanic.happens(base + cand_0_vp + cand_1_vp)) {
			ret = finalCandidates.get(0);
		} else {
			ret = finalCandidates.get(1);
		}
		this.clearCandidates();
		return ret;
	}
}
