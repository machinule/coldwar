package com.berserkbentobox.coldwar.ui;

import java.util.function.Function;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.berserkbentobox.coldwar.Superpower.UsaLeaderParty;
import com.berserkbentobox.coldwar.logic.Client;
import com.berserkbentobox.coldwar.logic.mechanics.superpower.UsaLeader;

public class CandidateCard extends UsaLeaderCard {

	protected static Color color;
	protected static CandidateCard rep_currentSelected;
	protected static CandidateCard dem_currentSelected;
	protected Function<Client, String> nameFn;
	
	public CandidateCard(Client client, Function<Client, UsaLeader.Settings> settingsFn, Skin skin) {
		super(client, settingsFn, skin);
		color = this.getColor();
		nameFn = c -> settingsFn.apply(c).getSettings().getName();

		infoBox.addListener(new ChangeListener() {
			@Override
			public void changed(final ChangeEvent event, final Actor actor) {
				if(settingsFn.apply(CandidateCard.this.client).getSettings().getParty() == UsaLeaderParty.REPUBLICAN) {
					if(rep_currentSelected != null) {
						rep_currentSelected.setColor(color);
					}
					CandidateCard.this.setColor(Color.RED);
					rep_currentSelected = CandidateCard.this;
				} else {
					if(dem_currentSelected != null) {
						dem_currentSelected.setColor(color);
					}
					CandidateCard.this.setColor(Color.BLUE);
					dem_currentSelected = CandidateCard.this;
				}
				CandidateCard.this.client.getMoveBuilder().addNominateMove(nameFn.apply(CandidateCard.this.client));
			}
		});
	}
	
	public void changeLeader(Function<Client, UsaLeader.Settings> settingsFn) {
		this.settingsFn = settingsFn;
	}
	
	public static void deselect() {
		if(rep_currentSelected != null)
			rep_currentSelected.setColor(color);
		if(dem_currentSelected != null)
			dem_currentSelected.setColor(color);
		dem_currentSelected = null;
		rep_currentSelected = null;
	}
}
