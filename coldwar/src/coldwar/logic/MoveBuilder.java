package coldwar.logic;

import coldwar.GameStateOuterClass.GameState;
import coldwar.Logger;
import coldwar.MoveListOuterClass.MoveList;
import coldwar.MoveOuterClass.CovertMove;
import coldwar.MoveOuterClass.DiplomacyMove;
import coldwar.MoveOuterClass.FundDissidentsMove;
import coldwar.MoveOuterClass.MilitaryMove;
import coldwar.MoveOuterClass.Move;
import coldwar.ProvinceOuterClass.Province;
import coldwar.logic.Client.Player;

public class MoveBuilder {

	private ComputationCache cache;
	private final MoveList.Builder moves;
	private final GameState.Builder state;
	private final Player player;

	public MoveBuilder(Player player) {
		this.player = player;
		this.state = GameState.newBuilder();
		this.moves = MoveList.newBuilder();
		if (this.player == Player.USA) {
			this.cache = new ComputationCache(this.state.build(), this.moves.build(), MoveList.getDefaultInstance());
		} else {
			this.cache = new ComputationCache(this.state.build(), MoveList.getDefaultInstance(), this.moves.build());
		}
	}

	public void Influence_Dip(final Province.Id id, int magnitude) {
		this.moves.addMoves(
				Move.newBuilder().setDiaDipMove(DiplomacyMove.newBuilder().setProvinceId(id).setMagnitude(magnitude)).build());
		Logger.Dbg("Adding influence from political points in " + id + " with magnitude " + magnitude);
		this.setMoves();
	}
	
	public void Influence_Mil(final Province.Id id, int magnitude) {
		this.moves.addMoves(
				Move.newBuilder().setDiaMilMove(MilitaryMove.newBuilder().setProvinceId(id).setMagnitude(magnitude)).build());
		Logger.Dbg("Adding influence from military points in " + id + " with magnitude " + magnitude);
		this.setMoves();
	}
	
	public void Influence_Cov(final Province.Id id, int magnitude) {
		this.moves.addMoves(
				Move.newBuilder().setDiaCovMove(CovertMove.newBuilder().setProvinceId(id).setMagnitude(magnitude)).build());
		Logger.Dbg("Adding influence from covert points in " + id + " with magnitude " + magnitude);
		this.setMoves();
	}

	public void FundDissidents(final Province.Id id) {
		this.moves.addMoves(
				Move.newBuilder().setFundDissidentsMove(FundDissidentsMove.newBuilder().setProvinceId(id)).build());
		Logger.Dbg("Funding dissidents in " + id);
		this.setMoves();
	}

	public int getInfluence(final Province.Id provinceId) {
		return Computations.getInfluence(this.cache, provinceId);
	}

	public MoveList getMoveList() {
		return this.moves.build();
	}

	public boolean hasDissidents(final Province.Id provinceId) {
		return Computations.getHasDissidents(this.cache, provinceId);
	}

	private void setMoves() {
		if (this.player == Player.USA) {
			this.cache.setUSAMove(this.moves.build());
		} else {
			this.cache.setUSSRMove(this.moves.build());
		}
	}

	public void Undo() {
		this.moves.removeMoves(this.moves.getMovesCount());
		this.setMoves();
	}

}
