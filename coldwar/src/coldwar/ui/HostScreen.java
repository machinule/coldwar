package coldwar.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import coldwar.ColdWarGame;
import coldwar.Logger;

public class HostScreen extends AbstractScreen {

	public HostScreen(final ColdWarGame game) {
		super(game);
	}
	
	@Override
	public void show() {
		super.show();
		
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("textures/uiskin.atlas"));
		Skin skin = new Skin(Gdx.files.internal("textures/uiskin.json"), atlas);
        Table table = new Table(skin);
        table.setFillParent(true);
        table.setDebug(true);
        stage.addActor(table);
        Label portLabel = new Label("Port:", skin);
        table.add(portLabel);
        TextField portField = new TextField("7588", skin);
        table.add(portField);
        TextButton hostGameButton = new TextButton("Host", skin);
        hostGameButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
        	    Logger.Dbg("\"Host a Game\" button pressed.");
        	    game.host(Integer.parseInt(portField.getText()));
                game.setScreen(new MapScreen(game));
            }
        });
        table.add(hostGameButton).size( 300, 60 ).uniform().spaceBottom( 10 );
        table.row();
   	}
}
