package com.berserkbentobox.coldwar.ui;

import java.util.function.Function;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.berserkbentobox.coldwar.logic.Client;
import com.berserkbentobox.coldwar.logic.mechanics.superpower.UssrLeader;

public class TroikaLeaderCard extends UssrLeaderCard {
		
	public TroikaLeaderCard(final Client client,
		      				final Function<Client, UssrLeader.Settings> settingsFn,
		      				final Skin skin) {
		super(client, settingsFn, skin);
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		this.update();
	}
	
	public void update() {
		if(client.getMoveBuilder().getMechanics().getSuperpower().getUssr().getLeader(this.settingsFn.apply(client).getSettings().getName()).getState().getPartySupport() <= 0) {
			this.setVisible(false);
			this.setLayoutEnabled(false);
		}
	}
}
