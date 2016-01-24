package com.berserkbentobox.coldwar.logic.mechanics;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

import com.berserkbentobox.coldwar.GameSettingsOuterClass.GameSettings;
import com.berserkbentobox.coldwar.GameSettingsOuterClass.GameSettingsOrBuilder;
import com.berserkbentobox.coldwar.GameStateOuterClass.GameStateOrBuilder;
import com.berserkbentobox.coldwar.Technology.ResearchMove;
import com.berserkbentobox.coldwar.Technology.TechnologyGroupSettings;
import com.berserkbentobox.coldwar.Technology.TechnologyGroupState;
import com.berserkbentobox.coldwar.Technology.TechnologyMechanicMoves;
import com.berserkbentobox.coldwar.Technology.TechnologyMechanicSettings;
import com.berserkbentobox.coldwar.Technology.TechnologyMechanicState;
import com.berserkbentobox.coldwar.logic.Client.Player;
import com.berserkbentobox.coldwar.logic.Status;

public class TechnologyMechanic {
	
	public static class Settings {
		
		private GameSettingsOrBuilder gameSettings;
		private TechnologyMechanicSettings settings;
		private Map<String, TechnologyGroup.Settings> technologyGroupSettings;
		
		public Settings(GameSettingsOrBuilder gameSettings) {
			this.gameSettings = gameSettings;
			this.settings = gameSettings.getTechnologySettings();
			this.technologyGroupSettings = new HashMap<String, TechnologyGroup.Settings>();
			for (TechnologyGroupSettings tg : this.settings.getTechnologyGroupList()) {
				TechnologyGroup.Settings tgs = new TechnologyGroup.Settings(this, tg);
				this.technologyGroupSettings.put(tgs.getSettings().getId(), tgs);
			}
		}
		
		public Status validate() {
			return Status.OK;
		}
		
		public Collection<TechnologyGroup.Settings> getTechnologyGroupSettings() {
			return this.technologyGroupSettings.values();
		}
		
		public TechnologyGroup.Settings getTechnologyGroupSettings(String groupId) {
			return this.technologyGroupSettings.get(groupId);
		}
		
		public TechnologyMechanicState initialState() {
			TechnologyMechanicState.Builder state = TechnologyMechanicState.newBuilder();
			state
				.addAllUsaState(this.settings.getInitUsaTechnologyGroupList())
				.addAllUssrState(this.settings.getInitUssrTechnologyGroupList());
			return state.build();
		}
	}
	
	private Settings settings;
	private TechnologyMechanicState.Builder state;
	private Map<String, TechnologyGroup> usaTechnologyGroups;
	private Map<String, TechnologyGroup> ussrTechnologyGroups;
	
	public TechnologyMechanic(Settings settings, GameStateOrBuilder state) {
		this.settings = settings;
		this.state = state.getTechnologyState().toBuilder();
		this.usaTechnologyGroups = new HashMap<String, TechnologyGroup>();
		for (TechnologyGroupState.Builder tgs : this.state.getUsaStateBuilderList()) {
			TechnologyGroup tg = new TechnologyGroup(this, this.getSettings().getTechnologyGroupSettings(tgs.getId()), tgs);
			this.usaTechnologyGroups.put(tg.getState().getId(), tg);
		}
		this.ussrTechnologyGroups = new HashMap<String, TechnologyGroup>();
		for (TechnologyGroupState.Builder tgs : this.state.getUssrStateBuilderList()) {
			TechnologyGroup tg = new TechnologyGroup(this, this.getSettings().getTechnologyGroupSettings(tgs.getId()), tgs);
			this.ussrTechnologyGroups.put(tg.getState().getId(), tg);
		}
	}
		
	public Status validate() {
		return Status.OK;
	}
	
	public Settings getSettings() {
		return this.settings;
	}
	
	public TechnologyMechanicState buildState() {
		return this.state.build();
	}
	
	private Map<String, TechnologyGroup> getTechnologyGroupMaps(Player player) {
		if (player == Player.USA) {
			return this.usaTechnologyGroups;
		} else {
			return this.ussrTechnologyGroups;
		}
	}
	
	public Collection<TechnologyGroup> getTechnologyGroups(Player player) {
		return this.getTechnologyGroupMaps(player).values();
	}
	
	public TechnologyGroup getTechnologyGroup(Player player, String groupId) {
		return this.getTechnologyGroupMaps(player).get(groupId);
	}

	// Logic
	
	public void maybeMakeProgress(Player player, Random random) {
		for (TechnologyGroup group : this.getTechnologyGroups(player)) {
			group.maybeMakeProgress(random);
		}
	}	
	
	public void makeMoves(Player player, TechnologyMechanicMoves moves) {
		if (moves.hasResearchMove()) {
			makeResearchMove(player, moves.getResearchMove());
		}
	}	

	public void makeResearchMove(Player player, ResearchMove move) {
		this.getTechnologyGroupMaps(player).get(move.getTechnologyGroupId()).makeResearchMove(move);
	}
}
