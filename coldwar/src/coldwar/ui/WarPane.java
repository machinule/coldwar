package coldwar.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

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
		
		ProgressBar warProgress = new ProgressBar(-5, 5, 1, false, skin);
		warProgress.setValue(0);
		
		DynamicLabel progressTitle = new DynamicLabel(client,
				c -> "War Progress:",
				c -> Color.ORANGE, 
				skin);
		
		Label defenders = new Label("Defenders", skin);
		defenders.setColor(Color.BLUE);
		
		Label attackers = new Label("Attackers", skin);
		attackers.setColor(Color.RED);
		
		warHeader.add(progressTitle)
			.center();
		warHeader.row();
		warHeader.add(warProgress);
		
		Table warFooter = new Table();
		
		warFooter.add(attackers)
			.center();
		warFooter.add(defenders)
			.center();
		
		innerWar.add(warHeader);
		innerWar.row();
		innerWar.add(warFooter)
			.size(500, 100);
		
		this.add(innerWar);
		this.row();
	}
}
