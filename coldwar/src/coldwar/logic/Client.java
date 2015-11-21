package coldwar.logic;

import java.util.HashMap;
import java.util.Map;

import coldwar.GameStateOuterClass;
import coldwar.MoveListOuterClass;
import coldwar.MoveOuterClass;
import coldwar.ProvinceOuterClass;
import coldwar.GameStateOuterClass.GameState;
import coldwar.MoveListOuterClass.MoveList;
import coldwar.MoveOuterClass.Move;
import coldwar.ProvinceOuterClass.Province;
import coldwar.ProvinceOuterClass.Province.Builder;
import coldwar.ProvinceOuterClass.Province.Id;

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
		
		// Update turn-based critical state values
		state
		 .setTurn(state.getTurn()+1);
		
		// Extract the province builders into a hashmap (key = Province.Id)
		Map<Province.Id, Province.Builder> provinceMap = new HashMap<Province.Id, Province.Builder>();
		
		for(Province.Builder province : state.getProvincesBuilderList()) {
			provinceMap.put(province.getId(), province);
		}
		
		// USA moves
		for(Move move : usa.getMovesList()) {
			if(move.hasFoundNatoMove()) {
				state.getUsaBuilder().setFoundNato(true);
				state.getUsaBuilder().setUnrest(state.getUsa().getUnrest()+1);
			}
			// Direct influence actions
			if(move.hasDiaDipMove()) {
				provinceMap.get(move.getDiaDipMove().getProvinceId()).setInfluence(move.getDiaDipMove().getMagnitude());
			}
			if(move.hasDiaMilMove()) {
				provinceMap.get(move.getDiaMilMove().getProvinceId()).setInfluence(move.getDiaMilMove().getMagnitude());
			}
			if(move.hasDiaCovMove()) {
				provinceMap.get(move.getDiaCovMove().getProvinceId()).setInfluence(move.getDiaCovMove().getMagnitude());
			}
		}
		
		// USSR moves
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