package com.berserkbentobox.coldwar.logic.mechanics.province;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.berserkbentobox.coldwar.Logger;
import com.berserkbentobox.coldwar.DissidentsOuterClass.Dissidents;
import com.berserkbentobox.coldwar.DissidentsOuterClass.Government;
import com.berserkbentobox.coldwar.Id.ProvinceId;
import com.berserkbentobox.coldwar.logic.Client.Player;

public class DissidentUtil extends ProvinceUtil {
	
	public DissidentUtil(ProvinceUtil parent) {
		super(parent.provinces, parent.pseudorandom);
	}

	public void addDissidents(ProvinceId id, Player player) {
		Province target = provinces.get(id);
		Dissidents.Builder diss = Dissidents.newBuilder();
		// TODO: Pick a leader
		LinkedHashMap<Object, Integer> chances = new LinkedHashMap<Object, Integer>(ChanceUtil.getDissidentsChance(player, id));
		Government result = (Government) pseudorandom.roll(chances);
		diss.setGov(result);
		target.addDissidents(diss);
	}
	
	public void addDissidents(ProvinceId id) {
		Province target = provinces.get(id);
		Dissidents.Builder diss = Dissidents.newBuilder();
		// TODO: Pick a leader
		LinkedHashMap<Object, Integer> chances = new LinkedHashMap<Object, Integer>(ChanceUtil.getDissidentsChance(id));
		Government result = (Government) pseudorandom.roll(chances);
		diss.setGov(result);
		target.addDissidents(diss);
	}
	
	public void addDissidents(ProvinceId id, Government gov) {
		Province target = provinces.get(id);
		Dissidents.Builder diss = Dissidents.newBuilder();
		// TODO: Pick a leader
		diss.setGov(gov);
		target.addDissidents(diss);
	}
	
	// Suppress Dissidents
	
	public void suppressDissidents(ProvinceId id) {
		Province p = provinces.get(id);
		if(p.hasDissidents())
			maybeSuppressDissidents(p);
		else
			Logger.Err(id + " has no dissidents!");
	}
	
	public void maybeSuppressDissidents() {
		for(Province p : provinces.values()) {
			this.maybeSuppressDissidents(p);
		}
	}
	
	public void maybeSuppressDissidents(Province p) {
		
	}
	
	// Add Dissidents
}
