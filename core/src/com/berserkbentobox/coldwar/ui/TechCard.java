package com.berserkbentobox.coldwar.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import com.berserkbentobox.coldwar.Settings;
import com.berserkbentobox.coldwar.Technology.TechnologySettings;
import com.berserkbentobox.coldwar.logic.Client;

public class TechCard extends Table {
	
	protected Client client;
	protected Skin skin;
	protected Color color;
	protected Button infoBox;
	
	protected String id;
	protected TechnologySettings settings;
	
	public TechCard(final Client client,
					final TechnologySettings t,
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
		DynamicLabel progressions = new DynamicLabel(client,
				c -> "" + this.settings.getNumProgressions(),
				skin);
		DynamicLabel progress = new DynamicLabel(client,
				c -> "" + 0,
				skin);
		DynamicLabel name = new DynamicLabel(client,
				c -> "" + this.settings.getLabel(),
				skin);
		
		name.setAlignment(1); //Center in cell
		name.setFontScale((float)0.75);
		progressions.setFontScale((float)1.5);
		progress.setFontScale((float)1.5);
		
		ret.add(progressions).left().padTop(5).expand();
		ret.add(name).center();
		ret.add(progress).right().padTop(5).expand();
		ret.row();
		ret.add();
		
		return ret;
	}
}
