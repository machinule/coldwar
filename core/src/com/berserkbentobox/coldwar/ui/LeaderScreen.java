package com.berserkbentobox.coldwar.ui;

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
import com.berserkbentobox.coldwar.logic.Client.Player;
import com.berserkbentobox.coldwar.logic.mechanics.treaty.Treaty;

public class LeaderScreen extends AbstractScreen {

	private Client client;
	
	public LeaderScreen(final ColdWarGame game, Client client) {
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
		
		// Usa
		
		final Table usaWrapper = new Table(skin);
		usaWrapper.setFillParent(true);
		usaWrapper.setDebug(Settings.isDebug());
		
		final Table usaLeaderTable = new Table(skin);
		usaLeaderTable.setDebug(Settings.isDebug());

		Label president_label = new Label("President:", skin);
		usaLeaderTable.add(president_label)
			.minSize(200, 0)
			.center()
			.expand();
		Label vice_president_label = new Label("Vice President:", skin);
		usaLeaderTable.add(vice_president_label)
			.minSize(200, 0)
			.center()
			.expand();
		usaLeaderTable.row();
		UsaLeaderCard president_name = new UsaLeaderCard(this.client, c -> c.getMoveBuilder().getMechanics().getSuperpower().getUsa().getLeader(c.getMoveBuilder().getMechanics().getSuperpower().getUsa().getState().getPresident()).getSettings(), skin);
		usaLeaderTable.add(president_name)
			.minSize(200, 0)
			.center();
		UsaLeaderCard vice_president_name = new UsaLeaderCard(this.client, c -> c.getMoveBuilder().getMechanics().getSuperpower().getUsa().getLeader(c.getMoveBuilder().getMechanics().getSuperpower().getUsa().getState().getVicePresident()).getSettings(), skin);
		usaLeaderTable.add(vice_president_name)
			.minSize(200, 0)
			.center();
		usaLeaderTable.row();

		Table electionTime = new Table();
		electionTime.setDebug(Settings.isDebug());
		
		DynamicLabel timeUntilElection = new DynamicLabel(this.client, c -> "Time until next election: " + Integer.toString(3 - ((c.getMoveBuilder().getYear()) % 4)), skin);
		electionTime.add(timeUntilElection);
		electionTime.row();
		
		Table electionTable = new Table();
		electionTable.setDebug(Settings.isDebug());
		
		DynamicLabel election_label = new DynamicLabel(this.client, c -> "Elections! Do your goddamn democratic fucking duty, scum.", skin);
		electionTable.add(election_label)
			.center();
		electionTable.row();
		CandidateTable candidateTable = new CandidateTable(this.client, skin);
		electionTable.add(candidateTable);
		
		BinaryTable binaryElectionTable = new BinaryTable(this.client, c -> (c.getMoveBuilder().getYear() + 1) % 4 != 0, electionTime, electionTable, skin);
		
		usaWrapper.add(usaLeaderTable);
		usaWrapper.row();
		usaWrapper.add(binaryElectionTable);
		usaWrapper.row();
		
		// Ussr
		
		Table ussrLeaderTable = new Table(skin);
		ussrLeaderTable.setDebug(Settings.isDebug());
		
		Label hosLabel = new Label("Head of State: ", skin);
		UssrLeaderCard hosCard = new UssrLeaderCard(this.client, c -> c.getMoveBuilder().getMechanics().getSuperpower().getUssr().getLeader(c.getMoveBuilder().getMechanics().getSuperpower().getUssr().getState().getHos()).getSettings(), skin);
		ussrLeaderTable.add(hosLabel);
		ussrLeaderTable.row();
		ussrLeaderTable.add(hosCard)
			.minSize(200, 0)
			.center();
		
		TroikaTable troikaTable = new TroikaTable(client, skin);
		troikaTable.setDebug(Settings.isDebug());
		
		final BinaryTable ussrWrapper = new BinaryTable(this.client, c -> c.getMoveBuilder().getMechanics().getSuperpower().getUssr().hasTroika(), troikaTable, ussrLeaderTable, skin);
		ussrWrapper.setFillParent(true);
		ussrWrapper.setDebug(Settings.isDebug());
		
		final BinaryTable screenWrapper = new BinaryTable(client, c -> c.getPlayer() == Player.USSR, ussrWrapper, usaWrapper, skin);
		screenWrapper.setFillParent(true);
		//screenWrapper.setDebug(Settings.isDebug());
		screenWrapper.bottom().left();
		
		this.stage.addActor(screenWrapper);
		//this.stage.addActor(usaWrapper);
		
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
				LeaderScreen.this.game.setScreen(new MapScreen(LeaderScreen.this.game, client));
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
