package coldwar.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import coldwar.Logger;
import coldwar.ProvinceOuterClass.Province;
import coldwar.logic.MoveBuilder;

public class ProvinceInfoCard extends Table {

	protected MoveBuilder moveBuilder;
	protected Province province;
	protected Skin skin;
	protected Toolbar toolbar;

	public ProvinceInfoCard(final Province province, final MoveBuilder moveBuilder, final Toolbar toolbar,
			final Skin skin) {
		super();
		this.province = province;
		this.moveBuilder = moveBuilder;
		this.skin = skin;
		this.setDebug(true);

		final TextButton infoButton = new TextButton("INFO", skin);

		this.add(new Label(province.getId().getValueDescriptor().getName(), this.skin));
		infoButton.addListener(new ChangeListener() {
			@Override
			public void changed(final ChangeEvent event, final Actor actor) {
				Logger.Info("\"Info\" button pressed on " + province.getId().getValueDescriptor().getName());
				toolbar.onSelect(province);
			}
		});
		this.add(infoButton);
	}

}
