package coldwar;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;

import coldwar.ProvinceOuterClass.Province;

public class Toolbar extends Table {

	protected MoveBuilder moveBuilder;
	protected Skin skin;
	
	public Toolbar(MoveBuilder moveBuilder, Skin skin) {
		super();
		this.moveBuilder = moveBuilder;
		this.skin = skin;
		this.setDebug(true);
	}
	
	public void onSelect(Province province) {
		TextButton increaseButton;
		TextButton decreaseButton;
		TextButton dissidentsButton;
		
		increaseButton = new TextButton("+", skin);
        decreaseButton = new TextButton("-", skin);
        dissidentsButton = new TextButton("Fund Dissidents", skin);
        
        decreaseButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
            	Logger.Info("\"Reduce\" button pressed on " + province.getId().getValueDescriptor().getName());
            	moveBuilder.DecreaseInfluence(province.getId());
            	/* if(!moveBuilder.CanDecreaseInfluence(province.getId())) {
            		actor.setVisible(false);
            	}
            	increaseButton.setVisible(true); */
            }
        });
        
        increaseButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
            	Logger.Info("\"Increase\" button pressed on " + province.getId().getValueDescriptor().getName());
            	moveBuilder.IncreaseInfluence(province.getId());
            	/* if(!moveBuilder.CanIncreaseInfluence(province.getId())) {
            		actor.setVisible(false);
            	}
            	decreaseButton.setVisible(true); */
            }
        });
        
        dissidentsButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
            	Logger.Info("\"Dissidents\" button pressed on " + province.getId().getValueDescriptor().getName());
            	moveBuilder.FundDissidents(province.getId());
            	/* if(!moveBuilder.CanDecreaseInfluence(province.getId())) {
            		actor.setVisible(false);
            	}
            	increaseButton.setVisible(true); */
            }
        });
		
		this.clearChildren();
		this.add(new Label(province.getId().getValueDescriptor().getName(), this.skin));this.row();
        
		this.add(increaseButton);
        this.add(decreaseButton);
        this.add(dissidentsButton);
		this.add(new Label("Influence:", this.skin));
		this.add(new InfluenceLabel(province.getId(), moveBuilder, this.skin));
		this.add(new Label("Dissidents:", this.skin));
		this.add(new DissidentsLabel(province.getId(), moveBuilder, this.skin));
	}
		
	
}
