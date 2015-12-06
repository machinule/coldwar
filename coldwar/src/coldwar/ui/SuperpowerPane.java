package coldwar.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import coldwar.logic.Client;
import coldwar.logic.MoveBuilder;
import coldwar.logic.Client.Player;

public class SuperpowerPane extends Table {
	
	private final Client client;
	protected MoveBuilder moveBuilder;
	protected Skin skin;
	
	protected String playerLabelText;
	
	public SuperpowerPane(final Client client, final Skin skin) {
		super();
		this.client = client;
		this.skin = skin;
		
		playerLabelText = "National Actions";
	}
	
	public void show() {
		DynamicLabel playerLabel = new DynamicLabel(this.client, c -> playerLabelText, skin);
		
		final DynamicButton ciaButton = new DynamicButton(this.client, c -> c.getMoveBuilder().getComputedGameState().isValidFoundCIAMove(), c -> c.getPlayer() == Player.USA, c -> "Found CIA", this.skin);
		ciaButton.addListener(new ChangeListener() {
			@Override
			public void changed(final ChangeEvent event, final Actor actor) {
				client.getMoveBuilder().foundCIA();
			}
		});
		
		final DynamicButton kgbButton = new DynamicButton(this.client, c -> c.getMoveBuilder().getComputedGameState().isValidFoundKGBMove(), c -> c.getPlayer() == Player.USSR, c -> "Found KGB", this.skin);
		kgbButton.addListener(new ChangeListener() {
			@Override
			public void changed(final ChangeEvent event, final Actor actor) {
				client.getMoveBuilder().foundKGB();
			}
		});

		this.clearChildren();
		this.add(playerLabel);
		this.row();
		this.add(ciaButton);
		this.row();
		this.add(kgbButton);
		this.row();
	}
}
