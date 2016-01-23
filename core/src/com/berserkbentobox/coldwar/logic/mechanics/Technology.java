package com.berserkbentobox.coldwar.logic.mechanics;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.berserkbentobox.coldwar.GameSettingsOuterClass.GameSettingsOrBuilder;
import com.berserkbentobox.coldwar.GameStateOuterClass.GameStateOrBuilder;
import com.berserkbentobox.coldwar.Technology.ResearchMove;
import com.berserkbentobox.coldwar.Technology.TechnologyGroupSettings;
import com.berserkbentobox.coldwar.Technology.TechnologyMechanicMoves;
import com.berserkbentobox.coldwar.Technology.TechnologyMechanicSettings;
import com.berserkbentobox.coldwar.Technology.TechnologyMechanicState;
import com.berserkbentobox.coldwar.logic.Status;

public class Technology {
	
	private TechnologyMechanicState.Builder state;
	private TechnologyMechanicSettings settings;
	private Random random;
	private Map<String, Integer> baseResearchChanceMap;
	private Map<String, Integer> moveResearchChanceMap;
	private Map<String, Integer> moveEffectMap;
	
	public Technology(GameSettingsOrBuilder settings) {
		this.settings = settings.getTechnologySettings();
		this.state = TechnologyMechanicState.newBuilder();
		this.state
			.addAllUsaState(this.settings.getInitUsaTechnologyGroupList())
			.addAllUssrState(this.settings.getInitUssrTechnologyGroupList());
	}

	public Technology(GameStateOrBuilder state, Random random) {
		this.state = state.getTechnologyState().toBuilder();
		this.settings = state.getSettings().getTechnologySettings();
		this.random = random;
		this.baseResearchChanceMap = new HashMap<String, Integer>();
		for (TechnologyGroupSettings group : this.settings.getTechnologyGroupList()) {
			this.baseResearchChanceMap.put(group.getId(), group.getBaseResearchChance());
		}
		this.moveResearchChanceMap = new HashMap<String, Integer>();
		for (TechnologyGroupSettings group : this.settings.getTechnologyGroupList()) {
			this.moveResearchChanceMap.put(group.getId(), 0);
		}
		this.moveEffectMap = new HashMap<String, Integer>();
		for (TechnologyGroupSettings group : this.settings.getTechnologyGroupList()) {
			this.moveEffectMap.put(group.getId(), group.getResearchEffect());
		}
	}
	
	enum Error {
		FILL_ME_IN
	}
	
	public Status validate() {
		return Status.OK;
	}
	
	public TechnologyMechanicState state() {
		return this.state.build();
	}
	
	public int getProgress(String groupId, String technologyId) {
		return 0;
	}
	
	public int numProgressions(String groupId, String technologyId) {
		return 0;
	}
	
	public boolean isCompleted(String groupId) {
		return false;
	}

	public boolean isCompleted(String groupId, String technologyId) {
		return false;
	}
	
	public synchronized void addProgress(String groupId, int magnitude) {
	}
	
	public int getBaseChance(String groupId) {
		return this.baseResearchChanceMap.get(groupId);
	}

	public int getMoveChance(String groupId) {
		return this.moveResearchChanceMap.get(groupId);
	}

	public int getMoveEffect(String groupId) {
		return this.moveEffectMap.get(groupId);
	}
	
	public int getChance(String groupId) {
		return this.getBaseChance(groupId) + this.getMoveChance(groupId);
	}
	
	public void maybeProgress(String groupId) {
		if (this.random.nextInt(1000000) < this.getChance(groupId)) {
			addProgress(groupId, 1);
		}
	}
	
	public synchronized void makeResearchMove(ResearchMove move) {
		this.moveResearchChanceMap.compute(move.getTechnologyGroupId(), (k, c) -> c + move.getMagnitude() * this.getMoveEffect(move.getTechnologyGroupId()));
	}
	
	public void makeMoves(TechnologyMechanicMoves moves) {
		for (ResearchMove move : moves.getResearchMoveList()) {
			makeResearchMove(move);
		}
	}
	
	public void maybeProgressAll() {
		for (TechnologyGroupSettings group : this.settings.getTechnologyGroupList()) {
			this.maybeProgress(group.getId());
		}
	}
}
