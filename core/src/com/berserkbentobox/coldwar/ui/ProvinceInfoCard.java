package com.berserkbentobox.coldwar.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Tooltip;
import com.badlogic.gdx.scenes.scene2d.ui.TooltipManager;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import com.berserkbentobox.coldwar.DissidentsOuterClass.Government;
import com.berserkbentobox.coldwar.Province.ProvinceSettings;
import com.berserkbentobox.coldwar.Logger;
import com.berserkbentobox.coldwar.Conflict.ConflictType;
import com.berserkbentobox.coldwar.Id.ProvinceRegion;
import com.berserkbentobox.coldwar.Id.ProvinceId;
import com.berserkbentobox.coldwar.logic.Client;
import com.berserkbentobox.coldwar.logic.Client.Player;

public class ProvinceInfoCard extends Table {

	protected Client client;
	protected ProvinceSettings province;
	protected Skin skin;
	protected ActionPane actionPane;
	protected WarPane warPane;
	protected Button infoBox;
	static ProvinceInfoCard currentSelection;
	protected Color color;
	protected boolean paneSwitch;
	

	public boolean inConflict = false;
	
	public ProvinceInfoCard(final Client client,
							final ProvinceSettings province,
							final ActionPane actionPane,
							final WarPane warPane,
							final Skin skin) {
		super();
		this.client = client;
		this.province = province;
		this.skin = skin;
		this.actionPane = actionPane;
		this.warPane = warPane;
		
		infoBox = createLayout();
		infoBox.setColor(getValidColor());
		infoBox.addCaptureListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, final Actor actor) {
				Logger.Info(province.getId().getValueDescriptor().getName() + " selected");
				if(client.getMoveBuilder().getComputedGameState().isInArmedConflict(province.getId())) {
					warPane.onSelect(province);
					actionPane.clear();
				} else {
					actionPane.onSelect(province);
					warPane.clear();
				}
				if (currentSelection != null) {
					currentSelection.infoBox.setColor(currentSelection.getValidColor());
				}
				currentSelection = ProvinceInfoCard.this;
				currentSelection.infoBox.setColor(new Color(1, 1, 1, 1));
			}
		});
		TooltipManager manager = new TooltipManager();
		manager.initialTime = 0;
		manager.subsequentTime = 0;
		manager.resetTime = 0;
		manager.animations = false;
		manager.hideAll();
		Table test = new Table();
		test.add(new Label("This is", skin));
		test.row();
		test.add(new Label("But a test", skin));
		infoBox.addCaptureListener(new Tooltip<Table>(test, manager));
		this.add(infoBox).size(180, 40);
	}
	
	@Override
	public void act(final float delta) {
		super.act(delta);
		infoBox.setColor(getValidColor());
		if(currentSelection == this) {
			boolean currentSwitch = client.getMoveBuilder().getComputedGameState().isInArmedConflict(province.getId());
			if (paneSwitch != currentSwitch) {
				if(currentSwitch) {
					warPane.onSelect(province);
					actionPane.clear();
				} else {
					actionPane.onSelect(province);
					warPane.clear();
				}
				paneSwitch = currentSwitch;
			}
		}
	}
	
	public Color getValidColor() {
//		if(client.getMoveBuilder().getComputedGameState().isInRange(client.getPlayer(), province.getId()))
			return getRegionColor(province.getRegion());
//		else
//			return getNonAdjColor(province.getRegion());
	}
	
	protected Button createLayout() {
		Button ret = new Button(this.skin);
		//ret.setDebug(Settings.isDebug());
		DynamicLabel influence = new DynamicLabel(client,
				c -> Integer.toString(Math.abs(c.getMoveBuilder().getInfluence(province.getId()))),
				c -> c.getMoveBuilder().getInfluence(province.getId()) > 0 ? Color.BLUE : c.getMoveBuilder().getInfluence(province.getId()) < 0 ? Color.RED : Color.BLACK,
				skin);
		DynamicLabel stability = new DynamicLabel(client, c -> netStability(), skin);
		DynamicLabel name = new DynamicLabel(client,
				c -> province.getLabel(),
				c -> c.getMoveBuilder().getAlly(province.getId()) == Player.USA ? Color.BLUE :
					 c.getMoveBuilder().getAlly(province.getId()) == Player.USSR ? Color.RED :
					 Color.BLACK,
					 skin);
		
		name.setAlignment(1); //Center in cell
		name.setFontScale((float)0.75);
		stability.setFontScale((float)1.5);
		influence.setFontScale((float)1.5);
		
		Table modifiers = getModifiers();
		
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
    	if(client.getMoveBuilder().getComputedGameState().isInArmedConflict(province.getId())) {
    		return "X ";
    	} else {
	    	int netStab = client.getMoveBuilder().getComputedGameState().getNetStability(province.getId());
			String ret = netStab + " ";
			return ret;
    	}
	}
    
    protected Table getModifiers() {
    	Table ret = new Table();
		ret.add(new DynamicLabel(
			client,
			c -> c.getMoveBuilder().getMechanics().getProvinces().hasDissidents(province.getId()) ? "DISS" : "",
			c -> c.getMoveBuilder().getMechanics().getProvinces().getDissidentsGovernment(province.getId()) == Government.COMMUNISM ? Color.RED :
				 c.getMoveBuilder().getMechanics().getProvinces().getDissidentsGovernment(province.getId()) == Government.DEMOCRACY ? Color.BLUE :
			 	 Color.BLACK,
			 	 skin
		));
		ret.add(new DynamicLabel(
				client,
				c -> c.getMoveBuilder().getMechanics().getProvinces().hasSpyNetwork(province.getId()) ? "SPY" : "",
				c -> c.getMoveBuilder().getMechanics().getProvinces().getSpyNetworkOwner(province.getId()) == c.getPlayer() ? Color.YELLOW :
				Color.CLEAR,
				skin
		));
		// TODO: Unique proto for each government to include string label?
		ret.add(new DynamicLabel(
				client,
				c -> c.getMoveBuilder().getComputedGameState().isInArmedConflict(province.getId()) &&
					c.getMoveBuilder().getComputedGameState().activeConflicts.get(province.getId()).getType() == ConflictType.CIVIL_WAR ? "CIVIL WAR" :
				c.getMoveBuilder().getComputedGameState().isInArmedConflict(province.getId()) &&
					c.getMoveBuilder().getComputedGameState().activeConflicts.get(province.getId()).getType() == ConflictType.COLONIAL_WAR ? "COLONIAL WAR" :
				c.getMoveBuilder().getComputedGameState().isInArmedConflict(province.getId()) &&
					c.getMoveBuilder().getComputedGameState().activeConflicts.get(province.getId()).getType() == ConflictType.MILITARY_ACTION ? "MILITARY ACTION" :
				c.getMoveBuilder().getComputedGameState().isInArmedConflict(province.getId()) &&
					c.getMoveBuilder().getComputedGameState().activeConflicts.get(province.getId()).getType() == ConflictType.CONVENTIONAL_WAR ? "ACTIVE CONFLICT" :
					"",
				c -> Color.ORANGE,
				skin
		));
		ret.add(new DynamicLabel(
				client,
				c -> c.getMoveBuilder().getComputedGameState().governments.get(province.getId()) == Government.DEMOCRACY ? "DEM" :
					 c.getMoveBuilder().getComputedGameState().governments.get(province.getId()) == Government.AUTOCRACY ? "AUT" :
					 c.getMoveBuilder().getComputedGameState().governments.get(province.getId()) == Government.COMMUNISM ? "COM" :
					 c.getMoveBuilder().getComputedGameState().governments.get(province.getId()) == Government.COLONY ? "COLONY" :
					 c.getMoveBuilder().getComputedGameState().governments.get(province.getId()) == Government.OCCUPIED ? "OCCUPIED" :
					 "",
				c -> c.getMoveBuilder().getComputedGameState().governments.get(province.getId()) == Government.DEMOCRACY ? Color.BLUE :
					 c.getMoveBuilder().getComputedGameState().governments.get(province.getId()) == Government.COMMUNISM ? Color.RED :
					 c.getMoveBuilder().getComputedGameState().governments.get(province.getId()) == Government.OCCUPIED &&
					 	c.getMoveBuilder().getComputedGameState().occupiers.get(province.getId()) == ProvinceId.USA ? Color.BLUE :
					 c.getMoveBuilder().getComputedGameState().governments.get(province.getId()) == Government.OCCUPIED &&
					 	c.getMoveBuilder().getComputedGameState().occupiers.get(province.getId()) == ProvinceId.USSR ? Color.RED :
					 Color.BLACK,
				skin
		));
		ret.add(new DynamicLabel(
				client,
				c -> c.getMoveBuilder().getComputedGameState().hasLeader(province.getId()) ? "LDR" :
					 "",
				c -> Color.BLACK,
				skin
		));
		return ret;
    }
    
    protected Color getRegionColor(ProvinceRegion region) {
    	Color ret;
    	switch (region) {
	        case CENTRAL_AMERICA:
	        	ret = new Color(0, (float)201/255, (float)170/255, 1);
	            break;
	        case SOUTH_AMERICA:
	        	ret = new Color((float)51/255, (float)201/255, (float)0/255, 1);
	            break;
	        case WESTERN_EUROPE:
	        	ret = new Color(0, (float)155/255, (float)201/255, 1);
	            break;
	        case EASTERN_EUROPE:
	        	ret = new Color((float)201/255, (float)101/255, (float)101/255, 1);
	            break;
	        case MIDDLE_EAST:
	        	ret = new Color((float)201/255, (float)201/255, (float)0/255, 1);
	            break;
	        case SOUTH_ASIA:
	        	ret = new Color((float)101/255, (float)201/255, (float)201/255, 1);
	            break;
	        case EAST_ASIA:
	        	ret = new Color((float)201/255, (float)101/255, (float)201/255, 1);
	            break;
	        default:
	        	ret = Color.BLUE;
	        	break;
    	}
    	return ret;
    }
    
    protected Color getNonAdjColor(ProvinceRegion region) {
    	Color ret;
    	switch (region) {
	        case CENTRAL_AMERICA:
	        	ret = new Color(0, (float)170/255, (float)153/255, 1);
	            break;
	        case SOUTH_AMERICA:
	        	ret = new Color((float)51/255, (float)153/255, (float)0/255, 1);
	            break;	        
	        case WESTERN_EUROPE:
	        	ret = new Color(0, (float)100/255, (float)170/255, 1);
		        break;
	        case EASTERN_EUROPE:
	        	ret = new Color((float)201/255, (float)51/255, (float)51/255, 1);
	        	break;
	        case MIDDLE_EAST:
	        	ret = new Color((float)151/255, (float)151/255, (float)0/255, 1);
	            break;
	        case SOUTH_ASIA:
	        	ret = new Color((float)51/255, (float)151/255, (float)151/255, 1);
	            break;
	        case EAST_ASIA:
	        	ret = new Color((float)151/255, (float)51/255, (float)151/255, 1);
	            break;
	        default:
	        	ret = Color.BLACK;
	        	break;
    	}
    	return ret;
    }
}