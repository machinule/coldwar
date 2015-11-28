package coldwar.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;

import coldwar.Logger;
import coldwar.Settings;
import coldwar.ProvinceOuterClass.Province;
import coldwar.logic.Client;

public class ProvinceInfoCard extends Table {

	protected Client client;
	protected Province province;
	protected Skin skin;
	protected ActionPane toolbar;

	public ProvinceInfoCard(final Client client, final Province province, final ActionPane toolbar,
			final Skin skin) {
		super();
		this.client = client;
		this.province = province;
		this.skin = skin;
		
		final Button infoBox = createLayout();
		infoBox.addCaptureListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, final Actor actor) {
				Logger.Info(province.getId().getValueDescriptor().getName() + " selected");
				toolbar.onSelect(province);
			}
		});
		this.add(infoBox).size(180, 50);
	}
	
	protected Button createLayout() {
		Button ret = new Button(this.skin);
		//ret.setDebug(Settings.isDebug());
		DynamicLabel influence = new DynamicLabel(client, c -> Integer.toString(Math.abs(c.getMoveBuilder().getInfluence(province.getId()))), c -> c.getMoveBuilder().getInfluence(province.getId()) > 0 ? Color.BLUE : c.getMoveBuilder().getInfluence(province.getId()) < 0 ? Color.RED : Color.BLACK, skin);
		DynamicLabel stability = new DynamicLabel(client, c -> netStability(), skin);
		DynamicLabel name = new DynamicLabel(client, c -> province.getLabel(), skin);
		
		name.setAlignment(1); //Center in cell
		stability.setFontScale((float)1.5);
		influence.setFontScale((float)1.5);
		
		DynamicLabel modifiers = new DynamicLabel(client, c -> getModifiers(), skin);
		
		//modifiers.setFontScale((float)0.75);
		modifiers.setAlignment(1); //Center in cell
		
		ret.add(influence).left().expand();
		ret.add(name).center();
		ret.add(stability).right().expand();
		ret.row();
		ret.add();
		ret.add(modifiers).center();
		ret.row();
		return ret;
	}
	
    protected String netStability() {
    	int netStab = province.getStability() + client.getMoveBuilder().getStabilityModifier(province.getId());
		String ret = netStab + " ";
		return ret;
	}
    
    protected String getModifiers() {
    	String ret = " ";
    	if(province.getDissidents()) {
    		ret += "D ";
    	}
    	return ret;
    }

}