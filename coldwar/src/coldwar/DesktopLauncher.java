package coldwar;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class DesktopLauncher {

	public static void main(final String[] args) {
		new LwjglApplication(new ColdWarGame());
	}
}