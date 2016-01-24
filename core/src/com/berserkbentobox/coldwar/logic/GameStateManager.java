package com.berserkbentobox.coldwar.logic;

import java.util.Random;

import com.berserkbentobox.coldwar.GameStateOuterClass.GameState;
import com.berserkbentobox.coldwar.MoveOuterClass.Move;
import com.berserkbentobox.coldwar.MoveOuterClass.MoveList;
import com.berserkbentobox.coldwar.logic.Client.Player;

// The GameStateManager controls the logic of various mechanics.
// There is a strong separation between deterministic behavior that should
// be visible to the user while their turn is ongoing, and nondeterministic
// behavior that should only be run on turn end.
public class GameStateManager {

	private MechanicSettings settings;
	private GameState initialState;
	
	public GameStateManager(MechanicSettings settings, GameState initialState) {
		this.settings = settings;
		this.initialState = initialState;
	}
	
	public Mechanics computeDeterministicMechanics(MoveList usaMoves, MoveList ussrMoves) {
		Mechanics mechanics = new Mechanics(this.settings, this.initialState);
		
		// Technology
		for (Move move : usaMoves.getMovesList()) {
			if (move.hasTechnologyMechanicMoves()) {
				mechanics.getTechnology().makeMoves(Player.USA, move.getTechnologyMechanicMoves());							
			}
		}
		for (Move move : ussrMoves.getMovesList()) {
			if (move.hasTechnologyMechanicMoves()) {
				mechanics.getTechnology().makeMoves(Player.USSR, move.getTechnologyMechanicMoves());							
			}
		}
		return mechanics;
	}
	
	public GameState computeNextGameState(Mechanics mechanics) {
		Random random = new Random(this.initialState.getPseudorandomState().getSeed());
		
		// Technology
		mechanics.getTechnology().maybeMakeProgress(Player.USSR, random);
		mechanics.getTechnology().maybeMakeProgress(Player.USSR, random);
		
		GameState.Builder state = GameState.newBuilder();
		state.setTechnologyState(mechanics.getTechnology().buildState());
		return state.build();
	}
}
