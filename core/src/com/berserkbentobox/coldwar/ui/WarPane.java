package com.berserkbentobox.coldwar.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;

import com.berserkbentobox.coldwar.Logger;
import com.berserkbentobox.coldwar.DissidentsOuterClass.Government;
import com.berserkbentobox.coldwar.GameSettingsOuterClass.ProvinceSettings;
import com.berserkbentobox.coldwar.ProvinceOuterClass.Province;
import com.berserkbentobox.coldwar.logic.Client;
import com.berserkbentobox.coldwar.logic.Client.Player;

public class WarPane extends FooterPane {

	DynamicButton selected;
	boolean requiresSlider;
    
	public WarPane(final Client client, final Skin skin) {
		super(client, skin);
	}

	@Override
	public void onSelect(final ProvinceSettings province) {
		this.clearChildren();
		
		Table innerWar = new Table();
		
		Table warHeader = new Table();
		
		DynamicLabel progressTitle = new DynamicLabel(client,
				c -> "War!",
				c -> Color.ORANGE, 
				skin);
		DynamicLabel year = new DynamicLabel(client,
				c -> "Started: " + (c.getMoveBuilder().getComputedGameState().year - c.getMoveBuilder().getComputedGameState().activeConflicts.get(province.getId()).getLength() - 1), 
				c -> Color.BLACK,
				skin);
		DynamicLabel goal = new DynamicLabel(client,
				c -> "War Goal: " + c.getMoveBuilder().getComputedGameState().activeConflicts.get(province.getId()).getGoal(), 
				c -> Color.BLACK,
				skin);
		
		Label versus = new Label("versus", skin);
		versus.setColor(Color.BLACK);
		DynamicLabel defenderInfo = new DynamicLabel(client, 
				c -> province.getLabel(),
				c -> c.getMoveBuilder().getComputedGameState().governments.get(province.getId()) == Government.DEMOCRACY ? Color.BLUE :
					 c.getMoveBuilder().getComputedGameState().governments.get(province.getId()) == Government.COMMUNISM ? Color.RED :
					 Color.BLACK, skin); 
		
		Label attackers = new Label("Attackers", skin);
		attackers.setColor(Color.BLACK);
		DynamicLabel attackerInfo = new DynamicLabel(client, 
				c -> c.getMoveBuilder().getComputedGameState().activeConflicts.get(province.getId()).getRebels().getGov() == Government.DEMOCRACY ? "Pro-Democracy Fighters" :
					 c.getMoveBuilder().getComputedGameState().activeConflicts.get(province.getId()).getRebels().getGov() == Government.COMMUNISM ? "Communist Guerillas" :
					 "Unaligned Rebels",
				c -> c.getMoveBuilder().getComputedGameState().activeConflicts.get(province.getId()).getRebels().getGov() == Government.DEMOCRACY ? Color.BLUE :
					 c.getMoveBuilder().getComputedGameState().activeConflicts.get(province.getId()).getRebels().getGov() == Government.COMMUNISM ? Color.RED :
					 Color.BLACK, skin); 
		
		DynamicLabel defProgress = new DynamicLabel(client,
				c -> c.getMoveBuilder().getComputedGameState().activeConflicts.get(province.getId()).getDefenderProgress() + "", 
				c -> Color.BLACK,
				skin);
		DynamicLabel attProgress = new DynamicLabel(client,
				c -> c.getMoveBuilder().getComputedGameState().activeConflicts.get(province.getId()).getAttackerProgress() + "", 
				c -> Color.BLACK,
				skin);
		
		DynamicLabel defChance = new DynamicLabel(client,
				c -> (c.getMoveBuilder().getComputedGameState().activeConflicts.get(province.getId()).getBaseChance()
					  + c.getMoveBuilder().getComputedGameState().activeConflicts.get(province.getId()).getDefChanceMod())/10000 + "%", 
				c -> Color.BLACK,
				skin);
		DynamicLabel attChance = new DynamicLabel(client,
				c -> (c.getMoveBuilder().getComputedGameState().activeConflicts.get(province.getId()).getBaseChance()
					  + c.getMoveBuilder().getComputedGameState().activeConflicts.get(province.getId()).getAttChanceMod())/10000 + "%", c -> Color.BLACK,
				skin);
		
		DynamicButton supportAttackerButton = new DynamicButton(client,
				c -> (c.getMoveBuilder().getComputedGameState().isValidOvertFundAttackerMove(c.getPlayer(), province.getId()) &&
				     !c.getMoveBuilder().getComputedGameState().hasActed(province.getId())),
				c -> c.getMoveBuilder().getComputedGameState().activeConflicts.get(province.getId()).getAttackerSupporter() == Province.Id.USA ? "USA Backed" :
					 c.getMoveBuilder().getComputedGameState().activeConflicts.get(province.getId()).getAttackerSupporter() == Province.Id.USSR ? "USSR Backed" :
					 "Send Military Aid",
				skin);
		DynamicButton supportDefenderButton = new DynamicButton(client,
				c -> (c.getMoveBuilder().getComputedGameState().isValidOvertFundDefenderMove(c.getPlayer(), province.getId()) &&
					  !c.getMoveBuilder().getComputedGameState().hasActed(province.getId())),
				c -> c.getMoveBuilder().getComputedGameState().activeConflicts.get(province.getId()).getDefenderSupporter() == Province.Id.USA ? "USA Backed" :
					 c.getMoveBuilder().getComputedGameState().activeConflicts.get(province.getId()).getDefenderSupporter() == Province.Id.USSR ? "USSR Backed" :
					 "Send Military Aid",
				skin);
		
		supportAttackerButton.addListener(new ChangeListener() {
			@Override
			public void changed(final ChangeEvent event, final Actor actor) {
				Logger.Info("\"Send Military Aid to Attackers\" button pressed on " + province.getId().getValueDescriptor().getName());
				WarPane.this.client.getMoveBuilder().FundAttacker(province.getId());
			}
		});

		supportDefenderButton.addListener(new ChangeListener() {
			@Override
			public void changed(final ChangeEvent event, final Actor actor) {
				Logger.Info("\"Send Military Aid to Defenders\" button pressed on " + province.getId().getValueDescriptor().getName());
				WarPane.this.client.getMoveBuilder().FundDefender(province.getId());
			}
		});
		
		warHeader.add(progressTitle)
			.center();
		warHeader.row();
		warHeader.add(goal);
		warHeader.row();
		warHeader.add(year);
		
		Table warFooter = new Table();

		warFooter.add(supportAttackerButton)
			.center()
			.padRight(10);
		warFooter.add(attackerInfo)
			.center();
		warFooter.add(versus)
			.padLeft(10)
			.padRight(10)
			.center();
		warFooter.add(defenderInfo)
			.center();
		warFooter.add(supportDefenderButton)
			.center()
			.padLeft(10);	
		warFooter.row();
		warFooter.add();
		warFooter.add(attProgress)
			.center();
		warFooter.add()
			.padLeft(10)
			.padRight(10)
			.center();
		warFooter.add(defProgress)
			.center();
		warFooter.row();
		warFooter.add();
		warFooter.add(attChance)
			.center();
		warFooter.add()
			.padLeft(10)
			.padRight(10)
			.center();
		warFooter.add(defChance)
			.center();
		warFooter.row();	
		
		innerWar.add(warHeader).center();
		innerWar.row();
		innerWar.add(warFooter)
			.center();
		
		this.add(innerWar);
		this.row();
	}
}
