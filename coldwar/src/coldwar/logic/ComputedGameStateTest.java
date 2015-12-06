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

@RunWith(Parameterized.class)
public class ComputedGameStateTest {

	private static GameState GS(String... s) throws ParseException {
		GameState.Builder state = GameState.newBuilder();
		TextFormat.merge(String.join("\n", s), state);
		return state.build();
	}
	
	private static MoveList ML(String... s) throws ParseException {
		MoveList.Builder moves = MoveList.newBuilder();
		TextFormat.merge(String.join("\n", s), moves);
		return moves.build();
	}
	
    @Parameters
    public static Collection<Object[]> data() throws ParseException {
        return Arrays.asList(new Object[][] {     
                 {GS(
                		 "")},
           });
    }
    
    @Parameter
    public GameState baseState;

	@Test
	public void testNoMovesHaveNoEffect() {
		ComputedGameState state = new ComputedGameState(baseState, MoveList.getDefaultInstance(), MoveList.getDefaultInstance());
		assertEquals(state, baseState);  // not really. 
	}
    
	@Test
	public void testPolInfluenceIncreasesInfluence() throws ParseException {
		ComputedGameState state = new ComputedGameState(
				baseState,
				ML(
						"moves {",
						"}"),
				MoveList.getDefaultInstance());
		assertEquals(state, GS(""));
	}
}