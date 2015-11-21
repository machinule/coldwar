package coldwar;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;

import coldwar.ProvinceOuterClass.Province;


public class ProvinceInfoCard extends Table {

	protected Province province;
	protected MoveBuilder moveBuilder;
	protected Skin skin;
	protected Toolbar toolbar;
	
	public ProvinceInfoCard(Province province, MoveBuilder moveBuilder, Toolbar toolbar, Skin skin) {
		super();
		this.province = province;
		this.moveBuilder = moveBuilder;
		this.skin = skin;
		this.setDebug(true);
		
		TextButton infoButton = new TextButton("INFO", skin);
		
		this.add(new Label(province.getId().getValueDescriptor().getName(), this.skin));
		infoButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
            	Logger.Info("\"Info\" button pressed on " + province.getId().getValueDescriptor().getName());
            	toolbar.onSelect(province);
            }
        });
		this.add(infoButton);
	}
	
	
}
