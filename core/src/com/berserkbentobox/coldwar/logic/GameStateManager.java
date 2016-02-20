package com.berserkbentobox.coldwar.logic;

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
		
		for (Move move : usaMoves.getMovesList()) {
			// Provinces
			if(move.hasProvinceMechanicMoves()) {
				mechanics.getProvinces().makeMoves(Player.USA, move.getProvinceMechanicMoves());
			}
			// Technology
			if (move.hasTechnologyMechanicMoves()) {
				mechanics.getTechnology().makeMoves(Player.USA, move.getTechnologyMechanicMoves());							
			}
			// Treaty
			if (move.hasTreatyMechanicMoves()) {
				mechanics.getTreaty().makeMoves(Player.USA, move.getTreatyMechanicMoves());							
			}
			// Superpower
			if (move.hasSuperpowerMechanicMoves()) {
				mechanics.getSuperpower().makeMoves(Player.USA, move.getSuperpowerMechanicMoves());							
			}			
			// Crisis
			if (move.hasCrisisMechanicMoves()) {
				mechanics.getCrisis().makeMoves(Player.USSR, move.getCrisisMechanicMoves());							
			}
		}
		
		for (Move move : ussrMoves.getMovesList()) {
			// Provinces
			if(move.hasProvinceMechanicMoves()) {
				mechanics.getProvinces().makeMoves(Player.USSR, move.getProvinceMechanicMoves());
			}
			// Technology
			if (move.hasTechnologyMechanicMoves()) {
				mechanics.getTechnology().makeMoves(Player.USSR, move.getTechnologyMechanicMoves());							
			}
			// Treaty
			if (move.hasTreatyMechanicMoves()) {
				mechanics.getTreaty().makeMoves(Player.USSR, move.getTreatyMechanicMoves());							
			}
			// Superpower
			if (move.hasSuperpowerMechanicMoves()) {
				mechanics.getSuperpower().makeMoves(Player.USA, move.getSuperpowerMechanicMoves());							
			}			
			// Crisis
			if (move.hasCrisisMechanicMoves()) {
				mechanics.getCrisis().makeMoves(Player.USA, move.getCrisisMechanicMoves());							
			}
		}		
		
		// Heat
		mechanics.getHeat().decay();	
		mechanics.getHeat().normalize();	
		
		return mechanics;
	}
	
	public GameState computeNextGameState(Mechanics mechanics) {
		// Technology
		mechanics.getTechnology().maybeMakeProgress(Player.USSR);
		mechanics.getTechnology().maybeMakeProgress(Player.USA);
		
		// Superpower
		
		int year = initialState.getTurn() + 1948;
		if(mechanics.getSuperpower().getUsa().isElectionYear(year)) {
			mechanics.getSuperpower().getUsa().elections(year, mechanics.getPseudorandom());
		}
		mechanics.getSuperpower().getUsa().maybeKillVicePresident(year, mechanics.getPseudorandom());
		mechanics.getSuperpower().getUsa().maybeKillPresident(year, mechanics.getPseudorandom());
		
		mechanics.getSuperpower().getUssr().troikaUpdate(mechanics.getPseudorandom());
		mechanics.getSuperpower().getUssr().maybeKillLeader(year, mechanics.getPseudorandom());
		
		// Treaty
		mechanics.getTreaty().maybeSignTreaty(mechanics.getPseudorandom(), mechanics.getHeat(), mechanics.getDeterrence());

		//Crisis		
		mechanics.getCrisis().resolveCrisis(mechanics.getInfluence());
		mechanics.getCrisis().generateCrisis(year, mechanics.getPseudorandom());
		
		// Pseudorandom
		mechanics.getPseudorandom().reseed();
		

		return mechanics.buildState();
	}
}
