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
	protected ColdWarGame game;
	
	public Toolbar(ColdWarGame game, MoveBuilder moveBuilder, Skin skin) {
		super();
		this.game = game;
		this.moveBuilder = moveBuilder;
		this.skin = skin;
		this.setDebug(true);
	}
	
	public void onSelect(Province province) {
		TextButton increaseButton;
		TextButton decreaseButton;
		
		increaseButton = new TextButton("+", skin);
        decreaseButton = new TextButton("-", skin);
        
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
        
        TextButton endTurnButton = new TextButton("end Turn", skin);
        endTurnButton.addListener(new ChangeListener() {
        	public void changed (ChangeEvent event, Actor actor) {
        		Logger.Info("\"CEnd Turn\" button pressed.");
        		game.endTurn(moveBuilder);
        	}
        });
        
		this.clearChildren();
		this.add(endTurnButton);
		this.add(new Label(province.getId().getValueDescriptor().getName(), this.skin));this.row();
        
        this.add(decreaseButton);
		this.add(new Label("Influence:", this.skin));
		this.add(new InfluenceLabel(province.getId(), moveBuilder, this.skin));
        
        this.add(increaseButton);
	}
		
	
}
