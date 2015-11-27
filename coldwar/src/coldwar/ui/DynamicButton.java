package coldwar.ui;

import java.util.function.Function;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import coldwar.logic.Client;

public class DynamicButton extends TextButton {

	protected Client client;
	protected Function<Client, Boolean> enabledFn;

	public DynamicButton(final Client client, Function<Client, Boolean> enabledFn, String text, final Skin skin) {
		super(text, skin);
		this.client = client;
		this.enabledFn = enabledFn;
		this.update();
	}

	@Override
	public void act(final float delta) {
		this.update();
	}

	void update() {
		Boolean enabled = this.enabledFn.apply(this.client);
		this.setDisabled(!enabled);
		if (enabled) {
			this.setColor(Color.LIGHT_GRAY);			
		} else {
			this.setColor(Color.DARK_GRAY);
		}
	}
}
