package com.berserkbentobox.coldwar.logic.mechanics.province;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.berserkbentobox.coldwar.DissidentsOuterClass.Government;
import com.berserkbentobox.coldwar.Id.ProvinceId;
import com.berserkbentobox.coldwar.Logger;
import com.berserkbentobox.coldwar.logic.Client.Player;

public class ChanceUtil extends ProvinceUtil {
	
	// TODO: Implement actual chances
	
	public static LinkedHashMap<Government, Integer> getDissidentsChance(ProvinceId id) {
		LinkedHashMap<Government, Integer> ret = new LinkedHashMap<Government, Integer>();
		ret.put(Government.DEMOCRACY, 1);
		ret.put(Government.REPUBLIC, 1);
		ret.put(Government.COMMUNISM, 1);
		return ret;
	}
	
	public static LinkedHashMap<Government, Integer> getDissidentsChance(Player player, ProvinceId id) {
		LinkedHashMap<Government, Integer> ret = new LinkedHashMap<Government, Integer>();
		if(player == Player.USSR) {
			ret.put(Government.COMMUNISM, 1);
		}
		ret.put(Government.REPUBLIC, 1);
		if(player == Player.USA) {
			ret.put(Government.DEMOCRACY, 1);
		}
		return ret;
	}
}
