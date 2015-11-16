package coldwar;

import coldwar.ProvinceOuterClass.Province;

public class MoveBuilder {

	public static void IncreaseInfluence(Province.Id id) {
		Logger.Dbg("Increasing influence in province ID: " + id);
	}
	
	public static void DecreaseInfluence(Province.Id id) {
		Logger.Dbg("Decreasing influence in province ID: " + id);
	}

}
