package coldwar;

import coldwar.ProvinceOuterClass.Province;

public class MoveBuilder {
	
	public int getInfluence(Province.Id provinceId) {
		return (int)(Math.random() * 5.0);
	}
}
