package com.berserkbentobox.coldwar.logic.mechanics.province;

import java.util.Map;

import com.berserkbentobox.coldwar.Id.ProvinceId;
import com.berserkbentobox.coldwar.logic.mechanics.pseudorandom.PseudorandomMechanic;

public class ProvinceUtil {
	protected static Map<ProvinceId, Province> provinces;
	protected static PseudorandomMechanic pseudorandom;
	
	public static void set(Map<ProvinceId, Province> provinces, PseudorandomMechanic pseudorandom) {
		ProvinceUtil.provinces = provinces;
		ProvinceUtil.pseudorandom = pseudorandom;
	}
}
