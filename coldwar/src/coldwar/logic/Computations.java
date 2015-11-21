package coldwar.logic;

import coldwar.GameStateOuterClass;
import coldwar.Logger;
import coldwar.MoveListOuterClass;
import coldwar.MoveOuterClass;
import coldwar.ProvinceOuterClass;
import coldwar.GameStateOuterClass.GameState;
import coldwar.MoveListOuterClass.MoveList;
import coldwar.MoveOuterClass.Move;
import coldwar.ProvinceOuterClass.Province;
import coldwar.ProvinceOuterClass.Province.Id;

public class Computations {
	static private abstract class OneParameterComputation<T> {
		protected T param0;
		public OneParameterComputation(T param0) {
			this.param0 = param0;
		}
		protected abstract int paramAsInt(T p);
		public boolean equals(Object obj) {
			if (!(obj instanceof OneParameterComputation<?>)) {
				return false;
			}
			@SuppressWarnings("unchecked")
			OneParameterComputation<T> other = (OneParameterComputation<T>) obj;
			return (this.getClass() == other.getClass() &&
					this.param0.getClass() == other.param0.getClass() &&
					this.param0 == other.param0);
		}
		public int hashCode() {
			return this.getClass().hashCode() + paramAsInt(param0);
		}
	}
	
	static private class GetInfluenceComputation extends OneParameterComputation<Province.Id> implements IntegerComputation {
		public GetInfluenceComputation(Id param0) {
			super(param0);
		}
		@Override
		protected int paramAsInt(Province.Id p) {
			return p.getNumber();
		}
		@Override
		public int compute(GameState state, MoveList usa, MoveList ussr) {
			Logger.Dbg("Computing...");
			int infl = 0;
			for (Province p : state.getProvincesList()) {
				if (p.getId() == param0) {
					infl = p.getInfluence();
					break;
				}
			}
			for (Move m : usa.getMovesList()) {
				if (m.hasDiaDipMove() && m.getDiaDipMove().getProvinceId() == param0) {
					infl += m.getDiaDipMove().getMagnitude();
				}
			}
			for (Move m : ussr.getMovesList()) {
				if (m.hasDiaDipMove() && m.getDiaDipMove().getProvinceId() == param0) {
					infl += m.getDiaDipMove().getMagnitude();
				}
			}
			return infl;
		}
	}
	static public int getInfluence(ComputationCache cache, Province.Id provinceId) {
		return cache.computeInteger(new GetInfluenceComputation(provinceId));
	}
	
	// Add dissidents to a province
	static private class HasDissidentsComputation extends OneParameterComputation<Province.Id> implements BooleanComputation {
		public HasDissidentsComputation(Id param0) {
			super(param0);
		}
		@Override
		protected int paramAsInt(Province.Id p) {
			return p.getNumber();
		}
		@Override
		public boolean compute(GameState state, MoveList usa, MoveList ussr) {
			Logger.Dbg("Computing dissidents...");
			for (Province p : state.getProvincesList()) {
				if (p.getId() == param0) {
					return true;
				}
			}
			for (Move m : usa.getMovesList()) {
				if (m.hasFundDissidentsMove() && m.getFundDissidentsMove().getProvinceId() == param0) {
					return true;
				}
			}
			for (Move m : ussr.getMovesList()) {
				if (m.hasFundDissidentsMove() && m.getFundDissidentsMove().getProvinceId() == param0) {
					return true;
				}
			}
			return false;
		}
	}
	static public int getHasDissidents(ComputationCache cache, Province.Id provinceId) {
		return cache.computeBoolean(new HasDissidentsComputation(provinceId));
	}
}
