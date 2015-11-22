package coldwar.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import coldwar.ProvinceOuterClass.Province;
import coldwar.logic.MoveBuilder;

public class DissidentsLabel extends Label {

	protected MoveBuilder moveBuilder;
	protected Province.Id provinceId;

	public DissidentsLabel(final Province.Id provinceId, final MoveBuilder moveBuilder, final Skin skin) {
		super("", skin);
		this.provinceId = provinceId;
		this.moveBuilder = moveBuilder;
		this.updateText();
	}

	@Override
	public void act(final float delta) {
		this.updateText();
	}

	void updateText() {
		this.setText(this.moveBuilder.hasDissidents(this.provinceId) ? "true" : "false");
	}
}
