package com.berserkbentobox.coldwar.logic.mechanics.crisis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.berserkbentobox.coldwar.Crisis;
import com.berserkbentobox.coldwar.Logger;
import com.berserkbentobox.coldwar.Crisis.CrisisMechanicMoves;
import com.berserkbentobox.coldwar.Crisis.CrisisMechanicSettings;
import com.berserkbentobox.coldwar.Crisis.CrisisMechanicState;
import com.berserkbentobox.coldwar.Crisis.CrisisSettings;
import com.berserkbentobox.coldwar.Crisis.CrisisState;
import com.berserkbentobox.coldwar.Crisis.Effect;
import com.berserkbentobox.coldwar.GameSettingsOuterClass.GameSettingsOrBuilder;
import com.berserkbentobox.coldwar.GameStateOuterClass.GameStateOrBuilder;
import com.berserkbentobox.coldwar.logic.Client.Player;
import com.berserkbentobox.coldwar.logic.Status;
import com.berserkbentobox.coldwar.logic.mechanics.influencestore.InfluenceStoreMechanic;
import com.berserkbentobox.coldwar.logic.mechanics.pseudorandom.PseudorandomMechanic;

public class CrisisMechanic {
	public static class Settings {
		
		private GameSettingsOrBuilder gameSettings;
		private CrisisMechanicSettings settings;
		
		private Map<String, CrisisSettings> crisisSettings;
		
		public Settings(GameSettingsOrBuilder gameSettings) {
			this.gameSettings = gameSettings;
			this.settings = gameSettings.getCrisisSettings();
			this.crisisSettings = new HashMap<String, CrisisSettings>();
			for (CrisisSettings c : this.settings.getCrisesList()) {
				this.crisisSettings.put(c.getName(), c);
			}
			Logger.Dbg(this.settings.toString());
		}
		
		public Status validate() {
			return Status.OK;
		}
		
		public CrisisMechanicSettings getSettings() {
			return this.settings;
		}
		
		public CrisisMechanicState initialState() {
			CrisisMechanicState.Builder state = CrisisMechanicState.newBuilder();
			state.addCrises(settings.getInitCrisis());
			return state.build();
		}
		
		public CrisisSettings getCrisisSetting(String name) {
			Logger.Dbg(crisisSettings.toString());
			return crisisSettings.get(name);
		}
		
		public Collection<CrisisSettings> getCrisisSettings() {
			return crisisSettings.values();
		}
	}
	
	private Settings settings;
	private CrisisMechanicState.Builder state;
	private String usa_choice;
	private String ussr_choice;
	
	public CrisisMechanic(Settings settings, GameStateOrBuilder state) {
		this.settings = settings;
		this.state = state.getCrisisState().toBuilder();
	}
		
	public Status validate() {
		return Status.OK;
	}
	
	public Settings getSettings() {
		return this.settings;
	}
	
	public CrisisMechanicState.Builder getState() {
		return this.state;
	}
	
	public CrisisMechanicState buildState() {
		return this.state.build();
	}

	public boolean isActiveCrisis() {
		if(this.getState().getCrisesCount() > 0)
			return true;
		return false;
	}

	public void makeMoves(Player player, CrisisMechanicMoves moves) {
		if(isActiveCrisis()) {
			if(moves.hasChoice() && player == Player.USA)
				this.usa_choice = moves.getChoice().getName();
			if(moves.hasChoice() && player == Player.USSR)
				this.ussr_choice = moves.getChoice().getName();
		}
	}
	
	public void resolveCrisis(InfluenceStoreMechanic influence) {
		for(CrisisState c : this.state.getCrisesList()) {
			for(Effect e : this.getSettings().getCrisisSetting(c.getName()).getEffectsList()) {
				for(String usa : e.getUsaChoicesList()) {
					for(String ussr : e.getUssrChoicesList()) {
						if(usa.equals(usa_choice) && ussr.equals(ussr_choice)) {
							// Resolve here
						}
					}
				}
			}
		}
	}
	
	public String chooseCrisisByType(int year, Crisis.Type type, PseudorandomMechanic pseudorandom) {
		Collection<CrisisSettings> crises = this.getSettings().getCrisisSettings();
		List<Integer> chances = new ArrayList<Integer>();
		List<String> names = new ArrayList<String>();
		int total = 0;
		for(CrisisSettings c : crises) {
			if(c.getStartYear() <= year && c.getEndYear() >= year && c.getType() == type) {
				int weighted_chance = c.getChanceMultiplier() * this.getSettings().getSettings().getBaseChance();
				chances.add(weighted_chance);
				total += weighted_chance;
				names.add(c.getName());
			}
		}
		names.add("");
		int null_chance = this.getSettings().getSettings().getTotalChance() - total;
		if(null_chance < 0)
			Logger.Err("Total crisis chances add up over limit: " + null_chance + " > " + this.getSettings().getSettings().getTotalChance());
		else
			chances.add(null_chance);
		int result = pseudorandom.roll(chances);
		return names.get(result);
	}
	
	public boolean setCrisis(String name) {
		if(name != "") {
			this.getState().addCrises(CrisisState.newBuilder().setName(name));
			return true;
		}
		return false;
	}
	
	public void generateCrisis(int year, PseudorandomMechanic pseudorandom) {
		this.getState().clearCrises();
		String crisis = chooseCrisisByType(year, Crisis.Type.JOINT, pseudorandom);
		if(!setCrisis(crisis)) {
			String usa_crisis = chooseCrisisByType(year, Crisis.Type.USA_ONLY, pseudorandom);
			setCrisis(usa_crisis);
			String ussr_crisis = chooseCrisisByType(year, Crisis.Type.USSR_ONLY, pseudorandom);
			setCrisis(ussr_crisis);
		}
	}
}