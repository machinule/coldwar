package coldwar.logic;

import coldwar.MoveOuterClass.MoveList;

public class HotseatClient extends Client {

	private MoveBuilder currentMoveBuilder;
	private MoveList usa;
	private MoveList ussr;

	public HotseatClient() {
		super();
		this.player = Player.USA;
		this.state = this.getInitialGameState().build();
		this.currentMoveBuilder = new MoveBuilder(this.player, this.state);
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
			this.currentMoveBuilder = new MoveBuilder(this.player, this.state);
		} else {
			this.ussr = this.currentMoveBuilder.getMoveList();
			this.player = Player.USA;
			this.nextTurn();
			this.currentMoveBuilder = new MoveBuilder(this.player, this.state);
			this.usa = null;
			this.ussr = null;
		}
	}
	
	@Override
	public MoveBuilder getMoveBuilder() {
		return this.currentMoveBuilder;
	}

}
