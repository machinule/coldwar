package com.berserkbentobox.coldwar.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import com.berserkbentobox.coldwar.Province.ProvinceSettings;
import com.berserkbentobox.coldwar.Id.ProvinceId;
import com.berserkbentobox.coldwar.Leader.LeaderSettings;
import com.berserkbentobox.coldwar.Leader.LeaderState;
import com.berserkbentobox.coldwar.Logger;
import com.berserkbentobox.coldwar.logic.Client;
import com.berserkbentobox.coldwar.logic.ComputedGameState;

public class ActionPane extends FooterPane {

	DynamicButton selected;
	boolean requiresSlider;	
    
	public ActionPane(final Client client, final Skin skin) {
		super(client, skin);
	}

	@Override
	public void onSelect(final ProvinceSettings province) {
		ComputedGameState state = client.getMoveBuilder().getComputedGameState();
		
		DynamicButton diplomaticInfluenceButton;
		DynamicButton militaryInfluenceButton;
		DynamicButton covertInfluenceButton;
		
		DynamicButton dissidentsButton;
		DynamicButton politicalPressureButton;
		DynamicButton establishSpyNetworkButton;
		
		DynamicButton coupButton;
		DynamicButton invadeButton;
		
		DynamicButton submitButton;
		selected = null;
		requiresSlider = false;
		
		DynamicSliderContainer actionParamInput = new DynamicSliderContainer(client, 1, 2, 1, c -> requiresSlider, false, skin);
		
		int sizeX = 200;
		int sizeY = 25;
		
		Map<DynamicButton, Runnable> actionButtonMethods = new HashMap<>();
		Map<DynamicButton, Function<Client, Integer>> actionButtonCosts = new HashMap<>();
		
		diplomaticInfluenceButton = new DynamicButton(this.client, c -> c.getMoveBuilder().isValidDiplomacyMove(c.getPlayer(), province.getId()), "Economic Aid", this.skin);
		militaryInfluenceButton = new DynamicButton(this.client, c -> c.getMoveBuilder().isValidMilitaryMove(c.getPlayer(), province.getId()), "Arms Sales", this.skin);
		covertInfluenceButton = new DynamicButton(this.client, c -> c.getMoveBuilder().isValidCovertMove(c.getPlayer(), province.getId()), "Support Party", this.skin);
		
		dissidentsButton = new DynamicButton(this.client, c -> c.getMoveBuilder().isValidFundDissidentsMove(c.getPlayer(), province.getId()), "Fund Dissidents", this.skin);
		politicalPressureButton = new DynamicButton(this.client, c -> c.getMoveBuilder().getComputedGameState().isValidPoliticalPressureMove(c.getPlayer(), province.getId()), "Political Pressure", this.skin);
		establishSpyNetworkButton = new DynamicButton(this.client, c -> c.getMoveBuilder().isValidEstablishSpyNetworkMove(c.getPlayer(), province.getId()), "Establish Spy Network", this.skin);
		
		coupButton = new DynamicButton(this.client, c -> c.getMoveBuilder().getComputedGameState().isValidCoupMove(c.getPlayer(), province.getId()), "Initiate Coup", this.skin);
		invadeButton = new DynamicButton(this.client, c -> false, "Conduct Military Action", this.skin);

		actionButtonMethods.put(diplomaticInfluenceButton, () -> ActionPane.this.client.getMoveBuilder().influenceDip(province.getId(), actionParamInput.getValue()) );
		actionButtonMethods.put(militaryInfluenceButton, () -> ActionPane.this.client.getMoveBuilder().influenceMil(province.getId(), actionParamInput.getValue()) );
		actionButtonMethods.put(covertInfluenceButton, () -> ActionPane.this.client.getMoveBuilder().influenceCov(province.getId(), actionParamInput.getValue()) );

		actionButtonMethods.put(dissidentsButton, () -> ActionPane.this.client.getMoveBuilder().fundDissidents(province.getId()) );
		actionButtonMethods.put(politicalPressureButton, () -> ActionPane.this.client.getMoveBuilder().PoliticalPressure(province.getId()) );
		actionButtonMethods.put(establishSpyNetworkButton, () -> ActionPane.this.client.getMoveBuilder().establishSpyNetwork(province.getId()) );

		actionButtonMethods.put(coupButton, () -> ActionPane.this.client.getMoveBuilder().Coup(province.getId(), actionParamInput.getValue()) );
		//actionButtons.put(invadeButton, () -> ActionPane.this.client.getMoveBuilder().Invade(province.getId()) );
		
		actionButtonCosts.put(diplomaticInfluenceButton, c -> c.getMoveBuilder().getDiplomacyMoveCost(client.getPlayer(), province.getId(), actionParamInput.getValue()));
		actionButtonCosts.put(militaryInfluenceButton, c -> c.getMoveBuilder().getMilitaryMoveCost(client.getPlayer(), province.getId(), actionParamInput.getValue()));
		actionButtonCosts.put(covertInfluenceButton, c -> c.getMoveBuilder().getCovertMoveCost(client.getPlayer(), province.getId(), actionParamInput.getValue()));

		actionButtonCosts.put(dissidentsButton, c -> c.getMoveBuilder().getFundDissidentsMoveCost(client.getPlayer(), province.getId()));
		actionButtonCosts.put(politicalPressureButton, c -> state.getPoliticalPressureMoveCost() );
		actionButtonCosts.put(establishSpyNetworkButton, c -> c.getMoveBuilder().getEstablishSpyNetworkMoveCost(client.getPlayer(), province.getId()) );

		actionButtonCosts.put(coupButton, c -> state.getCoupMoveCost(province.getId()) );
		//actionButtons.put(invadeButton, () -> ActionPane.this.client.getMoveBuilder().Invade(province.getId()) );
		
		submitButton = new DynamicButton(this.client, c -> !(c.getMoveBuilder().hasActed(province.getId()) || (selected == null)), "Submit", this.skin);
		
		diplomaticInfluenceButton.addListener(new ChangeListener() {
			@Override
			public void changed(final ChangeEvent event, final Actor actor) {
				Logger.Info("\"Diplomatic Outreatch\" button pressed on " + province.getId().getValueDescriptor().getName());
				buttonSelect(diplomaticInfluenceButton);
				requiresSlider = true;
				actionParamInput.setBounds(1, 
						client.getMoveBuilder().getMechanics().getProvinces().getDiplomacyMoveMaxValue(client.getPlayer(), province.getId()),
						1);
			}
		});
		
		militaryInfluenceButton.addListener(new ChangeListener() {
			@Override
			public void changed(final ChangeEvent event, final Actor actor) {
				Logger.Info("\"Arms Sales\" button pressed on " + province.getId().getValueDescriptor().getName());
				buttonSelect(militaryInfluenceButton);
				requiresSlider = true;
				actionParamInput.setBounds(1, 
						client.getMoveBuilder().getMechanics().getProvinces().getMilitaryMoveMaxValue(client.getPlayer(), province.getId()),
						1);
			}
		});
		
		covertInfluenceButton.addListener(new ChangeListener() {
			@Override
			public void changed(final ChangeEvent event, final Actor actor) {
				Logger.Info("\"Support Party\" button pressed on " + province.getId().getValueDescriptor().getName());
				buttonSelect(covertInfluenceButton);
				requiresSlider = true;
				actionParamInput.setBounds(1, 
						client.getMoveBuilder().getMechanics().getProvinces().getCovertMoveMaxValue(client.getPlayer(), province.getId()),
						1);
			}
		});

		dissidentsButton.addListener(new ChangeListener() {
			@Override
			public void changed(final ChangeEvent event, final Actor actor) {
				Logger.Info("\"Dissidents\" button pressed on " + province.getId().getValueDescriptor().getName());
				buttonSelect(dissidentsButton);
				requiresSlider = false;
			}
		});
		
		establishSpyNetworkButton.addListener(new ChangeListener() {
			@Override
			public void changed(final ChangeEvent event, final Actor actor) {
				Logger.Info("\"Establish Spy Network\" button pressed on " + province.getId().getValueDescriptor().getName());
				buttonSelect(establishSpyNetworkButton);
				requiresSlider = false;
			}
		});
		
		politicalPressureButton.addListener(new ChangeListener() {
			@Override
			public void changed(final ChangeEvent event, final Actor actor) {
				Logger.Info("\"Political Pressure\" button pressed on " + province.getId().getValueDescriptor().getName());
				buttonSelect(politicalPressureButton);
				requiresSlider = false;
			}
		});
		
		coupButton.addListener(new ChangeListener() {
			@Override
			public void changed(final ChangeEvent event, final Actor actor) {
				Logger.Info("\"Sponsor coup\" button pressed on " + province.getId().getValueDescriptor().getName());
				buttonSelect(coupButton);
				requiresSlider = false;
			}
		});
		
		submitButton.addListener(new ChangeListener() {
			@Override
			public void changed(final ChangeEvent event, final Actor actor) {
				Logger.Info("\"Submit\" button pressed on " + province.getId().getValueDescriptor().getName());
				actionButtonMethods.get(selected).run();
				requiresSlider = false;
			}
		});
		
		this.clearChildren();
		
		Table innerTop = new Table(); 
		
		innerTop.add(new DynamicLabel(client, c -> formattedLabel(province), this.skin));
		
		Table innerConfirm = new Table();
		
		DynamicLabel actionParamLabel = new DynamicLabel(client, c -> requiresSlider ? "Value: " + actionParamInput.getValue() + " " : "", skin);
		DynamicLabel costLabel = new DynamicLabel(client, c -> selected != null ? "Cost: " + actionButtonCosts.get(selected).apply(client) + " " : "", skin);

		innerConfirm.add(submitButton).left();
		innerConfirm.add(actionParamLabel).expand().right();
		innerConfirm.add(actionParamInput).left();
		innerConfirm.add(costLabel).expand().right();
		
		this.add(innerConfirm).fill();
		this.row();
		this.add(innerTop);
		this.row();

		Table innerAction = new Table();
		
		innerAction.add(diplomaticInfluenceButton)
			.size(sizeX, sizeY)
			.right()
			.padTop(5);
		innerAction.add(dissidentsButton)
			.size(sizeX, sizeY)
			.left()
			.padTop(5)
			.padLeft(10);
		innerAction.add(coupButton)
			.size(sizeX, sizeY)
			.left()
			.padTop(5)
			.padLeft(10);
		innerAction.row();
		innerAction.add(militaryInfluenceButton)
			.size(sizeX, sizeY)
			.right()
			.padTop(5);
		innerAction.add(politicalPressureButton)
		.size(sizeX, sizeY)
			.left()
			.padTop(5)
			.padLeft(10);
		innerAction.add(invadeButton)
			.size(sizeX, sizeY)
			.left()
			.padTop(5)
			.padLeft(10);
		innerAction.row();
		innerAction.add(covertInfluenceButton)
			.size(sizeX, sizeY)
			.right()
			.padTop(5);
		innerAction.add(establishSpyNetworkButton)
			.size(sizeX, sizeY)
			.left()
			.padTop(5)
			.padLeft(10);
		innerAction.row();
		innerAction.add(new Label("Adjacencies:", this.skin));
		DynamicLabel adjacencyList = new DynamicLabel(this.client,
				c -> getFormattedAdjacencies(province),
				c -> state.isInRange(c.getPlayer(), province.getId()) ? Color.BLACK : Color.RED,
				this.skin);
		innerAction.add(adjacencyList)
			.size(0, 0)
			.left()
			.padLeft(10);
		innerAction.row();
		innerAction.add(new Label("Leader:", this.skin));
		innerAction.add(new DynamicLabel(this.client, c -> getFormattedLeader(province), this.skin))
			.size(0, 0)
			.padLeft(10)
			.left();
		innerAction.row();
		
		this.add(innerAction);
		this.row();
	}
	
	protected String formattedLabel(final ProvinceSettings province) {
		String ret = province.getLabel() + " | " + province.getStabilityBase();
		int modifier = client.getMoveBuilder().getStabilityModifier(province.getId());
		if (modifier > 0) {
			ret += " + " + modifier + " ";
		}
		if (modifier < 0) {
			ret += " - " + Math.abs(modifier) + " ";
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
	
	protected String getFormattedAdjacencies(final ProvinceSettings province) {
		String ret = "";
		int count = 0;
		int max = province.getAdjacencyCount();
		for(ProvinceId id : province.getAdjacencyList()) {
			count++;
			ret += client.getMoveBuilder().getComputedGameState().provinceSettings.get(id).getLabel();
			if(count < max) ret += ", ";
		}
		return ret;
	}
	
	protected String getFormattedLeader(final ProvinceSettings province) {
		ComputedGameState state = client.getMoveBuilder().getComputedGameState();
		if (state.hasLeader(province.getId())) {
			LeaderState l = state.leaders.get(province.getId());
			LeaderSettings s = client.getMoveBuilder().getComputedGameState().getLeaderSettings(l.getId());
			String ret = s.getId();
			ret += " (" + (state.year - s.getBirthYear()) + ") ";
			return ret;
		}
		else
			return "None";
	}
}
