package coldwar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Settings {
	static Preferences consts = Gdx.app.getPreferences("Constants");
	static Preferences prefs = Gdx.app.getPreferences("Preferences");
	
	public static void initPreferences() {
		prefs.putString("Player", "Player");
		
		prefs.flush();
	}
	
	public static void initConstants() {
		consts.putBoolean("Init", true);
		consts.putInteger("Heat - Starting", 40);
		consts.putInteger("Heat - Min", 0);
		consts.putInteger("Heat - Max", 100);
		
		consts.flush();
	}
	
	public static boolean getConstBool(String key) {
		return consts.getBoolean(key);
	}
	
	public static int getConstInt(String key) {
		return consts.getInteger(key);
	}
}
