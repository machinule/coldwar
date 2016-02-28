package com.berserkbentobox.coldwar.logic.mechanics.province;

import com.berserkbentobox.coldwar.Id.ProvinceId;
import com.berserkbentobox.coldwar.Province.SpyNetwork;
import com.berserkbentobox.coldwar.logic.Client.Player;

public class SpyNetworkUtil extends ProvinceUtil {
	
	public SpyNetworkUtil(ProvinceUtil parent) {
		super(parent.provinces, parent.pseudorandom);
	}

	public void establishNetwork(ProvinceId id, Player player) {
		ProvinceId owner;
		if(player == Player.USSR) {
			owner = ProvinceId.USSR;
		} else {
			owner = ProvinceId.USA;
		}
		SpyNetwork.Builder spy = SpyNetwork.newBuilder()
				.setOwner(owner);
		provinces.get(id).addSpyNetwork(spy);
	}
}
