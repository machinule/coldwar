package coldwar.ui;

import java.util.EnumMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import coldwar.ColdWarGame;
import coldwar.GameStateOuterClass.ProvinceSettings;
import coldwar.Settings;
import coldwar.ProvinceOuterClass.Province;
import coldwar.logic.Client;

public class MapScreen extends AbstractScreen {

	private Client client;
	
	public MapScreen(final ColdWarGame game, Client client) {
		super(game);
		this.client = client;
	}

	@Override
	public void show() {
		super.show();
		final TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("textures/uiskin.atlas"));
		final Skin skin = new Skin(Gdx.files.internal("textures/uiskin.json"), atlas);
		
		// Nations
		
		final Table nations = new Table(skin);
		nations.setFillParent(true);
		nations.setDebug(Settings.isDebug());
		this.stage.addActor(nations);
		final ActionPane actionPane = new ActionPane(this.client, skin);
		actionPane.setDebug(Settings.isDebug());
		final HeaderPane headerPane = new HeaderPane(this.client, skin);
		headerPane.setDebug(Settings.isDebug());
		final SuperpowerPane superpowerPane = new SuperpowerPane(this.client, skin);
		superpowerPane.setDebug(Settings.isDebug());
		final EnumMap<Province.Id, ProvinceInfoCard> cards = new EnumMap<Province.Id, ProvinceInfoCard>(Province.Id.class);
		for (final ProvinceSettings p : this.client.initialGameState.getSettings().getProvincesList()) {
			if(p.getRegionInit() != Province.Region.SUPERPOWERS) {
				cards.put(p.getId(), new ProvinceInfoCard(this.client, p, actionPane, skin));
			}
		}
		final Province.Id[][] provincePosition = {
			{Province.Id.MEXICO,    Province.Id.CUBA,       Province.Id.HAITI,     null,                      null},
			{Province.Id.GUATEMALA, Province.Id.HONDURAS,   Province.Id.NICARAGUA, Province.Id.DOMINICAN_REP, null},
			{null,                  Province.Id.COSTA_RICA, Province.Id.PANAMA,    Province.Id.LESS_ANTILLES, null},
			{null,                  null,                   Province.Id.COLOMBIA,  Province.Id.VENEZUELA,     Province.Id.GUYANA},
			{null,                  Province.Id.ECUADOR,    Province.Id.PERU,      Province.Id.BRAZIL,        null},
			{null,                  Province.Id.CHILE,      Province.Id.BOLIVIA,   null,                      null},
			{null,                  null,                   Province.Id.ARGENTINA, null,                      null},
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
		
		// Side Pane
		
		superpowerPane.setFillParent(true);
		this.stage.addActor(superpowerPane);
		superpowerPane.left();
		superpowerPane.show();
		
	}
}
