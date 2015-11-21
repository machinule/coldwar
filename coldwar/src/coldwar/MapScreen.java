package coldwar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import coldwar.ProvinceOuterClass.Province;

public class MapScreen extends AbstractScreen {
		
    public MapScreen(final ColdWarGame game) {
        super(game);
    }
    
	@Override
	public void show() {
		super.show();
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("textures/uiskin.atlas"));
		Skin skin = new Skin(Gdx.files.internal("textures/uiskin.json"), atlas);
        Table table = new Table(skin);
        table.setFillParent(true);
        table.setDebug(true);
        stage.addActor(table);
        MoveBuilder moveBuilder = new MoveBuilder();
        Toolbar toolbar = new Toolbar(moveBuilder, skin);
        for (Province.Id id : Province.Id.values()) {
            table.add(new ProvinceInfoCard(Province.newBuilder().setId(id).build(), moveBuilder, toolbar, skin));
            table.row();
        }
        table.add(toolbar);
	}    
}
