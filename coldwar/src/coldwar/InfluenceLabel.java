package coldwar;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import coldwar.ProvinceOuterClass.Province;

public class InfluenceLabel extends Label {

	protected Province.Id provinceId;
	protected MoveBuilder moveBuilder;
	
	public InfluenceLabel(Province.Id provinceId, MoveBuilder moveBuilder, Skin skin) {
		super("", skin);
		this.provinceId = provinceId;
		this.moveBuilder = moveBuilder;
		updateText();
	}

	void updateText() {
		setText(Integer.toString(moveBuilder.getInfluence(this.provinceId)));		
	}
	
	@Override
	public void act(float delta) {
		updateText();
	}
}
