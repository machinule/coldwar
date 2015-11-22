package coldwar.logic;

import coldwar.GameStateOuterClass.GameState;
import coldwar.MoveListOuterClass.MoveList;

public interface GameStateComputation {
	public GameState compute(GameState state, MoveList usa, MoveList ussr);
}
