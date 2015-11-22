package coldwar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Settings {
	static Preferences consts = Gdx.app.getPreferences("Constants");
	static Preferences prefs = Gdx.app.getPreferences("Preferences");

	public static void initConstants() {
		Settings.consts.putBoolean("Is this working?", true);
		Settings.consts.flush();
	}

	public static void initPreferences() {
		Settings.prefs.putString("Player", "Player");
		Settings.consts.flush();
	}
}
