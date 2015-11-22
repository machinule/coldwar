package coldwar.ui;

import java.util.function.Function;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import coldwar.logic.Client;

public class DynamicLabel extends Label {

	protected Client client;
	protected Function<Client, String> fn;

	public DynamicLabel(final Client client, Function<Client, String> fn, final Skin skin) {
		super("", skin);
		this.client = client;
		this.fn = fn;
		this.updateText();
	}

	@Override
	public void act(final float delta) {
		this.updateText();
	}

	void updateText() {
		this.setText(fn.apply(this.client));
	}
}
