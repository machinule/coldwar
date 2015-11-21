package coldwar;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import coldwar.ProvinceOuterClass.Province;


public class ProvinceInfoCard extends Table {

	protected Province province;
	protected MoveBuilder moveBuilder;
	protected Skin skin;
	
	public ProvinceInfoCard(Province province, MoveBuilder moveBuilder, Skin skin) {
		super();
		this.province = province;
		this.moveBuilder = moveBuilder;
		this.skin = skin;
		this.setDebug(true);
		
		this.add(new Label(province.getId().getValueDescriptor().getName(), this.skin));
		this.row();
        TextButton reduceButton = new TextButton("-", skin);
        reduceButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
            	Logger.Info("\"Reduce\" button pressed on " + province.getId().getValueDescriptor().getName());
            	moveBuilder.DecreaseInfluence(province.getId());
            }
        });
        this.add(reduceButton);
		this.add(new Label("Influence:", this.skin));
		this.add(new InfluenceLabel(province.getId(), moveBuilder, this.skin));
        TextButton increaseButton = new TextButton("+", skin);
        increaseButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
            	Logger.Info("\"Increase\" button pressed on " + province.getId().getValueDescriptor().getName());
            	moveBuilder.IncreaseInfluence(province.getId());
            }
        });
        this.add(increaseButton);
	}
	
	
}
