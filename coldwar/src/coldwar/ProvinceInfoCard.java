package coldwar;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import coldwar.ProvinceOuterClass.Province;


public class ProvinceInfoCard extends Group {

	protected Province province;
	protected Table table;
	protected MoveBuilder moveBuilder;
	protected Skin skin;
	
	public ProvinceInfoCard(Province province, MoveBuilder moveBuilder, Skin skin) {
		this.province = province;
		this.moveBuilder = moveBuilder;
		this.skin = skin;
		this.table = new Table();
		this.table.setFillParent(true);
		this.table.setDebug(true);
		addActor(this.table);
		
		this.table.add(new Label(province.getId().getValueDescriptor().getName(), this.skin));
		this.table.row();
        TextButton reduceButton = new TextButton("-", skin);
        reduceButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
            	Logger.Info("\"Reduce\" button pressed on " + province.getId().getValueDescriptor().getName());
            }
        });
        this.table.add(reduceButton);
		this.table.add(new Label("Influence: 0", this.skin));
        TextButton increaseButton = new TextButton("+", skin);
        increaseButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
            	Logger.Info("\"Increase\" button pressed on " + province.getId().getValueDescriptor().getName());
            }
        });
        this.table.add(increaseButton);
	}
	
	
}
