package com.berserkbentobox.coldwar.ui;

import java.util.function.Function;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import com.berserkbentobox.coldwar.logic.Client;

public class DynamicButton extends TextButton {

	protected Client client;
	protected Function<Client, Boolean> enabledFn;
	protected Function<Client, Boolean> visibleFn;
	protected Function<Client, String> textFn;
	public boolean isSelected;

	public DynamicButton(final Client client, Function<Client, Boolean> enabledFn, Function<Client, Boolean> visibleFn, Function<Client, String> textFn, final Skin skin) {
		super("", skin);
		this.client = client;
		this.textFn = textFn;
		this.visibleFn = visibleFn;
		this.enabledFn = enabledFn;
		this.update();
	}
	
	public DynamicButton(final Client client, Function<Client, Boolean> enabledFn, Function<Client, String> textFn, final Skin skin) {
		super("", skin);
		this.client = client;
		this.textFn = textFn;
		this.enabledFn = enabledFn;
		this.visibleFn = c -> true;
		this.update();
	}

	public DynamicButton(final Client client, Function<Client, Boolean> enabledFn, String text, final Skin skin) {
		super("", skin);
		this.client = client;
		this.textFn = c -> text;
		this.enabledFn = enabledFn;
		this.visibleFn = c -> true;
		this.update();
	}
	
	@Override
	public void act(final float delta) {
		super.act(delta);
		this.update();
	}

	void update() {
		boolean visible = this.visibleFn.apply(this.client);
		this.setVisible(visible);
		this.setLayoutEnabled(visible);
		boolean enabled = this.enabledFn.apply(this.client);
		this.setDisabled(!enabled);
		if (enabled) {
			if (isSelected) {
				this.setColor(Color.YELLOW);
			} else {
				this.setColor(Color.LIGHT_GRAY);
			}
		} else {
			this.setColor(Color.DARK_GRAY);
		}
		this.setText(textFn.apply(this.client));
	}
}
