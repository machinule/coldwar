package coldwar.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import coldwar.ProvinceOuterClass.Province;
import coldwar.logic.Client;

public class DissidentsLabel extends Label {

	protected Client client;
	protected Province.Id provinceId;

	public DissidentsLabel(final Province.Id provinceId, final Client client, final Skin skin) {
		super("", skin);
		this.provinceId = provinceId;
		this.client = client;
		this.updateText();
	}

	@Override
	public void act(final float delta) {
		this.updateText();
	}

	void updateText() {
		this.setText(this.client.getMoveBuilder().hasDissidents(this.provinceId) ? "true" : "false");
	}
}
