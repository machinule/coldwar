package com.berserkbentobox.coldwar.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import com.berserkbentobox.coldwar.Logger;
import com.berserkbentobox.coldwar.Technology.TechnologyId;
import com.berserkbentobox.coldwar.logic.Client;

public class TechCard extends Table {
	
	protected Client client;
	protected Skin skin;
	protected Color color;
	protected Button infoBox;
	
	protected TechnologyId id;
//	protected TechSettings techSettings;
	
	public TechCard(final Client client,
					final TechnologyId id,
					final Skin skin) {
		super();
		this.client = client;
		this.skin = skin;
		this.id = id;
//		this.techSettings = client.getMoveBuilder().getComputedGameState().getTechSettings(id);
		
		infoBox = createLayout();
		this.add(infoBox).size(200, 50);
	}
	
	protected Button createLayout() {
		Button ret = new Button(this.skin);
		//ret.setDebug(Settings.isDebug());
//		Logger.Dbg("Looking for " + id + ": " + client.getMoveBuilder().getComputedGameState().getTechSettings(id));
//		DynamicLabel progressions = new DynamicLabel(client,
//				c -> "" + techSettings.getProgressions(),
//				skin);
//		DynamicLabel progress = new DynamicLabel(client,
//				c -> "" + c.getMoveBuilder().getComputedGameState().getTech(client.getPlayer(), id).getProgress(),
//				skin);
//		DynamicLabel name = new DynamicLabel(client,
//				c -> "" + techSettings.getLabel(),
//				skin);
//		
//		if(client.getMoveBuilder().getComputedGameState().isTechAvailable(client.getPlayer(), id)) {
//			ret.setColor(Color.LIGHT_GRAY);
//		} else if(!client.getMoveBuilder().getComputedGameState().isTechCompleted(client.getPlayer(), id)) {
//			ret.setColor(Color.DARK_GRAY);
//		}
		
//		name.setAlignment(1); //Center in cell
//		name.setFontScale((float)0.75);
//		progressions.setFontScale((float)1.5);
//		progress.setFontScale((float)1.5);
		
//		ret.add(progressions).left().padTop(5).expand();
//		ret.add(name).center();
//		ret.add(progress).right().padTop(5).expand();
		ret.row();
		ret.add();
		
		return ret;
	}
}
