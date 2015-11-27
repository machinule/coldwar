package coldwar.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import coldwar.Logger;
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

		final TextButton infoButton = new TextButton(province.getLabel() + " | " + province.getStability() + " ", skin);
		infoButton.addListener(new ChangeListener() {
			@Override
			public void changed(final ChangeEvent event, final Actor actor) {
				Logger.Info(province.getLabel() + " button pressed on " + province.getId().getValueDescriptor().getName());
				toolbar.onSelect(province);
			}
		});
		this.add(infoButton);
		this.add(new DynamicLabel(this.client, c -> Integer.toString(c.getMoveBuilder().getStabilityModifier(this.province.getId())), this.skin));
	}

}
