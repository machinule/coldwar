package com.berserkbentobox.coldwar.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.berserkbentobox.coldwar.Logger;
import com.berserkbentobox.coldwar.logic.Client;

public class TroikaTable extends Table {

	protected Client client;
	protected Function<Client, List<String>> troikaFn;
	protected Function<Client, Integer> yearFn;
	protected int year;
	protected List<TroikaLeaderCard> troikaCards;
	protected Skin skin;
	protected Table troika;
	
	public TroikaTable(Client client, Skin skin) {
		super();
		this.client = client;
		this.skin = skin;
		troikaFn = c -> c.getMoveBuilder().getMechanics().getSuperpower().getUssr().getTroika();
		yearFn = c -> c.getMoveBuilder().getYear();this.skin = skin;
		troikaCards = new ArrayList<TroikaLeaderCard>();
		troika = new Table();
		
		String l = client.getMoveBuilder().getMechanics().getSuperpower().getUssr().getState().getHos();
		for(int i = 0; i < 3; i++) { // TODO: Setting
			TroikaLeaderCard card = new TroikaLeaderCard(this.client, c -> c.getMoveBuilder().getMechanics().getSuperpower().getUssr().getLeader(l).getSettings(), this.skin);
			troikaCards.add(card);
			troika.add(card);
			troika.row();
		}
		
		this.add(troika);
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		this.update();
	}
	
	public void update() {
		int currentYear = yearFn.apply(client);
		if (currentYear != this.year) {
			refreshTroika();
			this.year = currentYear;
		}
	}
	
	public void refreshTroika() {
		Collection<String> t = troikaFn.apply(this.client);
		if(t != null) {
			int count = 0;
			for(String l : t) {
				troikaCards.get(count).changeLeader(c -> c.getMoveBuilder().getMechanics().getSuperpower().getUssr().getLeader(l).getSettings());
				count++;
			}
		}
	}
}
