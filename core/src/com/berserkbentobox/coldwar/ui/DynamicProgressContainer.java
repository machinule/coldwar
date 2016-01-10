package coldwar.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import coldwar.logic.Client;

public class DynamicProgressContainer extends Table {

	protected Client client;
	protected int min;
	protected int max;
	protected int inc;
	protected boolean orientation;
	protected Slider slider;

	public DynamicProgressContainer(final Client client, 
								  int min,
								  int max,
							 	  int inc,
								  final boolean orientation,
								  final Skin skin) {
		super(skin);
		this.client = client;
		this.setBounds(min, max, inc);
	}
	
	public void setBounds(int min, int max, int inc) {
		this.min = min;
		this.max = max;
		this.inc = inc;
	}
	protected void setSlider() {
		if(slider != null) this.removeActor(slider);
		slider = new Slider(this.min, this.max, this.inc, orientation, this.getSkin());
		this.add(slider);
	}
	
	public int getValue() {
		if(slider != null) return (int) slider.getValue();
		return 0;
	}
}