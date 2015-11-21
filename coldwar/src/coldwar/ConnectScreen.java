package coldwar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class ConnectScreen extends AbstractScreen {

	public ConnectScreen(final ColdWarGame game) {
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
        Label hostLabel = new Label("Host:", skin);
        table.add(hostLabel);
        TextField hostField = new TextField("localhost", skin);
        table.add(hostField);
        Label portLabel = new Label("Port:", skin);
        table.add(portLabel);
        TextField portField = new TextField("7588", skin);
        table.add(portField);
        TextButton connectGameButton = new TextButton("Connect", skin);
        connectGameButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
        	    Logger.Dbg("\"Connect a Game\" button pressed.");
        	    game.connect(hostField.getText(), Integer.parseInt(portField.getText()));
                game.setScreen(new MapScreen(game));
            }
        });
        table.add(connectGameButton).size( 300, 60 ).uniform().spaceBottom( 10 );
        table.row();
   	}
}
