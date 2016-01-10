package com.berserkbentobox.coldwar.ui;

import java.util.function.Function;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;

import com.berserkbentobox.coldwar.logic.Client;

public class DynamicSliderContainer extends DynamicTable {

	protected Client client;
	protected int min;
	protected int max;
	protected int inc;
	protected boolean orientation;
	protected Slider slider;

	public DynamicSliderContainer(final Client client, 
								  int min,
								  int max,
							 	  int inc,
							 	  Function<Client, Boolean> visibleFn,
								  final boolean orientation,
								  final Skin skin) {
		super(client, visibleFn, skin);
		this.client = client;
		this.visibleFn = visibleFn;
		this.setBounds(min, max, inc);
		this.update();
	}
	
	@Override
	public void act(final float delta) {
		this.update();
	}
	
	public void setBounds(int min, int max, int inc) {
		this.min = min;
		this.max = max;
		this.inc = inc;
		if(this.visibleFn.apply(this.client)) this.setSlider();
	}

	@Override
	void update() {
		boolean visible = this.visibleFn.apply(this.client);
		this.setVisible(visible);
		this.setLayoutEnabled(visible);
		if(!visible) this.clear();
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
