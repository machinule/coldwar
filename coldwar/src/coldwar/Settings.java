package coldwar;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * Wrapper around libGDX Preferences to make them easy to access.
 * @see com.badlogic.gx.Preferences
 */
public class Settings {
	static Preferences consts = Gdx.app.getPreferences("constants");
	static Preferences debug = Gdx.app.getPreferences("debug");
	static Preferences prefs = Gdx.app.getPreferences("preferences");

	public static void init() {
		Settings.initConstants();
		Settings.initDebug();
		Settings.initPreferences();
	}
	
	public static void initPreferences() {
		Settings.prefs.putString("player", "Player");
		Settings.prefs.putInteger("resolution_width", 1280);
		Settings.prefs.putInteger("resolution_height", 1024);
		Settings.prefs.putBoolean("fullscreen", false);

		Settings.prefs.flush();
	}

	public static void initConstants() {
		Settings.consts.putBoolean("pack_textures", true);
		Settings.consts.putInteger("heat_start", 40);
		Settings.consts.putInteger("heat_min", 0);
		Settings.consts.putInteger("heat_max", 100);

		Settings.consts.flush();
	}

	public static void initDebug() {
		Settings.debug.putBoolean("pack_textures", true);
		Settings.debug.putBoolean("debug", true);
		Settings.debug.putInteger("log_level", Application.LOG_DEBUG);

		Settings.consts.flush();
	}
	public static boolean isDebug() {
		return Settings.debug.getBoolean("debug");
	}
	
	public static int getLogLevel() {
		return Settings.prefs.getInteger("log_level");
	}	
	
	public static boolean getConstBool(final String key) {
		return Settings.consts.getBoolean(key);
	}

	public static int getConstInt(final String key) {
		return Settings.consts.getInteger(key);
	}
}
