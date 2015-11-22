package coldwar.logic;

import coldwar.GameStateOuterClass.GameState;

import java.util.HashMap;
import java.util.Map;

import coldwar.Logger;
import coldwar.MoveListOuterClass.MoveList;
import coldwar.MoveOuterClass.Move;
import coldwar.ProvinceOuterClass.Province;
import coldwar.ProvinceOuterClass.Province.Id;

public class Computations {
	
	// DIRECT INFLUENCE ACTIONS
	
	static private class GetDiplomacyInfluenceComputation extends OneParameterComputation<Province.Id>
			implements IntegerComputation {
		public GetDiplomacyInfluenceComputation(final Id param0) {
			super(param0);
		}

		@Override
		public int compute(final GameState state, final MoveList usa, final MoveList ussr) {
			Logger.Vrb("Computing diplomatic influence...");
			int infl = 0;
			for (final Province p : state.getProvincesList()) {
				if (p.getId() == this.param0) {
					infl = p.getInfluence();
					break;
				}
			}
			for (final Move m : usa.getMovesList()) {
				if (m.hasDiaDipMove() && m.getDiaDipMove().getProvinceId() == this.param0) {
					infl += m.getDiaDipMove().getMagnitude();
				}
			}
			for (final Move m : ussr.getMovesList()) {
				if (m.hasDiaDipMove() && m.getDiaDipMove().getProvinceId() == this.param0) {
					infl += m.getDiaDipMove().getMagnitude();
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
			for (final Province p : state.getProvincesList()) {
				if (p.getId() == this.param0) {
					infl = p.getInfluence();
					break;
				}
			}
			for (final Move m : usa.getMovesList()) {
				if (m.hasDiaMilMove() && m.getDiaMilMove().getProvinceId() == this.param0) {
					infl += m.getDiaMilMove().getMagnitude();
				}
			}
			for (final Move m : ussr.getMovesList()) {
				if (m.hasDiaMilMove() && m.getDiaMilMove().getProvinceId() == this.param0) {
					infl += m.getDiaMilMove().getMagnitude();
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
			for (final Province p : state.getProvincesList()) {
				if (p.getId() == this.param0) {
					infl = p.getInfluence();
					break;
				}
			}
			for (final Move m : usa.getMovesList()) {
				if (m.hasDiaCovMove() && m.getDiaCovMove().getProvinceId() == this.param0) {
					infl += m.getDiaCovMove().getMagnitude();
				}
			}
			for (final Move m : ussr.getMovesList()) {
				if (m.hasDiaCovMove() && m.getDiaCovMove().getProvinceId() == this.param0) {
					infl += m.getDiaCovMove().getMagnitude();
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
				if (p.getId() == this.param0) {
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

	static public int getInfluence(final ComputationCache cache, final Province.Id provinceId) {
		return cache.computeInteger(new GetDiplomacyInfluenceComputation(provinceId)) +
			   cache.computeInteger(new GetMilitaryInfluenceComputation(provinceId)) +
			   cache.computeInteger(new GetCovertInfluenceComputation(provinceId));
	}
	
	static private class NextGameStateComputation extends ZeroParameterComputation implements GameStateComputation {
		
		@Override
		public GameState compute(final GameState prevState, final MoveList usa, final MoveList ussr) {
			Logger.Vrb("Computing next game state...");
			final GameState.Builder state = GameState.newBuilder().mergeFrom(prevState);

			// Update turn-based critical state values
			state.setTurn(state.getTurn() + 1);

			// Extract the province builders into a hashmap (key = Province.Id)
			final Map<Province.Id, Province.Builder> provinceMap = new HashMap<Province.Id, Province.Builder>();

			for (final Province.Builder province : state.getProvincesBuilderList()) {
				provinceMap.put(province.getId(), province);
			}

			// USA moves
			for (final Move move : usa.getMovesList()) {
				if (move.hasFoundNatoMove()) {
					state.getUsaBuilder().setFoundNato(true);
					state.getUsaBuilder().setUnrest(state.getUsa().getUnrest() + 1);
				}
				// Direct influence actions
				if (move.hasDiaDipMove()) {
					provinceMap.get(move.getDiaDipMove().getProvinceId()).setInfluence(move.getDiaDipMove().getMagnitude());
				}
				if (move.hasDiaMilMove()) {
					provinceMap.get(move.getDiaMilMove().getProvinceId()).setInfluence(move.getDiaMilMove().getMagnitude());
				}
				if (move.hasDiaCovMove()) {
					provinceMap.get(move.getDiaCovMove().getProvinceId()).setInfluence(move.getDiaCovMove().getMagnitude());
				}
			}

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
		return cache.computeGameState(new NextGameStateComputation());
	}	
}
