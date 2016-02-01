package com.berserkbentobox.coldwar.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import com.berserkbentobox.coldwar.Settings;
import com.berserkbentobox.coldwar.logic.Client;
import com.berserkbentobox.coldwar.logic.mechanics.superpower.UsaLeader;
public class LeaderCard extends Table {
	
	protected Client client;
	protected Skin skin;
	protected Button infoBox;
	
	protected String id;
	protected UsaLeader.Settings settings;
	
	public LeaderCard(final Client client,
					  final UsaLeader.Settings t,
					  final Skin skin) {
		super();
		this.client = client;
		this.skin = skin;
		this.settings = t;
		
		infoBox = createLayout();
		this.add(infoBox).size(300, 50);
	}
	
	protected Button createLayout() {
		Button ret = new Button(this.skin);
		ret.setDebug(Settings.isDebug());
		DynamicLabel name = new DynamicLabel(client,
				c -> this.settings.getSettings().getName(),
				skin);
		DynamicLabel age = new DynamicLabel(client,
				c -> (c.getMoveBuilder().getYear() - this.settings.getSettings().getBirthYear()) + "",
				skin);
		
		name.setAlignment(1); //Center in cell
		name.setFontScale((float)1);
		age.setFontScale((float)1);
		
		ret.add(name).center();
		ret.add(age).right().padLeft(15);
		ret.row();
		ret.add();
		
		return ret;
	}
	
	@Override
	public void setColor(Color color) {
		infoBox.setColor(color);
	}
}
