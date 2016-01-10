package coldwar.ui;

import java.util.EnumMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import coldwar.ColdWarGame;
import coldwar.GameSettingsOuterClass.ProvinceSettings;
import coldwar.Settings;
import coldwar.ProvinceOuterClass.Province;
import coldwar.logic.Client;

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
		final EnumMap<Province.Id, ProvinceInfoCard> cards = new EnumMap<Province.Id, ProvinceInfoCard>(Province.Id.class);
		for (final ProvinceSettings p : this.client.initialGameState.getSettings().getProvincesList()) {
			if(p.getRegion() != Province.Region.SUPERPOWERS) {
				cards.put(p.getId(), new ProvinceInfoCard(this.client, p, actionPane, warPane, skin));
			}
		}
		final Province.Id[][] provincePosition = {
				{null,null,null,null,null,Province.Id.BENELUX,Province.Id.DENMARK,Province.Id.EAST_GERMANY,Province.Id.POLAND,null,null,null,Province.Id.NORTH_KOREA,null},
				{Province.Id.MEXICO,Province.Id.CUBA,Province.Id.HAITI,null,null,Province.Id.FRANCE,Province.Id.WEST_GERMANY,Province.Id.CZECHOSLOVAKIA,Province.Id.HUNGARY,null,Province.Id.AFGHANISTAN,null,Province.Id.SOUTH_KOREA,Province.Id.JAPAN},
				{Province.Id.GUATEMALA,Province.Id.HONDURAS,Province.Id.NICARAGUA,Province.Id.DOMINICAN_REP,Province.Id.PORTUGAL,Province.Id.SPAIN,Province.Id.ITALY,Province.Id.YUGOSLAVIA,Province.Id.ROMANIA,Province.Id.IRAN,Province.Id.PAKISTAN,Province.Id.BANGLADESH,Province.Id.CHINA,Province.Id.TAIWAN},
				{Province.Id.EL_SALVADOR,Province.Id.COSTA_RICA,Province.Id.PANAMA,Province.Id.LESS_ANTILLES,null,null,Province.Id.ALGERIA,Province.Id.BULGARIA,Province.Id.TURKEY,Province.Id.SYRIA,Province.Id.IRAQ,Province.Id.INDIA,Province.Id.VIETNAM,Province.Id.PHILIPPINES},
				{null,null,Province.Id.COLOMBIA,Province.Id.VENEZUELA,null,Province.Id.MOROCCO,Province.Id.WEST_AFRICA,Province.Id.GREECE,Province.Id.ISRAEL,Province.Id.SAUDI_ARABIA,Province.Id.GULF_STATES,Province.Id.BURMA,Province.Id.LAOS,Province.Id.INDONESIA},
				{null,Province.Id.ECUADOR,Province.Id.PERU,Province.Id.GUYANA,null,Province.Id.IVORY_GOLD_COAST,Province.Id.ZAIRE,Province.Id.LIBYA,Province.Id.EGYPT,Province.Id.YEMEN,null,Province.Id.THAILAND,Province.Id.CAMBODIA,null},
				{null,Province.Id.CHILE,Province.Id.BOLIVIA,Province.Id.BRAZIL,null,Province.Id.NIGERIA,Province.Id.ANGOLA,Province.Id.EAST_AFRICA,Province.Id.ETHIOPIA,null,null,null,Province.Id.MALAYSIA,Province.Id.AUSTRALIA},
				{null,null,Province.Id.ARGENTINA,Province.Id.URUGUAY,null,null,Province.Id.SOUTH_AFRICA,Province.Id.MOZAMBIQUE,null,null,null,null,null,null},		};
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
