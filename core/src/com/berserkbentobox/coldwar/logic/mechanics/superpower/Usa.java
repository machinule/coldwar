package com.berserkbentobox.coldwar.logic.mechanics.superpower;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.berserkbentobox.coldwar.Superpower.UsaSettings;
import com.berserkbentobox.coldwar.Superpower.UsaState;
import com.berserkbentobox.coldwar.Logger;
import com.berserkbentobox.coldwar.Superpower.UsaLeaderParty;
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
	
	public List<String> getPresidentialEligible(int year) {
		List<String> ret = new ArrayList<String>();
		for (UsaLeader l : this.leaders.values()) {
			UsaLeaderSettings settings = l.getSettings().getSettings();
			UsaLeaderState.Builder state = l.getState();
			if (settings.getStartYear() < year &&
				settings.getEndYear() > year &&
				state.getNumTermsAsPresident() < 2) {
				ret.add(l.getSettings().getSettings().getName());
			}
		}
		return ret;
	}
	
	public List<String> getPresidentialEligible(int year, UsaLeaderParty party) {
		List<String> eligible = getPresidentialEligible(year);
		List<String> ret = new ArrayList<String>();
		if(leaders.get(this.state.getPresident()).getState().getNumTermsAsPresident() == 1 &&
		   leaders.get(this.state.getPresident()).getSettings().getSettings().getParty() == party) {
			ret.add(this.state.getPresident());
		} else {
			for (String l : eligible) {
				if (leaders.get(l).getSettings().getSettings().getParty() == party)
						ret.add(l);
			}
		}
		return ret;
	}
	
	public List<String> getPresidentialEligible(int year, UsaLeaderParty party, int num, PseudorandomMechanic pseudorandom) {
		List<String> eligible = getPresidentialEligible(year, party);
		LinkedHashMap<Object, Integer> chances = new LinkedHashMap<Object, Integer>();
		List<String> ret = new ArrayList<String>();
		if (eligible.size() != 1) {
			for (String l : eligible) {
				chances.put(l, 1);
			}
			String result;
			for (int j = 0; j < num; j++) {
				result = (String) pseudorandom.roll(chances);
				ret.add(result);
				chances.remove(result);
			}
		} else {
			ret.add(eligible.get(0));
		}
		return ret;
	}

	public List<String> getVicePresidentialEligible(int year, UsaLeaderParty party) {
		List<String> ret = new ArrayList<String>();
		for (UsaLeader l : this.leaders.values()) {
			UsaLeaderSettings settings = l.getSettings().getSettings();
			UsaLeaderState.Builder state = l.getState();
			if (settings.getStartYear() - settings.getViceYears() < year &&
				settings.getEndYear() > year &&
				state.getNumTermsAsPresident() == 0 &&
				state.getNumTermsAsVicePresident() == 0 &&
				settings.getParty() == party) {
				ret.add(l.getSettings().getSettings().getName());
			}
		}
		return ret;
	}
	
	public UsaLeader getLeader(String name) {
		return this.leaders.get(name);
	}	
	
	// Logic
	
	private String rep_candidate = "";
	private String dem_candidate = "";
	
	public void clearCandidates() {
		rep_candidate = "";
		dem_candidate = "";
	}
	
	public boolean isReady(int year) {
		if(isElectionYear(year)) {
			if(rep_candidate != "" && dem_candidate != "")
				return true;
			return false;
		} else {
			return true;
		}
	}
	
	public boolean isElectionYear(int year) {
		return (year + 1) % 4 == 0;
	}
	
	public void setCandidate(String name) {
		Logger.Dbg("Setting candidate: " + name);
		if(leaders.get(name).getSettings().getSettings().getParty() == UsaLeaderParty.REPUBLICAN)
			rep_candidate = name;
		else
			dem_candidate = name;
	}
	
	public void elections(int year, PseudorandomMechanic pseudorandomMechanic) {
		String ret;
		int base = 500000;
		int vp_effect = 50000; // TODO: Settings value
		Logger.Dbg((year + 1) + " Election: " + rep_candidate + " vs " + dem_candidate);
		int cand_0_vp = leaders.get(rep_candidate).getState().getNumTermsAsVicePresident() * vp_effect;
		int cand_1_vp = -(leaders.get(dem_candidate).getState().getNumTermsAsVicePresident() * vp_effect);
		if (pseudorandomMechanic.happens(base + cand_0_vp + cand_1_vp)) {
			ret = rep_candidate;
		} else {
			ret = dem_candidate;
		}
		this.clearCandidates();
		Logger.Dbg("Winner: " + ret);
		boolean incumbent = getState().getPresident() == ret;
		getState().setPresident(ret);
		leaders.get(ret).getState().setNumTermsAsPresident(leaders.get(ret).getState().getNumTermsAsPresident()+1);
		chooseVicePresident(year, pseudorandomMechanic, incumbent);
	}
	
	public void chooseVicePresident(int year, PseudorandomMechanic pseudorandomMechanic, boolean incumbent) {
		if(!incumbent) {
			List<String> candidates = getVicePresidentialEligible(year, leaders.get(this.getState().getPresident()).getSettings().getSettings().getParty());
			LinkedHashMap<Object, Integer> candidateChances = new LinkedHashMap<Object, Integer>();
			for (String l : candidates) {
				candidateChances.put(l, 1);
			}
			String choice = (String) pseudorandomMechanic.roll(candidateChances);
			Logger.Dbg("Chosen vice president: " + choice);
			getState().setVicePresident(choice);
		}
			leaders.get(getState().getVicePresident()).getState().setNumTermsAsVicePresident(leaders.get(getState().getVicePresident()).getState().getNumTermsAsVicePresident()+1);
	}
	
	public void maybeKillPresident(int year, PseudorandomMechanic pseudorandomMechanic) {
		if(pseudorandomMechanic.dies(year - leaders.get(this.getState().getPresident()).getSettings().getSettings().getBirthYear())) {
			Logger.Dbg("President " + this.getState().getPresident() + " has died in office.");
			this.getState().setPresident(this.getState().getVicePresident());
			leaders.get(this.getState().getPresident()).getState().setNumTermsAsPresident(leaders.get(this.getState().getPresident()).getState().getNumTermsAsPresident()+1);
			chooseVicePresident(year, pseudorandomMechanic, false);
		}
	}
	
	public void maybeKillVicePresident(int year, PseudorandomMechanic pseudorandomMechanic) {
		if(pseudorandomMechanic.dies(year - leaders.get(this.getState().getVicePresident()).getSettings().getSettings().getBirthYear())) {
			Logger.Dbg("Vice President " + this.getState().getVicePresident() + " has died in office.");
			chooseVicePresident(year, pseudorandomMechanic, false);
		}
	}
}
