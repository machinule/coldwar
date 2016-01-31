package com.berserkbentobox.coldwar.ui;

import java.util.EnumMap;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.berserkbentobox.coldwar.ColdWarGame;
import com.berserkbentobox.coldwar.Logger;
import com.berserkbentobox.coldwar.Settings;
import com.berserkbentobox.coldwar.logic.Client;
import com.berserkbentobox.coldwar.logic.mechanics.treaty.Treaty;

public class UsaLeaderScreen extends AbstractScreen {

	private Client client;
	
	public UsaLeaderScreen(final ColdWarGame game, Client client) {
		super(game);
		this.client = client;
	}

	@Override
	public void show() {
		super.show();

		//final TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("textures/uiskin.atlas"));
		final TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("uiskin.atlas")); // When creating JAR
		//final Skin skin = new Skin(Gdx.files.internal("textures/uiskin.json"), atlas);
		final Skin skin = new Skin(Gdx.files.internal("uiskin.json"), atlas); // When creating JAR
		
		final Table wrapper = new Table(skin);
		wrapper.setFillParent(true);
		wrapper.setDebug(Settings.isDebug());
		
		final Table leaderTable = new Table(skin);
		leaderTable.setDebug(Settings.isDebug());

		Label president_label = new Label("President:", skin);
		leaderTable.add(president_label)
			.minSize(200, 0)
			.center();
		Label vice_president_label = new Label("Vice President:", skin);
		leaderTable.add(vice_president_label)
			.minSize(200, 0)
			.center();
		leaderTable.row();
		DynamicLabel president_name = new DynamicLabel(client, c -> this.client.getMoveBuilder().getMechanics().getSuperpower().getUsa().getState().getPresident(), skin);
		leaderTable.add(president_name)
			.minSize(200, 0)
			.center();
		DynamicLabel vice_president_name = new DynamicLabel(client, c -> this.client.getMoveBuilder().getMechanics().getSuperpower().getUsa().getState().getVicePresident(), skin);
		leaderTable.add(vice_president_name)
			.minSize(200, 0)
			.center();
		leaderTable.row();
		
		//((this.client.getMoveBuilder().getYear() + 1) % 4 == 0)
		//final DynamicTable electionTable = new DynamicTable(client, c -> true , skin);
		final Table electionTable = new Table();
		electionTable.setDebug(Settings.isDebug());
		
		Label election_label = new Label("Elections! Do your goddamn democratic fucking duty, scum.", skin);
		electionTable.add(election_label)
			.center();
		electionTable.row();
		
		wrapper.add(leaderTable);
		wrapper.row();
		wrapper.add(electionTable);
		wrapper.row();
		
		this.stage.addActor(wrapper);
		
		final HeaderPane headerPane = new HeaderPane(this.client, skin);
		headerPane.setDebug(Settings.isDebug());
		final SuperpowerPane superpowerPane = new SuperpowerPane(this, this.client, skin);
		superpowerPane.setDebug(Settings.isDebug());
		
		final Table bottomBar = new Table(skin);
		bottomBar.setFillParent(true);
		bottomBar.setDebug(Settings.isDebug());
		bottomBar.bottom();
		this.stage.addActor(bottomBar);
		final TextButton backButton = new TextButton("Back", skin);
		backButton.addListener(new ChangeListener() {
			@Override
			public void changed(final ChangeEvent event, final Actor actor) {
				Logger.Dbg("\"Back\" button pressed.");
				UsaLeaderScreen.this.game.setScreen(new MapScreen(UsaLeaderScreen.this.game, client));
			}
		});
		bottomBar.add(backButton);
		
		// Top Pane
		
		headerPane.setFillParent(true);
		this.stage.addActor(headerPane);
		headerPane.top();
		headerPane.show();
		
		// Side Pane
		
		superpowerPane.setFillParent(true);
		this.stage.addActor(superpowerPane);
		superpowerPane.left().top();
		superpowerPane.show();
	}
}
