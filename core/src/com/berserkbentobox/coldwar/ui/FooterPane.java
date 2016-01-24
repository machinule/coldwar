package com.berserkbentobox.coldwar.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import com.berserkbentobox.coldwar.Province.ProvinceSettings;
import com.berserkbentobox.coldwar.logic.Client;

public abstract class FooterPane extends Table {

	protected final Client client;
	protected Skin skin;
	DynamicButton selected;
	boolean requiresSlider;	
    
	public FooterPane(final Client client, final Skin skin) {
		super();
		this.client = client;
		this.skin = skin;
		this.setDebug(true);
	}
	
	abstract public void onSelect(ProvinceSettings province);
}