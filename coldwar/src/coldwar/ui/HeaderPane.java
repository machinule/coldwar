package coldwar.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;

import coldwar.ColdWarGame;
import coldwar.Logger;
import coldwar.ProvinceOuterClass.Province;
import coldwar.logic.MoveBuilder;

public class HeaderPane extends Table {

	private final ColdWarGame game;
	protected MoveBuilder moveBuilder;
	protected Skin skin;

	public HeaderPane(final ColdWarGame game, final MoveBuilder moveBuilder, final Skin skin) {
		super();
		this.game = game;
		this.moveBuilder = moveBuilder;
		this.skin = skin;
		this.setDebug(true);
	}
	
	public void show() {
		final TextButton endTurnButton = new TextButton("end Turn", this.skin);
		endTurnButton.addListener(new ChangeListener() {
			@Override
			public void changed(final ChangeEvent event, final Actor actor) {
				Logger.Info("\"CEnd Turn\" button pressed.");
				HeaderPane.this.game.endTurn(HeaderPane.this.moveBuilder);
			}
		});

		this.clearChildren();
		this.add(endTurnButton);
		this.row();
	}
}