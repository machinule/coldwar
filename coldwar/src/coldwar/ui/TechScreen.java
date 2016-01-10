package coldwar.ui;

import java.util.EnumMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import coldwar.ColdWarGame;
import coldwar.Logger;
import coldwar.Settings;
import coldwar.TechOuterClass.Tech;
import coldwar.TechOuterClass.TechGroup;
import coldwar.TechOuterClass.TechSettings;
import coldwar.logic.Client;

public class TechScreen extends AbstractScreen {

	private Client client;
	private TechGroup.Id activeTechGroup = TechGroup.Id.MILITARY;
	
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

		final EnumMap<Tech.Id, TechCard> cards = new EnumMap<Tech.Id, TechCard>(Tech.Id.class);
		for (final TechGroup g : this.client.initialGameState.getSettings().getTechsList()) {
			for (final TechSettings t : g.getTechSettingsList())
				cards.put(t.getId(), new TechCard(this.client, t.getId(), skin));
		}
		
		final Tech.Id[][] spaceTechPosition = {
				{Tech.Id.BASIC_ROCKETRY},     			  
				{Tech.Id.ARTIFICIAL_SATELLITES},     			  
				{Tech.Id.ANIMAL_IN_SPACE},     			  
				{Tech.Id.MANNED_SPACEFLIGHT}
			};
		
		final Tech.Id[][] milTechPosition = {
			{Tech.Id.ATOM_BOMB,     null},     			  
			{Tech.Id.HYDROGEN_BOMB, null}
		};
		
		Tech.Id[][] activeTechPosition;
		switch (activeTechGroup) {
			case MILITARY:
				activeTechPosition = milTechPosition;
				break;
			case SPACE:
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
				TechScreen.this.activeTechGroup = TechGroup.Id.MILITARY;
				TechScreen.this.stage.clear();
				TechScreen.this.show();
			}
		});
		final TextButton spaceButton = new TextButton("Space", skin);
		spaceButton.addListener(new ChangeListener() {
			@Override
			public void changed(final ChangeEvent event, final Actor actor) {
				Logger.Dbg("\"Space\" tech button pressed.");
				TechScreen.this.activeTechGroup = TechGroup.Id.SPACE;
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
