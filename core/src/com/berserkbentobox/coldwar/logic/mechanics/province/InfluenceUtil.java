package com.berserkbentobox.coldwar.logic.mechanics.province;

import java.util.Map;

import com.berserkbentobox.coldwar.Id.ProvinceId;
import com.berserkbentobox.coldwar.logic.Client.Player;
import com.berserkbentobox.coldwar.logic.mechanics.pseudorandom.PseudorandomMechanic;

public class InfluenceUtil extends ProvinceUtil {
	
	public InfluenceUtil(ProvinceUtil parent) {
		super(parent.provinces, parent.pseudorandom);
		// TODO Auto-generated constructor stub
	}

	public void influenceProvince(Player player, ProvinceId id, int magnitude) {
		if(player == Player.USSR)
			magnitude = magnitude * -1;
		influenceProvince(id, magnitude);
	}
	
	public void influenceProvince(ProvinceId id, int magnitude) {
		Province target = provinces.get(id);
		target.influence(magnitude);
	}
}
