package com.berserkbentobox.coldwar.logic.mechanics.province;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.berserkbentobox.coldwar.DissidentsOuterClass.Government;
import com.berserkbentobox.coldwar.Id.ProvinceId;
import com.berserkbentobox.coldwar.logic.Client.Player;

public class ChanceUtil extends ProvinceUtil {
	
	// TODO: Implement actual chances
	
	public static Map<Integer, Government> getDissidentsChance(ProvinceId id) {
		Map<Integer, Government> ret = new LinkedHashMap<Integer, Government>();
		ret.put(1, Government.DEMOCRACY);
		ret.put(1, Government.REPUBLIC);
		ret.put(1, Government.COMMUNISM);
		return ret;
	}
	
	public static Map<Integer, Government> getDissidentsChance(Player player, ProvinceId id) {
		Map<Integer, Government> ret = new LinkedHashMap<Integer, Government>();
		if(player != Player.USSR)
			ret.put(1, Government.DEMOCRACY);
		ret.put(1, Government.REPUBLIC);
		if(player != Player.USA)
			ret.put(1, Government.COMMUNISM);
		return ret;
	}
}
