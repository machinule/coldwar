package coldwar;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.backends.lwjgl.LwjglNet;

public class ColdWarGame extends Game {
	
    public static final String LOG = ColdWarGame.class.getSimpleName();
	public Net net;
    
	public ColdWarGame(Net net) {
		super();
		this.net = net;
	}
	
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
