package coldwar.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
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
		
		Table topBar = new Table();
		topBar.setDebug(true);
		
		topBar.add(new Label("POL:", this.skin));
		topBar.add(new DynamicLabel(this.client, c -> Integer.toString(c.getMoveBuilder().getPolStore()), this.skin));
		topBar.add(new Label("MIL:", this.skin));
		topBar.add(new DynamicLabel(this.client, c -> Integer.toString(c.getMoveBuilder().getMilStore()), this.skin));
		topBar.add(new Label("COV:", this.skin));
		topBar.add(new DynamicLabel(this.client, c -> Integer.toString(c.getMoveBuilder().getCovStore()), this.skin));
		topBar.add(new Label("HEAT:", this.skin));
		topBar.add(new DynamicLabel(this.client, c -> Integer.toString(c.getMoveBuilder().getHeat()), this.skin));
		topBar.add(new DynamicLabel(this.client, c -> c.getPlayer() == Player.USSR ? "PARTY UNITY:" : "PATRIOTISM:", this.skin));
		topBar.add(new DynamicLabel(this.client, c -> c.getPlayer() == Player.USSR
			? Integer.toString(c.getMoveBuilder().getComputedGameState().getNetPartyUnity())
			: Integer.toString(c.getMoveBuilder().getComputedGameState().getNetPatriotism()),
			this.skin));
		topBar.add(yearLabel);
		topBar.add(playerLabel);
		topBar.add(endTurnButton);
		topBar.row();
		
		Table botBar = new Table();
		botBar.setDebug(true);
		
		DynamicButton berlinBlockadeButton = new DynamicButton(this.client,
				c -> true,
				c -> c.getMoveBuilder().getComputedGameState().isBerlinBlockadeActive(),
				c -> c.getPlayer() == Player.USA ? c.getMoveBuilder().getComputedGameState().getCrisisUsaOption1() :
					 c.getMoveBuilder().getComputedGameState().getCrisisUssrOption1(),
				skin);
		berlinBlockadeButton.addListener(new ChangeListener() {
			@Override
			public void changed(final ChangeEvent event, final Actor actor) {
				Logger.Info("Action taken on berlin blockade.");
				if(client.getPlayer() == Player.USA) {
					HeaderPane.this.client.getMoveBuilder().BerlinAirlift();
					berlinBlockadeButton.enabledFn = c -> client.getPlayer() == Player.USA ? false : true;
				}
				else {
					HeaderPane.this.client.getMoveBuilder().LiftBerlinBlockade();
					berlinBlockadeButton.enabledFn = c -> client.getPlayer() == Player.USSR ? false : true;
				}
			}
		});
		
		
		botBar.add(new DynamicLabel(this.client, c -> c.getMoveBuilder().getComputedGameState().getCrisisInfo(), this.skin))
			.center()
			.padRight(5);
		botBar.add(berlinBlockadeButton);
		
		this.add(topBar)
			.center();
		this.row();
		this.add(botBar)
			.center();		
	}
}
