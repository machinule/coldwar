package coldwar.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import coldwar.logic.Client;
import coldwar.logic.Client.Player;

public class PlayerLabel extends Label {

	protected Client client;

	public PlayerLabel(final Client client, final Skin skin) {
		super("", skin);
		this.client = client;
		this.updateText();
	}

	@Override
	public void act(final float delta) {
		this.updateText();
	}

	void updateText() {
		if (this.client.getPlayer() == Player.USA) {
			this.setText("USA");
		} else {
			this.setText("USSR");
		}
	}
}
