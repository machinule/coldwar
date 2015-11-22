package coldwar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Settings {
	static Preferences consts = Gdx.app.getPreferences("Constants");
	static Preferences prefs = Gdx.app.getPreferences("Preferences");

	public static void initPreferences() {
		Settings.prefs.putString("Player", "Player");

		Settings.prefs.flush();
	}

	public static void initConstants() {
		Settings.consts.putBoolean("Init", true);
		Settings.consts.putInteger("Heat - Starting", 40);
		Settings.consts.putInteger("Heat - Min", 0);
		Settings.consts.putInteger("Heat - Max", 100);

		Settings.consts.flush();
	}

	public static boolean getConstBool(final String key) {
		return Settings.consts.getBoolean(key);
	}

	public static int getConstInt(final String key) {
		return Settings.consts.getInteger(key);
	}
}
