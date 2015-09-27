package coldwar;

import coldwar.GameStateOuterClass.GameState;
import coldwar.GameStateOuterClass.GameStateOrBuilder;
import coldwar.MoveListOuterClass.MoveList;
import coldwar.MoveOuterClass.Move;

public class Client {
	
	String ver = "0.0.1";
	
	public void Init() {
		GameState.Builder baseState = GameState.newBuilder();
		baseState.setVersion("0.01")
				 .setHeat(20)
				 .setTurn(0);
	}
	
	public GameState Update(GameState in_state, MoveList usa, MoveList ussr) { 
		
		GameState.Builder state = GameState.newBuilder().mergeFrom(in_state);
		
		state
		 .setTurn(state.getTurn()+1);
		
		// USA		
		for(Move move : usa.getMovesList()) {
			if(move.hasFoundNatoMove()) {
				state.getUsaBuilder().setFoundNato(true);
				state.getUsaBuilder().setUnrest(state.getUsa().getUnrest()+1);
			}
		}
		
		// USSR
		/*
		
		for(Move move : ussr.getMovesList()) {
			if(move.hasFoundPactMove()) {
				state.getUssrBuilder().setFoundPact(true);
				
			}
		}
	
		*/
		
		return state.build();
	}
}