package coldwar.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import coldwar.Logger;
import coldwar.ProvinceOuterClass.Province;
import coldwar.logic.Client;

public class ActionPane extends Table {

	private final Client client;
	protected Skin skin;

	public ActionPane(final Client client, final Skin skin) {
		super();
		this.client = client;
		this.skin = skin;
		this.setDebug(true);
	}

	public void onSelect(final Province province) {
		TextButton diplomaticInfluenceButton;
		TextButton militaryInfluenceButton;
		TextButton covertInfluenceButton;
		
		TextButton dissidentsButton;
		TextButton coupButton;
		TextButton establishBaseButton;

		diplomaticInfluenceButton = new DynamicButton(this.client, c -> c.getMoveBuilder().getPolStore() > 0, "Diplomatic Outreach", this.skin);
		militaryInfluenceButton = new DynamicButton(this.client, c -> c.getMoveBuilder().getMilStore() > 0, "Arms Sales", this.skin);
		covertInfluenceButton = new DynamicButton(this.client, c -> c.getMoveBuilder().getCovStore() > 0, "Support Party", this.skin);
		
		dissidentsButton = new DynamicButton(this.client, c -> !c.getMoveBuilder().hasDissidents(province.getId()), "Fund Dissidents", this.skin);
		coupButton = new TextButton("Organize Coup", this.skin);
		establishBaseButton = new TextButton("Establish Military Base", this.skin);
		
		diplomaticInfluenceButton.addListener(new ChangeListener() {
			@Override
			public void changed(final ChangeEvent event, final Actor actor) {
				Logger.Info("\"Diplomatic Outreatch\" button pressed on " + province.getId().getValueDescriptor().getName());
				ActionPane.this.client.getMoveBuilder().influenceDip(province.getId(), 1);
			}
		});

		militaryInfluenceButton.addListener(new ChangeListener() {
			@Override
			public void changed(final ChangeEvent event, final Actor actor) {
				Logger.Info("\"Arms Sales\" button pressed on " + province.getId().getValueDescriptor().getName());
				ActionPane.this.client.getMoveBuilder().influenceMil(province.getId(), 1);
			}
		});
		
		covertInfluenceButton.addListener(new ChangeListener() {
			@Override
			public void changed(final ChangeEvent event, final Actor actor) {
				Logger.Info("\"Support Party\" button pressed on " + province.getId().getValueDescriptor().getName());
				ActionPane.this.client.getMoveBuilder().influenceCov(province.getId(), 1);
			}
		});

		dissidentsButton.addListener(new ChangeListener() {
			@Override
			public void changed(final ChangeEvent event, final Actor actor) {
				Logger.Info("\"Dissidents\" button pressed on " + province.getId().getValueDescriptor().getName());
				ActionPane.this.client.getMoveBuilder().FundDissidents(province.getId());
				/*
				 * if(!moveBuilder.CanDecreaseInfluence(province.getId())) {
				 * actor.setVisible(false); } increaseButton.setVisible(true);
				 */
			}
		});

		this.clearChildren();
		
		Table innerTop = new Table();
		innerTop.add(new Label(province.getLabel() + " | Stability: " + province.getStability(), this.skin));
		
		Table innerBottom = new Table();
		
		innerBottom.add(diplomaticInfluenceButton)
			.right()
			.padTop(5);
		innerBottom.add(dissidentsButton)
			.left()
			.padTop(5)
			.padLeft(10);
		innerBottom.row();
		innerBottom.add(militaryInfluenceButton)
			.right()
			.padTop(5);
		innerBottom.add(coupButton)
			.left()
			.padTop(5)
			.padLeft(10);
		innerBottom.row();
		innerBottom.add(covertInfluenceButton)
			.right()
			.padTop(5);
		innerBottom.add(establishBaseButton)
			.left()
			.padTop(5)
			.padLeft(10);
		innerBottom.row();
		innerBottom.add(new Label("Dissidents:", this.skin));
		innerBottom.add(new DynamicLabel(this.client, c -> c.getMoveBuilder().hasDissidents(province.getId()) ? "True" : "False", this.skin));
		innerBottom.row();
		innerBottom.add(new Label("Influence:", this.skin));
		innerBottom.add(new DynamicLabel(
				this.client,
				c -> Integer.toString(Math.abs(c.getMoveBuilder().getInfluence(province.getId()))),
				c -> c.getMoveBuilder().getInfluence(province.getId()) > 0 ? Color.BLUE : c.getMoveBuilder().getInfluence(province.getId()) < 0 ? Color.RED : Color.BLACK,
				this.skin));
		innerBottom.row();
		
		this.add(innerTop);
		this.row();
		this.add(innerBottom);
		this.row();
		this.add(innerBottom);
		this.row();
	}

}
