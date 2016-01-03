package coldwar.ui;

import java.util.EnumMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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
		nations.setFillParent(true);
		nations.setDebug(Settings.isDebug());
		this.stage.addActor(nations);
		
		final ActionPane actionPane = new ActionPane(this.client, skin);
		actionPane.setDebug(Settings.isDebug());
		final WarPane warPane = new WarPane(this.client, skin);
		warPane.setDebug(Settings.isDebug());
		
		final HeaderPane headerPane = new HeaderPane(this.client, skin);
		headerPane.setDebug(Settings.isDebug());
		final SuperpowerPane superpowerPane = new SuperpowerPane(this.client, skin);
		superpowerPane.setDebug(Settings.isDebug());
		final EnumMap<Province.Id, ProvinceInfoCard> cards = new EnumMap<Province.Id, ProvinceInfoCard>(Province.Id.class);
		for (final ProvinceSettings p : this.client.initialGameState.getSettings().getProvincesList()) {
			if(p.getRegion() != Province.Region.SUPERPOWERS) {
				cards.put(p.getId(), new ProvinceInfoCard(this.client, p, actionPane, warPane, skin));
			}
		}
		final Province.Id[][] provincePosition = {
			{Province.Id.CANADA,      null,       			  null,     			 null,                      null,                 Province.Id.GREAT_BRITAIN, Province.Id.NORWAY,       Province.Id.SWEDEN,         Province.Id.FINLAND},
			{null,      			  null,       			  null,     			 null,                      null,                 Province.Id.BENELUX, 		 Province.Id.DENMARK,      Province.Id.EAST_GERMANY,   Province.Id.POLAND},
			{Province.Id.MEXICO,      Province.Id.CUBA,       Province.Id.HAITI,     null,                      null,                 Province.Id.FRANCE, 		 Province.Id.WEST_GERMANY, Province.Id.CZECHOSLOVAKIA, Province.Id.HUNGARY},
			{Province.Id.GUATEMALA,   Province.Id.HONDURAS,   Province.Id.NICARAGUA, Province.Id.DOMINICAN_REP, Province.Id.PORTUGAL, Province.Id.SPAIN, 		 Province.Id.ITALY, 	   Province.Id.YUGOSLAVIA, 	   Province.Id.ROMANIA},
			{Province.Id.EL_SALVADOR, Province.Id.COSTA_RICA, Province.Id.PANAMA,    Province.Id.LESS_ANTILLES, null,				  null, 					 null, 					   Province.Id.BULGARIA,       null},
			{null,                    null,                   Province.Id.COLOMBIA,  Province.Id.VENEZUELA,     null,				  null, 					 null, 					   Province.Id.GREECE,         Province.Id.TURKEY},
			{null,                    Province.Id.ECUADOR,    Province.Id.PERU,      Province.Id.GUYANA,        null},
			{null,                    Province.Id.CHILE,      Province.Id.BOLIVIA,   Province.Id.BRAZIL,        null},
			{null,                    null,                   Province.Id.ARGENTINA, Province.Id.URUGUAY,       null},
		};
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
		superpowerPane.left();
		superpowerPane.show();
		
	}
}
