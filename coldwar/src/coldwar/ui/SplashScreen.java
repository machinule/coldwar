package coldwar.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;

import coldwar.ColdWarGame;
import coldwar.Logger;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

public class SplashScreen extends AbstractScreen {

    public SplashScreen(final ColdWarGame game) {
    	super(game);
    }

	@Override
	public void show() {
        super.show();
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("textures/pack.atlas"));
        AtlasRegion splashRegion = atlas.findRegion("logo");
        Drawable splashDrawable = new TextureRegionDrawable(splashRegion);
        Image splashImage = new Image(splashDrawable, Scaling.none);
        splashImage.setFillParent(true);
        splashImage.getColor().a = 0f;
        
        splashImage.addAction(sequence(
        	fadeIn( 0.75f ),
        	delay( 1.75f ),
        	fadeOut( 0.75f ),
            new Action() {
                @Override
                public boolean act(float delta){
                    game.setScreen( new MainMenuScreen( game ) );
                    return true;
                }
            }));
        
        splashImage.addListener(new ClickListener() {
            public void clicked (InputEvent event, float x, float y) {
            	Logger.Dbg("Splash image clicked - skipping");
            	game.setScreen( new MainMenuScreen( game ) );
            }
        });
        stage.addActor(splashImage);
	}

}
