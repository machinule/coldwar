package com.berserkbentobox.coldwar;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * Wrapper around libGDX Preferences to make them easy to access.
 * @see com.badlogic.gx.Preferences
 */
public class Settings {
	static Preferences debug = Gdx.app.getPreferences("debug");
	static Preferences prefs = Gdx.app.getPreferences("preferences");

	public static void init() {
		Settings.initDebug();
		Settings.initPreferences();
	}
	
	public static void initPreferences() {
		Settings.prefs.putInteger("resolution_width", 1680);
		Settings.prefs.putInteger("resolution_height", 960);
		Settings.prefs.putBoolean("fullscreen", false);
		Settings.prefs.putInteger("splash_height", 480);
		Settings.prefs.putInteger("splash_width", 551);		

		Settings.prefs.flush();
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
}
