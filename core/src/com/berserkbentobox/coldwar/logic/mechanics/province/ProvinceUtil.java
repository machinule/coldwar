package com.berserkbentobox.coldwar.logic.mechanics.province;

import java.util.Map;

import com.berserkbentobox.coldwar.Id.ProvinceId;
import com.berserkbentobox.coldwar.logic.mechanics.pseudorandom.PseudorandomMechanic;

public class ProvinceUtil {
	protected Map<ProvinceId, Province> provinces;
	protected PseudorandomMechanic pseudorandom;
	
	public ProvinceUtil(Map<ProvinceId, Province> provinces, PseudorandomMechanic pseudorandom) {
		this.provinces = provinces;
		this.pseudorandom = pseudorandom;
	}
	
	public void set(Map<ProvinceId, Province> provinces, PseudorandomMechanic pseudorandom) {
		this.provinces = provinces;
		this.pseudorandom = pseudorandom;
	}
}
