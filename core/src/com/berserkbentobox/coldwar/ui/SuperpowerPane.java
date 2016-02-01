package com.berserkbentobox.coldwar.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import com.berserkbentobox.coldwar.logic.Client;
import com.berserkbentobox.coldwar.logic.Client.Player;
import com.berserkbentobox.coldwar.logic.MoveBuilder;

public class SuperpowerPane extends Table {
	
	private final Client client;
	private final AbstractScreen screen;
	protected MoveBuilder moveBuilder;
	protected Skin skin;
	
	protected String playerLabelText;
	
	public SuperpowerPane(final AbstractScreen screen, final Client client, final Skin skin) {
		super();
		this.client = client;
		this.skin = skin;
		this.screen = screen;
		playerLabelText = "National Actions";
	}
	
	public void show() {
		DynamicLabel playerLabel = new DynamicLabel(this.client, c -> playerLabelText, skin);
		
		final TextButton techButton = new TextButton("Tech", skin);
		techButton.addListener(new ChangeListener() {
			@Override
			public void changed(final ChangeEvent event, final Actor actor) {
				screen.game.setScreen(new TechScreen(screen.game, client));
			}
		});
		final TextButton treatyButton = new TextButton("Treaties", skin);
		treatyButton.addListener(new ChangeListener() {
			@Override
			public void changed(final ChangeEvent event, final Actor actor) {
				screen.game.setScreen(new TreatyScreen(screen.game, client));
			}
		});
		final TextButton leaderButton = new TextButton("Leadership", skin);
		leaderButton.addListener(new ChangeListener() {
			@Override
			public void changed(final ChangeEvent event, final Actor actor) {
				screen.game.setScreen(new LeaderScreen(screen.game, client));
			}
		});

		this.clearChildren();
		this.add(playerLabel);
		this.row();
		this.add(techButton);
		this.row();
		this.add(treatyButton);
		this.row();
		this.add(leaderButton);
		this.row();
	}
}
