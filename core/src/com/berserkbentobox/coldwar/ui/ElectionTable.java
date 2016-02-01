package com.berserkbentobox.coldwar.ui;

import java.util.Collection;
import java.util.function.Function;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.berserkbentobox.coldwar.Superpower.UsaLeaderParty;
import com.berserkbentobox.coldwar.logic.Client;

public class ElectionTable extends Table {
	
	protected Client client;
	protected Table democrats;
	protected Table republicans;
	protected Function<Client, Collection<String>> candidateFn;
	protected Function<Client, Integer> yearFn;
	
	public ElectionTable(final Client client, final Skin skin) {
		super(skin);
		this.client = client;
		democrats = new Table();
		republicans = new Table();
		yearFn = c -> c.getMoveBuilder().getYear();
		candidateFn = c -> c.getMoveBuilder().getMechanics().getSuperpower().getUsa().getPresidentialEligible(yearFn.apply(c));
		this.update();
	}
	
	@Override
	public void act(final float delta) {
		this.clear();
		this.update();
	}

	void update() {
		democrats.clear();
		republicans.clear();
		Collection<String> candidates = candidateFn.apply(this.client);
		for (String l : candidates) {
			CandidateCard c = new CandidateCard(this.client, client.getMoveBuilder().getMechanics().getSuperpower().getUsa().getLeader(l).getSettings(), this.getSkin());
			if(client.getMoveBuilder().getMechanics().getSuperpower().getUsa().getLeader(l).getSettings().getSettings().getParty() == UsaLeaderParty.REPUBLICAN) {
				republicans.add(c);
				republicans.row();
			} else {
				democrats.add(c);
				democrats.row();
			}
		}
		this.add(republicans);
		this.add(democrats);
	}
}
