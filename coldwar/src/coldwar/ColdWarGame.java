package coldwar;

import com.badlogic.gdx.Game;

import coldwar.MoveListOuterClass.MoveList;

public class ColdWarGame extends Game {
	
    public static final String LOG = ColdWarGame.class.getSimpleName();
	public Peer peer;
    
	public ColdWarGame(Peer peer) {
		super();
		this.peer = peer;
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

	public void connect(String host, int port) {
		this.peer.Connect(host, port);		
	}

	public void host(int parseInt) {
		this.peer.Host(parseInt);
	}

	public void endTurn(MoveBuilder moveBuilder) {
		MoveList local = moveBuilder.getMoveList();
		this.peer.sendMoveList(local);
		MoveList other = this.peer.getMoveList();
	}
}
