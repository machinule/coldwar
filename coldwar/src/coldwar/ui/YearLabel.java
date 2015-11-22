package coldwar.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import coldwar.logic.Client;

public class YearLabel extends Label {

	protected Client client;

	public YearLabel(final Client client, final Skin skin) {
		super("", skin);
		this.client = client;
		this.updateText();
	}

	@Override
	public void act(final float delta) {
		this.updateText();
	}

	void updateText() {
		this.setText(Integer.toString(this.client.getMoveBuilder().getYear()));
	}
}
