package coldwar;

import coldwar.ProvinceOuterClass.Province;

public class MoveBuilder {
	
	public int getInfluence(Province.Id provinceId) {
		return (int)(Math.random() * 5.0);
	}
	
	public static void IncreaseInfluence(Province.Id id) {
		Logger.Dbg("Increasing influence in province ID: " + id);
	}
	
	public static void DecreaseInfluence(Province.Id id) {
		Logger.Dbg("Decreasing influence in province ID: " + id);
	}

}
