package coldwar.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import coldwar.DissidentsOuterClass.Government;
import coldwar.GameSettingsOuterClass.ProvinceSettings;
import coldwar.logic.Client;

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
				c -> c.getMoveBuilder().getComputedGameState().activeConflicts.get(province.getId()).getRebels().getGov() == Government.DEMOCRACY ? "Freedom Fighters" :
					 c.getMoveBuilder().getComputedGameState().activeConflicts.get(province.getId()).getRebels().getGov() == Government.COMMUNISM ? "Communist Rebels" :
					 "Unaligned Rebels",
				c -> c.getMoveBuilder().getComputedGameState().activeConflicts.get(province.getId()).getRebels().getGov() == Government.DEMOCRACY ? Color.BLUE :
					 c.getMoveBuilder().getComputedGameState().activeConflicts.get(province.getId()).getRebels().getGov() == Government.COMMUNISM ? Color.RED :
					 Color.BLACK, skin); 
		
		warHeader.row();
		warHeader.add(progressTitle)
			.center();

		
		Table warFooter = new Table();

		warFooter.add(attackerInfo)
			.center()
			.expand();
		warFooter.add(versus)
			.padLeft(10)
			.padRight(10)
			.center();
		warFooter.add(defenderInfo)
			.center()
			.expand();
		
		innerWar.add(warHeader);
		innerWar.row();
		innerWar.add(warFooter)
			.size(500, 100);
		
		this.add(innerWar);
		this.row();
	}
}
