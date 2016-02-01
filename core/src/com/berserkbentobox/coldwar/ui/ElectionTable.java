package com.berserkbentobox.coldwar.ui;

import java.util.Collection;
import java.util.function.Function;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.berserkbentobox.coldwar.Superpower.UsaLeaderParty;
import com.berserkbentobox.coldwar.logic.Client;
import com.berserkbentobox.coldwar.logic.mechanics.superpower.UsaLeader;

public class ElectionTable extends Table {
	
	protected Client client;
	protected Table democrats;
	protected Table republicans;
	protected Function<Client, Collection<UsaLeader>> candidateFn;
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
		Collection<UsaLeader> candidates = candidateFn.apply(this.client);
		for (UsaLeader l : candidates) {
			CandidateCard c = new CandidateCard(this.client, l.getSettings(), this.getSkin());
			if(l.getSettings().getSettings().getParty() == UsaLeaderParty.REPUBLICAN) {
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
