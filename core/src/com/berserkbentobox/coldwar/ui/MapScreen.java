package com.berserkbentobox.coldwar.ui;

import java.util.EnumMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import com.berserkbentobox.coldwar.ColdWarGame;
import com.berserkbentobox.coldwar.GameSettingsOuterClass.ProvinceSettings;
import com.berserkbentobox.coldwar.Settings;
import com.berserkbentobox.coldwar.Id.ProvinceId;
import com.berserkbentobox.coldwar.Province.ProvinceRegion;
import com.berserkbentobox.coldwar.logic.Client;

public class MapScreen extends AbstractScreen {

	private Client client;
	
	public MapScreen(final ColdWarGame game, Client client) {
		super(game);
		this.client = client;
	}

	//@Override
	public void act() {
	}
	
	@Override
	public void show() {
		super.show();
		//final TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("textures/uiskin.atlas"));
		final TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("uiskin.atlas")); // When creating JAR
		//final Skin skin = new Skin(Gdx.files.internal("textures/uiskin.json"), atlas);
		final Skin skin = new Skin(Gdx.files.internal("uiskin.json"), atlas); // When creating JAR
		
		// Nations
		final Table nations = new Table(skin);
		nations.setDebug(Settings.isDebug());
		final ScrollPane scrollPane = new ScrollPane(nations, skin);
		scrollPane.setFillParent(true);
		this.stage.addActor(scrollPane);
		
		final ActionPane actionPane = new ActionPane(this.client, skin);
		actionPane.setDebug(Settings.isDebug());
		final WarPane warPane = new WarPane(this.client, skin);
		warPane.setDebug(Settings.isDebug());
		
		final HeaderPane headerPane = new HeaderPane(this.client, skin);
		headerPane.setDebug(Settings.isDebug());
		final SuperpowerPane superpowerPane = new SuperpowerPane(this, this.client, skin);
		superpowerPane.setDebug(Settings.isDebug());
		final EnumMap<ProvinceId, ProvinceInfoCard> cards = new EnumMap<ProvinceId, ProvinceInfoCard>(ProvinceId.class);
		for (final ProvinceSettings p : this.client.initialGameState.getSettings().getProvinceSettings().getProvinceList()) {
			if(p.getRegion() != ProvinceRegion.SUPERPOWERS) {
				cards.put(p.getId(), new ProvinceInfoCard(this.client, p, actionPane, warPane, skin));
			}
		}
		final ProvinceId[][] provincePosition = {
				{null,null,null,null,null,ProvinceId.BENELUX,ProvinceId.DENMARK,ProvinceId.EAST_GERMANY,ProvinceId.POLAND,null,null,null,ProvinceId.NORTH_KOREA,null},
				{ProvinceId.MEXICO,ProvinceId.CUBA,ProvinceId.HAITI,null,null,ProvinceId.FRANCE,ProvinceId.WEST_GERMANY,ProvinceId.CZECHOSLOVAKIA,ProvinceId.HUNGARY,null,ProvinceId.AFGHANISTAN,null,ProvinceId.SOUTH_KOREA,ProvinceId.JAPAN},
				{ProvinceId.GUATEMALA,ProvinceId.HONDURAS,ProvinceId.NICARAGUA,ProvinceId.DOMINICAN_REP,ProvinceId.PORTUGAL,ProvinceId.SPAIN,ProvinceId.ITALY,ProvinceId.YUGOSLAVIA,ProvinceId.ROMANIA,ProvinceId.IRAN,ProvinceId.PAKISTAN,ProvinceId.BANGLADESH,ProvinceId.CHINA,ProvinceId.TAIWAN},
				{ProvinceId.EL_SALVADOR,ProvinceId.COSTA_RICA,ProvinceId.PANAMA,ProvinceId.LESS_ANTILLES,null,null,ProvinceId.ALGERIA,ProvinceId.BULGARIA,ProvinceId.TURKEY,ProvinceId.SYRIA,ProvinceId.IRAQ,ProvinceId.INDIA,ProvinceId.VIETNAM,ProvinceId.PHILIPPINES},
				{null,null,ProvinceId.COLOMBIA,ProvinceId.VENEZUELA,null,ProvinceId.MOROCCO,ProvinceId.WEST_AFRICA,ProvinceId.GREECE,ProvinceId.ISRAEL,ProvinceId.SAUDI_ARABIA,ProvinceId.GULF_STATES,ProvinceId.BURMA,ProvinceId.LAOS,ProvinceId.INDONESIA},
				{null,ProvinceId.ECUADOR,ProvinceId.PERU,ProvinceId.GUYANA,null,ProvinceId.IVORY_GOLD_COAST,ProvinceId.ZAIRE,ProvinceId.LIBYA,ProvinceId.EGYPT,ProvinceId.YEMEN,null,ProvinceId.THAILAND,ProvinceId.CAMBODIA,null},
				{null,ProvinceId.CHILE,ProvinceId.BOLIVIA,ProvinceId.BRAZIL,null,ProvinceId.NIGERIA,ProvinceId.ANGOLA,ProvinceId.EAST_AFRICA,ProvinceId.ETHIOPIA,null,null,null,ProvinceId.MALAYSIA,ProvinceId.AUSTRALIA},
				{null,null,ProvinceId.ARGENTINA,ProvinceId.URUGUAY,null,null,ProvinceId.SOUTH_AFRICA,ProvinceId.MOZAMBIQUE,null,null,null,null,null,null},		};
		for (int r=0; r<provincePosition.length; r++) {
			for (int c=0; c<provincePosition[r].length; c++) {
				ProvinceInfoCard pic = cards.getOrDefault(provincePosition[r][c], null);
				if (pic != null) {
					pic.setDebug(Settings.isDebug());
					nations.add(pic);
				} else {
					nations.add();
				}
			}
			nations.row();
		}
		
		// Top Pane
		
		headerPane.setFillParent(true);
		this.stage.addActor(headerPane);
		headerPane.top();
		headerPane.show();
		
		// Bottom Pane
		
		actionPane.setFillParent(true);
		this.stage.addActor(actionPane);
		actionPane.bottom();
		
		warPane.setFillParent(true);
		this.stage.addActor(warPane);
		warPane.bottom();
		
		// Side Pane
		
		superpowerPane.setFillParent(true);
		this.stage.addActor(superpowerPane);
		superpowerPane.left().top();
		superpowerPane.show();
		
	}
}
