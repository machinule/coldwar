package coldwar.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import coldwar.ColdWarGame;
import coldwar.ProvinceOuterClass.Province;
import coldwar.logic.MoveBuilder;

public class MapScreen extends AbstractScreen {

	public MapScreen(final ColdWarGame game) {
		super(game);
	}

	@Override
	public void show() {
		super.show();
		final TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("textures/uiskin.atlas"));
		final Skin skin = new Skin(Gdx.files.internal("textures/uiskin.json"), atlas);
		
		// Nations
		
		final Table nations = new Table(skin);
		nations.setFillParent(true);
		nations.setDebug(true);
		this.stage.addActor(nations);
		final MoveBuilder moveBuilder = new MoveBuilder();
		final ActionPane actionPane = new ActionPane(this.game, moveBuilder, skin);
		final HeaderPane headerPane = new HeaderPane(this.game, moveBuilder, skin);
		for (final Province.Id id : Province.Id.values()) {
			nations.add(new ProvinceInfoCard(Province.newBuilder().setId(id).build(), moveBuilder, actionPane, skin));
			nations.row();
		}
		
		// Top Pane
		
		headerPane.setFillParent(true);
		headerPane.setDebug(true);
		this.stage.addActor(headerPane);
		headerPane.top();
		headerPane.show();
		
		// Bottom Pane
		
		actionPane.setFillParent(true);
		actionPane.setDebug(true);
		this.stage.addActor(actionPane);
		actionPane.bottom();
	}
}
