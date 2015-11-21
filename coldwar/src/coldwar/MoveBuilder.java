package coldwar;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import coldwar.GameStateOuterClass.GameState;
import coldwar.MoveListOuterClass.MoveList;
import coldwar.MoveOuterClass.DiplomacyMove;
import coldwar.MoveOuterClass.FundDissidentsMove;
import coldwar.MoveListOuterClass.MoveList;
import coldwar.MoveOuterClass.Move;
import coldwar.ProvinceOuterClass.Province;

public class MoveBuilder {

	private boolean isUSA;
	private GameState.Builder state;
	private MoveList.Builder moves;
	private ComputationCache cache;
	
	private MoveList.Builder pending = MoveList.newBuilder();
	private int INFL_MAX = 3;
	private int INFL_MIN = -3;
	
	public MoveBuilder() {
		this.isUSA = true; //Remove on turns
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
	
	public boolean hasDissidents(Province.Id provinceId) {
		return Computations.getHasDissidents(cache, provinceId) == 1;
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
		Logger.Vrb("Increasing influence in " + id);
		setMoves();
	}
		
	public void DecreaseInfluence(Province.Id id) {
		moves.addMoves(Move.newBuilder()
				.setDiaDipMove(DiplomacyMove.newBuilder()
						.setProvinceId(id)
						.setMagnitude(-1))
				.build());
		Logger.Vrb("Decreasing influence in " + id);
		setMoves();
	}
	
	public void FundDissidents(Province.Id id) {
		moves.addMoves(Move.newBuilder()
				.setFundDissidentsMove(FundDissidentsMove.newBuilder()
						.setProvinceId(id))
				.build());
		Logger.Vrb("Funding dissidents in " + id);
		setMoves();
	}

}
