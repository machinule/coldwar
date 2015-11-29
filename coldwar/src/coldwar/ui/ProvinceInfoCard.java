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

import coldwar.GameStateOuterClass.ProvinceSettings;
import coldwar.Logger;
import coldwar.Settings;
import coldwar.ProvinceOuterClass.Province;
import coldwar.ProvinceOuterClass.Province.Region;
import coldwar.logic.Client;
import coldwar.logic.Client.Player;

public class ProvinceInfoCard extends Table {

	protected Client client;
	protected ProvinceSettings province;
	protected Skin skin;
	protected ActionPane toolbar;
	protected Button infoBox;
	static ProvinceInfoCard currentSelection;
	protected Color color; // To be replaced with region color

	public ProvinceInfoCard(final Client client, final ProvinceSettings province, final ActionPane toolbar,
			final Skin skin) {
		super();
		this.client = client;
		this.province = province;
		this.skin = skin;
		color = getRegionColor(client.getMoveBuilder().getComputedGameState().regions.get(province.getId()));
		
		infoBox = createLayout();
		infoBox.setColor(color);
		infoBox.addCaptureListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, final Actor actor) {
				Logger.Info(province.getId().getValueDescriptor().getName() + " selected");
				toolbar.onSelect(province);
				if (currentSelection != null) {
					currentSelection.infoBox.setColor(currentSelection.color);
				}
				currentSelection = ProvinceInfoCard.this;
				currentSelection.infoBox.setColor(new Color(1, 1, 1, 1));
			}
		});
		this.add(infoBox).size(180, 40);
	}
	
	protected Button createLayout() {
		Button ret = new Button(this.skin);
		//ret.setDebug(Settings.isDebug());
		DynamicLabel influence = new DynamicLabel(client, c -> Integer.toString(Math.abs(c.getMoveBuilder().getInfluence(province.getId()))), c -> c.getMoveBuilder().getInfluence(province.getId()) > 0 ? Color.BLUE : c.getMoveBuilder().getInfluence(province.getId()) < 0 ? Color.RED : Color.BLACK, skin);
		DynamicLabel stability = new DynamicLabel(client, c -> netStability(), skin);
		DynamicLabel name = new DynamicLabel(client, c -> province.getLabel(), c -> c.getMoveBuilder().getComputedGameState().getAlly(province.getId()) == Player.USA ? Color.BLUE : c.getMoveBuilder().getComputedGameState().getAlly(province.getId()) == Player.USSR ? Color.RED : Color.BLACK, skin);
		
		name.setAlignment(1); //Center in cell
		name.setFontScale((float)0.75);
		stability.setFontScale((float)1.5);
		influence.setFontScale((float)1.5);
		
		DynamicLabel modifiers = new DynamicLabel(client, c -> getModifiers(), skin);
		
		modifiers.setFontScale((float)0.75);
		modifiers.setAlignment(1); //Center in cell
		
		ret.add(influence).left().padTop(12).expand();
		ret.add(name).center();
		ret.add(stability).right().padTop(12).expand();
		ret.row();
		ret.add();
		ret.add(modifiers).center();
		ret.row();
		return ret;
	}
	
    protected String netStability() {
    	int netStab = province.getStabilityBase() + client.getMoveBuilder().getStabilityModifier(province.getId());
		String ret = netStab + " ";
		return ret;
	}
    
    protected String getModifiers() {
    	String ret = " ";
    	if(client.getMoveBuilder().hasDissidents(province.getId())) {
    		ret += "DISS ";
    	}
    	if(client.getMoveBuilder().getBaseOwner(province.getId()) == Player.USA) {
    		ret += "USA-B ";
    	}
    	if(client.getMoveBuilder().getBaseOwner(province.getId()) == Player.USSR) {
    		ret += "USSR-B ";
    	}
    	return ret;
    }
    
    protected Color getRegionColor(Region region) {
    	Color ret;
    	switch (region) {
	        case CENTRAL_AMERICA:
	        	ret = new Color(0, (float)201/255, (float)201/255, 1);
	            break;
	        case SOUTH_AMERICA:
	        	ret = new Color((float)51/255, (float)201/255, (float)0/255, 1);
	            break;
	        default:
	        	ret = Color.BLACK;
	        	break;
    	}
    	return ret;
    }
}