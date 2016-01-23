package com.berserkbentobox.coldwar.ui;

import java.util.EnumMap;
import java.util.HashMap;

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
import com.berserkbentobox.coldwar.Technology.TechnologySettings;
import com.berserkbentobox.coldwar.Technology.TechnologyGroupSettings;
import com.berserkbentobox.coldwar.logic.Client;

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

		final HashMap<String, TechCard> cards = new HashMap<String, TechCard>();
		for (final TechnologyGroupSettings g : this.client.initialGameState.getSettings().getTechnologySettings().getTechnologyGroupList()) {
			for (final TechnologySettings t : g.getTechnologyList())
				cards.put(t.getId(), new TechCard(this.client, t.getId(), skin));
		}
		
		final String[][] spaceTechPosition = {
				{"BASIC_ROCKETRY"},     			  
				{"ARTIFICIAL_SATELLITES"},     			  
				{"ANIMAL_IN_SPACE"},     			  
				{"MANNED_SPACEFLIGHT"}
			};
		
		final String[][] milTechPosition = {   			  
			{"HYDROGEN_BOMB", null}
		};
		
		String[][] activeTechPosition;
		switch (activeTechGroup) {
			case "ARMY":
				activeTechPosition = milTechPosition;
				break;
			case "SPACE":
				activeTechPosition = spaceTechPosition;
				break;
			default:
				activeTechPosition = milTechPosition;
				break;
		}
		
		for (int r=0; r<activeTechPosition.length; r++) {
			for (int c=0; c<activeTechPosition[r].length; c++) {
				TechCard tc = cards.getOrDefault(activeTechPosition[r][c], null);
				if (tc != null) {
					tc.setDebug(Settings.isDebug());
					techs.add(tc);
				} else {
					techs.add();
				}
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
		final TextButton militaryButton = new TextButton("Military", skin);
		militaryButton.addListener(new ChangeListener() {
			@Override
			public void changed(final ChangeEvent event, final Actor actor) {
				Logger.Dbg("\"Military\" tech button pressed.");
				TechScreen.this.activeTechGroup = "ARMY";
				TechScreen.this.stage.clear();
				TechScreen.this.show();
			}
		});
		final TextButton spaceButton = new TextButton("Space", skin);
		spaceButton.addListener(new ChangeListener() {
			@Override
			public void changed(final ChangeEvent event, final Actor actor) {
				Logger.Dbg("\"Space\" tech button pressed.");
				TechScreen.this.activeTechGroup = "SPACE";
				TechScreen.this.stage.clear();
				TechScreen.this.show();
			}
		});
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
		
		// Bottom Pane
		
		bottomBar.add(spaceButton)
			.size(300, 60)
			.bottom();
		bottomBar.add(militaryButton)
			.size(300, 60)
			.bottom();
		bottomBar.add(backButton)
			.size(300, 60)
			.bottom();
		bottomBar.row();
	}
}
