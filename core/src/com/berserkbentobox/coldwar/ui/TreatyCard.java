package com.berserkbentobox.coldwar.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import com.berserkbentobox.coldwar.Settings;
import com.berserkbentobox.coldwar.Technology.TechnologySettings;
import com.berserkbentobox.coldwar.logic.Client;
import com.berserkbentobox.coldwar.logic.mechanics.technology.Technology;
import com.berserkbentobox.coldwar.logic.mechanics.treaty.Treaty;

public class TreatyCard extends Table {
	
	protected Client client;
	protected Skin skin;
	protected Color color;
	protected Button infoBox;
	
	protected String id;
	protected Treaty.Settings settings;
	
	public TreatyCard(final Client client,
					final Treaty.Settings t,
					final Skin skin) {
		super();
		this.client = client;
		this.skin = skin;
		this.settings = t;
		
		infoBox = createLayout();
		this.add(infoBox).size(200, 50);
	}
	
	protected Button createLayout() {
		Button ret = new Button(this.skin);
		ret.setDebug(Settings.isDebug());
		DynamicLabel name = new DynamicLabel(client,
				c -> this.settings.getSettings().getId(),
				skin);
		DynamicLabel signed = new DynamicLabel(client,
				c -> this.client.getMoveBuilder().getMechanics().getTreaty().getTreaty(this.settings.getSettings().getId()).isSigned() ? "Signed" : "Not Signed",
				skin);
		
		name.setAlignment(1); //Center in cell
		name.setFontScale((float)0.75);
		signed.setFontScale((float)1.5);
		signed.setFontScale((float)1.5);
		
		ret.add(name).center();
		ret.add(signed).right().padTop(5).expand();
		ret.row();
		ret.add();
		
		return ret;
	}
}
