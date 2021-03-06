package com.berserkbentobox.coldwar.logic.mechanics.technology;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;

import com.berserkbentobox.coldwar.GameSettingsOuterClass.GameSettings;
import com.berserkbentobox.coldwar.GameStateOuterClass.GameState;
import com.berserkbentobox.coldwar.Technology.ResearchMove;
import com.berserkbentobox.coldwar.Technology.TechnologyGroupSettings;
import com.berserkbentobox.coldwar.Technology.TechnologyMechanicMoves;
import com.berserkbentobox.coldwar.Technology.TechnologyMechanicState;
import com.berserkbentobox.coldwar.logic.Client.Player;
import com.berserkbentobox.coldwar.logic.mechanics.pseudorandom.PseudorandomMechanic;
import com.berserkbentobox.coldwar.logic.mechanics.pseudorandom.PseudorandomMechanic.Settings;

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
			.setNumProgressions(100000)
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
	
	public TechnologyMechanic getMechanic() {
		GameState.Builder state = GameState.newBuilder();
		TechnologyMechanic.Settings settings = getSettings();
		state.setTechnologyState(settings.initialState());
		return new TechnologyMechanic(null, settings, state.build());
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
	
	@Test
	public void testInitialTechnologyMechanic() {
		TechnologyMechanic mechanic = getMechanic();
		assertEquals(mechanic.getTechnologyGroup(Player.USA, "GROUP_A").getState().getId(), "GROUP_A");
		assertEquals(mechanic.getTechnologyGroup(Player.USA, "GROUP_A").getResearch(), 0);
		assertEquals(mechanic.getTechnologyGroup(Player.USA, "GROUP_A").getResearchProgressChance(), 0);
		assertEquals(mechanic.getTechnologyGroup(Player.USA, "GROUP_A").getProgressChance(), 500000);
		assertEquals(mechanic.getTechnologyGroup(Player.USA, "GROUP_A").getFirstInProgressTechnology().getState().getId(), "TECH_A_1");
		assertEquals(mechanic.getTechnologyGroup(Player.USA, "GROUP_A").getTechnology("TECH_A_1").getState().getId(), "TECH_A_1");
		assertEquals(mechanic.getTechnologyGroup(Player.USA, "GROUP_A").getTechnology("TECH_A_1").getState().getProgress(), 0);
		assertEquals(mechanic.getTechnologyGroup(Player.USA, "GROUP_A").getTechnology("TECH_A_2").getState().getId(), "TECH_A_2");
		assertEquals(mechanic.getTechnologyGroup(Player.USA, "GROUP_A").getTechnology("TECH_A_2").getState().getProgress(), 0);
		assertEquals(mechanic.getTechnologyGroup(Player.USA, "GROUP_B").getState().getId(), "GROUP_B");
		assertEquals(mechanic.getTechnologyGroup(Player.USA, "GROUP_B").getResearch(), 0);
		assertEquals(mechanic.getTechnologyGroup(Player.USA, "GROUP_B").getResearchProgressChance(), 0);
		assertEquals(mechanic.getTechnologyGroup(Player.USA, "GROUP_B").getProgressChance(), 500000);
		assertEquals(mechanic.getTechnologyGroup(Player.USA, "GROUP_B").getTechnology("TECH_B_1").getState().getId(), "TECH_B_1");
		assertEquals(mechanic.getTechnologyGroup(Player.USA, "GROUP_B").getTechnology("TECH_B_1").getState().getProgress(), 0);
		assertEquals(mechanic.getTechnologyGroup(Player.USA, "GROUP_B").getTechnology("TECH_B_2").getState().getId(), "TECH_B_2");
		assertEquals(mechanic.getTechnologyGroup(Player.USA, "GROUP_B").getTechnology("TECH_B_2").getState().getProgress(), 0);
		assertEquals(mechanic.getTechnologyGroup(Player.USSR, "GROUP_A").getState().getId(), "GROUP_A");
		assertEquals(mechanic.getTechnologyGroup(Player.USSR, "GROUP_A").getResearch(), 0);
		assertEquals(mechanic.getTechnologyGroup(Player.USSR, "GROUP_A").getResearchProgressChance(), 0);
		assertEquals(mechanic.getTechnologyGroup(Player.USSR, "GROUP_A").getProgressChance(), 500000);
		assertEquals(mechanic.getTechnologyGroup(Player.USSR, "GROUP_A").getTechnology("TECH_A_1").getState().getId(), "TECH_A_1");
		assertEquals(mechanic.getTechnologyGroup(Player.USSR, "GROUP_A").getTechnology("TECH_A_1").getState().getProgress(), 0);
		assertEquals(mechanic.getTechnologyGroup(Player.USSR, "GROUP_A").getTechnology("TECH_A_2").getState().getId(), "TECH_A_2");
		assertEquals(mechanic.getTechnologyGroup(Player.USSR, "GROUP_A").getTechnology("TECH_A_2").getState().getProgress(), 0);
		assertEquals(mechanic.getTechnologyGroup(Player.USSR, "GROUP_B").getState().getId(), "GROUP_B");
		assertEquals(mechanic.getTechnologyGroup(Player.USSR, "GROUP_B").getResearch(), 0);
		assertEquals(mechanic.getTechnologyGroup(Player.USSR, "GROUP_B").getResearchProgressChance(), 0);
		assertEquals(mechanic.getTechnologyGroup(Player.USSR, "GROUP_B").getProgressChance(), 500000);
		assertEquals(mechanic.getTechnologyGroup(Player.USSR, "GROUP_B").getTechnology("TECH_B_1").getState().getId(), "TECH_B_1");
		assertEquals(mechanic.getTechnologyGroup(Player.USSR, "GROUP_B").getTechnology("TECH_B_1").getState().getProgress(), 0);
		assertEquals(mechanic.getTechnologyGroup(Player.USSR, "GROUP_B").getTechnology("TECH_B_2").getState().getId(), "TECH_B_2");
		assertEquals(mechanic.getTechnologyGroup(Player.USSR, "GROUP_B").getTechnology("TECH_B_2").getState().getProgress(), 0);
	}

	@Test
	public void testGetFirstInProgressTechnology() {
		TechnologyMechanic mechanic = getMechanic();
		mechanic.getTechnologyGroup(Player.USA, "GROUP_A").getTechnology("TECH_A_1").getState().setProgress(2);
		assertEquals(mechanic.getTechnologyGroup(Player.USA, "GROUP_A").getFirstInProgressTechnology().getState().getId(), "TECH_A_2");
	}

	@Test
	public void testIsInProgress() {
		TechnologyMechanic mechanic = getMechanic();
		assertEquals(mechanic.getTechnologyGroup(Player.USA, "GROUP_A").getTechnology("TECH_A_1").isInProgress(), true);
		mechanic.getTechnologyGroup(Player.USA, "GROUP_A").getTechnology("TECH_A_1").getState().setProgress(1);
		assertEquals(mechanic.getTechnologyGroup(Player.USA, "GROUP_A").getTechnology("TECH_A_1").isInProgress(), true);
		mechanic.getTechnologyGroup(Player.USA, "GROUP_A").getTechnology("TECH_A_1").getState().setProgress(2);
		assertEquals(mechanic.getTechnologyGroup(Player.USA, "GROUP_A").getTechnology("TECH_A_1").isInProgress(), false);
	}
	
	@Test
	public void testTechnologyMakeProgress() {
		TechnologyMechanic mechanic = getMechanic();
		assertEquals(mechanic.getTechnologyGroup(Player.USA, "GROUP_A").getTechnology("TECH_A_1").getState().getProgress(), 0);
		mechanic.getTechnologyGroup(Player.USA, "GROUP_A").getTechnology("TECH_A_1").makeProgress();
		assertEquals(mechanic.getTechnologyGroup(Player.USA, "GROUP_A").getTechnology("TECH_A_1").getState().getProgress(), 1);
	}

	@Test
	public void testHasAnyInProgressTechnologies() {
		TechnologyMechanic mechanic = getMechanic();
		assertEquals(mechanic.getTechnologyGroup(Player.USA, "GROUP_A").hasAnyInProgressTechnologies(), true);
		mechanic.getTechnologyGroup(Player.USA, "GROUP_A").getTechnology("TECH_A_1").getState().setProgress(2);
		assertEquals(mechanic.getTechnologyGroup(Player.USA, "GROUP_A").hasAnyInProgressTechnologies(), true);
		mechanic.getTechnologyGroup(Player.USA, "GROUP_A").getTechnology("TECH_A_2").getState().setProgress(3);
		assertEquals(mechanic.getTechnologyGroup(Player.USA, "GROUP_A").hasAnyInProgressTechnologies(), false);
	}
	
	@Test
	public void testTechnologyGroupMakeProgress() {
		TechnologyMechanic mechanic = getMechanic();
		assertEquals(mechanic.getTechnologyGroup(Player.USA, "GROUP_A").getTechnology("TECH_A_1").getState().getProgress(), 0);
		assertEquals(mechanic.getTechnologyGroup(Player.USA, "GROUP_A").getTechnology("TECH_A_2").getState().getProgress(), 0);

		mechanic.getTechnologyGroup(Player.USA, "GROUP_A").makeProgress();
		assertEquals(mechanic.getTechnologyGroup(Player.USA, "GROUP_A").getTechnology("TECH_A_1").getState().getProgress(), 1);
		assertEquals(mechanic.getTechnologyGroup(Player.USA, "GROUP_A").getTechnology("TECH_A_2").getState().getProgress(), 0);
	
		mechanic.getTechnologyGroup(Player.USA, "GROUP_A").makeProgress();
		assertEquals(mechanic.getTechnologyGroup(Player.USA, "GROUP_A").getTechnology("TECH_A_1").getState().getProgress(), 2);
		assertEquals(mechanic.getTechnologyGroup(Player.USA, "GROUP_A").getTechnology("TECH_A_2").getState().getProgress(), 0);
		
		mechanic.getTechnologyGroup(Player.USA, "GROUP_A").makeProgress();
		assertEquals(mechanic.getTechnologyGroup(Player.USA, "GROUP_A").getTechnology("TECH_A_1").getState().getProgress(), 2);
		assertEquals(mechanic.getTechnologyGroup(Player.USA, "GROUP_A").getTechnology("TECH_A_2").getState().getProgress(), 1);
		
		mechanic.getTechnologyGroup(Player.USA, "GROUP_A").makeProgress();
		assertEquals(mechanic.getTechnologyGroup(Player.USA, "GROUP_A").getTechnology("TECH_A_1").getState().getProgress(), 2);
		assertEquals(mechanic.getTechnologyGroup(Player.USA, "GROUP_A").getTechnology("TECH_A_2").getState().getProgress(), 2);
		
		mechanic.getTechnologyGroup(Player.USA, "GROUP_A").makeProgress();
		assertEquals(mechanic.getTechnologyGroup(Player.USA, "GROUP_A").getTechnology("TECH_A_1").getState().getProgress(), 2);
		assertEquals(mechanic.getTechnologyGroup(Player.USA, "GROUP_A").getTechnology("TECH_A_2").getState().getProgress(), 3);
		
		mechanic.getTechnologyGroup(Player.USA, "GROUP_A").makeProgress();
		assertEquals(mechanic.getTechnologyGroup(Player.USA, "GROUP_A").getTechnology("TECH_A_1").getState().getProgress(), 2);
		assertEquals(mechanic.getTechnologyGroup(Player.USA, "GROUP_A").getTechnology("TECH_A_2").getState().getProgress(), 3);
	}
	
	@Test
	public void testMakeResearchMove() {
		TechnologyMechanic mechanic = getMechanic();
		assertEquals(mechanic.getTechnologyGroup(Player.USA, "GROUP_A").getResearch(), 0);
		assertEquals(mechanic.getTechnologyGroup(Player.USA, "GROUP_A").getResearchProgressChance(), 0);
		assertEquals(mechanic.getTechnologyGroup(Player.USA, "GROUP_A").getProgressChance(), 500000);
		
		mechanic.makeResearchMove(Player.USA, ResearchMove.newBuilder().setTechnologyGroupId("GROUP_A").setMagnitude(1).build());
		assertEquals(mechanic.getTechnologyGroup(Player.USA, "GROUP_A").getResearch(), 1);
		assertEquals(mechanic.getTechnologyGroup(Player.USA, "GROUP_A").getResearchProgressChance(), 100000);
		assertEquals(mechanic.getTechnologyGroup(Player.USA, "GROUP_A").getProgressChance(), 600000);
		
		mechanic.makeResearchMove(Player.USA, ResearchMove.newBuilder().setTechnologyGroupId("GROUP_A").setMagnitude(2).build());
		assertEquals(mechanic.getTechnologyGroup(Player.USA, "GROUP_A").getResearch(), 3);
		assertEquals(mechanic.getTechnologyGroup(Player.USA, "GROUP_A").getResearchProgressChance(), 300000);
		assertEquals(mechanic.getTechnologyGroup(Player.USA, "GROUP_A").getProgressChance(), 800000);
	}
	
	@Test
	public void testMakeMoves() {
		TechnologyMechanic mechanic = getMechanic();
		assertEquals(mechanic.getTechnologyGroup(Player.USA, "GROUP_A").getResearch(), 0);
		assertEquals(mechanic.getTechnologyGroup(Player.USA, "GROUP_A").getResearchProgressChance(), 0);
		assertEquals(mechanic.getTechnologyGroup(Player.USA, "GROUP_A").getProgressChance(), 500000);
		
		ResearchMove research = ResearchMove.newBuilder().setTechnologyGroupId("GROUP_A").setMagnitude(1).build();
		mechanic.makeMoves(Player.USA, TechnologyMechanicMoves.newBuilder().setResearchMove(research).build());
		assertEquals(mechanic.getTechnologyGroup(Player.USA, "GROUP_A").getResearch(), 1);
		assertEquals(mechanic.getTechnologyGroup(Player.USA, "GROUP_A").getResearchProgressChance(), 100000);
		assertEquals(mechanic.getTechnologyGroup(Player.USA, "GROUP_A").getProgressChance(), 600000);
	}
	
	public PseudorandomMechanic getPseudorandom() {
		GameSettings.Builder settings = GameSettings.newBuilder();
		settings.getPseudorandomSettingsBuilder().setInitSeed(0);
		PseudorandomMechanic.Settings s = new PseudorandomMechanic.Settings(settings.build());
		GameState.Builder state = GameState.newBuilder();
		state.setPseudorandomState(s.initialState());
		return new PseudorandomMechanic(null, s, state);
	}
	
	@Test
	public void testMaybeMakeProgress() {
		TechnologyMechanic mechanic = getMechanic();
		assertEquals(mechanic.getTechnologyGroup(Player.USA, "GROUP_A").getTechnology("TECH_A_1").getState().getProgress(), 0);
		assertEquals(mechanic.getTechnologyGroup(Player.USA, "GROUP_A").getProgressChance(), 500000);
		mechanic.makeResearchMove(Player.USA, ResearchMove.newBuilder().setTechnologyGroupId("GROUP_A").setMagnitude(5).build());
		
		mechanic.maybeMakeProgress(Player.USA);
		assertEquals(mechanic.getTechnologyGroup(Player.USA, "GROUP_A").getTechnology("TECH_A_1").getState().getProgress(), 1);
	}

	@Test
	public void testMaybeMakeProgressDistribution() {
		TechnologyMechanic mechanic = getMechanic();
		assertEquals(mechanic.getTechnologyGroup(Player.USA, "GROUP_B").getTechnology("TECH_B_1").getState().getProgress(), 0);
		assertEquals(mechanic.getTechnologyGroup(Player.USA, "GROUP_B").getTechnology("TECH_B_1").getSettings().getSettings().getNumProgressions(), 100000);
		assertEquals(mechanic.getTechnologyGroup(Player.USA, "GROUP_B").getProgressChance(), 500000);
		
		PseudorandomMechanic random = getPseudorandom();
		for (int i = 0; i < 100000; i++) {
			mechanic.maybeMakeProgress(Player.USA);
		}
		assertEquals(mechanic.getTechnologyGroup(Player.USA, "GROUP_B").getTechnology("TECH_B_1").getState().getProgress() / 100000.0, .5, .01);
	}
	
	@Test
	public void testStatePropagation() {
		TechnologyMechanic initMechanic = getMechanic();
		initMechanic.getTechnologyGroup(Player.USA, "GROUP_A").makeProgress();
		initMechanic.makeResearchMove(Player.USA, ResearchMove.newBuilder().setTechnologyGroupId("GROUP_A").setMagnitude(1).build());
		GameState state = GameState.newBuilder().setTechnologyState(initMechanic.buildState()).build();
		TechnologyMechanic mechanic = new TechnologyMechanic(null, initMechanic.getSettings(), state);
		assertEquals(mechanic.getTechnologyGroup(Player.USA, "GROUP_A").getTechnology("TECH_A_1").getState().getProgress(), 1);
		assertEquals(mechanic.getTechnologyGroup(Player.USA, "GROUP_A").getResearch(), 0);  // Research is not propagated.
	}
}
