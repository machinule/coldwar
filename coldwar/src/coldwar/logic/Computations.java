package coldwar.logic;

import coldwar.GameStateOuterClass.GameState;
import coldwar.InfluenceStoreOuterClass.InfluenceStore;

import java.util.HashMap;
import java.util.Map;

import com.google.protobuf.Message;

import coldwar.Logger;
import coldwar.MoveOuterClass;
import coldwar.MoveListOuterClass.MoveList;
import coldwar.MoveOuterClass.Move;
import coldwar.ProvinceOuterClass.Province;
import coldwar.ProvinceOuterClass.Province.Id;
import coldwar.logic.Client.Player;

public class Computations {
	
	// DIRECT INFLUENCE ACTIONS
	
	static private class GetBaseInfluenceComputation extends OneParameterComputation<Province.Id> implements IntegerComputation {
		public GetBaseInfluenceComputation(final Id param0) {
			super(param0);
		}
		
		@Override
		public int compute(final GameState state, final MoveList usa, final MoveList ussr) {
			Logger.Vrb("Computing base influence...");
			int infl = 0;
			for (final Province p : state.getProvincesList()) {
				if (p.getId() == this.param0) {
					infl = p.getInfluence();
					break;
				}
			}
			return infl;
		}
		
		@Override
		protected int paramAsInt(final Province.Id p) {
			return p.getNumber();
		}
	}
	
	static private class GetDiplomacyInfluenceComputation extends OneParameterComputation<Province.Id>
			implements IntegerComputation {
		public GetDiplomacyInfluenceComputation(final Id param0) {
			super(param0);
		}

		@Override
		public int compute(final GameState state, final MoveList usa, final MoveList ussr) {
			Logger.Vrb("Computing diplomatic influence...");
			int infl = 0;
			for (final Move m : usa.getMovesList()) {
				if (m.hasDiaDipMove() && m.getDiaDipMove().getProvinceId() == this.param0) {
					infl += m.getDiaDipMove().getMagnitude();
				}
			}
			for (final Move m : ussr.getMovesList()) {
				if (m.hasDiaDipMove() && m.getDiaDipMove().getProvinceId() == this.param0) {
					infl -= m.getDiaDipMove().getMagnitude();
				}
			}
			return infl;
		}

		@Override
		protected int paramAsInt(final Province.Id p) {
			return p.getNumber();
		}
	}

	static private class GetMilitaryInfluenceComputation extends OneParameterComputation<Province.Id>
			implements IntegerComputation {
		public GetMilitaryInfluenceComputation(final Id param0) {
			super(param0);
		}
		
		@Override
		public int compute(final GameState state, final MoveList usa, final MoveList ussr) {
			Logger.Vrb("Computing military influence...");
			int infl = 0;
			for (final Move m : usa.getMovesList()) {
				if (m.hasDiaMilMove() && m.getDiaMilMove().getProvinceId() == this.param0) {
					infl += m.getDiaMilMove().getMagnitude();
				}
			}
			for (final Move m : ussr.getMovesList()) {
				if (m.hasDiaMilMove() && m.getDiaMilMove().getProvinceId() == this.param0) {
					infl -= m.getDiaMilMove().getMagnitude();
				}
			}
			return infl;
		}
		
		@Override
		protected int paramAsInt(final Province.Id p) {
			return p.getNumber();
		}
	}
	
	static private class GetCovertInfluenceComputation extends OneParameterComputation<Province.Id>
			implements IntegerComputation {
		public GetCovertInfluenceComputation(final Id param0) {
			super(param0);
		}
		
		@Override
		public int compute(final GameState state, final MoveList usa, final MoveList ussr) {
			Logger.Vrb("Computing covert influence...");
			int infl = 0;
			for (final Move m : usa.getMovesList()) {
				if (m.hasDiaCovMove() && m.getDiaCovMove().getProvinceId() == this.param0) {
					infl += m.getDiaCovMove().getMagnitude();
				}
			}
			for (final Move m : ussr.getMovesList()) {
				if (m.hasDiaCovMove() && m.getDiaCovMove().getProvinceId() == this.param0) {
					infl -= m.getDiaCovMove().getMagnitude();
				}
			}
			return infl;
		}
		
		@Override
		protected int paramAsInt(final Province.Id p) {
			return p.getNumber();
		}
	}
	
	// TARGETED ACTIONS
	
	// Add dissidents to a province
	static private class HasDissidentsComputation extends OneParameterComputation<Province.Id>
			implements BooleanComputation {
		public HasDissidentsComputation(final Id param0) {
			super(param0);
		}

		@Override
		public boolean compute(final GameState state, final MoveList usa, final MoveList ussr) {
			Logger.Vrb("Computing dissidents...");
			for (final Province p : state.getProvincesList()) {
				if (p.getId() == this.param0 && p.getDissidents()) {
					return true;
				}
			}
			for (final Move m : usa.getMovesList()) {
				if (m.hasFundDissidentsMove() && m.getFundDissidentsMove().getProvinceId() == this.param0) {
					return true;
				}
			}
			for (final Move m : ussr.getMovesList()) {
				if (m.hasFundDissidentsMove() && m.getFundDissidentsMove().getProvinceId() == this.param0) {
					return true;
				}
			}
			return false;
		}

		@Override
		protected int paramAsInt(final Province.Id p) {
			return p.getNumber();
		}
	}
	static private abstract class ZeroParameterComputation {
		@Override
		public boolean equals(final Object obj) {
			return this.getClass() == obj.getClass();
		}

		@Override
		public int hashCode() {
			return this.getClass().hashCode();
		}
	}
	static private abstract class OneParameterComputation<T> {
		protected T param0;

		public OneParameterComputation(final T param0) {
			this.param0 = param0;
		}

		@Override
		public boolean equals(final Object obj) {
			if (!(obj instanceof OneParameterComputation<?>)) {
				return false;
			}
			@SuppressWarnings("unchecked")
			final OneParameterComputation<T> other = (OneParameterComputation<T>) obj;
			return (this.getClass() == other.getClass() && this.param0.getClass() == other.param0.getClass()
					&& this.param0 == other.param0);
		}

		@Override
		public int hashCode() {
			return this.getClass().hashCode() + this.paramAsInt(this.param0);
		}

		protected abstract int paramAsInt(T p);
	}

	static public boolean getHasDissidents(final ComputationCache cache, final Province.Id provinceId) {
		return cache.computeBoolean(new HasDissidentsComputation(provinceId));
	}

	/*
	 * Returns the influence in a province. Positive is USA influence, negative is USSR.
	 */
	static public int getInfluence(final ComputationCache cache, final Province.Id provinceId) {
		return cache.computeInteger(new GetBaseInfluenceComputation(provinceId)) +
			   cache.computeInteger(new GetDiplomacyInfluenceComputation(provinceId)) +
			   cache.computeInteger(new GetMilitaryInfluenceComputation(provinceId)) +
			   cache.computeInteger(new GetCovertInfluenceComputation(provinceId));
	}
	static public boolean isUSAInfluenced(final ComputationCache cache, final Province.Id provinceId) {
		return getInfluence(cache, provinceId) > 0;
	}
	static public boolean isUSSRInfluenced(final ComputationCache cache, final Province.Id provinceId) {
		return getInfluence(cache, provinceId) < 0;
	}	
	static public boolean isNotInfluenced(final ComputationCache cache, final Province.Id provinceId) {
		return getInfluence(cache, provinceId) == 0;
	}
	
	static private class NextGameStateComputation extends ZeroParameterComputation implements GameStateComputation {

		// The computation cache is used for chaining operations together.
		ComputationCache cache;
		
		public NextGameStateComputation(ComputationCache cache) {
			this.cache = cache;
		}
		
		@Override
		public GameState compute(final GameState prevState, final MoveList usa, final MoveList ussr) {
			Logger.Vrb("Computing next game state...");
			Logger.Vrb("prev:\n" +prevState.toString());
			Logger.Vrb("usa:\n" +usa.toString());
			Logger.Vrb("ussr:\n" +ussr.toString());
			final GameState.Builder state = GameState.newBuilder().mergeFrom(prevState);

			// Update turn-based critical state values
			state.setTurn(state.getTurn() + 1);
			
			for (final Province.Builder province : state.getProvincesBuilderList()) {
				province.setDissidents(Computations.getHasDissidents(this.cache, province.getId()));
				province.setInfluence(Computations.getInfluence(this.cache, province.getId()));
			}
			
			state.getUsaBuilder().getInfluenceStoreBuilder()
			    .setPolitical(Computations.getPolStore(cache, Player.USA))
			    .setMilitary(Computations.getMilStore(cache, Player.USA))
			    .setCovert(Computations.getCovStore(cache, Player.USA));
			state.getUssrBuilder().getInfluenceStoreBuilder()
			    .setPolitical(Computations.getPolStore(cache, Player.USSR))
			    .setMilitary(Computations.getMilStore(cache, Player.USSR))
			    .setCovert(Computations.getCovStore(cache, Player.USSR));

			// USA moves
//			for (final Move move : usa.getMovesList()) {
//				if (move.hasFoundNatoMove()) {
//					state.getUsaBuilder().setFoundNato(true);
//					state.getUsaBuilder().setUnrest(state.getUsa().getUnrest() + 1);
//				}
//				// Direct influence actions
//				if (move.hasDiaDipMove()) {
//					provinceMap.get(move.getDiaDipMove().getProvinceId()).setInfluence(move.getDiaDipMove().getMagnitude());
//				}
//				if (move.hasDiaMilMove()) {
//					provinceMap.get(move.getDiaMilMove().getProvinceId()).setInfluence(move.getDiaMilMove().getMagnitude());
//				}
//				if (move.hasDiaCovMove()) {
//					provinceMap.get(move.getDiaCovMove().getProvinceId()).setInfluence(move.getDiaCovMove().getMagnitude());
//				}
//			}

			// USSR moves
			/*
			 *
			 * for(Move move : ussr.getMovesList()) { if(move.hasFoundPactMove()) {
			 * state.getUssrBuilder().setFoundPact(true);
			 *
			 * } }
			 *
			 */

			return state.build();
		}
	}
	static public GameState getNextGameState(final ComputationCache cache) {
		return cache.computeGameState(new NextGameStateComputation(cache));
	}
	static private class YearComputation extends ZeroParameterComputation implements IntegerComputation {
		
		@Override
		public int compute(final GameState state, final MoveList usa, final MoveList ussr) {
			Logger.Vrb("Computing year...");
			return state.getTurn() + 1947;
		}
	}
	public static int getYear(ComputationCache cache) {
		return cache.computeInteger(new YearComputation());
	}	
	static private class PolStoreComputation extends OneParameterComputation<Player> implements IntegerComputation {

		public PolStoreComputation(final Player param0) {
			super(param0);
		}

		@Override
		protected int paramAsInt(final Player p) {
			return p.ordinal();
		}
		
		@Override
		public int compute(final GameState state, final MoveList usa, final MoveList ussr) {
			Logger.Vrb("Computing POL for " + param0);
			int pol = 0;
			MoveList moves;
			if (param0 == Player.USA) {
				pol += state.getUsa().getInfluenceStore().getPolitical();
				moves = usa;
			} else {
				pol += state.getUssr().getInfluenceStore().getPolitical();
				moves = ussr;
			}
			for (Move m : moves.getMovesList()) {
				pol -= moveDetails(m).getDescriptorForType().getOptions().getExtension(MoveOuterClass.polCost);
			}
			return pol;
		}
	}
	public static int getPolStore(ComputationCache cache, Player player) {
		return cache.computeInteger(new PolStoreComputation(player));
	}
	static private class MilStoreComputation extends OneParameterComputation<Player> implements IntegerComputation {

		public MilStoreComputation(final Player param0) {
			super(param0);
		}

		@Override
		protected int paramAsInt(final Player p) {
			return p.ordinal();
		}
		
		@Override
		public int compute(final GameState state, final MoveList usa, final MoveList ussr) {
			Logger.Vrb("Computing MIL for " + param0);
			int mil = 0;
			MoveList moves;
			if (param0 == Player.USA) {
				mil += state.getUsa().getInfluenceStore().getMilitary();
				moves = usa;
			} else {
				mil += state.getUssr().getInfluenceStore().getMilitary();
				moves = ussr;
			}
			for (Move m : moves.getMovesList()) {
				mil -= moveDetails(m).getDescriptorForType().getOptions().getExtension(MoveOuterClass.milCost);
			}
			return mil;
		}
	}
	public static int getMilStore(ComputationCache cache, Player player) {
		return cache.computeInteger(new MilStoreComputation(player));
	}
	static private class CovStoreComputation extends OneParameterComputation<Player> implements IntegerComputation {

		public CovStoreComputation(final Player param0) {
			super(param0);
		}

		@Override
		protected int paramAsInt(final Player p) {
			return p.ordinal();
		}
		
		@Override
		public int compute(final GameState state, final MoveList usa, final MoveList ussr) {
			Logger.Vrb("Computing COV for " + param0);
			int cov = 0;
			MoveList moves;
			if (param0 == Player.USA) {
				cov += state.getUsa().getInfluenceStore().getCovert();
				moves = usa;
			} else {
				cov += state.getUssr().getInfluenceStore().getCovert();
				moves = ussr;
			}
			for (Move m : moves.getMovesList()) {
				cov -= moveDetails(m).getDescriptorForType().getOptions().getExtension(MoveOuterClass.covCost);
			}
			return cov;
		}
	}
	private static Message moveDetails(Move m) {
		return (Message)m.getAllFields().values().iterator().next();
	}
	public static int getCovStore(ComputationCache cache, Player player) {
		return cache.computeInteger(new CovStoreComputation(player));
	}
}
