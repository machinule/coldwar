package com.berserkbentobox.coldwar.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.berserkbentobox.coldwar.Logger;
import com.berserkbentobox.coldwar.Superpower.UsaLeaderParty;
import com.berserkbentobox.coldwar.logic.Client;

public class CandidateTable extends Table {
	
	protected Client client;
	protected Table democrats;
	protected Table republicans;
	protected Function<Client, Collection<String>> repCandidateFn;
	protected Function<Client, Collection<String>> demCandidateFn;
	protected Function<Client, Integer> yearFn;
	protected List<CandidateCard> repCards;
	protected List<CandidateCard> demCards;
	protected int year;
	
	public CandidateTable(final Client client, final Skin skin) {
		super(skin);
		this.client = client;
		democrats = new Table();
		republicans = new Table();
		yearFn = c -> c.getMoveBuilder().getYear();
		int num = 3; // TODO: num to settings
		repCandidateFn = c -> c.getMoveBuilder().getMechanics().getSuperpower().getUsa().getPresidentialEligible(yearFn.apply(c), UsaLeaderParty.REPUBLICAN, num, c.getMoveBuilder().getMechanics().getPseudorandom());
		demCandidateFn = c -> c.getMoveBuilder().getMechanics().getSuperpower().getUsa().getPresidentialEligible(yearFn.apply(c), UsaLeaderParty.DEMOCRAT, num, c.getMoveBuilder().getMechanics().getPseudorandom());
		repCards = new ArrayList<CandidateCard>();
		demCards = new ArrayList<CandidateCard>();
		
		Collection<String> repCandidates = repCandidateFn.apply(this.client);
		for (String l : repCandidates) {
			CandidateCard card = new CandidateCard(this.client, c -> c.getMoveBuilder().getMechanics().getSuperpower().getUsa().getLeader(l).getSettings(), this.getSkin());
			repCards.add(card);
			republicans.add(card);
			republicans.row();
		}
		Collection<String> demCandidates = demCandidateFn.apply(this.client);
		for (String l : demCandidates) {
			CandidateCard card = new CandidateCard(this.client, c -> c.getMoveBuilder().getMechanics().getSuperpower().getUsa().getLeader(l).getSettings(), this.getSkin());
			demCards.add(card);
			democrats.add(card);
			democrats.row();
		}
		
		this.add(democrats);
		this.add(republicans);
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		this.update();
	}
	
	public void update() {
		int currentYear = yearFn.apply(client);
		if (currentYear != this.year) {
			refreshCandidates();
			this.year = currentYear;
		}
	}
	
	public void refreshCandidates() {
		Collection<String> repCandidates = repCandidateFn.apply(this.client);
		Collection<String> demCandidates = demCandidateFn.apply(this.client);
		for (CandidateCard c : demCards) {
			c.setVisible(false);
			c.setLayoutEnabled(false);
		}
		for (CandidateCard c : repCards) {
			c.setVisible(false);
			c.setLayoutEnabled(false);
		}
		int index = 0;
		for (String l : demCandidates) {
			demCards.get(index).changeLeader(c -> c.getMoveBuilder().getMechanics().getSuperpower().getSettings().getUsaSettings().getLeaderSettings(l));
			demCards.get(index).setVisible(true);
			demCards.get(index).setLayoutEnabled(true);
			demCards.get(index).nameFn = c -> l;
			CandidateCard.deselect();
			index++;
		}
		index = 0;
		for (String l : repCandidates) {
			repCards.get(index).changeLeader(c -> c.getMoveBuilder().getMechanics().getSuperpower().getSettings().getUsaSettings().getLeaderSettings(l));
			repCards.get(index).setVisible(true);
			repCards.get(index).setLayoutEnabled(true);
			repCards.get(index).nameFn = c -> l;
			CandidateCard.deselect();
			index++;
		}
	}
}
