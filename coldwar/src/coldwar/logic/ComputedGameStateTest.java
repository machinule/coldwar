package coldwar.logic;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import com.google.protobuf.TextFormat;
import com.google.protobuf.TextFormat.ParseException;

import coldwar.GameStateOuterClass.GameState;
import coldwar.MoveOuterClass.MoveList;

public class ComputedGameStateTest {

	private static GameState GS(String... s) throws ParseException {
		return GSB(s).build();
	}
	
	private static GameState.Builder GSB(String... s) throws ParseException {
		GameState.Builder state = GameState.newBuilder();
		TextFormat.merge(String.join("\n", s), state);
		return state;
	}

	private static MoveList ML(String... s) throws ParseException {
		return MLB(s).build();
	}
	
	private static MoveList.Builder MLB(String... s) throws ParseException {
		MoveList.Builder moves = MoveList.newBuilder();
		TextFormat.merge(String.join("\n", s), moves);
		return moves;
	}
	
	private static GameState computeState(GameState state, MoveList[]... turns) {
		GameState currentState = state;
		for(MoveList[] turn : turns) {
			currentState = (new ComputedGameState(currentState, turn[0], turn[1])).nextState;
		}
		return currentState;
	}
    
	private static GameState.Builder eventFreeGameState() throws ParseException {
		return GSB(
				"settings {",
				"  version: 'test'",
				"  heat_init: 40",
				"  heat_min: 0",
				"  heat_max: 100",
				"  seed_init: 0",
				"  usa_alias: 'USA'",
				"  usa_pol_store_init: 4",
				"  usa_pol_income_base: 4",
				"  usa_mil_store_init: 3",
				"  usa_mil_income_base: 3",
				"  usa_cov_store_init: 1",
				"  usa_cov_income_base: 1",
				"  ussr_alias: 'USSR'",
				"  ussr_pol_store_init: 4",
				"  ussr_pol_income_base: 4",
				"  ussr_mil_store_init: 3",
				"  ussr_mil_income_base: 3",
				"  ussr_cov_store_init: 1",
				"  ussr_cov_income_base: 1",
				"  random_leader_spawn_chance: 0",
				"  random_leader_death_chance: 0",
				"  random_province_coup_chance: 0",
				"  random_province_democracy_chance: 0",
				"  random_province_communism_chance: 0",
				"  random_province_autocracy_chance: 0",
				"  random_province_republic_chance: 0",
				"  random_province_faux_pas_chance: 0",
				"  random_province_A_C_D_dissidents_chance: 0",
				"  random_province_default_dissidents_chance: 0",
				"  random_province_A_C_D_dissidents_suppressed_chance: 0",
				"  random_province_default_dissidents_suppressed_chance: 0",
				"  random_us_ally_democracy_chance: 0",
				"  random_us_ally_communism_chance: 0",
				"  random_end_civil_war_chance: 0",
				"  coup_success_chance: 0",
				"  coup_leader_chance: 0",
				"  coup_backfire_chance: 0",
				"  kgb_cov_income_modifier: 2",
				"  cia_cov_income_modifier: 2",
				"}");
	}
	
	@Test
	public void testTurnsIncrement() throws ParseException {
		GameState.Builder initial = eventFreeGameState();
		initial.setTurn(12);
		GameState state;
		state = computeState(
				initial.build(),
				new MoveList[]{ML(""), ML("")});
		assertEquals(13, state.getTurn());
		state = computeState(
				initial.build(),
				new MoveList[]{ML(""), ML("")},
				new MoveList[]{ML(""), ML("")},
				new MoveList[]{ML(""), ML("")},
				new MoveList[]{ML(""), ML("")},
				new MoveList[]{ML(""), ML("")});
		assertEquals(17, state.getTurn());
	}
    
	@Test
	public void testMinimumHeat() throws ParseException {
		GameState.Builder initial = eventFreeGameState();
		initial.setHeat(40);
		initial.getSettingsBuilder().setHeatMin(35);
		GameState state;
		state = computeState(
				initial.build(),
				new MoveList[]{ML(""), ML("")});
		assertEquals(35, state.getHeat());
	}
	
	@Test
	public void testHeatDecays() throws ParseException {
		GameState.Builder initial = eventFreeGameState();
		initial.setHeat(42);
		initial.getSettingsBuilder().setHeatMin(0);
		GameState state;
		state = computeState(
				initial.build(),
				new MoveList[]{ML(""), ML("")},
				new MoveList[]{ML(""), ML("")});
		assertEquals(22, state.getHeat());
	}

	@Test
	public void testNoEventsForZeroChance() throws ParseException {
		for(int i=0; i<1000; i++) {
			GameState.Builder initial = eventFreeGameState();
			initial.setSeed(i);
			GameState state;
			state = computeState(
					initial.build(),
					new MoveList[]{ML(""), ML("")});
			assertEquals(0, state.getTurnLog().getEventsCount());	
		}
	}
	
	@Test
	public void testSettingsNotDuplicated() throws ParseException {
		GameState.Builder initial = eventFreeGameState();
		GameState state;
		state = computeState(
				initial.build(),
				new MoveList[]{ML(""), ML("")});
		assertEquals(false, state.getTurnLog().getInitialState().hasSettings());	
	}
	
	@Test
	public void testTurnLogCopiesMoves() throws ParseException {
		GameState.Builder initial = eventFreeGameState();
		GameState state;
		state = computeState(
				initial.build(),
				new MoveList[]{ML("moves{found_cia_move{}}"), ML("moves{found_kgb_move{}}")});
		assertEquals(ML("moves{found_cia_move{}}"), state.getTurnLog().getUsaMoves());	
		assertEquals(ML("moves{found_kgb_move{}}"), state.getTurnLog().getUssrMoves());	
	}
	
	@Test
	public void testTurnLogCopiesTurnLog() throws ParseException {
		GameState.Builder initial = eventFreeGameState();
		GameState state;
		state = computeState(
				initial.build(),
				new MoveList[]{ML("moves{found_cia_move{}}"), ML("moves{found_kgb_move{}}")},
				new MoveList[]{ML(""), ML("")});
		assertEquals(ML("moves{found_cia_move{}}"), state.getTurnLog().getInitialState().getTurnLog().getUsaMoves());	
		assertEquals(ML("moves{found_kgb_move{}}"), state.getTurnLog().getInitialState().getTurnLog().getUssrMoves());	
	}
	
	@Test
	public void testTurnLogCopiesGameState() throws ParseException {
		GameState.Builder initial = eventFreeGameState();
		GameState state;
		state = computeState(
				initial.build(),
				new MoveList[]{ML(""), ML("")});
		assertEquals(initial.clearSettings().build(), state.getTurnLog().getInitialState());	
	}
}