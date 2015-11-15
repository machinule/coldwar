package coldwar;

import com.badlogic.gdx.Game;

public class ColdWarGame extends Game {
	
    public static final String LOG = ColdWarGame.class.getSimpleName();

	@Override
	public void create() {
        this.setScreen(new SplashScreen(this));
	}

    public void render() {
        super.render(); //important!
    }

    public void dispose() {
    }
}
