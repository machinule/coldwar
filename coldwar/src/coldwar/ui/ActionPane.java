package coldwar.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import coldwar.GameStateOuterClass.ProvinceSettings;
import coldwar.Logger;
import coldwar.ProvinceOuterClass.Province;
import coldwar.logic.Client;
import coldwar.logic.ComputedGameState;

public class ActionPane extends Table {

	private final Client client;
	protected Skin skin;

	public ActionPane(final Client client, final Skin skin) {
		super();
		this.client = client;
		this.skin = skin;
		this.setDebug(true);
	}

	public void onSelect(final ProvinceSettings province) {
		TextButton diplomaticInfluenceButton;
		TextButton militaryInfluenceButton;
		TextButton covertInfluenceButton;
		
		TextButton dissidentsButton;
		TextButton coupButton;
		TextButton establishBaseButton;

		diplomaticInfluenceButton = new DynamicButton(this.client, c -> c.getMoveBuilder().getComputedGameState().isValidDiaDipMove(c.getPlayer()), "Diplomatic Outreach", this.skin);
		militaryInfluenceButton = new DynamicButton(this.client, c -> c.getMoveBuilder().getComputedGameState().isValidDiaMilMove(c.getPlayer()), "Arms Sales", this.skin);
		covertInfluenceButton = new DynamicButton(this.client, c -> c.getMoveBuilder().getComputedGameState().isValidDiaCovMove(c.getPlayer()), "Support Party", this.skin);
		
		dissidentsButton = new DynamicButton(this.client, c -> c.getMoveBuilder().getComputedGameState().isValidFundDissidentsMove(c.getPlayer(), province.getId()), "Fund Dissidents", this.skin);
		coupButton = new TextButton("Organize Coup", this.skin);
		establishBaseButton = new DynamicButton(this.client, c -> c.getMoveBuilder().getComputedGameState().isValidEstablishBaseMove(c.getPlayer(), province.getId()), "Establish Military Base", this.skin);
		
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
		
		establishBaseButton.addListener(new ChangeListener() {
			@Override
			public void changed(final ChangeEvent event, final Actor actor) {
				Logger.Info("\"Establish Base\" button pressed on " + province.getId().getValueDescriptor().getName());
				ActionPane.this.client.getMoveBuilder().EstablishBase(province.getId());
			}
		});
		
		this.clearChildren();
		
		Table innerTop = new Table();
		int netStability = province.getStabilityBase() + client.getMoveBuilder().getStabilityModifier(province.getId());
		innerTop.add(new DynamicLabel(client, c -> formattedLabel(province), this.skin));
		
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
	
	protected String formattedLabel(final ProvinceSettings province) {
		String ret = province.getLabel() + " | " + province.getStabilityBase();
		int positiveModifier = 0;
		int negativeModifier = client.getMoveBuilder().getStabilityModifier(province.getId());
		if (positiveModifier != 0) {
			ret += " + " + positiveModifier + " ";
		}
		if (negativeModifier != 0) {
			ret += " - " + Math.abs(negativeModifier) + " ";
		}
		return ret;
	}

}
