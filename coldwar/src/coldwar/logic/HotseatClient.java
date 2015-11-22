package coldwar.logic;

import coldwar.MoveListOuterClass.MoveList;

public class HotseatClient extends Client {

	private MoveBuilder currentMoveBuilder;
	private MoveList usa;
	private MoveList ussr;

	public HotseatClient() {
		super();
		this.player = Player.USA;
		this.currentMoveBuilder = new MoveBuilder(this.player);
	}
	
	@Override
	public MoveList getUSAMove() {
		return this.usa;
	}

	@Override
	public MoveList getUSSRMove() {
		return this.ussr;
	}

	@Override
	public void endTurn() {
		if (this.player == Player.USA) {
			this.usa = this.currentMoveBuilder.getMoveList();
			this.player = Player.USSR;
			this.currentMoveBuilder = new MoveBuilder(this.player);
		} else {
			this.ussr = this.currentMoveBuilder.getMoveList();
			this.player = Player.USA;
			this.currentMoveBuilder = new MoveBuilder(this.player);
			this.nextTurn();
			this.usa = null;
			this.ussr = null;
		}
	}
	
	@Override
	public MoveBuilder getMoveBuilder() {
		return this.currentMoveBuilder;
	}

}
