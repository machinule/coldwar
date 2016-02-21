package com.berserkbentobox.coldwar.logic.mechanics.province;

import java.util.Map;

import com.berserkbentobox.coldwar.Id.ProvinceId;
import com.berserkbentobox.coldwar.logic.Client.Player;

public class InfluenceUtil extends ProvinceUtil {
	
	public static void influenceProvince(Player player, ProvinceId id, int magnitude) {
		if(player == Player.USSR)
			magnitude = magnitude * -1;
		influenceProvince(id, magnitude);
	}
	
	public static void influenceProvince(ProvinceId id, int magnitude) {
		Province target = provinces.get(id);
		target.influence(magnitude);
	}
}
