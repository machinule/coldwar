package coldwar.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import coldwar.ColdWarGame;
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
		for (final Province p : this.client.initialGameState.getProvincesList()) {
			ProvinceInfoCard card = new ProvinceInfoCard(p, actionPane, skin);
			card.setDebug(Settings.isDebug());
			nations.add(card);
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
