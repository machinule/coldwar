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
		Settings.prefs.putInteger("resolution_height", 960);
		Settings.prefs.putBoolean("fullscreen", false);

		Settings.prefs.flush();
	}

	public static void initConstants() {
		Settings.consts.putBoolean("pack_textures", true);
		Settings.consts.putInteger("splash_x", 480);
		Settings.consts.putInteger("splash_y", 551);
		Settings.consts.putInteger("heat_start", 40);
		Settings.consts.putInteger("heat_min", 0);
		Settings.consts.putInteger("heat_max", 100);
		Settings.consts.putInteger("starting_pol", 4);
		Settings.consts.putInteger("starting_mil", 4);
		Settings.consts.putInteger("starting_cov", 1);
		Settings.consts.putInteger("base_income_pol", 4);
		Settings.consts.putInteger("base_income_mil", 4);
		Settings.consts.putInteger("base_income_cov", 1);
		
		Settings.consts.putInteger("action_dissidents_cost", 2); // COV
		Settings.consts.putInteger("action_dissidents_heat", 4);
		
		Settings.consts.putInteger("action_pressure_cost", 2); // POL
		Settings.consts.putInteger("action_pressure_heat", 4);
		Settings.consts.putInteger("action_pressure_heat_extra", 4); // If target was allied to enemy
		
		Settings.consts.putInteger("action_base_cost", 2); // MIL
		Settings.consts.putInteger("action_base_heat", 4);

		Settings.consts.putInteger("action_coup_cost_per_stab", 1); // COV
		Settings.consts.putInteger("action_coup_heat_fixed", 5);
		Settings.consts.putInteger("action_coup_heat_per_stab", 2);
		Settings.consts.putInteger("action_coup_lock_time", 2);
		Settings.consts.putInteger("action_coup_stab_threshold", 2);
		

		Settings.consts.flush();
	}

	public static void initDebug() {
		Settings.debug.putBoolean("pack_textures", true);
		Settings.debug.putBoolean("debug", true);
		Settings.debug.putInteger("log_level", Application.LOG_DEBUG);

		Settings.debug.flush();
	}
	public static boolean isDebug() {
		return Settings.debug.getBoolean("debug");
	}
	
	public static int getLogLevel() {
		return Settings.debug.getInteger("log_level");
	}	
	
	public static boolean getConstBool(final String key) {
		return Settings.consts.getBoolean(key);
	}

	public static int getConstInt(final String key) {
		return Settings.consts.getInteger(key);
	}
}
