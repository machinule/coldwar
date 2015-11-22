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
		final Table table = new Table(skin);
		table.setFillParent(true);
		table.setDebug(true);
		this.stage.addActor(table);
		final MoveBuilder moveBuilder = new MoveBuilder();
		final Toolbar toolbar = new Toolbar(this.game, moveBuilder, skin);
		for (final Province.Id id : Province.Id.values()) {
			table.add(new ProvinceInfoCard(Province.newBuilder().setId(id).build(), moveBuilder, toolbar, skin));
			table.row();
		}
		table.add(toolbar);
	}
}
