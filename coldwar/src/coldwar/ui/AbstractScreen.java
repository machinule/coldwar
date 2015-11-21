package coldwar.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import coldwar.ColdWarGame;
import coldwar.Logger;

public abstract class AbstractScreen implements Screen {
	protected final ColdWarGame game;
	
	private Viewport viewport;
	protected Stage stage;
	
	public AbstractScreen(final ColdWarGame game) {
	    this.game = game;
	    this.viewport = new ScreenViewport();
	    this.stage = new Stage(this.viewport);
	}
	
    protected String getName(){
        return getClass().getSimpleName();
    }

	@Override
	public void show(){
	    Logger.Dbg("Showing screen: " + getName());
	    Gdx.input.setInputProcessor(stage);
	}
	
	@Override
	public void resize(int width, int height){
	    Logger.Dbg("Resizing screen: " + getName() + " to: " + width + " x " + height);
	}
	
	@Override
	public void render(float delta){
	    stage.act(delta);
	    Gdx.gl.glClearColor( 1.0f, 1.0f, 1.0f, 1f );
	    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	    stage.draw();
	}
	
	@Override
	public void hide(){
		 Logger.Dbg("Hiding screen: " + getName());
	    dispose();
	}
	
	@Override
	public void pause(){
		 Logger.Dbg("Pausing screen: " + getName());
	}
	
	@Override
	public void resume(){
		 Logger.Dbg("Resuming screen: " + getName());
	}
	
	@Override
	public void dispose(){
		 Logger.Dbg("Disposing screen: " + getName());
	    stage.dispose();
	}
}