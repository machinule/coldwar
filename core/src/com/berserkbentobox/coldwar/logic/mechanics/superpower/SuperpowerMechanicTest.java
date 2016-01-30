package com.berserkbentobox.coldwar.logic.mechanics.superpower;

import static org.junit.Assert.*;

import org.junit.Test;

import com.berserkbentobox.coldwar.GameSettingsOuterClass.GameSettings;
import com.berserkbentobox.coldwar.GameStateOuterClass.GameState;
import com.berserkbentobox.coldwar.Superpower.SuperpowerMechanicState;
import com.berserkbentobox.coldwar.Superpower.UsaLeaderParty;
import com.berserkbentobox.coldwar.Superpower.UsaSettings;

public class SuperpowerMechanicTest {
	
	SuperpowerMechanic.Settings getSettings() {
		GameSettings.Builder settings = GameSettings.newBuilder();
		
		UsaSettings.Builder usa = settings.getSuperpowerSettingsBuilder()
			.getUsaSettingsBuilder();
		usa.addLeaderSettingsBuilder()
			.setName("LEADER_US_1")
			.setParty(UsaLeaderParty.REPUBLICAN)
			.setStartYear(1945)
			.setInitNumTermsAsPresident(0)
			.setInitNumTermsAsVicePresident(1);;
		usa.addLeaderSettingsBuilder()
			.setName("LEADER_US_2")
			.setParty(UsaLeaderParty.DEMOCRAT)
			.setStartYear(1945)
			.setInitNumTermsAsPresident(1)
			.setInitNumTermsAsVicePresident(0);
		
		return new SuperpowerMechanic.Settings(settings);
	}
	
	public SuperpowerMechanic getMechanic() {
		GameState.Builder state = GameState.newBuilder();
		SuperpowerMechanic.Settings settings = getSettings();
		state.setSuperpowerState(settings.initialState());
		return new SuperpowerMechanic(settings, state.build());
	}
	
	@Test
	public void testInititialState() {
		SuperpowerMechanic.Settings settings = getSettings();
		SuperpowerMechanicState state = settings.initialState();
		assertEquals(state.getUsaState().getLeaderCount(), 2);
		assertEquals(state.getUsaState().getLeader(0).getName(), "LEADER_US_1");
		assertEquals(state.getUsaState().getLeader(0).getNumTermsAsPresident(), 0);
		assertEquals(state.getUsaState().getLeader(0).getNumTermsAsVicePresident(), 1);
		assertEquals(state.getUsaState().getLeader(1).getName(), "LEADER_US_2");
		assertEquals(state.getUsaState().getLeader(1).getNumTermsAsPresident(), 1);
		assertEquals(state.getUsaState().getLeader(1).getNumTermsAsVicePresident(), 0);
	}
	
	@Test
	public void testInitialTechnologyMechanic() {
		SuperpowerMechanic mechanic = getMechanic();
		assertEquals(mechanic.getUsa().getLeader("LEADER_US_1").getState().getName(), "LEADER_US_1");
		/*
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
	*/
	}
/*
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
		return new PseudorandomMechanic(s, state);
	}
	
	@Test
	public void testMaybeMakeProgress() {
		TechnologyMechanic mechanic = getMechanic();
		assertEquals(mechanic.getTechnologyGroup(Player.USA, "GROUP_A").getTechnology("TECH_A_1").getState().getProgress(), 0);
		assertEquals(mechanic.getTechnologyGroup(Player.USA, "GROUP_A").getProgressChance(), 500000);
		mechanic.makeResearchMove(Player.USA, ResearchMove.newBuilder().setTechnologyGroupId("GROUP_A").setMagnitude(5).build());
		
		mechanic.maybeMakeProgress(Player.USA, getPseudorandom());
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
			mechanic.maybeMakeProgress(Player.USA, random);
		}
		assertEquals(mechanic.getTechnologyGroup(Player.USA, "GROUP_B").getTechnology("TECH_B_1").getState().getProgress() / 100000.0, .5, .01);
	}
	
	@Test
	public void testStatePropagation() {
		TechnologyMechanic initMechanic = getMechanic();
		initMechanic.getTechnologyGroup(Player.USA, "GROUP_A").makeProgress();
		initMechanic.makeResearchMove(Player.USA, ResearchMove.newBuilder().setTechnologyGroupId("GROUP_A").setMagnitude(1).build());
		GameState state = GameState.newBuilder().setTechnologyState(initMechanic.buildState()).build();
		TechnologyMechanic mechanic = new TechnologyMechanic(initMechanic.getSettings(), state);
		assertEquals(mechanic.getTechnologyGroup(Player.USA, "GROUP_A").getTechnology("TECH_A_1").getState().getProgress(), 1);
		assertEquals(mechanic.getTechnologyGroup(Player.USA, "GROUP_A").getResearch(), 0);  // Research is not propagated.
	}
	*/
}
