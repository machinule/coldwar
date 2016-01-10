package coldwar.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import coldwar.ColdWarGame;
import coldwar.Logger;
import coldwar.Settings;
import coldwar.logic.Client;
import coldwar.logic.RemoteClient;

public class HostScreen extends AbstractScreen {

	public HostScreen(final ColdWarGame game) {
		super(game);
	}

	@Override
	public void show() {
		super.show();

		//final TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("textures/uiskin.atlas"));
		final TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("uiskin.atlas")); // When creating JAR
		//final Skin skin = new Skin(Gdx.files.internal("textures/uiskin.json"), atlas);
		final Skin skin = new Skin(Gdx.files.internal("uiskin.json"), atlas); // When creating JAR
		final Table table = new Table(skin);
		table.setFillParent(true);
		table.setDebug(Settings.isDebug());
		this.stage.addActor(table);
		final Label portLabel = new Label("Port:", skin);
		table.add(portLabel);
		final TextField portField = new TextField("7588", skin);
		table.add(portField);
		final TextButton hostGameButton = new TextButton("Host", skin);
		hostGameButton.addListener(new ChangeListener() {
			@Override
			public void changed(final ChangeEvent event, final Actor actor) {
				Logger.Dbg("\"Host a Game\" button pressed.");
				Client client = new RemoteClient(Integer.parseInt(portField.getText()));
				HostScreen.this.game.setScreen(new MapScreen(HostScreen.this.game, client));
			}
		});
		table.add(hostGameButton).size(300, 60).uniform().spaceBottom(10);
		table.row();
	}
}
