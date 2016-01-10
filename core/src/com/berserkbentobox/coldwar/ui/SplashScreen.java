package com.berserkbentobox.coldwar.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;

import com.berserkbentobox.coldwar.ColdWarGame;
import com.berserkbentobox.coldwar.Logger;

public class SplashScreen extends AbstractScreen {

	public SplashScreen(final ColdWarGame game) {
		super(game);
	}

	@Override
	public void show() {
		super.show();
		//final TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("textures/pack.atlas"));
		final TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("pack.atlas")); // When creating JAR
		final AtlasRegion splashRegion = atlas.findRegion("Knife'sEdge");
		final Drawable splashDrawable = new TextureRegionDrawable(splashRegion);
		final Image splashImage = new Image(splashDrawable, Scaling.none);
		splashImage.setFillParent(true);
		splashImage.getColor().a = 0f;

		splashImage.addAction(
				Actions.sequence(Actions.fadeIn(0.75f), Actions.delay(1.75f), Actions.fadeOut(0.75f), new Action() {
					@Override
					public boolean act(final float delta) {
						game.setRes();
						SplashScreen.this.game.setScreen(new MainMenuScreen(SplashScreen.this.game));
						return true;
					}
				}));

		splashImage.addListener(new ClickListener() {
			@Override
			public void clicked(final InputEvent event, final float x, final float y) {
				Logger.Dbg("Splash image clicked - skipping");
				game.setRes();
				SplashScreen.this.game.setScreen(new MainMenuScreen(SplashScreen.this.game));
			}
		});
		this.stage.addActor(splashImage);
	}

}
