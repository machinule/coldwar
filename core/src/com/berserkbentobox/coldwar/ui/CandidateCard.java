package com.berserkbentobox.coldwar.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.berserkbentobox.coldwar.Superpower.UsaLeaderParty;
import com.berserkbentobox.coldwar.logic.Client;
import com.berserkbentobox.coldwar.logic.mechanics.superpower.UsaLeader.Settings;

public class CandidateCard extends LeaderCard {

	protected static Color color;
	protected static CandidateCard rep_currentSelected;
	protected static CandidateCard dem_currentSelected;
	
	public CandidateCard(Client client, Settings t, Skin skin) {
		super(client, t, skin);
		color = this.getColor();

		infoBox.addListener(new ChangeListener() {
			@Override
			public void changed(final ChangeEvent event, final Actor actor) {
				if(t.getSettings().getParty() == UsaLeaderParty.REPUBLICAN) {
					if(rep_currentSelected != null) {
						rep_currentSelected.setColor(color);
					}
					CandidateCard.this.setColor(Color.RED);
					client.getMoveBuilder().getMechanics().getSuperpower().getUsa().setCandidate(t.getSettings().getName());
					rep_currentSelected = CandidateCard.this;
				} else {
					if(dem_currentSelected != null) {
						dem_currentSelected.setColor(color);
					}
					CandidateCard.this.setColor(Color.BLUE);
					client.getMoveBuilder().getMechanics().getSuperpower().getUsa().setCandidate(t.getSettings().getName());
					dem_currentSelected = CandidateCard.this;
				}
			}
		});
	}

}
