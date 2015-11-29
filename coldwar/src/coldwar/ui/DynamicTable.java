package coldwar.ui;

import java.util.function.Function;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import coldwar.logic.Client;

public class DynamicTable extends Table {

	protected Client client;
	protected Function<Client, Boolean> visibleFn;

	public DynamicTable(final Client client, Function<Client, Boolean> visibleFn, final Skin skin) {
		super(skin);
		this.client = client;
		this.visibleFn = visibleFn;
		this.update();
	}
	
	@Override
	public void act(final float delta) {
		this.update();
	}

	void update() {
		boolean visible = this.visibleFn.apply(this.client);
		this.setVisible(visible);
		this.setLayoutEnabled(visible);
	}
}
