package coldwar.logic;

import com.badlogic.gdx.Gdx;

import coldwar.MoveListOuterClass.MoveList;
import coldwar.Peer;

public class RemoteClient extends Client {

	private Peer peer;
	
	public RemoteClient(String host, int port) {
		super();
		this.peer = new Peer(Gdx.app.getNet());
		this.peer.Connect(host, port);
	}

	public RemoteClient(int port) {
		super();
		this.peer = new Peer(Gdx.app.getNet());
		this.peer.Host(port);
	}

	@Override
	public MoveList getUSAMove() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MoveList getUSSRMove() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MoveBuilder getMoveBuilder() {
		// TODO Auto-generated method stub
		return null;
	}

}
