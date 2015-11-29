package coldwar.ui;

import java.util.function.Function;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import coldwar.logic.Client;

public class DynamicLabel extends Label {

	protected Client client;
	protected Function<Client, String> textFn;
	protected Function<Client, Color> colorFn;
	protected Function<Client, Boolean> visibleFn;

	public DynamicLabel(final Client client, Function<Client, String> textFn, final Skin skin) {
		super("", skin);
		this.client = client;
		this.textFn = textFn;
		this.colorFn = c -> Color.BLACK;
		this.visibleFn = c -> true;
		this.update();
	}

	public DynamicLabel(final Client client, Function<Client, String> textFn, Function<Client, Color> colorFn, final Skin skin) {
		super("", skin);
		this.client = client;
		this.textFn = textFn;
		this.colorFn = colorFn;
		this.visibleFn = c -> true;
		this.update();
	}

	public void setVisibleFn(Function<Client, Boolean> visibleFn) {
		this.visibleFn = visibleFn;
	}
	
	@Override
	public void act(final float delta) {
		this.update();
	}

	void update() {
		this.setColor(colorFn.apply(this.client));
		this.setText(textFn.apply(this.client));
		boolean visible = this.visibleFn.apply(this.client);
		this.setVisible(visible);
		this.setLayoutEnabled(visible);
		if(!visible) this.clear();
	}
}
