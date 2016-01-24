package com.berserkbentobox.coldwar.logic.mechanics;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.berserkbentobox.coldwar.Technology.ResearchMove;
import com.berserkbentobox.coldwar.Technology.TechnologyGroupSettings;
import com.berserkbentobox.coldwar.Technology.TechnologyGroupState;
import com.berserkbentobox.coldwar.Technology.TechnologySettings;
import com.berserkbentobox.coldwar.Technology.TechnologyState;
import com.berserkbentobox.coldwar.logic.Status;

public class TechnologyGroup {
	public static class Settings {
		
		private TechnologyMechanic.Settings parent;
		private TechnologyGroupSettings settings;
		private Map<String, Technology.Settings> technologySettings;
		
		public Settings(TechnologyMechanic.Settings parent, TechnologyGroupSettings settings) {
			this.parent = parent;
			this.settings = settings;
			this.technologySettings = new HashMap<String, Technology.Settings>();
			for (TechnologySettings t : this.settings.getTechnologyList()) {
				Technology.Settings ts = new Technology.Settings(this, t);
				this.technologySettings.put(ts.getSettings().getId(), ts);
			}
		}
		
		public Status validate() {
			return Status.OK;
		}
		
		public TechnologyGroupSettings getSettings() {
			return settings;
		}
		
		public Collection<Technology.Settings> getTechnologySettings() {
			return this.technologySettings.values();
		}
		
		public Technology.Settings getTechnologySettings(String technologyId) {
			return this.technologySettings.get(technologyId);
		}
		
		public TechnologyGroupState initialState() {
			TechnologyGroupState.Builder state = TechnologyGroupState.newBuilder();
			state.setId(this.settings.getId());
			for (Technology.Settings ts : this.getTechnologySettings()) {
				state.addTechnology(ts.initialState());
			}
			return state.build();
		}
	}
	
	private Settings settings;
	private TechnologyMechanic parent;
	private TechnologyGroupState.Builder state;
	private Map<String, Technology> technologies;
	
	// The research accumulated on one turn.
	private int research;
	
	public TechnologyGroup(TechnologyMechanic parent, Settings settings, TechnologyGroupState.Builder state) {
		this.parent = parent;
		this.settings = settings;
		this.state = state;
		this.technologies = new HashMap<String, Technology>();
		for (TechnologyState.Builder ts : this.state.getTechnologyBuilderList()) {
			Technology t = new Technology(this, parent.getSettings().getTechnologyGroupSettings(state.getId()).getTechnologySettings(ts.getId()), ts);
			this.technologies.put(t.getState().getId(), t);
		}
		this.research = 0;
	}
	
	public TechnologyGroupState.Builder getState() {
		return this.state;
	}

	public Settings getSettings() {
		return this.settings;
	}

	public Collection<Technology> getTechnologies() {
		return this.technologies.values();
	}
	
	public Technology getTechnology(String technologyId) {
		return this.technologies.get(technologyId);
	}

	public void maybeMakeProgress(Random random) {
		if (random.nextInt(1000000) < this.getProgressChance()) {
			this.makeProgress();
		}
	}	

	public int getResearch() {
		return this.research;
	}
	
	public int getResearchProgressChance() {
		return this.research * this.settings.getSettings().getResearchEffect();
	}
	
	public int getProgressChance() {
		return this.settings.getSettings().getBaseResearchChance() + this.getResearchProgressChance();
	}
	
	public Technology getFirstInProgressTechnology() {
		for (Technology t : this.getTechnologies()) {
			if (t.isInProgress()) {
				return t;
			}
		}
		return null;
	}
	
	public boolean hasAnyInProgressTechnologies() {
		return this.getFirstInProgressTechnology() == null;
	}
	
	public void makeProgress() {
		if (this.hasAnyInProgressTechnologies()) {
			this.getFirstInProgressTechnology().makeProgress();
		}
	}

	public synchronized void makeResearchMove(ResearchMove move) {
		this.research += move.getMagnitude();
	}
}
