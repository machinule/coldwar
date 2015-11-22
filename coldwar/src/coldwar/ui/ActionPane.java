package coldwar.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import coldwar.ColdWarGame;
import coldwar.Logger;
import coldwar.ProvinceOuterClass.Province;
import coldwar.logic.MoveBuilder;

public class ActionPane extends Table {

	private final ColdWarGame game;
	protected MoveBuilder moveBuilder;
	protected Skin skin;

	public ActionPane(final ColdWarGame game, final MoveBuilder moveBuilder, final Skin skin) {
		super();
		this.game = game;
		this.moveBuilder = moveBuilder;
		this.skin = skin;
		this.setDebug(true);
	}

	public void onSelect(final Province province) {
		TextButton increaseButton;
		TextButton decreaseButton;
		TextButton dissidentsButton;

		increaseButton = new TextButton("+", this.skin);
		decreaseButton = new TextButton("-", this.skin);
		dissidentsButton = new TextButton("Fund Dissidents", this.skin);

		decreaseButton.addListener(new ChangeListener() {
			@Override
			public void changed(final ChangeEvent event, final Actor actor) {
				Logger.Info("\"Reduce\" button pressed on " + province.getId().getValueDescriptor().getName());
				ActionPane.this.moveBuilder.DecreaseInfluence(province.getId());
				/*
				 * if(!moveBuilder.CanDecreaseInfluence(province.getId())) {
				 * actor.setVisible(false); } increaseButton.setVisible(true);
				 */
			}
		});

		increaseButton.addListener(new ChangeListener() {
			@Override
			public void changed(final ChangeEvent event, final Actor actor) {
				Logger.Info("\"Increase\" button pressed on " + province.getId().getValueDescriptor().getName());
				ActionPane.this.moveBuilder.IncreaseInfluence(province.getId());
				/*
				 * if(!moveBuilder.CanIncreaseInfluence(province.getId())) {
				 * actor.setVisible(false); } decreaseButton.setVisible(true);
				 */
			}
		});

		dissidentsButton.addListener(new ChangeListener() {
			@Override
			public void changed(final ChangeEvent event, final Actor actor) {
				Logger.Info("\"Dissidents\" button pressed on " + province.getId().getValueDescriptor().getName());
				ActionPane.this.moveBuilder.FundDissidents(province.getId());
				/*
				 * if(!moveBuilder.CanDecreaseInfluence(province.getId())) {
				 * actor.setVisible(false); } increaseButton.setVisible(true);
				 */
			}
		});

		this.clearChildren();
		this.add(new Label(province.getId().getValueDescriptor().getName(), this.skin));
		this.row();

		this.add(increaseButton);
		this.add(decreaseButton);
		this.add(dissidentsButton);
		this.add(new Label("Influence:", this.skin));
		this.add(new InfluenceLabel(province.getId(), this.moveBuilder, this.skin));
		this.add(new Label("Dissidents:", this.skin));
		this.add(new DissidentsLabel(province.getId(), this.moveBuilder, this.skin));
	}

}
