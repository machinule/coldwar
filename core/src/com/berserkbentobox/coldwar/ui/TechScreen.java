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
import com.berserkbentobox.coldwar.Technology.TechnologySettings;
import com.berserkbentobox.coldwar.Technology.TechnologyGroupSettings;
import com.berserkbentobox.coldwar.logic.Client;
import com.berserkbentobox.coldwar.logic.mechanics.technology.Technology;
import com.berserkbentobox.coldwar.logic.mechanics.technology.TechnologyGroup;
import com.berserkbentobox.coldwar.logic.mechanics.technology.TechnologyMechanic;

public class TechScreen extends AbstractScreen {

	private Client client;
	private String activeTechGroup = "ARMY";
	
	public TechScreen(final ColdWarGame game, Client client) {
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
		
		final Table techs = new Table(skin);
		techs.setFillParent(true);
		techs.setDebug(Settings.isDebug());

		for (final TechnologyGroup.Settings g : this.client.getSettings().getTechnology().getTechnologyGroupSettings()) {
			techs.add(new Label(g.getSettings().getLabel(), skin));
			final TextButton researchButton = new TextButton("Research", skin);
			researchButton.addListener(new ChangeListener() {
				@Override
				public void changed(final ChangeEvent event, final Actor actor) {
					TechScreen.this.client.getMoveBuilder().addResearchMove(g.getSettings().getId());
				}
			});
			techs.add(researchButton);

			for (final Technology.Settings t : g.getTechnologySettings()) {
				techs.add(new TechCard(this.client, t, skin));
			}
			techs.row();
		}
		this.stage.addActor(techs);
		
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
				TechScreen.this.game.setScreen(new MapScreen(TechScreen.this.game, client));
			}
		});
		
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
