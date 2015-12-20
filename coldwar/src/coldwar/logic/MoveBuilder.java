package coldwar.logic;

import coldwar.GameStateOuterClass.GameState;
import coldwar.Logger;
import coldwar.MoveOuterClass.MoveList;
import coldwar.MoveOuterClass.ConflictOvertFundAttackerMove;
import coldwar.MoveOuterClass.ConflictOvertFundDefenderMove;
import coldwar.MoveOuterClass.CoupMove;
import coldwar.MoveOuterClass.CovertMove;
import coldwar.MoveOuterClass.DiplomacyMove;
import coldwar.MoveOuterClass.EstablishBaseMove;
import coldwar.MoveOuterClass.FoundCIAMove;
import coldwar.MoveOuterClass.FoundKGBMove;
import coldwar.MoveOuterClass.FundDissidentsMove;
import coldwar.MoveOuterClass.MilitaryMove;
import coldwar.MoveOuterClass.Move;
import coldwar.MoveOuterClass.PoliticalPressureMove;
import coldwar.MoveOuterClass.USABerlinBlockadeAirliftMove;
import coldwar.MoveOuterClass.USSRBerlinBlockadeLiftBlockadeMove;
import coldwar.ProvinceOuterClass.Province;
import coldwar.logic.Client.Player;

public class MoveBuilder {

	private ComputedGameState computedState;
	private final MoveList.Builder moves;
	private final GameState state;
	private final Player player;

	public MoveBuilder(Player player, GameState state) {
		this.player = player;
		this.state = state;
		this.moves = MoveList.newBuilder();
		this.computeState();
	}
	
	private void computeState() {
		if (this.player == Player.USA) {
			this.computedState = new ComputedGameState(this.state, this.moves.build(), MoveList.getDefaultInstance());
		} else {
			this.computedState = new ComputedGameState(this.state, MoveList.getDefaultInstance(), this.moves.build());
		}
	}

	public void influenceDip(final Province.Id id, int magnitude) {
		this.moves.addMoves(
				Move.newBuilder().setDiaDipMove(DiplomacyMove.newBuilder().setProvinceId(id).setMagnitude(magnitude)).build());
		Logger.Dbg("Adding influence from political points in " + id + " with magnitude " + magnitude);
		this.computeState();
	}
	
	public void influenceMil(final Province.Id id, int magnitude) {
		this.moves.addMoves(
				Move.newBuilder().setDiaMilMove(MilitaryMove.newBuilder().setProvinceId(id).setMagnitude(magnitude)).build());
		Logger.Dbg("Adding influence from military points in " + id + " with magnitude " + magnitude);
		this.computeState();
	}
	
	public void influenceCov(final Province.Id id, int magnitude) {
		this.moves.addMoves(
				Move.newBuilder().setDiaCovMove(CovertMove.newBuilder().setProvinceId(id).setMagnitude(magnitude)).build());
		Logger.Dbg("Adding influence from covert points in " + id + " with magnitude " + magnitude);
		this.computeState();
	}
	
	public void foundKGB() {
		this.moves.addMoves(
				Move.newBuilder().setFoundKgbMove(FoundKGBMove.getDefaultInstance()));
		this.computeState();
	}

	public void foundCIA() {
		this.moves.addMoves(
				Move.newBuilder().setFoundCiaMove(FoundCIAMove.getDefaultInstance()));
		this.computeState();
	}
	
	public void FundDissidents(final Province.Id id) {
		this.moves.addMoves(
				Move.newBuilder().setFundDissidentsMove(FundDissidentsMove.newBuilder().setProvinceId(id)).build());
		Logger.Dbg("Funding dissidents in " + id);
		this.computeState();
	}
	
	public void EstablishBase(final Province.Id id) {
		this.moves.addMoves(
				Move.newBuilder().setEstablishBaseMove(EstablishBaseMove.newBuilder().setProvinceId(id)).build());
		Logger.Dbg("Establishing " + player + " base in " + id);
		this.computeState();
	}
	
	public void PoliticalPressure(final Province.Id id) {
		this.moves.addMoves(
				Move.newBuilder().setPoliticalPressureMove(PoliticalPressureMove.newBuilder().setProvinceId(id)).build());
		Logger.Dbg("Applying political pressure in " + id);
		this.computeState();
	}
	
	public void Coup(final Province.Id id, final int magnitude) {
		this.moves.addMoves(
				Move.newBuilder().setCoupMove(CoupMove.newBuilder().setProvinceId(id).setMagnitude(magnitude)).build());
		Logger.Dbg("Preparing coup in " + id + " with extra support " + magnitude);
		this.computeState();
	}
	
	public void FundDefender(final Province.Id id) {
		this.moves.addMoves(
				Move.newBuilder().setConflictOvertFundDefenderMove(ConflictOvertFundDefenderMove.newBuilder().setProvinceId(id)).build());
		Logger.Dbg("Sending military aid to defender in " + id);
		this.computeState();
	}
	
	public void FundAttacker(final Province.Id id) {
		this.moves.addMoves(
				Move.newBuilder().setConflictOvertFundAttackerMove(ConflictOvertFundAttackerMove.newBuilder().setProvinceId(id)).build());
		Logger.Dbg("Sending military aid to attacker in " + id);
		this.computeState();
	}
	
	// Crises
	
	public void BerlinAirlift() {
		this.moves.addMoves(
				Move.newBuilder().setUsaBerlinBlockadeAirliftMove(USABerlinBlockadeAirliftMove.newBuilder()).build());
		Logger.Dbg("Airlifting Berlin");
		this.computeState();
	}
	
	public void LiftBerlinBlockade() {
		this.moves.addMoves(
				Move.newBuilder().setUssrBerlinBlockadeLiftBlockadeMove(USSRBerlinBlockadeLiftBlockadeMove.newBuilder()).build());
		Logger.Dbg("Airlifting Berlin");
		this.computeState();
	}
	
	public final ComputedGameState getComputedGameState() {
		return this.computedState;
	}

	public int getInfluence(final Province.Id provinceId) {
		return this.computedState.totalInfluence.get(provinceId);
	}
	
	public int getPolStore() {
		return this.computedState.polStore.get(this.player);
	}
	
	public int getMilStore() {
		return this.computedState.milStore.get(this.player);
	}
	
	public int getCovStore() {
		return this.computedState.covStore.get(this.player);
	}
	
	public int getHeat() {
		return this.computedState.heat;
	}
	
	public int getPartyUnity() {
		return this.computedState.partyUnity;
	}

	public int getPatriotism() {
		return this.computedState.patriotism;
	}
	
	public MoveList getMoveList() {
		return this.moves.build();
	}
	
	public Player getBaseOwner(final Province.Id provinceId) {
		return this.computedState.bases.get(provinceId);
	}

	public void Undo() {
		this.moves.removeMoves(this.moves.getMovesCount());
		this.computeState();
	}

	public int getYear() {
		return this.computedState.year;
	}

	public int getStabilityModifier(Province.Id provinceId) {
		return this.computedState.stabilityModifier.getOrDefault(provinceId, 0);
	}

}
