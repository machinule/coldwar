package coldwar.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import coldwar.GameSettingsOuterClass.ProvinceSettings;
import coldwar.LeaderOuterClass.Leader;
import coldwar.ProvinceOuterClass.Province;
import coldwar.Logger;
import coldwar.Settings;
import coldwar.logic.Client;
import coldwar.logic.ComputedGameState;

public class ActionPane extends Table {

	private final Client client;
	protected Skin skin;
	DynamicButton selected;
	boolean requiresSlider;	
    
	public ActionPane(final Client client, final Skin skin) {
		super();
		this.client = client;
		this.skin = skin;
		this.setDebug(true);
	}

	public void onSelect(final ProvinceSettings province) {
		ComputedGameState state = client.getMoveBuilder().getComputedGameState();
		
		DynamicButton diplomaticInfluenceButton;
		DynamicButton militaryInfluenceButton;
		DynamicButton covertInfluenceButton;
		
		DynamicButton dissidentsButton;
		DynamicButton politicalPressureButton;
		DynamicButton establishBaseButton;
		
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
		
		diplomaticInfluenceButton = new DynamicButton(this.client, c -> c.getMoveBuilder().getComputedGameState().isValidDiaDipMove(c.getPlayer(), province.getId()), "Diplomatic Outreach", this.skin);
		militaryInfluenceButton = new DynamicButton(this.client, c -> c.getMoveBuilder().getComputedGameState().isValidDiaMilMove(c.getPlayer(), province.getId()), "Arms Sales", this.skin);
		covertInfluenceButton = new DynamicButton(this.client, c -> c.getMoveBuilder().getComputedGameState().isValidDiaCovMove(c.getPlayer(), province.getId()), "Support Party", this.skin);
		
		dissidentsButton = new DynamicButton(this.client, c -> c.getMoveBuilder().getComputedGameState().isValidFundDissidentsMove(c.getPlayer(), province.getId()), "Fund Dissidents", this.skin);
		politicalPressureButton = new DynamicButton(this.client, c -> c.getMoveBuilder().getComputedGameState().isValidPoliticalPressureMove(c.getPlayer(), province.getId()), "Political Pressure", this.skin);
		establishBaseButton = new DynamicButton(this.client, c -> c.getMoveBuilder().getComputedGameState().isValidEstablishBaseMove(c.getPlayer(), province.getId()), "Establish Military Base", this.skin);
		
		coupButton = new DynamicButton(this.client, c -> c.getMoveBuilder().getComputedGameState().isValidCoupMove(c.getPlayer(), province.getId()), "Initiate Coup", this.skin);
		invadeButton = new DynamicButton(this.client, c -> false, "Conduct Military Action", this.skin);

		actionButtonMethods.put(diplomaticInfluenceButton, () -> ActionPane.this.client.getMoveBuilder().influenceDip(province.getId(), actionParamInput.getValue()) );
		actionButtonMethods.put(militaryInfluenceButton, () -> ActionPane.this.client.getMoveBuilder().influenceMil(province.getId(), actionParamInput.getValue()) );
		actionButtonMethods.put(covertInfluenceButton, () -> ActionPane.this.client.getMoveBuilder().influenceCov(province.getId(), actionParamInput.getValue()) );

		actionButtonMethods.put(dissidentsButton, () -> ActionPane.this.client.getMoveBuilder().FundDissidents(province.getId()) );
		actionButtonMethods.put(politicalPressureButton, () -> ActionPane.this.client.getMoveBuilder().PoliticalPressure(province.getId()) );
		actionButtonMethods.put(establishBaseButton, () -> ActionPane.this.client.getMoveBuilder().EstablishBase(province.getId()) );

		actionButtonMethods.put(coupButton, () -> ActionPane.this.client.getMoveBuilder().Coup(province.getId(), actionParamInput.getValue()) );
		//actionButtons.put(invadeButton, () -> ActionPane.this.client.getMoveBuilder().Invade(province.getId()) );
		
		actionButtonCosts.put(diplomaticInfluenceButton, c -> state.getDiaDipMoveIncrement(client.getPlayer(), province.getId()) );
		actionButtonCosts.put(militaryInfluenceButton, c -> state.getDiaMilMoveIncrement() );
		actionButtonCosts.put(covertInfluenceButton, c -> state.getDiaCovMoveIncrement() );

		actionButtonCosts.put(dissidentsButton, c -> state.getFundDissidentsMoveCost() );
		actionButtonCosts.put(politicalPressureButton, c -> state.getPoliticalPressureMoveCost() );
		actionButtonCosts.put(establishBaseButton, c -> state.getEstablishBaseMoveCost() );

		actionButtonCosts.put(coupButton, c -> state.getCoupMoveCost(province.getId()) );
		//actionButtons.put(invadeButton, () -> ActionPane.this.client.getMoveBuilder().Invade(province.getId()) );
		
		submitButton = new DynamicButton(this.client, c -> !(c.getMoveBuilder().getComputedGameState().hasActed(province.getId()) || (selected == null)), "Submit", this.skin);
		
		diplomaticInfluenceButton.addListener(new ChangeListener() {
			@Override
			public void changed(final ChangeEvent event, final Actor actor) {
				Logger.Info("\"Diplomatic Outreatch\" button pressed on " + province.getId().getValueDescriptor().getName());
				buttonSelect(diplomaticInfluenceButton);
				requiresSlider = true;
				actionParamInput.setBounds(state.getDiaDipMoveMin(client.getPlayer(), province.getId()), 
						state.getDiaDipMoveMax(client.getPlayer(), province.getId()),
						state.getDiaDipMoveIncrement(client.getPlayer(), province.getId()));
			}
		});
		
		militaryInfluenceButton.addListener(new ChangeListener() {
			@Override
			public void changed(final ChangeEvent event, final Actor actor) {
				Logger.Info("\"Arms Sales\" button pressed on " + province.getId().getValueDescriptor().getName());
				buttonSelect(militaryInfluenceButton);
				requiresSlider = true;
				actionParamInput.setBounds(state.getDiaMilMoveMin(), 
						state.getDiaMilMoveMax(client.getPlayer(), province.getId()),
						state.getDiaMilMoveIncrement());
			}
		});
		
		covertInfluenceButton.addListener(new ChangeListener() {
			@Override
			public void changed(final ChangeEvent event, final Actor actor) {
				Logger.Info("\"Support Party\" button pressed on " + province.getId().getValueDescriptor().getName());
				buttonSelect(covertInfluenceButton);
				requiresSlider = true;
				actionParamInput.setBounds(state.getDiaCovMoveMin(), 
						state.getDiaCovMoveMax(client.getPlayer(), province.getId()),
						state.getDiaCovMoveIncrement());
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
		
		establishBaseButton.addListener(new ChangeListener() {
			@Override
			public void changed(final ChangeEvent event, final Actor actor) {
				Logger.Info("\"Establish Base\" button pressed on " + province.getId().getValueDescriptor().getName());
				buttonSelect(establishBaseButton);
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
		DynamicLabel costLabel = new DynamicLabel(client, c -> requiresSlider ? "Cost: " + actionParamInput.getValue() + " " : selected != null ? "Cost: " + actionButtonCosts.get(selected).apply(client) + " " : "", skin);

		innerConfirm.add(submitButton).left();
		innerConfirm.add(actionParamLabel).expand().right();
		innerConfirm.add(actionParamInput).left();
		innerConfirm.add(costLabel).expand().right();
		
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
		innerBottom.add(new Label("Adjacencies:", this.skin));
		DynamicLabel adjacencyList = new DynamicLabel(this.client,
				c -> getFormattedAdjacencies(province),
				c -> state.isInRange(c.getPlayer(), province.getId()) ? Color.BLACK : Color.RED,
				this.skin);
		innerBottom.add(adjacencyList)
			.size(0, 0)
			.left()
			.padLeft(10);
		innerBottom.row();
		innerBottom.add(new Label("Leader:", this.skin));
		innerBottom.add(new DynamicLabel(this.client, c -> getFormattedLeader(province), this.skin))
			.size(0, 0)
			.padLeft(10)
			.left();
		innerBottom.row();
		
		this.add(innerConfirm).fill();
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
		for(Province.Id id : province.getAdjacencyList()) {
			count++;
			ret += client.getMoveBuilder().getComputedGameState().provinceSettings.get(id).getLabel();
			if(count < max) ret += ", ";
		}
		return ret;
	}
	
	protected String getFormattedLeader(final ProvinceSettings province) {
		ComputedGameState state = client.getMoveBuilder().getComputedGameState();
		if (state.leaders.containsKey(province.getId())) {
			Leader l = state.leaders.get(province.getId());
			String ret = l.getName();
			ret += " (" + (state.year - l.getBirth()) + ") ";
			switch (l.getType()) {
				case POLITICAL:
					ret += "+" + Settings.getConstInt("leader_income_pol") + " POL";
					break;
				case MILITARY:
					ret += "+" + Settings.getConstInt("leader_income_mil") + " MIL";
					break;
				case COVERT:
					ret += "+" + Settings.getConstInt("leader_income_cov") + " COV";
					break;
				default:
					break;
			}
			return ret;
		}
		else
			return "None";
	}
}
