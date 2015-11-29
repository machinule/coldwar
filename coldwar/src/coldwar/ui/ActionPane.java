package coldwar.ui;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import coldwar.GameStateOuterClass.ProvinceSettings;
import coldwar.Logger;
import coldwar.logic.Client;

public class ActionPane extends Table {

	private final Client client;
	protected Skin skin;
	DynamicButton selected;
	
	public ActionPane(final Client client, final Skin skin) {
		super();
		this.client = client;
		this.skin = skin;
		this.setDebug(true);
	}

	public void onSelect(final ProvinceSettings province) {
		DynamicButton diplomaticInfluenceButton;
		DynamicButton militaryInfluenceButton;
		DynamicButton covertInfluenceButton;
		
		DynamicButton dissidentsButton;
		DynamicButton politicalPressureButton;
		DynamicButton establishBaseButton;
		
		DynamicButton coupButton;
		DynamicButton invadeButton;
		
		DynamicButton submitButton;
		
		int sizeX = 200;
		int sizeY = 25;
		
		int param; //For use in lambda's
		Map<DynamicButton, Runnable> actionButtons = new HashMap<>();
		
		diplomaticInfluenceButton = new DynamicButton(this.client, c -> c.getMoveBuilder().getComputedGameState().isValidDiaDipMove(c.getPlayer()), "Diplomatic Outreach", this.skin);
		militaryInfluenceButton = new DynamicButton(this.client, c -> c.getMoveBuilder().getComputedGameState().isValidDiaMilMove(c.getPlayer(), province.getId()), "Arms Sales", this.skin);
		covertInfluenceButton = new DynamicButton(this.client, c -> c.getMoveBuilder().getComputedGameState().isValidDiaCovMove(c.getPlayer()), "Support Party", this.skin);
		
		dissidentsButton = new DynamicButton(this.client, c -> c.getMoveBuilder().getComputedGameState().isValidFundDissidentsMove(c.getPlayer(), province.getId()), "Fund Dissidents", this.skin);
		politicalPressureButton = new DynamicButton(this.client, c -> c.getMoveBuilder().getComputedGameState().isValidPoliticalPressureMove(c.getPlayer(), province.getId()), "Political Pressure", this.skin);
		establishBaseButton = new DynamicButton(this.client, c -> c.getMoveBuilder().getComputedGameState().isValidEstablishBaseMove(c.getPlayer(), province.getId()), "Establish Military Base", this.skin);
		
		coupButton = new DynamicButton(this.client, c -> c.getMoveBuilder().getComputedGameState().isValidCoupMove(c.getPlayer(), province.getId()), "Initiate Coup", this.skin);
		invadeButton = new DynamicButton(this.client, c -> false, "Conduct Military Action", this.skin);

		actionButtons.put(diplomaticInfluenceButton, () -> ActionPane.this.client.getMoveBuilder().influenceDip(province.getId(), 1) );
		actionButtons.put(militaryInfluenceButton, () -> ActionPane.this.client.getMoveBuilder().influenceMil(province.getId(), 1) );
		actionButtons.put(covertInfluenceButton, () -> ActionPane.this.client.getMoveBuilder().influenceCov(province.getId(), 1) );

		actionButtons.put(dissidentsButton, () -> ActionPane.this.client.getMoveBuilder().FundDissidents(province.getId()) );
		actionButtons.put(politicalPressureButton, () -> ActionPane.this.client.getMoveBuilder().PoliticalPressure(province.getId()) );
		actionButtons.put(establishBaseButton, () -> ActionPane.this.client.getMoveBuilder().EstablishBase(province.getId()) );

		actionButtons.put(coupButton, () -> ActionPane.this.client.getMoveBuilder().Coup(province.getId(), 1) );
		//actionButtons.put(invadeButton, () -> ActionPane.this.client.getMoveBuilder().Invade(province.getId()) );
		
		submitButton = new DynamicButton(this.client, c -> true, "Submit", this.skin);
		
		diplomaticInfluenceButton.addListener(new ChangeListener() {
			@Override
			public void changed(final ChangeEvent event, final Actor actor) {
				Logger.Info("\"Diplomatic Outreatch\" button pressed on " + province.getId().getValueDescriptor().getName());
				buttonSelect(diplomaticInfluenceButton);
			}
		});

		militaryInfluenceButton.addListener(new ChangeListener() {
			@Override
			public void changed(final ChangeEvent event, final Actor actor) {
				Logger.Info("\"Arms Sales\" button pressed on " + province.getId().getValueDescriptor().getName());
				buttonSelect(militaryInfluenceButton);
			}
		});
		
		covertInfluenceButton.addListener(new ChangeListener() {
			@Override
			public void changed(final ChangeEvent event, final Actor actor) {
				Logger.Info("\"Support Party\" button pressed on " + province.getId().getValueDescriptor().getName());
				buttonSelect(covertInfluenceButton);
			}
		});

		dissidentsButton.addListener(new ChangeListener() {
			@Override
			public void changed(final ChangeEvent event, final Actor actor) {
				Logger.Info("\"Dissidents\" button pressed on " + province.getId().getValueDescriptor().getName());
				buttonSelect(dissidentsButton);
			}
		});
		
		establishBaseButton.addListener(new ChangeListener() {
			@Override
			public void changed(final ChangeEvent event, final Actor actor) {
				Logger.Info("\"Establish Base\" button pressed on " + province.getId().getValueDescriptor().getName());
				buttonSelect(establishBaseButton);
			}
		});
		
		politicalPressureButton.addListener(new ChangeListener() {
			@Override
			public void changed(final ChangeEvent event, final Actor actor) {
				Logger.Info("\"Political Pressure\" button pressed on " + province.getId().getValueDescriptor().getName());
				buttonSelect(politicalPressureButton);
			}
		});
		
		coupButton.addListener(new ChangeListener() {
			@Override
			public void changed(final ChangeEvent event, final Actor actor) {
				Logger.Info("\"Sponsor coup\" button pressed on " + province.getId().getValueDescriptor().getName());
				buttonSelect(coupButton);
			}
		});
		
		submitButton.addListener(new ChangeListener() {
			@Override
			public void changed(final ChangeEvent event, final Actor actor) {
				Logger.Info("\"Submit\" button pressed on " + province.getId().getValueDescriptor().getName());
				actionButtons.get(selected).run();
			}
		});
		
		this.clearChildren();
		
		Table innerTop = new Table(); 
		
		innerTop.add(new DynamicLabel(client, c -> formattedLabel(province), this.skin));
		
		Table innerConfirm = new Table();
		
		innerConfirm.add(submitButton);
		
		Table innerBottom = new Table();
		
		innerBottom.add(diplomaticInfluenceButton)
			.size(sizeX, sizeY)
			.right()
			.padTop(5);
		innerBottom.add(dissidentsButton)
		.size(sizeX, sizeY)
			.left()
			.padTop(5)
			.padLeft(10);
		innerBottom.add(coupButton)
		.size(sizeX, sizeY)
			.left()
			.padTop(5)
			.padLeft(10);
		innerBottom.row();
		innerBottom.add(militaryInfluenceButton)
		.size(sizeX, sizeY)
			.right()
			.padTop(5);
		innerBottom.add(politicalPressureButton)
		.size(sizeX, sizeY)
			.left()
			.padTop(5)
			.padLeft(10);
		innerBottom.add(invadeButton)
		.size(sizeX, sizeY)
		.left()
		.padTop(5)
		.padLeft(10);
		innerBottom.row();
		innerBottom.add(covertInfluenceButton)
		.size(sizeX, sizeY)
			.right()
			.padTop(5);
		innerBottom.add(establishBaseButton)
		.size(sizeX, sizeY)
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
		
		this.add(innerConfirm);
		this.row();
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
	
	protected void buttonSelect(DynamicButton button) {
		if(selected != null) {
			selected.isSelected = false;
		}
		selected = button;
		selected.isSelected = true;
	}
}
