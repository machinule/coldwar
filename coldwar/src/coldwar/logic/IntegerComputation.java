package coldwar.logic;

import coldwar.GameStateOuterClass;
import coldwar.MoveListOuterClass;
import coldwar.GameStateOuterClass.GameState;
import coldwar.MoveListOuterClass.MoveList;

public interface IntegerComputation {
	public int compute(GameState state, MoveList usa, MoveList ussr);
}
