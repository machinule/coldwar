package coldwar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

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
	    Gdx.app.log(ColdWarGame.LOG, "Showing screen: " + getName());
	    Gdx.input.setInputProcessor(stage);
	}
	
	@Override
	public void resize(int width, int height){
	    Gdx.app.log(ColdWarGame.LOG, "Resizing screen: " + getName() + " to: " + width + " x " + height);
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
	    Gdx.app.log(ColdWarGame.LOG, "Hiding screen: " + getName());
	    dispose();
	}
	
	@Override
	public void pause(){
	    Gdx.app.log(ColdWarGame.LOG, "Pausing screen: " + getName());
	}
	
	@Override
	public void resume(){
	    Gdx.app.log(ColdWarGame.LOG, "Resuming screen: " + getName());
	}
	
	@Override
	public void dispose(){
	    Gdx.app.log(ColdWarGame.LOG, "Disposing screen: " + getName());
	    stage.dispose();
	}
}