package coldwar;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglNet;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class DesktopLauncher  {
   public static void main(String[] args) {
	  TexturePacker.Settings settings = new TexturePacker.Settings();
	  settings.maxWidth = 16384;
	  settings.maxHeight = 8192;
	  TexturePacker.process(settings, "assets", "textures", "pack");
      LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
      config.title = "Cold War";
      config.width = 800;
      config.height = 480;
      Logger.Start();
      Logger.SetLevel("verbose"); //none, info, debug, verbose
      new LwjglApplication(new ColdWarGame(new Peer(new LwjglNet())), config);
      
      Settings.initConstants();
      Settings.initPreferences();
      Logger.Info("Settings loaded: " + Settings.consts.getString("Successful Initialization"));
   }
}