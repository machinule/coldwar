package com.berserkbentobox.coldwar.logic.mechanics.superpower;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.berserkbentobox.coldwar.Superpower.UssrSettings;
import com.berserkbentobox.coldwar.Superpower.UssrState;
import com.berserkbentobox.coldwar.Logger;
import com.berserkbentobox.coldwar.Superpower.UsaLeaderParty;
import com.berserkbentobox.coldwar.Superpower.UsaLeaderSettings;
import com.berserkbentobox.coldwar.Superpower.UsaLeaderState;
import com.berserkbentobox.coldwar.Superpower.UssrLeaderSettings;
import com.berserkbentobox.coldwar.Superpower.UssrLeaderState;
import com.berserkbentobox.coldwar.logic.Status;
import com.berserkbentobox.coldwar.logic.mechanics.pseudorandom.PseudorandomMechanic;

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
					.setHos(settings.getSecretariatSettings().getInitHos())
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
	
	public List<String> getAvailableLeaders(int year) {
		List<String> ret = new ArrayList<String>();
		for (UssrLeader l : this.leaders.values()) {
			UssrLeaderSettings settings = l.getSettings().getSettings();
			UssrLeaderState.Builder state = l.getState();
			if (settings.getStartYear() < year &&
				state.getAvailable() &&
				l.getState().getName() != this.getState().getHos()) {
				ret.add(l.getSettings().getSettings().getName());
			}
		}
		return ret;
	}
	
	public List<String> getAvailableLeaders(int year, int num, PseudorandomMechanic pseudorandom) {
		List<String> eligible = getAvailableLeaders(year);
		List<Integer> chances = new ArrayList<Integer>();
		List<String> ret = new ArrayList<String>();
		if (eligible.size() != 1) {
			for (String l : eligible) {
				chances.add(1);
			}
			int result;
			for (int j = 0; j < num; j++) {
				result = pseudorandom.roll(chances);
				ret.add(eligible.get(result));
				chances.remove(result);
				eligible.remove(result);
			}
		} else {
			ret.add(eligible.get(0));
		}
		return ret;
	}
	
	public List<String> getTroika() {
		if(this.getState().getTroikaCount() == 3) {
			return this.getState().getTroikaList();
		} else {
			return null;
		}
	}
	
	//Logic
	
	public boolean hasTroika() {
		return !(leaders.get(this.getState().getHos()).getState().getAvailable());
	}
	
	public void generateTroika(int year, PseudorandomMechanic pseudorandom) {
		List<String> eligible = getAvailableLeaders(year, 3, pseudorandom); // TODO: Settings
		Logger.Dbg(eligible.toString());
		int initPartySupport = 4; // TODO: Setting
		this.getState().clearTroika();
		for (String l : eligible) {
			this.getState().addTroika(l);
			leaders.get(l).getState().setPartySupport(initPartySupport);
		}
	}
	
	public void refreshTroika()	{
		int startingPartySupport = 6;
		List<String> temp = new ArrayList<String>();
		for(String l : this.getState().getTroikaList()) {
			Logger.Dbg("Measuring political power: " + l);
			if(leaders.get(l).getState().getPartySupport() > 0) {
				temp.add(l);
			} else {
				Logger.Dbg(l + " knocked out of the troika.");
				leaders.get(l).getState().setAvailable(false);
			}
		}	
		Logger.Dbg(temp.toString());
		if(temp.size() == 1) {
			UssrLeader hos = leaders.get(temp.get(0));
			Logger.Dbg(hos.getState().getName() + " is the new head of state of the USSR.");
			this.getState().setHos(hos.getState().getName());
			leaders.get(this.getState().getHos()).getState().setPartySupport(startingPartySupport);
			this.getState().clearTroika();
			return;
		}
		this.getState().clearTroika();
		this.getState().addAllTroika(temp);
	}
	
	public void troikaUpdate(PseudorandomMechanic pseudorandom) {
		Logger.Dbg("Troika is " + hasTroika());
		if(hasTroika()) {
			List<Integer> chances = new ArrayList<Integer>();
			int magnitude = 2; // TODO: Settings
			for(String n : this.getState().getTroikaList()) {
				chances.add(1);
			}
			int result = pseudorandom.roll(chances);
			UssrLeader l = leaders.get(this.getState().getTroika(result));
			Logger.Dbg("Reduing policial power of " + l);
			l.getState().setPartySupport(l.getState().getPartySupport() - magnitude);
			refreshTroika();
		}
	}
	
	public void maybeKillLeader(int year, PseudorandomMechanic pseudorandom) {
		Collection<String> potential = getAvailableLeaders(year);
		for(String l : potential) {
			Logger.Dbg("Seeing if " + l + " will die at age " + (year - leaders.get(l).getSettings().getSettings().getBirthYear()));
			if(pseudorandom.dies(year - leaders.get(l).getSettings().getSettings().getBirthYear())) {
				Logger.Dbg(l + " has died.");
				leaders.get(l).getState().setAvailable(false);
				if(this.getState().getHos().equals(l)) {
					Logger.Dbg("The Soviet head of state has died.");
					this.generateTroika(year, pseudorandom);
				}
			}
		}
	}
}
