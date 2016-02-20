package com.berserkbentobox.coldwar.logic.mechanics.superpower;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.Collections;

import org.junit.Test;

import com.berserkbentobox.coldwar.GameSettingsOuterClass.GameSettings;
import com.berserkbentobox.coldwar.GameStateOuterClass.GameState;
import com.berserkbentobox.coldwar.Superpower.PresidencySettings;
import com.berserkbentobox.coldwar.Superpower.UsaLeaderParty;
import com.berserkbentobox.coldwar.Superpower.UsaSettings;
import com.berserkbentobox.coldwar.Superpower.UssrSettings;
import com.berserkbentobox.coldwar.logic.mechanics.pseudorandom.PseudorandomMechanic;

public class SuperpowerMechanicTest {
	
	SuperpowerMechanic.Settings getSettings() {
		GameSettings.Builder settings = GameSettings.newBuilder();
		
		UsaSettings.Builder usa = settings.getSuperpowerSettingsBuilder()
			.getUsaSettingsBuilder();
		usa.addLeaderSettingsBuilder()
			.setName("LEADER_US_R_1")
			.setParty(UsaLeaderParty.REPUBLICAN)
			.setBirthYear(1920)
			.setStartYear(1945)
			.setEndYear(1960)
			.setInitNumTermsAsVicePresident(1);
		usa.addLeaderSettingsBuilder()
			.setName("LEADER_US_R_2")
			.setParty(UsaLeaderParty.REPUBLICAN)
			.setBirthYear(1920)
			.setStartYear(1945)
			.setEndYear(1960);
		usa.addLeaderSettingsBuilder()
			.setName("LEADER_US_R_3")
			.setParty(UsaLeaderParty.REPUBLICAN)
			.setBirthYear(1920)
			.setStartYear(1945)
			.setEndYear(1960);
		usa.addLeaderSettingsBuilder()
			.setName("LEADER_US_R_4")
			.setParty(UsaLeaderParty.REPUBLICAN)
			.setBirthYear(1920)
			.setViceYears(15)
			.setStartYear(1960)
			.setEndYear(1980);
		usa.addLeaderSettingsBuilder()
			.setName("LEADER_US_R_5")
			.setParty(UsaLeaderParty.REPUBLICAN)
			.setBirthYear(1920)
			.setStartYear(1960)
			.setEndYear(1980);
		
		usa.addLeaderSettingsBuilder()
			.setName("LEADER_US_D_1")
			.setParty(UsaLeaderParty.DEMOCRAT)
			.setBirthYear(1905)
			.setStartYear(1945)
			.setEndYear(1960)
			.setInitNumTermsAsPresident(1);
		usa.addLeaderSettingsBuilder()
			.setName("LEADER_US_D_2")
			.setParty(UsaLeaderParty.DEMOCRAT)
			.setBirthYear(1905)
			.setStartYear(1945)
			.setEndYear(1960);
		usa.addLeaderSettingsBuilder()
			.setName("LEADER_US_D_3")
			.setParty(UsaLeaderParty.DEMOCRAT)
			.setBirthYear(1905)
			.setStartYear(1945)
			.setEndYear(1960);
		usa.addLeaderSettingsBuilder()
			.setName("LEADER_US_D_4")
			.setParty(UsaLeaderParty.DEMOCRAT)
			.setBirthYear(1920)
			.setViceYears(15)
			.setStartYear(1960)
			.setEndYear(1980);
		usa.addLeaderSettingsBuilder()
			.setName("LEADER_US_D_5")
			.setParty(UsaLeaderParty.DEMOCRAT)
			.setBirthYear(1920)
			.setStartYear(1960)
			.setEndYear(1980);
		
		PresidencySettings.Builder presidency = usa.getPresidencySettingsBuilder()
			.setInitPresident("LEADER_US_D_1")
			.setInitVicePresident("LEADER_US_R_1");
		
		UssrSettings.Builder ussr = settings.getSuperpowerSettingsBuilder()
			.getUssrSettingsBuilder();
		ussr.addLeaderSettingsBuilder()
			.setName("LEADER_USSR_1")
			.setInitPartySupport(4)
			.setBirthYear(1900)
			.setStartYear(1945);
		ussr.addLeaderSettingsBuilder()
			.setName("LEADER_USSR_2")
			.setBirthYear(1905)
			.setStartYear(1945);
		
		return new SuperpowerMechanic.Settings(settings);
	}
	
	public SuperpowerMechanic getMechanic() {
		GameState.Builder state = GameState.newBuilder();
		SuperpowerMechanic.Settings settings = getSettings();
		state.setSuperpowerState(settings.initialState());
		return new SuperpowerMechanic(null, settings, state.build());
	}
	
	@Test
	public void testInitialSuperpowerMechanic() {
		SuperpowerMechanic mechanic = getMechanic();

		assertEquals(mechanic.getUsa().getState().getPresident(), "LEADER_US_D_1");
		assertEquals(mechanic.getUsa().getState().getVicePresident(), "LEADER_US_R_1");
		
		assertEquals(mechanic.getUsa().getLeader("LEADER_US_R_1").getState().getName(), "LEADER_US_R_1");
		assertEquals(mechanic.getUsa().getLeader("LEADER_US_R_1").getState().getNumTermsAsPresident(), 0);
		assertEquals(mechanic.getUsa().getLeader("LEADER_US_R_1").getState().getNumTermsAsVicePresident(), 1);
		assertEquals(mechanic.getUsa().getLeader("LEADER_US_R_2").getState().getName(), "LEADER_US_R_2");
		assertEquals(mechanic.getUsa().getLeader("LEADER_US_R_2").getState().getNumTermsAsPresident(), 0);
		assertEquals(mechanic.getUsa().getLeader("LEADER_US_R_2").getState().getNumTermsAsVicePresident(), 0);
		assertEquals(mechanic.getUsa().getLeader("LEADER_US_R_3").getState().getName(), "LEADER_US_R_3");
		assertEquals(mechanic.getUsa().getLeader("LEADER_US_R_3").getState().getNumTermsAsPresident(), 0);
		assertEquals(mechanic.getUsa().getLeader("LEADER_US_R_3").getState().getNumTermsAsVicePresident(), 0);
		assertEquals(mechanic.getUsa().getLeader("LEADER_US_R_4").getState().getName(), "LEADER_US_R_4");
		assertEquals(mechanic.getUsa().getLeader("LEADER_US_R_4").getState().getNumTermsAsPresident(), 0);
		assertEquals(mechanic.getUsa().getLeader("LEADER_US_R_4").getState().getNumTermsAsVicePresident(), 0);
		assertEquals(mechanic.getUsa().getLeader("LEADER_US_R_5").getState().getName(), "LEADER_US_R_5");
		assertEquals(mechanic.getUsa().getLeader("LEADER_US_R_5").getState().getNumTermsAsPresident(), 0);
		assertEquals(mechanic.getUsa().getLeader("LEADER_US_R_5").getState().getNumTermsAsVicePresident(), 0);
		
		assertEquals(mechanic.getUsa().getLeader("LEADER_US_D_1").getState().getName(), "LEADER_US_D_1");
		assertEquals(mechanic.getUsa().getLeader("LEADER_US_D_1").getState().getNumTermsAsPresident(), 1);
		assertEquals(mechanic.getUsa().getLeader("LEADER_US_D_1").getState().getNumTermsAsVicePresident(), 0);
		assertEquals(mechanic.getUsa().getLeader("LEADER_US_D_2").getState().getName(), "LEADER_US_D_2");
		assertEquals(mechanic.getUsa().getLeader("LEADER_US_D_2").getState().getNumTermsAsPresident(), 0);
		assertEquals(mechanic.getUsa().getLeader("LEADER_US_D_2").getState().getNumTermsAsVicePresident(), 0);
		assertEquals(mechanic.getUsa().getLeader("LEADER_US_D_3").getState().getName(), "LEADER_US_D_3");
		assertEquals(mechanic.getUsa().getLeader("LEADER_US_D_3").getState().getNumTermsAsPresident(), 0);
		assertEquals(mechanic.getUsa().getLeader("LEADER_US_D_3").getState().getNumTermsAsVicePresident(), 0);
		assertEquals(mechanic.getUsa().getLeader("LEADER_US_D_4").getState().getName(), "LEADER_US_D_4");
		assertEquals(mechanic.getUsa().getLeader("LEADER_US_D_4").getState().getNumTermsAsPresident(), 0);
		assertEquals(mechanic.getUsa().getLeader("LEADER_US_D_4").getState().getNumTermsAsVicePresident(), 0);
		assertEquals(mechanic.getUsa().getLeader("LEADER_US_D_5").getState().getName(), "LEADER_US_D_5");
		assertEquals(mechanic.getUsa().getLeader("LEADER_US_D_5").getState().getNumTermsAsPresident(), 0);
		assertEquals(mechanic.getUsa().getLeader("LEADER_US_D_5").getState().getNumTermsAsVicePresident(), 0);
		
		assertEquals(mechanic.getUssr().getLeader("LEADER_USSR_1").getState().getName(), "LEADER_USSR_1");
		assertEquals(mechanic.getUssr().getLeader("LEADER_USSR_1").getState().getPartySupport(), 4);
		assertEquals(mechanic.getUssr().getLeader("LEADER_USSR_2").getState().getName(), "LEADER_USSR_2");
	}

	@Test
	public void testEligiblePresidents() {
		SuperpowerMechanic mechanic = getMechanic();
		Collection<String> eligible = mechanic.getUsa().getPresidentialEligible(1946);
		assertEquals(6, eligible.size());
	}
	
	@Test
	public void testEligibleVicePresidents() {
		SuperpowerMechanic mechanic = getMechanic();
		Collection<String> eligibleRep = mechanic.getUsa().getVicePresidentialEligible(1946, UsaLeaderParty.REPUBLICAN);
		assertEquals(8, eligibleRep.size());
		Collection<String> eligibleDem = mechanic.getUsa().getVicePresidentialEligible(1946, UsaLeaderParty.REPUBLICAN);
		assertEquals(8, eligibleDem.size());
	}
	
	@Test
	public void testElection() {
		SuperpowerMechanic mechanic = getMechanic();
		mechanic.getUsa().setCandidate("LEADER_US_D_2");
		mechanic.getUsa().setCandidate("LEADER_US_D_3");
	}
/*
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
