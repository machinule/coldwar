package coldwar.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import coldwar.Logger;
import coldwar.logic.Client;
import coldwar.logic.MoveBuilder;
import coldwar.logic.Client.Player;

public class HeaderPane extends Table {

	private final Client client;
	protected MoveBuilder moveBuilder;
	protected Skin skin;

	public HeaderPane(final Client client, final Skin skin) {
		super();
		this.client = client;
		this.skin = skin;
	}
	
	public void show() {
		DynamicLabel playerLabel = new DynamicLabel(this.client, c -> c.getPlayer() == Player.USA ? "USA" : "USSR", this.skin);
		DynamicLabel yearLabel = new DynamicLabel(this.client, c -> Integer.toString(c.getMoveBuilder().getYear()), this.skin);
		
		final TextButton endTurnButton = new TextButton("end Turn", this.skin);
		endTurnButton.addListener(new ChangeListener() {
			@Override
			public void changed(final ChangeEvent event, final Actor actor) {
				Logger.Info("\"End Turn\" button pressed.");
				HeaderPane.this.client.endTurn();
			}
		});

		this.clearChildren();
		this.add(yearLabel);
		this.add(playerLabel);
		this.add(endTurnButton);
		this.row();
	}
}
