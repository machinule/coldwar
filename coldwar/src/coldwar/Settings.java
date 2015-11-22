package coldwar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Settings {
	static Preferences prefs = Gdx.app.getPreferences("Preferences");
	static Preferences consts = Gdx.app.getPreferences("Constants");
	
	public static void initPreferences() {
		prefs.putString("Player", "Player");
		consts.flush();
	}
	
	public static void initConstants() {
		consts.putBoolean("Is this working?", true);
		consts.flush();
	}
}
