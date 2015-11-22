package coldwar.logic;

import coldwar.GameStateOuterClass.GameState;
import coldwar.MoveListOuterClass.MoveList;

public interface BooleanComputation {
	public boolean compute(GameState state, MoveList usa, MoveList ussr);
}
