package coldwar.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;

import coldwar.Logger;
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
		
		this.playerLabelText = client.getPlayer() == Player.USA ? "United States" : "Soviet Union";
	}
	
	public void show() {
		DynamicLabel playerLabel = new DynamicLabel(this.client, c -> playerLabelText, skin);
		
		final TextButton testButton = new TextButton("Destroy the world", this.skin);
		testButton.addListener(new ChangeListener() {
			@Override
			public void changed(final ChangeEvent event, final Actor actor) {
				Logger.Info("\"Destroy the world\" button pressed.");
				playerLabelText = "WHY WOULD YOU PRESS THAT?";
			}
		});

		this.clearChildren();
		this.add(playerLabel);
		this.row();
		this.add(testButton);
		this.row();
	}
}
