package coldwar.logic;

import java.util.concurrent.ConcurrentHashMap;

import coldwar.GameStateOuterClass.GameState;
import coldwar.MoveListOuterClass.MoveList;

public class ComputationCache {

	private final ConcurrentHashMap<BooleanComputation, Boolean> boolCache;

	private final ConcurrentHashMap<IntegerComputation, Integer> intCache;
	private GameState state;
	private MoveList usa;
	private MoveList ussr;

	public ComputationCache(final GameState state, final MoveList usa, final MoveList ussr) {
		this.intCache = new ConcurrentHashMap<IntegerComputation, Integer>();
		this.boolCache = new ConcurrentHashMap<BooleanComputation, Boolean>();
		this.state = state;
		this.usa = usa;
		this.ussr = ussr;
	}

	public void clearCaches() {
		this.intCache.clear();
		this.boolCache.clear();
	}

	public boolean computeBoolean(final BooleanComputation bc) {
		return this.boolCache.computeIfAbsent(bc,
				c -> c.compute(this.getState(), this.getUSAMove(), this.getUSSRMove()));
	}

	public int computeInteger(final IntegerComputation ic) {
		return this.intCache.computeIfAbsent(ic,
				c -> c.compute(this.getState(), this.getUSAMove(), this.getUSSRMove()));
	}

	public GameState getState() {
		return this.state;
	}

	public MoveList getUSAMove() {
		return this.usa;
	}

	public MoveList getUSSRMove() {
		return this.ussr;
	}

	public void setState(final GameState state) {
		this.state = state;
		this.clearCaches();
	}

	public void setUSAMove(final MoveList usa) {
		this.usa = usa;
		this.clearCaches();
	}

	public void setUSSRMove(final MoveList ussr) {
		this.ussr = ussr;
		this.clearCaches();
	}
}
