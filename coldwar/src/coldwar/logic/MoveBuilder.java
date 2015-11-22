package coldwar.logic;

import coldwar.GameStateOuterClass.GameState;
import coldwar.Logger;
import coldwar.MoveListOuterClass.MoveList;
import coldwar.MoveOuterClass.DiplomacyMove;
import coldwar.MoveOuterClass.FundDissidentsMove;
import coldwar.MoveOuterClass.Move;
import coldwar.ProvinceOuterClass.Province;

public class MoveBuilder {

	private ComputationCache cache;
	private final boolean isUSA;
	private final MoveList.Builder moves;
	private final GameState.Builder state;

	public MoveBuilder() {
		this.isUSA = true; // Remove on turns
		this.state = GameState.newBuilder();
		this.moves = MoveList.newBuilder();
		if (this.isUSA) {
			this.cache = new ComputationCache(this.state.build(), this.moves.build(), MoveList.getDefaultInstance());
		} else {
			this.cache = new ComputationCache(this.state.build(), MoveList.getDefaultInstance(), this.moves.build());
		}
	}

	public void DecreaseInfluence(final Province.Id id) {
		this.moves.addMoves(
				Move.newBuilder().setDiaDipMove(DiplomacyMove.newBuilder().setProvinceId(id).setMagnitude(-1)).build());
		Logger.Vrb("Decreasing influence in " + id);
		this.setMoves();
	}

	public void FundDissidents(final Province.Id id) {
		this.moves.addMoves(
				Move.newBuilder().setFundDissidentsMove(FundDissidentsMove.newBuilder().setProvinceId(id)).build());
		Logger.Vrb("Funding dissidents in " + id);
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

	public void IncreaseInfluence(final Province.Id id) {
		this.moves.addMoves(
				Move.newBuilder().setDiaDipMove(DiplomacyMove.newBuilder().setProvinceId(id).setMagnitude(1)).build());
		Logger.Vrb("Increasing influence in " + id);
		this.setMoves();
	}

	private void setMoves() {
		if (this.isUSA) {
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
