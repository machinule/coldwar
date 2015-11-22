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

public class ConnectScreen extends AbstractScreen {

	public ConnectScreen(final ColdWarGame game) {
		super(game);
	}

	@Override
	public void show() {
		super.show();

		final TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("textures/uiskin.atlas"));
		final Skin skin = new Skin(Gdx.files.internal("textures/uiskin.json"), atlas);
		final Table table = new Table(skin);
		table.setFillParent(true);
		table.setDebug(true);
		this.stage.addActor(table);
		final Label hostLabel = new Label("Host:", skin);
		table.add(hostLabel);
		final TextField hostField = new TextField("localhost", skin);
		table.add(hostField);
		final Label portLabel = new Label("Port:", skin);
		table.add(portLabel);
		final TextField portField = new TextField("7588", skin);
		table.add(portField);
		final TextButton connectGameButton = new TextButton("Connect", skin);
		connectGameButton.addListener(new ChangeListener() {
			@Override
			public void changed(final ChangeEvent event, final Actor actor) {
				Logger.Dbg("\"Connect a Game\" button pressed.");
				ConnectScreen.this.game.connect(hostField.getText(), Integer.parseInt(portField.getText()));
				ConnectScreen.this.game.setScreen(new MapScreen(ConnectScreen.this.game));
			}
		});
		table.add(connectGameButton).size(300, 60).uniform().spaceBottom(10);
		table.row();
	}
}