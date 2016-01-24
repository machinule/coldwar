package com.berserkbentobox.coldwar.logic.mechanics;

import static org.junit.Assert.*;

import org.junit.Test;

import com.berserkbentobox.coldwar.GameSettingsOuterClass.GameSettings;
import com.berserkbentobox.coldwar.Technology.TechnologyGroupSettings;
import com.berserkbentobox.coldwar.Technology.TechnologyMechanicState;

public class TechnologyMechanicTest {
	
	TechnologyMechanic.Settings getSettings() {
		GameSettings.Builder settings = GameSettings.newBuilder();
		
		TechnologyGroupSettings.Builder groupA = settings.getTechnologySettingsBuilder()
			.addTechnologyGroupBuilder()
				.setId("GROUP_A")
				.setBaseResearchChance(500000)
				.setMaxResearchFocusPerTurn(1)
				.setResearchEffect(100000);
		groupA.getUnitResearchCostBuilder().setMilitaryPoints(2);
		groupA.addTechnologyBuilder()
			.setId("TECH_A_1")
			.setNumProgressions(2)
			.setDeterrence(3)
			.setFirstVp(2)
			.setBaseVp(1);
		groupA.addTechnologyBuilder()
			.setId("TECH_A_2")
			.setNumProgressions(3)
			.setDeterrence(2)
			.setFirstVp(2)
			.setBaseVp(2);

		TechnologyGroupSettings.Builder groupB = settings.getTechnologySettingsBuilder()
				.addTechnologyGroupBuilder()
					.setId("GROUP_B")
					.setBaseResearchChance(500000)
					.setMaxResearchFocusPerTurn(1)
					.setResearchEffect(100000);
		groupB.getUnitResearchCostBuilder().setMilitaryPoints(2);
		groupB.addTechnologyBuilder()
			.setId("TECH_B_1")
			.setNumProgressions(2)
			.setDeterrence(3)
			.setFirstVp(2)
			.setBaseVp(1);
		groupB.addTechnologyBuilder()
			.setId("TECH_B_2")
			.setNumProgressions(3)
			.setDeterrence(2)
			.setFirstVp(2)
			.setBaseVp(2);
		
		return new TechnologyMechanic.Settings(settings);
	}
	
	@Test
	public void testInititialState() {
		TechnologyMechanic.Settings settings = getSettings();
		TechnologyMechanicState state = settings.initialState();
		assertEquals(state.getUsaStateCount(), 2);
		assertEquals(state.getUsaState(0).getId(), "GROUP_A");
		assertEquals(state.getUsaState(0).getTechnologyCount(), 2);
		assertEquals(state.getUsaState(0).getTechnology(0).getId(), "TECH_A_1");
		assertEquals(state.getUsaState(0).getTechnology(0).getProgress(), 0);
		assertEquals(state.getUsaState(0).getTechnology(1).getId(), "TECH_A_2");
		assertEquals(state.getUsaState(0).getTechnology(1).getProgress(), 0);
		assertEquals(state.getUsaState(1).getId(), "GROUP_B");
		assertEquals(state.getUsaState(1).getTechnologyCount(), 2);
		assertEquals(state.getUsaState(1).getTechnology(0).getId(), "TECH_B_1");
		assertEquals(state.getUsaState(1).getTechnology(0).getProgress(), 0);
		assertEquals(state.getUsaState(1).getTechnology(1).getId(), "TECH_B_2");
		assertEquals(state.getUsaState(1).getTechnology(1).getProgress(), 0);
		assertEquals(state.getUssrStateCount(), 2);
		assertEquals(state.getUssrState(0).getId(), "GROUP_A");
		assertEquals(state.getUssrState(0).getTechnologyCount(), 2);
		assertEquals(state.getUssrState(0).getTechnology(0).getId(), "TECH_A_1");
		assertEquals(state.getUssrState(0).getTechnology(0).getProgress(), 0);
		assertEquals(state.getUssrState(0).getTechnology(1).getId(), "TECH_A_2");
		assertEquals(state.getUssrState(0).getTechnology(1).getProgress(), 0);
		assertEquals(state.getUssrState(1).getId(), "GROUP_B");
		assertEquals(state.getUssrState(1).getTechnologyCount(), 2);
		assertEquals(state.getUssrState(1).getTechnology(0).getId(), "TECH_B_1");
		assertEquals(state.getUssrState(1).getTechnology(0).getProgress(), 0);
		assertEquals(state.getUssrState(1).getTechnology(1).getId(), "TECH_B_2");
		assertEquals(state.getUssrState(1).getTechnology(1).getProgress(), 0);
	}
}
