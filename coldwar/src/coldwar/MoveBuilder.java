package coldwar;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import coldwar.GameStateOuterClass.GameState;
import coldwar.MoveListOuterClass.MoveList;
import coldwar.MoveOuterClass.DiplomacyMove;
import coldwar.MoveOuterClass.Move;
import coldwar.ProvinceOuterClass.Province;

public class MoveBuilder {

	private boolean isUSA;
	private GameState.Builder state;
	private MoveList.Builder moves;
	private ComputationCache cache;
	
	public MoveBuilder() {
		this.isUSA = true;
		this.state = GameState.newBuilder();
		this.moves = MoveList.newBuilder();
		if (this.isUSA) {
			this.cache = new ComputationCache(this.state.build(), this.moves.build(), MoveList.getDefaultInstance());
		} else {
			this.cache = new ComputationCache(this.state.build(), MoveList.getDefaultInstance(), this.moves.build());
		}
	}

	private void setMoves() {
		if (isUSA) {
			cache.setUSAMove(moves.build());
		} else {
			cache.setUSSRMove(moves.build());
		}
	}
	
	public int getInfluence(Province.Id provinceId) {
		return Computations.getInfluence(cache, provinceId);
	}
	
	public void Undo() {
		moves.removeMoves(moves.getMovesCount());
		setMoves();
	}
	
	public void IncreaseInfluence(Province.Id id) {
		moves.addMoves(Move.newBuilder()
				.setDiaDipMove(DiplomacyMove.newBuilder()
						.setProvinceId(id)
						.setMagnitude(1))
				.build());
		setMoves();
		Logger.Dbg("Increasing influence in province ID: " + id);
	}
	
	public void DecreaseInfluence(Province.Id id) {
		moves.addMoves(Move.newBuilder()
				.setDiaDipMove(DiplomacyMove.newBuilder()
						.setProvinceId(id)
						.setMagnitude(-1))
				.build());
		setMoves();
		Logger.Dbg("Decreasing influence in province ID: " + id);
	}

}
