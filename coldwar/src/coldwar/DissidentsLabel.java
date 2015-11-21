package coldwar;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import coldwar.ProvinceOuterClass.Province;

public class DissidentsLabel extends Label {

	protected Province.Id provinceId;
	protected MoveBuilder moveBuilder;
	
	public DissidentsLabel(Province.Id provinceId, MoveBuilder moveBuilder, Skin skin) {
		super("", skin);
		this.provinceId = provinceId;
		this.moveBuilder = moveBuilder;
		updateText();
	}

	void updateText() {
		setText(moveBuilder.hasDissidents(this.provinceId)?"true":"false");		
	}
	
	@Override
	public void act(float delta) {
		updateText();
	}
}
