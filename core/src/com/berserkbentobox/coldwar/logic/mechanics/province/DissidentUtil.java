package com.berserkbentobox.coldwar.logic.mechanics.province;

import java.util.List;
import java.util.Map;

import com.berserkbentobox.coldwar.Logger;
import com.berserkbentobox.coldwar.DissidentsOuterClass.Dissidents;
import com.berserkbentobox.coldwar.DissidentsOuterClass.Government;
import com.berserkbentobox.coldwar.Id.ProvinceId;
import com.berserkbentobox.coldwar.logic.Client.Player;

public class DissidentUtil extends ProvinceUtil {
	
	public static void addDissidents(ProvinceId id, Player player) {
		Province target = provinces.get(id);
		Dissidents.Builder diss = Dissidents.newBuilder();
		// TODO: Pick a leader
		Map<Integer, Government> chances = ChanceUtil.getDissidentsChance(player, id);
		int result = pseudorandom.roll((List<Integer>) chances.keySet());
		diss.setGov(chances.get(result));
		target.addDissidents(diss.build());
	}
	
	public static void addDissidents(ProvinceId id) {
		Province target = provinces.get(id);
		Dissidents.Builder diss = Dissidents.newBuilder();
		// TODO: Pick a leader
		Map<Integer, Government> chances = ChanceUtil.getDissidentsChance(id);
		int result = pseudorandom.roll((List<Integer>) chances.keySet());
		target.addDissidents(diss.build());
	}
	
	public static void addDissidents(ProvinceId id, Government gov) {
		Province target = provinces.get(id);
		Dissidents.Builder diss = Dissidents.newBuilder();
		// TODO: Pick a leader
		diss.setGov(gov);
		target.addDissidents(diss.build());
	}
	
	// Suppress Dissidents
	
	public static void suppressDissidents(ProvinceId id) {
		Province p = provinces.get(id);
		if(p.hasDissidents())
			maybeSuppressDissidents(p);
		else
			Logger.Err(id + " has no dissidents!");
	}
	
	public static void maybeSuppressDissidents() {
		for(Province p : provinces.values()) {
			DissidentUtil.maybeSuppressDissidents(p);
		}
	}
	
	public static void maybeSuppressDissidents(Province p) {
		
	}
	
	// Add Dissidents
}
