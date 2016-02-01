package com.berserkbentobox.coldwar.ui;

import java.util.function.Function;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import com.berserkbentobox.coldwar.logic.Client;

public class BinaryTable extends Table {

	protected Client client;
	protected boolean prevSwtch = true;
	protected Function<Client, Boolean> switchFn;
	protected Table table1;
	protected Table table2;

	public BinaryTable(final Client client,
					   Function<Client, Boolean> switchFn,
					   Table table1,
					   Table table2,
					   final Skin skin) {
		super(skin);
		this.client = client;
		this.switchFn = switchFn;
		this.table1 = table1;
		this.table2 = table2;
		this.add(table1);
		this.update();
	}
	
	@Override
	public void act(final float delta) {
		this.update();
		super.act(delta);
	}

	void update() {
		boolean swtch = this.switchFn.apply(this.client);
		if(swtch != prevSwtch)
			if(swtch) {
				this.removeActor(table2);
				this.add(table1);
			}
			else {
				this.removeActor(table1);
				this.add(table2);
			}
		prevSwtch = swtch;
	}
	
	void setSwitchFn(Function<Client, Boolean> switchFn) {
		this.switchFn = switchFn;
	}
}
