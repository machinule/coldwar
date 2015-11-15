package coldwar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class MainMenuScreen extends AbstractScreen {
	
    public MainMenuScreen(final ColdWarGame game) {
        super(game);
    }

	@Override
	public void show() {
		super.show();
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("textures/uiskin.atlas"));
		Skin skin = new Skin(Gdx.files.internal("textures/uiskin.json"), atlas);
        Table table = new Table(skin);
        table.setFillParent(true);
        stage.addActor(table);

        table.add("Brink").spaceBottom(50);
        table.row();

        TextButton hostGameButton = new TextButton("Host a Game", skin);
        hostGameButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
        	    Gdx.app.log(ColdWarGame.LOG, "\"Host a Game\" button pressed.");
            }
        });
        table.add(hostGameButton).size( 300, 60 ).uniform().spaceBottom( 10 );
        table.row();
        
        TextButton connectGameButton = new TextButton("Connect to a Game", skin);
        connectGameButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
        	    Gdx.app.log(ColdWarGame.LOG, "\"Connect to a Game\" button pressed.");
            }
        });
        table.add(connectGameButton).uniform().fill().spaceBottom( 10 );
        table.row();
        
        TextButton optionsButton = new TextButton("Options", skin);
        optionsButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
        	    Gdx.app.log(ColdWarGame.LOG, "\"Options\" button pressed.");
            }
        });
        table.add(optionsButton).uniform().fill().spaceBottom( 10 );
        table.row();

        TextButton quitButton = new TextButton("Quit", skin);
       quitButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
        	    Gdx.app.log(ColdWarGame.LOG, "\"Quit\" button pressed.");
            	Gdx.app.exit();
            }
        });
       table.add(quitButton).uniform().fill();
	}

}