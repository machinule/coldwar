package com.berserkbentobox.coldwar.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import com.berserkbentobox.coldwar.ColdWarGame;
import com.berserkbentobox.coldwar.Logger;
import com.berserkbentobox.coldwar.Settings;
import com.berserkbentobox.coldwar.logic.Client;
import com.berserkbentobox.coldwar.logic.HotseatClient;

public class MainMenuScreen extends AbstractScreen {

	public MainMenuScreen(final ColdWarGame game) {
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

		table.add(this.game.getFlavor().get("INTERFACE", "TITLE")).spaceBottom(50);
		table.row();
		
		final TextButton hotseatGameButton = new TextButton("Start a Hotseat Game", skin);
		hotseatGameButton.addListener(new ChangeListener() {
			@Override
			public void changed(final ChangeEvent event, final Actor actor) {
				Logger.Dbg("\"Host a Game\" button pressed.");
				Client client = new HotseatClient();
				MainMenuScreen.this.game.setScreen(new MapScreen(MainMenuScreen.this.game, client));
			}
		});
		table.add(hotseatGameButton).size(300, 60).uniform().spaceBottom(10);
		table.row();
		
		final TextButton hostGameButton = new TextButton("Host a Game", skin);
		hostGameButton.addListener(new ChangeListener() {
			@Override
			public void changed(final ChangeEvent event, final Actor actor) {
				Logger.Dbg("\"Host a Game\" button pressed.");
				MainMenuScreen.this.game.setScreen(new HostScreen(MainMenuScreen.this.game));
			}
		});
		table.add(hostGameButton).size(300, 60).uniform().spaceBottom(10);
		table.row();

		final TextButton connectGameButton = new TextButton("Connect to a Game", skin);
		connectGameButton.addListener(new ChangeListener() {
			@Override
			public void changed(final ChangeEvent event, final Actor actor) {
				Logger.Dbg("\"Connect to a Game\" button pressed.");
				MainMenuScreen.this.game.setScreen(new ConnectScreen(MainMenuScreen.this.game));
			}
		});
		table.add(connectGameButton).uniform().fill().spaceBottom(10);
		table.row();

		final TextButton optionsButton = new TextButton("Options", skin);
		optionsButton.addListener(new ChangeListener() {
			@Override
			public void changed(final ChangeEvent event, final Actor actor) {
				Logger.Dbg("\"Options\" button pressed.");
			}
		});
		table.add(optionsButton).uniform().fill().spaceBottom(10);
		table.row();

		final TextButton quitButton = new TextButton("Quit", skin);
		quitButton.addListener(new ChangeListener() {
			@Override
			public void changed(final ChangeEvent event, final Actor actor) {
				Logger.Dbg("\"Quit\" button pressed.");
				Gdx.app.exit();
			}
		});
		table.add(quitButton).uniform().fill();
	}

}
